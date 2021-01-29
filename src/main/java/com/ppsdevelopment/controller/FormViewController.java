package com.ppsdevelopment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("fform")
public class FormViewController {

    @GetMapping
    public String index(Map<String, Object> model){

        model.put("headervalues","tableHeader");
        return "formpage";

    }
}
