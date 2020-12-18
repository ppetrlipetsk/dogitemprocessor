package com.ppsdevelopment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


//@Configuration
//@PropertySource("classpath:config.properties")


@Configuration
public class ConfigProperties {

    @Value("${tablename}")
    String tableName;

//    @Value("${datasource.url}")
    String datasourceURL;

//    @Value("${datasource.username}")
    String userName;

  //  @Value("${datasource.password}")
    String password;



    public String getDatasourceURL() {
        return datasourceURL;
    }

    public void setDatasourceURL(String datasourceURL) {
        this.datasourceURL = datasourceURL;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
