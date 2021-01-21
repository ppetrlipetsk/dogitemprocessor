package com.ppsdevelopment.service.databasetableimpl.tablecacheservice;

import com.ppsdevelopment.envinronment.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UsersTablesCache {

    private Credentials credentials;

    private static final Map<Long, TableCache> cache = new HashMap<>();

    public  Map<Long, TableCache> getCache() {
        return cache;
    }


    public void setTableCache(TableCache tableCache){
        Long userId=credentials.getUserId();
        cache.put(userId,tableCache);
    }

    public TableCache getTableCache(){
        Long userId=credentials.getUserId();
        return cache.get(userId);
    }

    public void setTableRow(String tableId, RowCollection row){
        Long userId=credentials.getUserId();
        TableCache tc=cache.get(userId);
        if (tc==null) {
            tc = new TableCache();
            cache.put(userId,tc);
        }
        tc.put(tableId,row);
    }

    public RowCollection getTableRowsCollection(String tableId){
        Long userId=credentials.getUserId();
        TableCache tc=cache.get(userId);
        if (tc!=null)
            return tc.get(tableId);
        else return null;
    }

    public RowFields getTableRowFields(String tableId, Long rowId ){
        Long userId=credentials.getUserId();
        TableCache tc=cache.get(userId);
        if (tc!=null){
            RowCollection rows= tc.get(tableId);
            if (rows==null) return null;
            return rows.get(rowId);
        }
        else return null;
    }

    public void setTableRowFields(String tableId, Long rowId, RowFields fields ){
        Long userId=credentials.getUserId();
        TableCache tc=cache.get(userId);
        if (tc==null) {
            tc = new TableCache();
            cache.put(userId,tc);
        }
            RowCollection rows= tc.get(tableId);
            if (rows==null) rows=new RowCollection();
            rows.put(rowId, fields);
            tc.put(tableId,rows);
    }

    public Object getTableRowFieldValue(String tableId, Long rowId, String fieldName ){
        Long userId=credentials.getUserId();
        TableCache tc=cache.get(userId);
        if (tc!=null){
            RowCollection rows= tc.get(tableId);
            if (rows==null) return null;
            return rows.getFieldValue(rowId,fieldName);
        }
        else return null;
    }

    public void setTableRowFieldValue(String tableId, Long rowId, String fieldName, Object value){
        Long userId=credentials.getUserId();
        TableCache tc=cache.get(userId);
        if (tc==null) {
            tc = new TableCache();
            cache.put(userId, tc);
        }
        RowCollection rows= tc.get(tableId);

        if (rows==null) rows=new RowCollection();
        rows.setFieldValue(rowId,fieldName,value);
        tc.put(tableId,rows);
    }

    @Autowired
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
