package com.ppsdevelopment.controller;

import com.google.gson.Gson;
import com.ppsdevelopment.config.ConfigProperties;
import com.ppsdevelopment.controller.annotations.Action;
import com.ppsdevelopment.controller.requestclass.ApplyFilter;
import com.ppsdevelopment.datalib.TableCellRequest;
import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.envinronment.UsersSettingsRepository;
import com.ppsdevelopment.json.MapJson;
import com.ppsdevelopment.service.FilterQuery;
import com.ppsdevelopment.service.SourceTableImpl;
import com.ppsdevelopment.tmctypeslib.DetectType;
import com.ppsdevelopment.tmctypeslib.FieldType;
import com.ppsdevelopment.viewlib.FilterHelper;
import com.ppsdevelopment.viewlib.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@RestController
@RequestMapping("tablerest")
public class MainPageRESTController {

    private SourceTableImpl sourceTable;
    private PaginationHelper paginationHelper;
    private FilterHelper filterHelper;
    private static final Map<String, Method> actions = new HashMap<>();

    static
    {
        for (Method m : MainPageRESTController.class.getDeclaredMethods())
        {
            if (m.isAnnotationPresent(Action.class))
            {
                Action cmd = m.getAnnotation(Action.class);
                actions.put(cmd.name(), m);
            }
        }
    }

    @PostMapping(value="/setcell", consumes = "application/json;charset=UTF-8")
    public @ResponseBody String setCell(@RequestBody TableCellRequest data){
        String fieldName=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldalias();
        String fieldType=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldtype();
        if (DetectType.isValueValid(FieldType.valueOf(fieldType),data.getValue())){
            sourceTable.updateFieldValue(Long.valueOf(data.getId()),data.getValue(), fieldName, FieldType.valueOf(fieldType));
        }
        return data.toString();
    }

    @PostMapping("/pagination")
    public @ResponseBody String pagination(@RequestBody String s) throws Exception {
        try {
            String action = Objects.requireNonNull(MapJson.get("action", s)).asText();
            Method m = actions.get(action);
            if (m != null)
            {
                String[] nargs= new String[] {s};
                Pagination pagination= (Pagination) m.invoke(this, nargs);
                String tableData=sourceTable.getResultAsJSONLine(sourceTable.getAll()); //paginationName
                FilterQuery filterItem=filterHelper.getFilter(sourceTable.getFilterName());
                return sourceTable.getPaginationJsonResponse(tableData,pagination,filterItem);
            }
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/filterdata")
    public @ResponseBody String filter(@RequestBody String s) throws Exception {
        Gson gson = new Gson();
        int cn = Objects.requireNonNull(MapJson.get("columnNumber", s)).asInt();
        List lines=sourceTable.getColumnUniqValues(--cn);
        //List response=
        return gson.toJson(lines);
    }

    @PostMapping("/applyfilter")
    public @ResponseBody String applyFilter(@RequestBody ApplyFilter request) throws Exception {
        setFilter(request.getColumnNumber()-1, request.getData());
        Pagination pagination=paginationHelper.getPagination(sourceTable.getPaginationName());
        pagination.setRecordsCount(sourceTable.getCount());
        pagination.setCurrentPage(1);
        pagination.setFirstPage(1);
        String tableData=sourceTable.getResultAsJSONLine(sourceTable.getAll());
        FilterQuery filterItem=filterHelper.getFilter(sourceTable.getFilterName());
        return sourceTable.getPaginationJsonResponse(tableData,pagination,filterItem); //TODO сделать этот метод, чтобы tableData,pagination,filterItem брались в классе sourceTable
    }

    private void setFilter(Integer columnNumber, String[] data) {
        FilterQuery filterItem=filterHelper.getFilter(sourceTable.getFilterName());
        filterItem.set(sourceTable.getAliases().get(columnNumber).getFieldalias(),Arrays.asList(data));
        filterHelper.setFilter(sourceTable.getFilterName(),filterItem);
    }

    @Action(name="pagesize")
    public Pagination mainTablePageSize(String s){
        Integer pageSizeNew= Objects.requireNonNull(MapJson.get("pagesize", s)).asInt();
        return paginationHelper.pageSize(pageSizeNew,sourceTable.getPaginationName());
    }

    @Action(name="setpage")
    public  Pagination setPage( String s) {
        Integer pageNumber= Objects.requireNonNull(MapJson.get("pagenumber", s)).asInt();
        return paginationHelper.setPage(pageNumber,sourceTable.getPaginationName());
    }

    @Action(name="setpageblock")
    public  Pagination setPageBlock(String s) {
        Integer pageNumber= Objects.requireNonNull(MapJson.get("pagenumber", s)).asInt();
        Integer firstPage= Objects.requireNonNull(MapJson.get("firstpage", s)).asInt();
        return paginationHelper.setPageBlock(pageNumber,firstPage,sourceTable.getPaginationName());
    }

    @Action(name="sortmaintable")
    public   Pagination sortMainPage(String s){
        Integer columnnumber= Objects.requireNonNull(MapJson.get("columnnumber", s)).asInt();
        return paginationHelper.sortPage(columnnumber,sourceTable.getAliases(),sourceTable.getPaginationName());
    }

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl sourceTable) {
        this.sourceTable = sourceTable;
    }

    @Autowired
    public void setPaginationHelper(PaginationHelper paginationHelper) {
        this.paginationHelper = paginationHelper;
    }


    @Autowired
    public void setFilterHelper(FilterHelper filterHelper) {
        this.filterHelper = filterHelper;
    }

}
