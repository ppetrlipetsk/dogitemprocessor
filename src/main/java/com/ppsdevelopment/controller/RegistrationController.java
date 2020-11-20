package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.Role;
import com.ppsdevelopment.domain.User;
import com.ppsdevelopment.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
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
    }

    private boolean isCredentialsValid(User user, String password2) {
        return (user!=null)&&(password2!=null)&&(!user.getUsername().isEmpty()&&!user.getFio().isEmpty()&&!user.getPassword().isEmpty()&&user.getPassword().equals(password2));
    }
}
