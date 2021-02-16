package com.ppsdevelopment.domain;

import javax.persistence.*;

@Entity
@Table(name = "aliases")

public class Aliases {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private String fieldalias;
  private String fieldname;
  private String columnstyle;
  private Integer columnwidth;
  private Long table_id;
  private String fieldtype;
  private String columnclass;
  private Boolean columnVisibility;

/*

  @OneToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name="style_id")
  private Styles style;
*/


  public long getId() {
    return id;
  }

  public String getFieldalias() {
    return fieldalias;
  }

  public void setFieldalias(String fieldalias) {
    this.fieldalias = fieldalias;
  }


  public String getFieldname() {
    return fieldname;
  }

  public void setFieldname(String fieldname) {
    this.fieldname = fieldname;
  }

  public Long getTable_id() {
    return table_id;
  }

  public void setTable_id(Long table_id) {
    this.table_id = table_id;
  }

  public String getFieldtype() {
    return fieldtype;
  }

  public void setFieldtype(String fieldtype) {
    this.fieldtype = fieldtype;
  }

  public String getColumnstyle() {
    return columnstyle;
  }

  public void setColumnstyle(String columnstyle) {
    this.columnstyle = columnstyle;
  }

  public Integer getColumnwidth() {
    return columnwidth;
  }

  public void setColumnwidth(Integer columnwidth) {
    this.columnwidth = columnwidth;
  }

/*
  public boolean isColumnvisible() {
    return columnvisible;
  }

  public void setColumnvisible(boolean columnvisible) {
    this.columnvisible = columnvisible;
  }
*/

  @Override
  public String toString() {
    return "Aliases{" +
            "id=" + id +
            ", fieldalias='" + fieldalias + '\'' +
            ", fieldname='" + fieldname + '\'' +
            ", table_id=" + table_id +
            ", fieldtype='" + fieldtype + '\'' +
            '}';
  }


  public Boolean getColumnvisibility() {
    return columnVisibility;
  }

  public void setColumnvisibility(Boolean columnvisibility) {
    this.columnVisibility = columnvisibility;
  }

  public String getColumnclass() {
    return columnclass;
  }

  public void setColumnclass(String columnclass) {
    this.columnclass = columnclass;
  }
}
