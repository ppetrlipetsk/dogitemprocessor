package com.ppsdevelopment.envinronment;

import com.ppsdevelopment.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Credentials {

    public static String getUserName(){
        return  ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public static Long getUserId(){
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    public static User getUser(){
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

}
