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


  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="table_id")
  public Tables tables;



/*  private long table_id;*/

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


/*

  public long getTableId() {
    return table_id;
  }

  public void setTableId(long tableId) {
    this.table_id = tableId;
  }
*/



  public Tables getTables() {
    return tables;
  }

  public void setTables(Tables tables) {
    this.tables = tables;
  }


  public String getFieldtype() {
    return fieldtype;
  }

  public void setFieldtype(String fieldtype) {
    this.fieldtype = fieldtype;
  }

}
