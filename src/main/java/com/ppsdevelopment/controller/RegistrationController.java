package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.User;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


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
            , @Valid User user
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
            Map<String, String> errors=ControllerUtils.collectErrors(bindingResult);
            isError=true;
            model.addAttribute("error",true);
            model.addAttribute("errors",errors);
        }

        if (isError) {
            return "registration";
        }
        else
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