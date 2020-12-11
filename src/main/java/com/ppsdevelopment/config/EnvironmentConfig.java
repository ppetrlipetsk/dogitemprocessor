package com.ppsdevelopment.config;

import com.ppsdevelopment.envinronment.Pagination;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentConfig {
    @Bean
    public Pagination pagination(){
        Pagination pagination=new Pagination();
        pagination.setFirstPage(1);
        pagination.setPageSize(3);
        pagination.setButtonsCount(5);
        pagination.setCurrentPage(1);
        return pagination;
    }

}
