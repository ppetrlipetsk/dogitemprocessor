package com.ppsdevelopment.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

public class UserClass {
    private Long id;

    @NotBlank(message = "Username cannot be empty")
    @Size(min=5, max=30)
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    //@Size(min=5, max=30)
    @NotBlank(message = "FIO cannot be empty")
    private String fio;


/*
    @Transient
    @NotBlank(message = "Password confirmation cannot be empty")
    private String password2;
*/

    private boolean active;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

}
