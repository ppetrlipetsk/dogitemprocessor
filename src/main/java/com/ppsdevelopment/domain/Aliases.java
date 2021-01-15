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
  /*private boolean columnvisible;*/

//  @ManyToOne(fetch = FetchType.EAGER)
//  @JoinColumn(name="table_id")
//  public Tables tables;

  private Long table_id;

  @OneToOne(cascade = CascadeType.DETACH)
  @JoinColumn(name="style_id")
  private Styles style;

  private String fieldtype;


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

  public Styles getStyle() {
    return style;
  }

  public void setStyle(Styles style) {
    this.style = style;
  }

  public String getStyleClass(){
    if (this.style!=null) return this.style.styleClass;
    else
      return "";
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
            ", style=" + style +
            ", fieldtype='" + fieldtype + '\'' +
            '}';
  }

  public String toCellString() {
    if (columnstyle ==null) columnstyle ="";
    return "[{" +
            "id:" + id +
            ", fieldalias:'" + fieldalias + '\'' +
            ", fieldname:'" + fieldname + '\'' +
            ", styleClass:'" + this.getStyleClass() +"'"+
            ", fieldtype:'" + fieldtype + '\'' +
            ", columnStyle:'" + columnstyle + '\'' +
            "}]";
  }


}
