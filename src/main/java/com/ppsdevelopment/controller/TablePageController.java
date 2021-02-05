package com.ppsdevelopment.controller;

import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.dictclasses.AliasSettings;
import com.ppsdevelopment.domain.dictclasses.AliasesSettingsCollection;
import com.ppsdevelopment.envinronment.*;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.AliasesSettingsImpl;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.SourceTableImpl;
import com.ppsdevelopment.service.viewservices.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("table")
public class TablePageController {
    private SourceTableImpl sourceTable;
    private SettingsManager settingsManager;
    private AliasesSettingsImpl aliasesSettingsImpl;
    private AliasesSettingsCollection aliasesSettingsCollection;

    @GetMapping
    public String index(Map<String, Object> model) throws Exception {
        Pagination pagination= (Pagination) settingsManager.getSettingsValue(sourceTable.getPaginationName(), Pagination.class);

        if (pagination==null) {
            pagination = new Pagination();
            pagination.setSortColumnName("id");
            pagination.setSortDirection(true);
        }
        settingsManager.setSettingsValue(sourceTable.getPaginationName(),pagination);


        //AliasesSettingsCollection aliasesSettingsCollection=aliasesSettings.getSettingsList(sourceTable.getTableName(),sourceTable.getTableId(),sourceTable.getAliases());
        Map<Long, AliasSettings> aliasSettings=aliasesSettingsImpl.getSettingsCollection(
                sourceTable.getTableName()
                ,aliasesSettingsCollection
                ,sourceTable.getAliasesKeys()
                ,sourceTable.getAliases()
        );

        String tableHeader= sourceTable.getTableHeader(sourceTable.getAliases(),aliasSettings);
        model.put("headervalues",tableHeader);


        String tableData=sourceTable.getResultAsStringLine(sourceTable.getAll(aliasSettings));
        model.put("tabledata",tableData);

        pagination.setRecordsCount(sourceTable.getCount());
        model.put("pagination",pagination);

        String filterColumns=sourceTable.getFilteredColumnsAsJson();
        model.put("filteredcolumns", filterColumns);


        //session.setAttribute("pagination",pagination);

        return "tablepage";
    }

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl xTable) {
        this.sourceTable = xTable;
    }

    @Autowired
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Autowired
    public void setAliasesSettingsImpl(AliasesSettingsImpl aliasesSettingsImpl) {
        this.aliasesSettingsImpl = aliasesSettingsImpl;
    }

    @Autowired
    public void setAliasesSettingsCollection(AliasesSettingsCollection aliasesSettingsCollection) {
        this.aliasesSettingsCollection = aliasesSettingsCollection;
    }
}
