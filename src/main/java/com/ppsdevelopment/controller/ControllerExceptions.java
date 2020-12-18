package com.ppsdevelopment.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptions {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String processException(Exception e) {
        return e.getMessage();
    }
}
