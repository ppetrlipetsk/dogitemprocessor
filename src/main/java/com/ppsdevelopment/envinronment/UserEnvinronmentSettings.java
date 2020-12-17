package com.ppsdevelopment.envinronment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserEnvinronmentSettings implements Serializable {
    // <String,Object>=<name, value>
    private Map<String, Object> envinronment=new HashMap<>();

    public void put(String name, Object value){
        envinronment.put(name,value);
    }

    public Object get(String name){
        return  envinronment.get(name);
    }


}
