package com.ppsdevelopment.domain.dictclasses;

import java.io.Serializable;

public class ColumnItem extends AliasSettings implements Serializable {
    private String fieldAlias;
    private String fieldName;
    private String fieldType;

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
