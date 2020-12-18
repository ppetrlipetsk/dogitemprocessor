package com.ppsdevelopment.config;

import com.ppsdevelopment.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

//@Configuration
public class DataBaseCFG {
//@Autowired
    ConfigProperties configProperties;
//    @Bean
    /*public DataSource dataSource() {

        String url=configProperties.getDatasourceURL();
        String username=configProperties.getUserName();
        String password=configProperties.getPassword();
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return dataSourceBuilder.build();

    }*/
}
