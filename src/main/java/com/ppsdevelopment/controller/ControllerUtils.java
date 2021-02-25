package com.ppsdevelopment.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {
    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

    static Map<String, String> collectErrors(BindingResult bindingResult){
        Map<String, String> map=new HashMap<>();
        for (FieldError fieldError:bindingResult.getFieldErrors()){
            String key=fieldError.getField();
            String message=fieldError.getDefaultMessage();
            if (map.containsKey(key)){
                message=map.get(key)+" "+message;
            }
            map.put(key,message);
        }
        return map;
    }

    static List<String> listErrors(BindingResult bindingResult){
        List<String> list=new LinkedList<>();
        for (FieldError fieldError:bindingResult.getFieldErrors()){
            String message=fieldError.getDefaultMessage();
            list.add(message);
        }
        return list;
    }


}
