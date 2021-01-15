package com.ppsdevelopment.service.tablecacheservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RowCollection {
    private final Map<Long, RowFields> row;

    public RowCollection() {
        row=new HashMap<>();
    }

    public void put(Long rowId, RowFields value){
        row.put(rowId,value);
    }

    public RowFields get(Long rowId){
        return row.get(rowId);
    }

    public void setFieldValue(Long rowId, String fieldName, Object value){
        RowFields fields=row.get(rowId);
        if (fields==null) {
            fields=new RowFields();
            row.put(rowId, fields);
        }
        fields.put(fieldName, value);
    }

    public Object getFieldValue(Long rowId, String fieldName){
        RowFields fields=row.get(rowId);
        if (fields!=null)
        return fields.get(fieldName);
        else
            return null;
    }

    public Set<Long> getKeys(){
        return row.keySet();
    }

    public RowFields getRowValues(Long key){
        return row.get(key);
    }

    public void clear(){
        this.row.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RowCollection)) return false;
        RowCollection row1 = (RowCollection) o;
        return row.equals(row1.row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row);
    }

    @Override
    public String toString() {
        return "Row{" +
                "row=" + row +
                '}';
    }
}
