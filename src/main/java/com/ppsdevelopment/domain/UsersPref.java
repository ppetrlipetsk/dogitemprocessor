package com.ppsdevelopment.domain;


import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="userspref")
public class UsersPref {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    Long userId;

    @Type(type="org.hibernate.type.TextType")
    String settings;

    String objectname;
/*
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id")
    User user;
*/
    String paramName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectname() {
        return objectname;
    }

    public void setObjectname(String objectname) {
        this.objectname = objectname;
    }
}
