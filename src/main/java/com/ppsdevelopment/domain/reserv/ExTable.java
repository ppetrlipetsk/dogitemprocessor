package com.ppsdevelopment.domain.reserv;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;



public class ExTable {
    @Id
    @GeneratedValue
    Long id;
    List<Object> items;

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public ExTable() {
    }

    public ExTable(List<Object> items) {
        this.items = items;
    }
}
