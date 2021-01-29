package com.ppsdevelopment.controller.rest;

import com.ppsdevelopment.controller.requestclass.TableCellRequest;
import com.ppsdevelopment.envinronment.SettingsManager;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.AliasesSettingsImpl;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.SourceTableImpl;
import com.ppsdevelopment.tmctypeslib.DetectType;
import com.ppsdevelopment.tmctypeslib.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("useraliasesdict")
public class UserAliasesDictController {
    private SourceTableImpl sourceTable;
    private SettingsManager settingsManager;
    private AliasesSettingsImpl aliasesSettings;
    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl sourceTable) {
        this.sourceTable = sourceTable;
    }

    @Autowired
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @PostMapping(value="/getaliaseslist", consumes = "application/json;charset=UTF-8")
    public @ResponseBody  List getaliasesList(@RequestBody  String request) {
//        List l=sourceTable.getAliases();
  //      List l1=sourceTable.getAliasesWithStyle();
    //    System.out.println(l1);

        List l=aliasesSettings.getSettingsList(sourceTable.getTableName(),sourceTable.getTableId(),sourceTable.getAliases());
        return l;
    }

    @Autowired
    public void setAliasesSettings(AliasesSettingsImpl aliasesSettings) {
        this.aliasesSettings = aliasesSettings;
    }
}