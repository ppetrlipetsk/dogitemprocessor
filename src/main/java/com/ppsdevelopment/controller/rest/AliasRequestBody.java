package com.ppsdevelopment.controller.rest;

import java.io.Serializable;

public class AliasRequestBody implements Serializable {
    String aliasid;
    boolean visibility;

    public String getAliasid() {
        return aliasid;
    }

    public void setAliasid(String aliasid) {
        this.aliasid = aliasid;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
