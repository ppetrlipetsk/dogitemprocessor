package com.ppsdevelopment.service;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.*;

public class FilterQuery implements Serializable {
    private final Map<String, List<Object>>  filter=new HashMap();

    public List<Object> get(String columnName){
        return filter.get(columnName);
    }

    public void set(String columnName, List value){
        if ((value!=null)&&(value.size()>0))
            filter.put(columnName,value);
        else
            filter.remove(columnName);
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

    public String toJson(){
        return new Gson().toJson(filter);
    }

    public String getColumnsNamesAsJson(){
        return new Gson().toJson(filter.keySet());
    }


    @Override
    public String toString() {
        return "FilterQuery{" +
                "filter=" + filter +
                '}';
    }
}
