package com.ppsdevelopment.service;

import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.config.ConfigProperties;
import com.ppsdevelopment.controller.requestclass.FilterDataClass;
import com.ppsdevelopment.converters.DateFormatter;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.repos.AliasesRepo;
import com.ppsdevelopment.repos.TablesRepo;
import com.ppsdevelopment.tmctypeslib.FieldType;
import com.ppsdevelopment.viewlib.FilterHelper;
import com.ppsdevelopment.viewlib.PaginationHelper;
import com.ppsdevelopment.viewlib.dataprepare.datalib.DataAdapter;
import com.ppsdevelopment.viewlib.dataprepare.datalib.HeaderAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.logging.Filter;

@Service("SourceTableImpl")
@Repository

public class SourceTableImpl {

    private TablesRepo tablesRepo;
    private AliasesRepo aliasesRepo;
    private PropertiesService propertiesService;
    //private Pagination pagination;
    private ConfigProperties configProperties;
    private HeaderAdapter headerAdapter;
    private PaginationHelper paginationHelper;
    private FilterHelper filterHelper;

    //private static final String PAGENAME="mainpage";
    private static final String FILTERNAME="filter";
    private static final String PAGINATIONNAME="pagination";

    private static final String WHERE_WORD="%where%";


    @PersistenceContext
    private EntityManager em;

    private  String tableName;
    private  Long tableId;
    private static List<Aliases> aliases;
    private static String aliasesStringList;
    private String tableHeader;

    @PostConstruct
    private void init(){
        tableName=configProperties.getTableName();
        List<Tables> table=tablesRepo.findByTablename(tableName);
        this.tableId=table.get(0).getId();
        aliases=aliasesRepo.getAllByTable(tableId);
        aliasesStringList="id,".concat(headerAdapter.getColumnsList(aliases));
        tableHeader= headerAdapter.getHeaderDataList(aliases);
    }


    private String getLimitsQuery(){
        Pagination pagination=paginationHelper.getPagination(getPaginationName());
        return String.valueOf((pagination.getCurrentPage()-1)*pagination.getPageSize());
    }

    private String getOrderQuery(){
        Pagination pagination=paginationHelper.getPagination(getPaginationName());
        String order=pagination.getSortColumnName();
        if (order==null) order="id";
        order+=((pagination.isSortDirection())?" ASC":" DESC");
        return order;
    }

    private String getFilterQuery() {
            FilterQuery filterQuery = filterHelper.getFilter(getFilterName());
            StringJoiner q=new StringJoiner(" and ");
            for (String key : filterQuery.getKeys()) {
                List values = filterQuery.get(key);
                StringJoiner s = new StringJoiner(" or ");
                if ((values!=null)&&(values.size()>0)) {
                    for (Object item : values) {
                        String s1 = null;
                        try {
                            s1 = key + "=" + SqlQueryPreparer.getCaptionFromValue(item.toString(), item.getClass(), DateFormatter.INTERNATIONAL_DATE_FORMAT, "/");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        s.add(s1);
                    }
                    q.add("(" + s + ")");
                }
            }
            return q.toString();
    }

    private String getFilterQueryWithoutColumn(String columnName) {
        FilterQuery filterQuery = filterHelper.getFilter(getFilterName());
        StringJoiner q=new StringJoiner(" and ");
        for (String key : filterQuery.getKeys()) {
            List values = filterQuery.get(key);
            if (!key.equals(columnName)) {
                StringJoiner s = new StringJoiner(" or ");
                if ((values != null) && (values.size() > 0)) {
                    for (Object item : values) {
                        String s1 = null;
                        try {
                            s1 = key + "=" + SqlQueryPreparer.getCaptionFromValue(item.toString(), item.getClass(), DateFormatter.INTERNATIONAL_DATE_FORMAT, "/");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        s.add(s1);
                    }
                    q.add("(" + s + ")");
                }
            }
        }
        return q.toString();
    }


    private String prepareQuery(String query){
            String filter = getFilterQuery();
            String order=getOrderQuery();
        return whereQueryReplacer(query,filter,order);
    }

    private String prepareQueryWithoutColumn(String columnName, String query){
        String filter = getFilterQueryWithoutColumn(columnName);
        String order=getOrderQuery();
        return whereQueryReplacer(query,filter,order);
    }

    private String whereQueryReplacer(String query, String filter, String order){
            if (query.contains(WHERE_WORD)) {
                String where=((filter!=null)&&(filter.length()>0))?" where "+filter:"";

                query = query.replace("%tablename%",tableName)
                        .replace("%order%", order)
                        .replace("%where%",where);
            }
            return query;
    }


    public List getAll() throws Exception {
        Pagination pagination=paginationHelper.getPagination(getPaginationName());
        String queryString=propertiesService.properties().getProperty("tableselectpageble");
        if ((queryString==null)||(queryString.length()==0)) throw new Exception("Ошибка чтения текста запроса получения данных таблицы "+tableName);
        queryString=prepareQuery(queryString);
        queryString=queryString
                .replace("%fields%", aliasesStringList)
                .replace("%from%",getLimitsQuery())
                .replace("%count%",Long.valueOf(pagination.getPageSize()).toString());
        try {
            return  em.createNativeQuery(queryString).getResultList();
        }
        catch (Exception e){
            System.out.println("Ошибка:"+e.toString());
        }

        return new ArrayList();
    }


    public Long getCount() throws Exception {
        String queryString=propertiesService.properties().getProperty("sourceTableCountQuery");
        if ((queryString==null)||(queryString.length()==0)) throw new Exception("Ошибка чтения текста запроса получения данных таблицы "+tableName);
        queryString=prepareQuery(queryString);

        queryString=queryString.replace("%tablename%",tableName);
        List result = em.createNativeQuery(queryString).getResultList();
        if ((result!=null)&&(result.size()>0))
            return ((Number)result.get(0)).longValue();
        return 0L;
    }

    public List<Aliases> getAliases() {
        return aliases;
    }

    @Transactional
    public void updateFieldValue(Long id, String value, String fieldName, FieldType fieldType) {
        String query=propertiesService.properties().getProperty("fieldupdate");
        Class<?> c=getClassForFieldType(fieldType);
        String s="";
        try {
            s=SqlQueryPreparer.getCaptionFromValue(value,c, DateFormatter.INTERNATIONAL_DATE_FORMAT,"/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        query= Objects.requireNonNull(query).replace("%tablename%",this.tableName).replace("%fieldname%",fieldName).replace("%value%",s).replace("%id%",id.toString());
        em.createNativeQuery(query).executeUpdate();

    }

    private Class<?> getClassForFieldType(FieldType t) {
        switch(t){
            case BIGINTTYPE: return Long.class;
            case INTTYPE:
            case DECIMALTYPE:
                return Double.class;
            case FLOATTYPE: return Float.class;
            case DATETYPE: return java.sql.Date.class;
            default: return String.class;
        }
    }

    public String getTableHeader() {
        return tableHeader;
    }

/*
    public String getPaginationJsonResponse(String datatable, Pagination pagination){
        String dataLine="\"datatable\":"+datatable;
        String paging="\"pagination\":{"+pagination.toValueString()+"}";
        return "{"+dataLine+","+paging+"}";
    }
*/

    public String getPaginationJsonResponse(String datatable, Pagination pagination, FilterQuery filter){
        String dataLine="\"datatable\":"+datatable;
        String paging="\"pagination\":{"+pagination.toValueString()+"}";
        String filterStr="\"filtercolumns\":"+filter.getColumnsNamesAsJson();
        return "{"+dataLine+","+paging+","+filterStr+"}";
    }

    public String getFilteredColumnsAsJson(){
        FilterQuery filterItem=filterHelper.getFilter(getFilterName());
        return filterItem.getColumnsNamesAsJson();
    }

    @Autowired
    public void setTablesRepo(TablesRepo tablesRepo) {
        this.tablesRepo = tablesRepo;
    }

    @Autowired
    public void setAliasesRepo(AliasesRepo aliasesRepo) {
        this.aliasesRepo = aliasesRepo;
    }


    @Autowired
    public void setPropertiesService(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    @Autowired
    public void setConfigProperties(ConfigProperties c){
        this.configProperties=c;
    }

    @Autowired
    public void setHeaderAdapter(HeaderAdapter headerAdapter) {
        this.headerAdapter = headerAdapter;
    }

    @Autowired
    public void setPaginationHelper(PaginationHelper paginationHelper) {
        this.paginationHelper = paginationHelper;
    }

    @Autowired
    public void setFilterHelper(FilterHelper filterHelper) {
        this.filterHelper = filterHelper;
    }


    public String getResultAsStringLine(List all) {
        return DataAdapter.getResultAsArrayLine(all);
    }

    public String getResultAsJSONLine(List all) {
        return DataAdapter.getResultAsJSONLine(all);
    }

    public List getColumnUniqValues(int cn) throws Exception {
        String cname=aliases.get(cn).getFieldalias();
        if (cname!=null){
            String queryString=propertiesService.properties().getProperty("columnuniqvalues");
            if ((queryString==null)||(queryString.length()==0)) throw new Exception("Ошибка чтения текста запроса получения данных таблицы "+tableName);
            queryString=prepareQueryWithoutColumn(cname,queryString);
            queryString=queryString
                    .replace("%column_name%", cname);
            List resultList= em.createNativeQuery(queryString).getResultList();
            List<FilterDataClass> list=new ArrayList<>();
            if (resultList!=null){
                for (Object item:resultList){
                    FilterDataClass line=new FilterDataClass();
                    line.setValue(item.toString());
                    boolean isChecked=getFilterChecked(cname,item.toString());
                    line.setChecked(isChecked);
                    list.add(line);
                }
            }
            return list;
        }
        else
            throw new Exception("Ошибочный номер столбца");
    }

    private boolean getFilterChecked(String fieldName, Object s) {
        FilterQuery filterQuery = filterHelper.getFilter(getFilterName());
        List list=filterQuery.get(fieldName);
        if (list!=null){
            return list.contains(s);
        }
        return false;
    }

    public String getFilterName(){
        return configProperties.getTableName()+"."+FILTERNAME;
    }

    public String getPaginationName(){
        return configProperties.getTableName()+"."+PAGINATIONNAME;
    }

}
