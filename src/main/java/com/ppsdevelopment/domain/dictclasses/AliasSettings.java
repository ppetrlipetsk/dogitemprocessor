package com.ppsdevelopment.domain.dictclasses;

import java.io.Serializable;

/**
 * Класс настроек столбца таблицы
 */
public class AliasSettings implements Serializable {
    private Long aliasid;
    private boolean visibility;

    public AliasSettings() {
        this.aliasid =0L;
        this.visibility=true;
    }

    public Long getAliasid() {
        return aliasid;
    }

    public void setAliasid(Long fieldAliasId) {
        this.aliasid = fieldAliasId;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

}
