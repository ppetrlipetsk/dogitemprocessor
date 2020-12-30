package com.ppsdevelopment.service;

import java.io.Serializable;
import java.util.*;

public class FilterQuery implements Serializable {
    private final Map<String, List<Object>>  filter=new HashMap();

    public List<Object> get(String columnName){
        return filter.get(columnName);
    }

    public void set(String columnName, List value){
        filter.put(columnName,value);
    }

    public void addFilterValue(String columnName, String value){
        List values=filter.get(columnName);
        if (value!=null) values.add(value);
        else{
            values=new ArrayList();
            values.add(value);
            filter.put(columnName,values);
        }
    }

    public Set<String> getKeys(){
        return filter.keySet();
    }

}
