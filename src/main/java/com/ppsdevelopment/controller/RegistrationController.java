package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.User;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

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
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordError", "Passwords are different!");
        }

        if (bindingResult.hasErrors()) {
            //Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            //model.mergeAttributes(errors);

            return "registration";
        }

        if (!userSevice.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }


}