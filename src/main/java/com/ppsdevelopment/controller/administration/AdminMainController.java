package com.ppsdevelopment.controller.administration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.dictclasses.AliasSettings;
import com.ppsdevelopment.envinronment.SettingsManager;
import com.ppsdevelopment.repos.AliasesRepo;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.AliasesSettingsImpl;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.SourceTableImpl;
import com.ppsdevelopment.service.viewservices.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("administration")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminMainController {
    private static final int TOPCOUNT=10;

    private SourceTableImpl sourceTable;
    private AliasesSettingsImpl aliasesSettingsImpl;
    private AliasesRepo aliasesRepo;

    // Основная страница
    @RolesAllowed(value={"ADMIN"})
    @GetMapping("/tablesettings")
    public String tableSettings(Map<String, Object> model) throws Exception {

            Pagination pagination = new Pagination();
            pagination.setSortColumnName("id");
            pagination.setSortDirection(true);

            Map<Long, AliasSettings> aliasSettings=new HashMap<>();
            String tableHeader= sourceTable.getTableHeader(sourceTable.getAliases(),aliasSettings);
            model.put("headervalues",tableHeader);

            String tableData=sourceTable.getResultAsStringLine(sourceTable.getTopRecords(aliasSettings,TOPCOUNT));
            model.put("tabledata",tableData);

            pagination.setRecordsCount(TOPCOUNT);
            model.put("pagination",pagination);

        return "/administration/tablepage";
    }

    //Сохранение настроек столбцов
    @PostMapping(value="/applysettings", consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    String applySettings(@RequestBody String request) throws Exception {

        request= request.replace("\\\"","\"");//.split("},");

        ObjectMapper mapper = new ObjectMapper();
        LinkedList<AliasSettings> list=new LinkedList<>();
        TypeReference<LinkedList<AliasSettings>> typeReference = new TypeReference<LinkedList<AliasSettings>>(){};
        try{
            list=mapper.readValue( request,typeReference);
        }
        catch (Exception ignored){}

        if (list==null) throw new RuntimeException("Request error");

        for (AliasSettings aliasSettings:list){
            {
                Aliases alias = aliasesRepo.findFirstById(aliasSettings.getId());
                if (alias != null) {
                    alias.setColumnvisibility(aliasSettings.isVisibility());
                    alias.setColumnstyle(aliasSettings.getColumnStyle());
                    alias.setColumnclass(aliasSettings.getColumnClass());
                    alias.setColumnwidth(aliasSettings.getColumnWidth());
                    aliasesRepo.save(alias);
                } else
                    throw new RuntimeException("Alias not found. Id=" + aliasSettings.getId());
            }
            Aliases alias=sourceTable.getAliasById(aliasSettings.getId());
            if (alias!=null){
                alias.setColumnvisibility(aliasSettings.isVisibility());
                alias.setColumnstyle(aliasSettings.getColumnStyle());
                alias.setColumnclass(aliasSettings.getColumnClass());
                alias.setColumnwidth(aliasSettings.getColumnWidth());
            }
/*
            alias.(aliasSettings);
            alias.(aliasSettings);
*/
        }
        Map collection=aliasesSettingsImpl.getAliasesSettingsCollection(sourceTable.getAliases());
        String s=sourceTable.getResponseForAdminColumnsSettingsApply(collection,TOPCOUNT);
        return s;
    }


    @PostMapping(value="/getaliaseslist")
    public @ResponseBody
    List getaliasesList(@RequestBody  String request) {
        Map collection=aliasesSettingsImpl.getAliasesSettingsCollection(sourceTable.getAliases());
        return aliasesSettingsImpl.getSettingsList(collection,sourceTable.getAliases());
    }






    @RolesAllowed(value={"ADMIN"})
    @GetMapping
    public String index(){
        return "/administration/mainpage";
    }

    @RolesAllowed(value={"ADMIN"})
    @PostMapping("userssettings")
    public String usersSettings(){
        return "/administration/user";
    }

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl sourceTable) {
        this.sourceTable = sourceTable;
    }

    @Autowired
    public void setAliasesSettingsImpl(AliasesSettingsImpl aliasesSettingsImpl) {
        this.aliasesSettingsImpl = aliasesSettingsImpl;
    }

    @Autowired
    public void setAliasesRepo(AliasesRepo aliasesRepo) {
        this.aliasesRepo = aliasesRepo;
    }
}
