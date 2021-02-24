package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.User;
import com.ppsdevelopment.domain.UserClass;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/*
@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

   */
/* @PostMapping("/registration")
    public String addUser(User user, @RequestParam("password2") String password2,Map<String, Object> model) {
        if (isCredentialsValid(user, password2)) {

            User userFromDb = userRepo.findByUsername(user.getUsername());

            model.put("message", "User exists!");
            if (userFromDb != null) {
                model.put("message", "Пользователь существует!");
                return "registration";
            }

            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            userRepo.save(user);

            return "redirect:/login";
        }
        else
        {
            model.put("message","Неправильно заполнены поля!");
            model.put("user",user);
            model.put("password2",password2);
            return "registration";
        }
    }*//*

   @PostMapping("/registration")
   public String addUser(User user, BindingResult bindingResult, Model model) {
       if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
           model.addAttribute("passwordError", "Passwords are different!");
       }

       if (bindingResult.hasErrors()) {
           Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

           model.mergeAttributes(errors);

           return "registration";
       }

       if (!userSevice.addUser(user)) {
           model.addAttribute("usernameError", "User exists!");
           return "registration";
       }

       return "redirect:/login";
   }
    private boolean isCredentialsValid(User user, String password2) {
        return (user!=null)&&(password2!=null)&&(!user.getUsername().isEmpty()&&!user.getFio().isEmpty()&&!user.getPassword().isEmpty()&&user.getPassword().equals(password2));
    }
}
*/
@Controller
public class RegistrationController {
    @Autowired
    private UserService userSevice;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm
//            , @Valid UserClass user
            , @Valid User user

            //, Errors errors
            , BindingResult bindingResult
            , Model model) {

        boolean isError=false;
        model.addAttribute("password2",passwordConfirm);

        if (user.getPassword() == null || !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("error",true);
            model.addAttribute("password_confirm_error", "Пароль и подтверждение пароля должны совпадать!");
            isError=true;
        }

        if (bindingResult.hasErrors()) {
        //if (errors.hasErrors()) {
            /*Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);*/
            Map<String, String> errors=ControllerUtils.collectErrors(bindingResult);
            isError=true;
            model.addAttribute("error",true);
            model.addAttribute("errors",errors);
        }

/*
        if ((user!=null)&&(user.getFio().length()==0)){
            model.addAttribute("fio_error",true);
        }

        if ((user!=null)&&(user.getUsername().length()==0)){
            model.addAttribute("username_error",true);
        }
*/

        if ((user!=null)&&(user.getPassword().length()==0)){
            model.addAttribute("password_error",true);
        }

        if (isError) {
            return "registration";
        }

        if (!userSevice.addUser(user)) {
            model.addAttribute("error",true);
            model.addAttribute("errormessage", "Пользователь существует!");
            return "registration";
        }
        return "redirect:/login";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}