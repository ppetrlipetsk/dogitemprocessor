package com.ppsdevelopment.service.databasetableimpl.tablecacheservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RowFields {
    private final Map<String, Object> records;

    public RowFields() {
        records =new HashMap<>();
    }

    public Map<String, Object> getRecords() {
        return records;
    }


    public Map<String,Object> getCollection(){
        return records;
    }

    public Object get(String key){
        if ((records.containsKey(key))) return records.get(key);
        else
            return null;
    }

    public void put(String key, Object value){
        records.put(key,value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RowFields)) return false;
        RowFields rowFields = (RowFields) o;
        return Objects.equals(records, rowFields.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(records);
    }

    @Override
    public String toString() {
        return "RowFields{" +
                "record=" + records +
                '}';
    }
}
