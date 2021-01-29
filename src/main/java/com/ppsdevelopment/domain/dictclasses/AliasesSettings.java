package com.ppsdevelopment.domain.dictclasses;


import java.io.Serializable;

public class AliasesSettings implements Serializable {
    private Long id;
    private Long fieldAliasId;
    private String fieldAlias;
    private String fieldname;
    private String globalColumnStyle;
    private Integer globalColumnWidth;
    private Long table_id;
    private Long userId;
    private String styleClass;
    private String style;
    private boolean visibility;
    private Integer width;

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public Long getFieldAliasId() {
        return fieldAliasId;
    }

    public void setFieldaliasId(Long fieldAliasId) {
        this.fieldAliasId = fieldAliasId;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getGlobalColumnStyle() {
        return globalColumnStyle;
    }

    public void setGlobalColumnStyle(String globalColumnStyle) {
        this.globalColumnStyle = globalColumnStyle;
    }

    public Integer getGlobalColumnWidth() {
        return globalColumnWidth;
    }

    public void setGlobalColumnWidth(Integer globalColumnWidth) {
        this.globalColumnWidth = globalColumnWidth;
    }

    public Long getTable_id() {
        return table_id;
    }

    public void setTable_id(Long table_id) {
        this.table_id = table_id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
