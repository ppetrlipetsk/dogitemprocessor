package com.ppsdevelopment.service.databasetableimpl.tablecacheservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TableCache {
    private final Map<String, RowCollection> tableCache;

    public TableCache() {
        tableCache = new HashMap<>();
    }

    public void put(String tableId, RowCollection row){
        tableCache.put(tableId,row);
    }
    public RowCollection get(String tableId){
        return tableCache.get(tableId);
    }


    public void setFields(String tableId, Long rowId, RowFields rowField){
        RowCollection rowCollection=tableCache.get(tableId);
        if (rowCollection==null) {
            rowCollection=new RowCollection();
            tableCache.put(tableId,rowCollection);
        }
        rowCollection.put(rowId,rowField);
    }

    public RowFields getFields(String tableId, Long rowId){
        RowCollection row=tableCache.get(tableId);
        if (row!=null)
            return row.get(rowId);
        else return null;
    }


    public void setFieldValue(String tableId, Long rowId, String fieldName, Object value){
        RowCollection row=tableCache.get(tableId);
        if (row==null){
            row=new RowCollection();
            tableCache.put(tableId,row);
        }
        row.setFieldValue(rowId, fieldName,value);
    }

    public Object getFieldValue(String tableId, Long rowId, String fieldName){
        RowCollection row=tableCache.get(tableId);
        if (row!=null) return  row.getFieldValue(rowId, fieldName);
        else
            return null;
    }

    public void clear(String tableId){
         RowCollection c=this.tableCache.get(tableId);
         if (c!=null) c.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableCache)) return false;
        TableCache that = (TableCache) o;
        return tableCache.equals(that.tableCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCache);
    }

    @Override
    public String toString() {
        return "TableCache{" +
                "tableCache=" + tableCache +
                '}';
    }
}
