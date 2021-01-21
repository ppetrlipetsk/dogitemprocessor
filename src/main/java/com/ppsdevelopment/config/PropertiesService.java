package com.ppsdevelopment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {
    @Autowired
    private Environment environment;
    public String get(String propertyName){
        return environment.getProperty(propertyName);
    }
}
