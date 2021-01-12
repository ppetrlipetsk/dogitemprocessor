package com.ppsdevelopment.service;

import com.ppsdevelopment.config.QueriesProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {
    private AnnotationConfigApplicationContext context=null;
    public ConfigurableEnvironment properties() {
        if (context==null) {
            context = new AnnotationConfigApplicationContext();
            //context.register(ConfigProperties.class);
            context.register(QueriesProperties.class);
            context.refresh();
        }
        return context.getEnvironment();
    }

    public PropertiesService() {

        //System.out.println("Properties service construcrot...");
    }
}
