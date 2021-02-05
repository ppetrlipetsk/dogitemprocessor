package com.ppsdevelopment.domain.dictclasses;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
@SessionScope
public class AliasesSettingsCollection implements Serializable {

    private HashMap<Long, AliasSettings> collection=new HashMap<>();

    public HashMap<Long, AliasSettings> getCollection() {
        return collection;
    }

    public void setCollection(HashMap<Long, AliasSettings> collection) {
        this.collection = collection;
    }

    public AliasSettings get(Long key) {
        return collection.get(key);
    }

    public AliasSettings put(Long key, AliasSettings value) {
        return collection.put(key, value);
    }

    public boolean keysEquals(Set<Long> keys){
        if (keys.size()!=collection.size()) return false;
        for(Long key:keys){
            if (!collection.containsKey(key)) return false;
        }
        return true;
    }


}
