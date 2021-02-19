package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.reserv.Message;
import com.ppsdevelopment.domain.User;
import com.ppsdevelopment.repos.res.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;
    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages = messageRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @GetMapping("/intro")
    public String intro() {
        return "index";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Map<String, Object> model
    ) {
        Message message = new Message(text, tag, user);

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();

        model.put("messages", messages);

        return "main";
    }

    @PostMapping("/logoutsession")
    public String logoutsession(
            @AuthenticationPrincipal User user // можно убрать
            , HttpServletResponse response // можно убрать
            , HttpSession session // можно убрать
            , HttpServletRequest request
            ) {

        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
/*
Этот код тоже работает
        SecurityContextHolder.clearContext();
        if(session != null)
            session.invalidate();
            */
        return "redirect:/login";
    }

    @GetMapping("/accessdenied")
    public String accessDenied(){
        return "accessdenied";
    }

}
