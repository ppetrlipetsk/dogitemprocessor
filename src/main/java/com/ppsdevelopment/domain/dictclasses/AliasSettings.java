package com.ppsdevelopment.domain.dictclasses;

import com.ppsdevelopment.domain.Aliases;
import java.io.Serializable;

import static com.ppsdevelopment.tmctypeslib.FieldType.*;

/**
 * Класс настроек столбца таблицы
 */
public class AliasSettings implements Serializable {
    //private String aliasid;
    private Integer columnWidth;
    private Boolean visibility;
    private String columnClass;
    private String columnStyle;
    private Long id;
    private String fieldAlias;
    private String fieldName;
    private String fieldType;


    public AliasSettings() {
        //this.visibility=true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public static  Integer getDefaultWidth(Aliases alias) {
        Integer w=null;
        if (alias!=null){
            if ( (alias.getFieldtype().equals(BIGINTTYPE.toString()))
                    || (alias.getFieldtype().equals(INTTYPE.toString()))
                    || (alias.getFieldtype().equals(DECIMALTYPE.toString()))
                    || (alias.getFieldtype().equals(FLOATTYPE.toString()))
                    || (alias.getFieldtype().equals(DATETYPE.toString()))
            )    w=80;
            else
                w=200;
        }
        return w;
    }

    private static String getDefaultClass(String fieldType){
        String c="";
        if ((fieldType!=null)){
            if ( (fieldType.equals(BIGINTTYPE.toString()))
                    || (fieldType.equals(INTTYPE.toString()))
                    || (fieldType.equals(DECIMALTYPE.toString()))
                    || (fieldType.equals(FLOATTYPE.toString()))
                    || (fieldType.equals(DATETYPE.toString()))
            )    c="numericcell";
            else
                c="stringcell";
        }
        return c;
    }


    public String getColumnClass() {
        return columnClass;
    }

    public void setColumnClass(String columnClass) {
        this.columnClass = columnClass;
    }

    public String getColumnStyle() {
        return columnStyle;
    }

    public void setColumnStyle(String columnStyle) {
        this.columnStyle = columnStyle;
    }

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

    public static boolean getColumnVisibility(Aliases alias, Boolean vis){
        if (vis!=null) return vis;
        else
        if (alias.getColumnvisibility()!=null) return alias.getColumnvisibility();
        else
            return true;
    }

    public static Integer getColumnWidth(Aliases alias, Integer w){
        if (w!=null) return w;
        else
            if (alias.getColumnwidth()!=null) return alias.getColumnwidth();
            else
                return getDefaultWidth(alias);
    }

    public static String getColumnClass(Aliases alias, String columnclass){

        if ((columnclass!=null)&&(columnclass.length()>0)) return columnclass;
        else
        if ((alias.getColumnclass()!=null)&&(alias.getColumnclass().length()>0)) return alias.getColumnclass();
        else
            return "";//getDefaultClass(alias.getFieldtype());
    }

    public static String getColumnStyle(Aliases alias, String columnstyle){
        if ((columnstyle!=null)&&(columnstyle.length()>0)) return columnstyle;
        else
        if ((alias.getColumnstyle()!=null)&&(alias.getColumnstyle().length()>0)) return alias.getColumnstyle();
        else
            return "";
    }

}
