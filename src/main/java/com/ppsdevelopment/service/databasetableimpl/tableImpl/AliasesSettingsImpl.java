package com.ppsdevelopment.service.databasetableimpl.tableImpl;

import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.ColumnSettings;
import com.ppsdevelopment.domain.dictclasses.AliasesSettings;
import com.ppsdevelopment.envinronment.Credentials;
import com.ppsdevelopment.envinronment.SettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class AliasesSettingsImpl {

    private SettingsManager settingsManager;
    private Credentials credentials;

    @PersistenceContext
    protected EntityManager em;

    public Map<Long, AliasesSettings> aliasSettings=new HashMap<>();

    public Map<Long,AliasesSettings> getSettingsCollection(String tableName, Long tableId){
        if ((aliasSettings!=null) && (aliasSettings.size()>0))return aliasSettings;
        else {
            aliasSettings=new HashMap<>();
            Map c= (Map) settingsManager.getSettingsValue(tableName+".AliasSettings",aliasSettings.getClass(),false);
            if ((c==null)||(c.size()==0)){
                List<ColumnSettings> list = getAliasesSettings(tableId);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        AliasesSettings als = new AliasesSettings();
                        als.setId(list.get(i).getId());
                        als.setFieldaliasId(list.get(i).getAliasId());
                        als.setStyle(list.get(i).getStyle());
                        als.setStyleClass(list.get(i).getStyleClass());
                        als.setVisibility(list.get(i).isVisibility());
                        als.setWidth(list.get(i).getWidth());
                        aliasSettings.put(list.get(i).getAliasId(), als);
                    }
                    settingsManager.setSettingsValue(tableName+".AliasSettings",aliasSettings,false);
                    return aliasSettings;
                } else
                    return null;
            }
            else
                return c;
        }

    }


    public List<AliasesSettings> getSettingsList(String tableName, Long tableId, List<Aliases> aliases){
        aliasSettings=getSettingsCollection(tableName,tableId);
        List<AliasesSettings> list=new LinkedList<>();
        for (Aliases alias:aliases){
            AliasesSettings item=aliasSettings.get(alias.getId());
            if (item==null)
                item=new AliasesSettings();
            item.setFieldaliasId(alias.getId());
            item.setFieldAlias(alias.getFieldalias());
            item.setFieldname(alias.getFieldname());
            item.setTable_id(alias.getTable_id());
            list.add(item);
        }
        return list;
    }

    private List getAliasesSettings(Long tableId){
        List list =em.createQuery(
                "select cs from ColumnSettings cs  left outer join Aliases al on cs.aliasId=al.id where al.table_id=?1 and cs.userId=?2"
        )
                .setParameter(1,tableId)
                .setParameter(2,credentials.getUserId())
                .getResultList();
        return list;
    }

    @Autowired
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Autowired
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
