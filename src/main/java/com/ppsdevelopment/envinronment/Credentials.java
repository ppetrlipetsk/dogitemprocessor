package com.ppsdevelopment.envinronment;

import com.ppsdevelopment.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class Credentials {

    public  String getUserName(){
        return  ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    public  Long getUserId(){
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    public  User getUser(){
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

}
