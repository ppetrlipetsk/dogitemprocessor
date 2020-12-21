package com.ppsdevelopment.controller;

import com.ppsdevelopment.controller.annotations.Action;
import com.ppsdevelopment.datalib.TableCellRequest;
import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.json.MapJson;
import com.ppsdevelopment.service.SourceTableImpl;
import com.ppsdevelopment.tmctypeslib.DetectType;
import com.ppsdevelopment.tmctypeslib.FieldType;
import com.ppsdevelopment.viewlib.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("tablerest")
public class MainPageRESTController {

    private SourceTableImpl sourceTable;
    private PaginationHelper paginationHelper;
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
    public @ResponseBody String pagination(@RequestBody String s){
        try {
            String action = Objects.requireNonNull(MapJson.get("action", s)).asText();
            Method m = actions.get(action);
            if (m != null)
            {
                String[] nargs= new String[] {s};
                Pagination pagination= (Pagination) m.invoke(this, nargs);
                String tableData=sourceTable.getResultAsArrayLine(sourceTable.getAll(pagination));
                return sourceTable.getPaginationJsonResponse(tableData,pagination);
            }
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Action(name="pagesize")
    public Pagination mainTablePageSize(String s){
        Integer pageSizeNew= Objects.requireNonNull(MapJson.get("pagesize", s)).asInt();
        return paginationHelper.pageSize(pageSizeNew);
    }

    @Action(name="setpage")
    public  Pagination setPage( String s) {
        Integer pageNumber= Objects.requireNonNull(MapJson.get("pagenumber", s)).asInt();
        return paginationHelper.setPage(pageNumber);
    }

    @Action(name="setpageblock")
    public  Pagination setPageBlock(String s) {
        Integer pageNumber= Objects.requireNonNull(MapJson.get("pagenumber", s)).asInt();
        Integer firstPage= Objects.requireNonNull(MapJson.get("firstpage", s)).asInt();
        return paginationHelper.setPageBlock(pageNumber,firstPage);
    }

    @Action(name="sortmaintable")
    public   Pagination sortMainPage(String s){
        Integer columnnumber= Objects.requireNonNull(MapJson.get("columnnumber", s)).asInt();
        return paginationHelper.sortMainPage(columnnumber,sourceTable.getAliases());
    }

    @Autowired
    public void setSourceTable(@Qualifier("SourceTableImpl") SourceTableImpl sourceTable) {
        this.sourceTable = sourceTable;
    }

    @Autowired
    public void setPaginationHelper(PaginationHelper paginationHelper) {
        this.paginationHelper = paginationHelper;
    }
}
