package com.ppsdevelopment.controller;

import com.ppsdevelopment.envinronment.*;
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

    @GetMapping
    public String index(Map<String, Object> model) throws Exception {
        Pagination pagination= (Pagination) settingsManager.getSettingsValue(sourceTable.getPaginationName(), Pagination.class);

        if (pagination==null) {
            pagination = new Pagination();
            pagination.setSortColumnName("id");
            pagination.setSortDirection(true);
        }
        settingsManager.setSettingsValue(sourceTable.getPaginationName(),pagination);

        String tableHeader= sourceTable.getTableHeader();
        model.put("headervalues",tableHeader);


        String tableData=sourceTable.getResultAsStringLine(sourceTable.getAll());
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

/*
    @Autowired
    public void setSession(HttpSession session) {
        this.session = session;
    }
*/

/*
    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
*/

    @Autowired
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}
