package com.ppsdevelopment.domain.dictclasses;

/**
 * Класс для отправки результата запроса с сервераб для формирования вывода полей справочника видимости столбцов
 */
public class NamedAliasSettings {
    private Long fieldAliasId;
    private String fieldAlias;
    private String fieldname;
    private boolean visibility;


    public Long getFieldAliasId() {
        return fieldAliasId;
    }

    public void setFieldAliasId(Long fieldAliasId) {
        this.fieldAliasId = fieldAliasId;
    }

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
}
