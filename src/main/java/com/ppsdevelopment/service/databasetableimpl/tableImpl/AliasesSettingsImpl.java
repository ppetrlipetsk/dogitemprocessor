package com.ppsdevelopment.service.databasetableimpl.tableImpl;

import com.fasterxml.jackson.core.type.TypeReference;

import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.dictclasses.AliasSettings;
import com.ppsdevelopment.domain.dictclasses.NamedAliasSettings;
import com.ppsdevelopment.envinronment.SettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.*;

@Service
public class AliasesSettingsImpl implements Serializable {

    private SettingsManager settingsManager;

    @PersistenceContext
    protected EntityManager em;

    private boolean keysEquals(Set<Long> keys, List<AliasSettings> collection){
        if (keys.size()!=collection.size()) return false;
        for(Long key:keys){
            if (!containKey(collection, key)) return false;
        }
        return true;
    }

    private boolean containKey(List<AliasSettings> list, Long key){
        for (AliasSettings settings:list){
            if (settings.getId().equals(key)) return true;
        }
        return false;
    }

    public HashMap<Long,AliasSettings> getSettingsCollection(String tableName, Set<Long> aliasesKeys, List<Aliases> aliases){
        TypeReference<LinkedList<AliasSettings>> typeRef
                = new TypeReference<LinkedList<AliasSettings>>(){};
        LinkedList<AliasSettings> lset= (LinkedList<AliasSettings>) settingsManager.getSettingsValue(tableName+".AliasSettings",null,typeRef);
        HashMap<Long, AliasSettings> collection=getCollectionFromList(lset);
        if ((lset!=null) &&  (keysEquals(aliasesKeys, lset))) return getValidatedCollection(collection, aliases,tableName+".AliasSettings");
        else{
                    createAliasesCollection(collection, aliases);
                    List list=getSettingsList(collection);
                    settingsManager.setSettingsValue(tableName+".AliasSettings",list);
                    return collection;
                }
    }

    /** Возвращает коллекцию настроек столбцов из таблицы aliases
     * @param aliases
     * @return
     */
    public HashMap<Long,AliasSettings> getAliasesSettingsCollection(List<Aliases> aliases){
            HashMap<Long, AliasSettings> collection=new HashMap<>();
            createAliasesCollection(collection, aliases);
            return collection;
    }


    private HashMap<Long, AliasSettings> getValidatedCollection(HashMap<Long, AliasSettings> collection, List<Aliases> aliases, String collectionId) {
        boolean changed=false;
        for (Aliases alias:aliases){
            AliasSettings settings=collection.get(alias.getId());
            if (settings.getColumnWidth()==null){
                settings.setColumnWidth(AliasSettings.getColumnWidth(alias,null));
                changed=true;
            }
            if (settings.getColumnClass()==null) {
                settings.setColumnClass(AliasSettings.getColumnClass(alias,null));
                changed=true;
            }
            if (settings.getColumnStyle()==null) {
                settings.setColumnStyle(AliasSettings.getColumnStyle(alias,null));
                changed=true;
            }

        }
        if (changed) settingsManager.setSettingsValue(collectionId,getSettingsList(collection));
        return collection;
    }

    public List<NamedAliasSettings> getSettingsList(Map<Long,AliasSettings> aliasSettings, List<Aliases> aliases){
        List<NamedAliasSettings> list=new LinkedList<>();
        for (Aliases alias:aliases){
            NamedAliasSettings item=new NamedAliasSettings();
            item.setFieldAliasId(alias.getId());
            item.setFieldAlias(alias.getFieldalias());
            item.setFieldname(alias.getFieldname());
            item.setVisibility(aliasSettings.get(alias.getId()).isVisibility());
            item.setColumnWidth(aliasSettings.get(alias.getId()).getColumnWidth());
            item.setColumnClass(aliasSettings.get(alias.getId()).getColumnClass());
            item.setColumnStyle(aliasSettings.get(alias.getId()).getColumnStyle());
            list.add(item);
        }
        return list;
    }

//    @Transactional
    public void applySettings(LinkedList<AliasSettings> list, String tableName, HashMap<Long, AliasSettings> aliasSettings) {
        LinkedList<AliasSettings> aliasSettingsList = new LinkedList<>();
        for (AliasSettings settings:list){
            //AliasSettings x= (AliasSettings) DataAdapter.objectFromJson(s+"}", AliasSettings.class);

            AliasSettings n=aliasSettings.get(settings.getId());
            if (n!=null){
                n.setVisibility(settings.isVisibility());
                n.setColumnWidth(settings.getColumnWidth());
                n.setColumnClass(settings.getColumnClass());
            }
            else
                aliasSettings.put(settings.getId(),settings);

            aliasSettingsList.add(settings);
        }
        settingsManager.setSettingsValue(tableName+".AliasSettings",aliasSettingsList);
    }


    private HashMap<Long,AliasSettings> getCollectionFromList(List<AliasSettings> list){
        HashMap<Long, AliasSettings> c = new HashMap();
        if (list!=null) {
            for (AliasSettings settings:list) {
                c.put(settings.getId(), settings);
            }
        }
        return c;
    }

    private HashMap<Long, AliasSettings> createAliasesCollection(HashMap<Long, AliasSettings> collection, List<Aliases> aliases) {
        for (Aliases alias: aliases){
            Integer width=null;
            String cellClass=null;
            String cellStyle=null;
            Boolean visibility=null;
            if ((collection!=null)&&(collection.containsKey(alias.getId()))){
                //collection.get(alias.getId()).setVisibility(collection.get(alias.getId()).isVisibility());
                visibility=collection.get(alias.getId()).isVisibility();
                width=collection.get(alias.getId()).getColumnWidth();
                cellClass=collection.get(alias.getId()).getColumnClass();
            }
                AliasSettings settings=new AliasSettings();
                cellClass=AliasSettings.getColumnClass(alias,cellClass);
                cellStyle=AliasSettings.getColumnStyle(alias,cellStyle);
                boolean columnVisibility=settings.getColumnVisibility(alias,visibility);
                int columnWidth=settings.getColumnWidth(alias,width);


                settings.setId(alias.getId());
                settings.setVisibility(columnVisibility);
                settings.setColumnWidth(columnWidth);
                settings.setColumnClass(cellClass);
                settings.setColumnStyle(cellStyle);

                collection.put(alias.getId(),settings);
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

    public void applyWidthSettings(String tableName, Set<Long> aliasesKeys, List<Aliases> aliases, LinkedList<AliasSettings> list) {
    }
}
