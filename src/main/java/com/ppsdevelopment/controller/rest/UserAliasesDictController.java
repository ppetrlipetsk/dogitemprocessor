package com.ppsdevelopment.controller.rest;

import com.ppsdevelopment.domain.dictclasses.AliasesSettingsCollection;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.AliasesSettingsImpl;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.SourceTableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("useraliasesdict")
public class UserAliasesDictController {
    private static final String ID_NULL="null";
    private SourceTableImpl sourceTable;
    private AliasesSettingsImpl aliasesSettingsImpl;
    private AliasesSettingsCollection aliasesSettingsCollection;

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl sourceTable) {
        this.sourceTable = sourceTable;
    }

    @Autowired
    public void setAliasesSettingsImpl(AliasesSettingsImpl aliasesSettingsImpl) {
        this.aliasesSettingsImpl = aliasesSettingsImpl;
    }

    @PostMapping(value="/getaliaseslist")
    public @ResponseBody  List getaliasesList(@RequestBody  String request) {
        Map collection=aliasesSettingsImpl.getSettingsCollection(
                sourceTable.getTableName()
                ,aliasesSettingsCollection
                ,sourceTable.getAliasesKeys()
                ,sourceTable.getAliases()
                );

        return aliasesSettingsImpl.getSettingsList(collection,sourceTable.getAliases());
    }

    @PostMapping(value="applysettings", consumes = "application/json;charset=UTF-8")
    public @ResponseBody  String applySettings(@RequestBody  String request) throws Exception {
        String[] list= request.substring(2).substring(0,(request.length()-5)).replace("\\\"","\"").split("},");
        String tableName=sourceTable.getTableName();
        aliasesSettingsImpl.applySettings(list,  tableName, aliasesSettingsCollection.getCollection());
        Map collection=aliasesSettingsImpl.getSettingsCollection(
                sourceTable.getTableName()
                ,aliasesSettingsCollection
                ,sourceTable.getAliasesKeys()
                ,sourceTable.getAliases()
        );
        String s=sourceTable.getJsonResponseForColumnsSettingsApply(collection);
        return s;
    }

    @Autowired
    public void setAliasesSettingsCollection(AliasesSettingsCollection aliasesSettingsCollection) {
        this.aliasesSettingsCollection = aliasesSettingsCollection;
    }
}