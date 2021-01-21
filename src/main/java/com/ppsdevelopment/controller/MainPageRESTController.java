package com.ppsdevelopment.controller;

import com.google.gson.Gson;
import com.ppsdevelopment.controller.annotations.Action;
import com.ppsdevelopment.controller.requestclass.ApplyFilter;
import com.ppsdevelopment.controller.requestclass.TableCellRequest;
import com.ppsdevelopment.envinronment.SettingsManager;
import com.ppsdevelopment.service.viewservices.Pagination;
import com.ppsdevelopment.service.FilterQuery;
import com.ppsdevelopment.service.databasetableimpl.tableImpl.SourceTableImpl;
import com.ppsdevelopment.tmctypeslib.DetectType;
import com.ppsdevelopment.tmctypeslib.FieldType;
import com.ppsdevelopment.viewlib.dataprepare.statichelpers.FilterHelper;
import com.ppsdevelopment.viewlib.PaginationHelper;
import com.ppsdevelopment.service.databasetableimpl.helpers.DataAdapter;
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
    private SettingsManager settingsManager;
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

/*
    @PostMapping(value="/setcell", consumes = "application/json;charset=UTF-8")
    public @ResponseBody String setCell(@RequestBody TableCellRequest data){
        String fieldName=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldalias();
        String fieldType=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldtype();
        if (DetectType.isValueValid(FieldType.valueOf(fieldType),data.getValue())){
            sourceTable.updateFieldValue(Long.valueOf(data.getId()),data.getValue(), fieldName, FieldType.valueOf(fieldType));
        }
        return data.toString();
    }
*/

    /*@PostMapping(value="/setcachedcell", consumes = "application/json;charset=UTF-8")*/
    @PostMapping(value="/setcell", consumes = "application/json;charset=UTF-8")
    public @ResponseBody String setCachedCell(@RequestBody TableCellRequest data){
        String fieldName=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldalias();
        String fieldType=sourceTable.getAliases().get(data.getFieldIndex()-1).getFieldtype();
        if (DetectType.isValueValid(FieldType.valueOf(fieldType),data.getValue())){
            sourceTable.updateFieldValueToCache(Long.valueOf(data.getId()),data.getValue(), fieldName);
        }
        return data.toString();
    }

    @PostMapping(value="/save", consumes = "application/json;charset=UTF-8")
    public @ResponseBody String save(){
            sourceTable.updateFieldValueFromCache();
        return "data.toString()";
    }


    @PostMapping("/paginations/{action}")
    public @ResponseBody String pagination(@PathVariable String action, @RequestBody String s) throws Exception {
        System.out.println(action+s);
        Gson g=new Gson();
        Pagination p=g.fromJson(s,Pagination.class);
        System.out.println(p.getPageSize());
        return action;
    }

    @PostMapping("/pagination/{action}")
    public @ResponseBody String pagination(@PathVariable String action, @RequestBody Pagination pag) throws Exception {
        try {
            //String action = Objects.requireNonNull(DataAdapter.valueFromJson("action", s).toString());
            Method m = actions.get(action);
            if (m != null)
            {
                Object[] nargs= new Object[] {pag};
                Pagination pagination= (Pagination) m.invoke(this, nargs);
                paginationHelper.setPagination(sourceTable.getPaginationName(),pagination);
                return sourceTable.getJsonResponseForFilterApply();
            }
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/filterdata")
    public @ResponseBody String filter(@RequestBody String s) throws Exception {
        sourceTable.updateFieldValueFromCache();
        Gson gson = new Gson();
        int cn = Objects.requireNonNull(Integer.valueOf(DataAdapter.valueFromJson("columnNumber", s).toString()));
        List lines=sourceTable.getColumnUniqValues(--cn);
        return gson.toJson(lines);
    }

    @PostMapping("/applyfilter")
    public @ResponseBody String applyFilter(@RequestBody ApplyFilter request) throws Exception {
        setFilter(request.getColumnNumber()-1, request.getData());
        sourceTable.setActualPaginationValues();
        return sourceTable.getJsonResponseForFilterApply();
    }

    private void setFilter(Integer columnNumber, String[] data) {
        FilterQuery filterItem=FilterHelper.getFilter(sourceTable.getFilterName(),settingsManager);
        filterItem.set(sourceTable.getAliases().get(columnNumber).getFieldalias(),Arrays.asList(data));
        FilterHelper.setFilter(sourceTable.getFilterName(),filterItem,settingsManager);
    }

    @PostMapping("/savecache")
    public @ResponseBody String saveCache(){
            sourceTable.updateFieldValueFromCache();
        return "";
    }


    @Action(name="pagesize")
    public Pagination mainTablePageSize(Pagination pagination){
        //Integer pageSizeNew= Objects.requireNonNull(Integer.valueOf(DataAdapter.valueFromJson("pagesize", s).toString()));
        return paginationHelper.pageSize(pagination.getPageSize(),sourceTable.getPaginationName());
    }

    @Action(name="setpage")
    public  Pagination setPage( Pagination p) {
        Integer pageNumber=(int)p.getCurrentPage();
                //Objects.requireNonNull(Integer.valueOf(DataAdapter.valueFromJson("pagenumber", s).toString()));
        return paginationHelper.setPage(pageNumber,sourceTable.getPaginationName());
    }

    @Action(name="setpageblock")
    public  Pagination setPageBlock(Pagination pag) {
/*
        Integer pageNumber= Objects.requireNonNull(Integer.valueOf(DataAdapter.valueFromJson("pagenumber", s).toString()));
        Integer firstPage= Objects.requireNonNull(Integer.valueOf(DataAdapter.valueFromJson("firstpage", s).toString()));
*/
        return paginationHelper.setPageBlock((int)pag.getCurrentPage(),pag.getFirstPage(),sourceTable.getPaginationName());
    }

    @Action(name="sorttable")
    public   Pagination sortMainPage(Pagination pag){
//        Integer columnnumber= Objects.requireNonNull(Integer.valueOf(DataAdapter.valueFromJson("columnnumber", s).toString()));
        sourceTable.updateFieldValueFromCache();
        return paginationHelper.sortPage(pag.getSortColumnNumber(),sourceTable.getAliases(),sourceTable.getPaginationName());
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
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}
