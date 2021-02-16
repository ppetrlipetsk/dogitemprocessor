package com.ppsdevelopment.controller.rest;

import java.io.Serializable;

class AliasRequestBodyw implements Serializable {
    String aliasid;
    boolean visibility;
    Integer columnWidth;
    String columnStyle;
    Integer id;

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

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public String getColumnStyle() {
        return columnStyle;
    }

    public void setColumnStyle(String columnStyle) {
        this.columnStyle = columnStyle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
