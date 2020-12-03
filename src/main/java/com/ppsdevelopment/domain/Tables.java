package com.ppsdevelopment.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="tables")
public class Tables {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private long id;

  private String tablename;

/*
  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "aliases", joinColumns = @JoinColumn(name = "table_id"))
  @Enumerated(EnumType.STRING)
*/


//  @OneToMany(mappedBy = "tables", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//  public Set<Aliases> aliases;



  public long getId() {
    return id;
  }

  public String getTablename() {
    return tablename;
  }

  public void setTablename(String tablename) {
    this.tablename = tablename;
  }

/*

  public Set<Aliases> getAliases() {
    return aliases;
  }

  public void setAliases(Set<Aliases> aliases) {
    this.aliases = aliases;
  }
*/


}
