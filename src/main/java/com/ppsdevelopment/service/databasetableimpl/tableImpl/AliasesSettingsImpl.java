package com.ppsdevelopment.service.databasetableimpl.tableImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.dictclasses.AliasSettings;
import com.ppsdevelopment.domain.dictclasses.AliasesSettingsCollection;
import com.ppsdevelopment.domain.dictclasses.NamedAliasSettings;
import com.ppsdevelopment.envinronment.SettingsManager;
import com.ppsdevelopment.service.databasetableimpl.helpers.DataAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.*;

@Service
public class AliasesSettingsImpl implements Serializable {

    private SettingsManager settingsManager;

    @PersistenceContext
    protected EntityManager em;

    public Map<Long,AliasSettings> getSettingsCollection(String tableName, AliasesSettingsCollection aliasSettingsCollection, Set<Long> aliasesKeys, List<Aliases> aliases){
        if ((aliasSettingsCollection!=null) && (aliasSettingsCollection.getCollection()!=null) && (aliasSettingsCollection.keysEquals(aliasesKeys))) return aliasSettingsCollection.getCollection();
        else {
                TypeReference<LinkedList<AliasSettings>> typeRef
                        = new TypeReference<LinkedList<AliasSettings>>(){};
                List<AliasSettings> lset= (List<AliasSettings>) settingsManager.getSettingsValue(tableName+".AliasSettings",null,typeRef);
                HashMap<Long, AliasSettings> c=getCollectionFromList(lset);
                aliasSettingsCollection.setCollection(c);

                if (aliasSettingsCollection.keysEquals(aliasesKeys))
                    return c;
                else{
                    {
                        c=createAliasesCollection(c, aliases);
                        aliasSettingsCollection.setCollection(c);
                        List list=getSettingsList(aliasSettingsCollection.getCollection());
                        settingsManager.setSettingsValue(tableName+".AliasSettings",list);
                    }

                    return aliasSettingsCollection.getCollection();
                }
        }
    }

    public List<NamedAliasSettings> getSettingsList(Map<Long,AliasSettings> aliasSettings, List<Aliases> aliases){
        List<NamedAliasSettings> list=new LinkedList<>();
        for (Aliases alias:aliases){
            NamedAliasSettings item=new NamedAliasSettings();
            item.setFieldAliasId(alias.getId());
            item.setFieldAlias(alias.getFieldalias());
            item.setFieldname(alias.getFieldname());
            item.setVisibility(aliasSettings.get(alias.getId()).isVisibility());
            list.add(item);
        }
        return list;
    }

    @Transactional
    public void applySettings(String[] list, String tableName, HashMap<Long, AliasSettings> aliasSettings) {
        LinkedList<AliasSettings> aliasSettingsList = new LinkedList<>();
        for (String s:list){
            AliasSettings x= (AliasSettings) DataAdapter.objectFromJson(s+"}", AliasSettings.class);
            AliasSettings n=aliasSettings.get(x.getAliasid());
            if (n!=null) n.setVisibility(x.isVisibility());
            else aliasSettings.put(x.getAliasid(),x);
            aliasSettingsList.add(x);
        }
        settingsManager.setSettingsValue(tableName+".AliasSettings",aliasSettingsList);
    }

    private HashMap<Long,AliasSettings> getCollectionFromList(List<AliasSettings> list){
        HashMap c=new HashMap();
        for(int i=0;i<list.size();i++){
            AliasSettings s=list.get(i);
            c.put(s.getAliasid(),s);
        }
        return c;
    }

    private HashMap<Long, AliasSettings> createAliasesCollection(HashMap<Long, AliasSettings> collection, List<Aliases> aliases) {
        for (Aliases alias: aliases){
            if ((collection!=null)&&(collection.containsKey(alias.getId()))){
                collection.get(alias.getId()).setVisibility(collection.get(alias.getId()).isVisibility());
            }
            else{
                AliasSettings settings=new AliasSettings();
                settings.setAliasid(alias.getId());
                collection.put(alias.getId(),settings);
            }
        }
        return collection;
    }

    private List<AliasSettings> getSettingsList(Map<Long, AliasSettings> collection){
        LinkedList<AliasSettings> list=new LinkedList();
        for (Long key: collection.keySet()){
            list.add(collection.get(key));
        }
        return list;
    }

    @Autowired
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

}
