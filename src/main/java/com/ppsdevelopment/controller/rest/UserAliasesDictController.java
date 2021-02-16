package com.ppsdevelopment.controller.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppsdevelopment.domain.dictclasses.AliasSettings;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.AliasesSettingsImpl;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.SourceTableImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("useraliasesdict")
public class UserAliasesDictController {
    private static final String ID_NULL="null";
    private SourceTableImpl sourceTable;
    private AliasesSettingsImpl aliasesSettingsImpl;

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
                ,sourceTable.getAliasesKeys()
                ,sourceTable.getAliases()
                );

        return aliasesSettingsImpl.getSettingsList(collection,sourceTable.getAliases());
    }

    @PostMapping(value="applysettings", consumes = "application/json;charset=UTF-8")
    public @ResponseBody  String applySettings(@RequestBody  String request) throws Exception {

        request= request.replace("\\\"","\"");//.split("},");

        ObjectMapper mapper = new ObjectMapper();
        LinkedList<AliasSettings> list=new LinkedList<>();
        TypeReference<LinkedList<AliasSettings>> typeReference = new TypeReference<LinkedList<AliasSettings>>(){};
        try{
            list=mapper.readValue( request,typeReference);
        }
        catch (Exception ignored){}

        if (list==null) throw new RuntimeException("Request error");

        String tableName=sourceTable.getTableName();
        HashMap<Long, AliasSettings> collection=aliasesSettingsImpl.getSettingsCollection(
                sourceTable.getTableName()
                ,sourceTable.getAliasesKeys()
                ,sourceTable.getAliases()
        );
        aliasesSettingsImpl.applySettings(list,  tableName, collection);
        String s=sourceTable.getJsonResponseForColumnsSettingsApply(collection);
        return s;
    }

    @PostMapping(value = "widthsettings", consumes = "application/json;charset=UTF-8")
    public String widthSettingsApply(@RequestBody String request){
        TypeReference<LinkedList<AliasSettings>> typeReference
                = new TypeReference<LinkedList<AliasSettings>>(){};
        ObjectMapper mapper = new ObjectMapper();

        HashMap<Long, AliasSettings> collection=aliasesSettingsImpl.getSettingsCollection(
                sourceTable.getTableName()
                ,sourceTable.getAliasesKeys()
                ,sourceTable.getAliases()
        );

        LinkedList<AliasSettings> list=new LinkedList<>();
        try{
              list=mapper.readValue( request,typeReference);
            System.out.println(list.size());
        }
        catch (Exception ignored){
            System.out.println(ignored);
        }

        aliasesSettingsImpl.applySettings(
                list
                ,sourceTable.getTableName()
                /*,sourceTable.getAliasesKeys()*/
                //,sourceTable.getAliases()
                ,collection
        );

        return "";
    }



}