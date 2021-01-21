package com.ppsdevelopment.service.databasetableimpl.tableImpl;

import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.config.PropertiesService;
import com.ppsdevelopment.controller.requestclass.FilterDataClass;
import com.ppsdevelopment.converters.DateFormatter;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.envinronment.SettingsManager;
import com.ppsdevelopment.repos.AliasesRepo;
import com.ppsdevelopment.repos.TablesRepo;
import com.ppsdevelopment.service.FilterQuery;
import com.ppsdevelopment.service.databasetableimpl.tablecacheservice.*;
import com.ppsdevelopment.service.viewservices.Pagination;
import com.ppsdevelopment.tmctypeslib.FieldType;
import com.ppsdevelopment.viewlib.dataprepare.statichelpers.FilterHelper;
import com.ppsdevelopment.viewlib.PaginationHelper;
import com.ppsdevelopment.service.databasetableimpl.helpers.DataAdapter;
import com.ppsdevelopment.viewlib.dataprepare.statichelpers.HeaderGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.hibernate.jpa.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

public abstract class TableClass {
    protected Map<String,String> queries=new HashMap<>();
    protected TablesRepo tablesRepo;
    protected AliasesRepo aliasesRepo;
    protected PropertiesService propertiesService;
    protected PaginationHelper paginationHelper;
    protected UsersTablesCache usersTablesCache;
    protected SettingsManager settingsManager;

    @PersistenceContext
    protected EntityManager em;

    protected static final String FILTERNAME="filter";
    protected static final String PAGINATIONNAME="pagination";
    protected static final String WHERE_WORD="%where%";
    protected static final String TABLENAME_PROPERTY="tablename";
    protected static final String CACHABLE_PROPERTY="data.chachable";
    protected static final String TABLENAME_TAG="%tablename%";
    protected static final String FILTER_TAG="%filter%";
    protected static final String ORDER_TAG="%order%";
    protected static final String WHERE_TAG="%where%";
    protected static final String FIELDNAME_TAG="%fieldname%";
    protected static final String FIELDS_TAG="%fields%";
    protected static final String VALUE_TAG="%value%";
    protected static final String ID_TAG="%id%";
    protected static final String FIELDS_SET_TAG="%fieldsset%";
    protected static final String FROM_TAG="%from%";
    protected static final String COUNT_TAG="%count%";

    protected static final String QUERY_SELECT_ALL="query_select_all";
    protected static final String QUERY_COUNT="query_count";
    protected static final String QUERY_UPDATE="query_update";
    protected static final String QUERY_UPDATE_FROM_CACHE="query_update_from_cache";


    private   String tableName;
    private static List<Aliases> aliases;
    protected static String aliasesStringList;
    private String tableHeader;
    private boolean cachable;


    /* Public Methods block begin*/


    public Long getCount() {
        String queryString=getQuery(QUERY_COUNT);
        if ((queryString==null)||(queryString.length()==0)) return 0L;
        queryString=prepareQuery(queryString);
        queryString=queryString.replace(TABLENAME_TAG,tableName);
        List result = em.createNativeQuery(queryString).getResultList();
        if ((result!=null)&&(result.size()>0))
            return ((Number)result.get(0)).longValue();
        return 0L;
    }

    @Transactional
    public void updateFieldValue(Long id, String value, String fieldName, FieldType fieldType) {
        String query=getQuery(QUERY_UPDATE);
        Class<?> c=getClassForFieldType(fieldType);
        String s="";
        try {
            s=SqlQueryPreparer.getCaptionFromValue(value,c, DateFormatter.INTERNATIONAL_DATE_FORMAT,"/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        query= Objects.requireNonNull(query).replace(TABLENAME_TAG,this.tableName)
                .replace(FIELDNAME_TAG,fieldName)
                .replace(VALUE_TAG,s)
                .replace(ID_TAG,id.toString());
        em.createNativeQuery(query).executeUpdate();
    }

    @Transactional
    public void updateFieldValueFromCache() {
        TableCache tc=this.usersTablesCache.getTableCache();
        RowCollection rowCollection;
        if (tc!=null) rowCollection=tc.get(this.tableName);
        else return;
        if (rowCollection!=null) {
            Set<Long> keys = rowCollection.getKeys();
            for (Long key : keys) {
                saveCacheRowToDataBase(key, rowCollection.getRowValues(key));
            }
            tc.clear(this.tableName);
        }
    }

    public void updateFieldValueToCache(Long id, String value, String fieldName) {
        this.usersTablesCache.setTableRowFieldValue(this.tableName,id,fieldName,value);
    }

    public String getTableHeader() {
        return tableHeader;
    }


    public List getAll() throws Exception {
        String queryString=getQuery(QUERY_SELECT_ALL);
        if ((queryString==null)||(queryString.length()==0)) throw new Exception("Ошибка чтения текста запроса получения данных таблицы "+tableName);
        queryString=prepareQuery(queryString);
        queryString=replaceTagsGetAllQuery(queryString);
        try {
            List result=  em.createNativeQuery(queryString)
                    .setHint( "org.hibernate.cacheable", "true")
                    .setHint( QueryHints.HINT_CACHEABLE, "true")
                    .setHint( QueryHints.HINT_CACHE_REGION, "query.cache.sourcetable" )
                    .getResultList();
            if (cachable) return updateFromCache(result);
            else
            return result;
        }
        catch (Exception e){
            System.out.println("Ошибка:"+e.toString());
        }
        return new ArrayList();
    }

    private List updateFromCache(List result) {
        for (int i=0;i<result.size();i++){
            Object[] resultSet= (Object[]) result.get(i);
            long id= (((Number) resultSet[0])).longValue();
            RowFields rowFields=usersTablesCache.getTableRowFields(tableName,id);
            if (rowFields!=null){
                Set<String> keys=rowFields.getRecords().keySet();
                for (String key:keys){
                    int index=getAliasIndex(key);
                    resultSet[index+1]=rowFields.get(key);
                }
                result.set(i,resultSet);
            }
        }
        return result;
    }

    public String getJsonResponseForFilterApply() throws Exception { //String datatable, Pagination pagination, FilterQuery filter
        Pagination pagination=paginationHelper.getPagination(getPaginationName());
        String tableData=getResultAsJSONLine(this.getAll());
        FilterQuery filterItem=FilterHelper.getFilter(getFilterName(),settingsManager);
        String dataLine="\"datatable\":"+tableData;
        String paging="\"pagination\":{"+pagination.toValueString()+"}";
        String filterStr="\"filtercolumns\":"+filterItem.getColumnsNamesAsJson();
        return "{"+dataLine+","+paging+","+filterStr+"}";
    }

    public String getFilteredColumnsAsJson(){
        FilterQuery filterItem=FilterHelper.getFilter(getFilterName(),settingsManager);
        return filterItem.getColumnsNamesAsJson();
    }

    public void setActualPaginationValues() {
        Pagination pagination=paginationHelper.getPagination(getPaginationName());
        pagination.setRecordsCount(getCount());
        pagination.setCurrentPage(1);
        pagination.setFirstPage(1);
        paginationHelper.setPagination(getPaginationName(),pagination);
    }

    public String getResultAsStringLine(List all) {
        return DataAdapter.asJSON(all);
    }

    public String getResultAsJSONLine(List all) {
        return DataAdapter.getListAsJSONLine(all);
    }

    public List getColumnUniqValues(int cn) throws Exception {
        String cname=aliases.get(cn).getFieldalias();
        if (cname!=null){
            String queryString=propertiesService.get("columnuniqvalues");
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

    public String getPaginationName(){
        return this.tableName+"."+PAGINATIONNAME;
    }


    public int getAliasIndex(String key) {
        int index=0;
        for(Aliases aliasesItem:aliases){
            if (aliasesItem.getFieldalias().equals(key)) return index;
            index++;
        }
        return -1;
    }

    /* Properties block begin */

    public String getFilterName(){
        return this.tableName+"."+FILTERNAME;
    }

    public List<Aliases> getAliases() {
        return aliases;
    }
    /* Properties block end*/

    public String getTableName() {
        return this.tableName;
    }

    /* Public Methods block end*/

    protected String getQuery(String queryName){
        if (queries.containsKey(queryName)) return queries.get(queryName);
        else
            throw new RuntimeException("Query "+queryName+" not found!");
    }

    @PostConstruct
    protected void init(){
        tableName=propertiesService.get(TABLENAME_PROPERTY);
        cachable=Boolean.valueOf(propertiesService.get(CACHABLE_PROPERTY));
        List<Tables> table=tablesRepo.findByTablename(tableName);
        Long tableId=null;
        if ((table!=null)&&(table.size()>0))  tableId=table.get(0).getId();
        else
            throw new RuntimeException("Table"+tableName+" not found!");
        aliases=aliasesRepo.getAllByTable(tableId);
        aliasesStringList="id,".concat(HeaderGenerator.getColumnsList(aliases));
        tableHeader= HeaderGenerator.getHeaderDataList(aliases);
        fillQueriesCollection();
    }

    protected abstract void fillQueriesCollection();

    protected String getLimitsQuery(){
        Pagination pagination=paginationHelper.getPagination(getPaginationName());
        return String.valueOf((pagination.getCurrentPage()-1)*pagination.getPageSize());
    }

    private String getOrderQuery(){
        Pagination pagination=paginationHelper.getPagination(getPaginationName());
        String order=pagination.getSortColumnName();
        boolean isId=false;
        if (order==null) {
            order = "id";
            isId=true;
        }
        order+=((pagination.isSortDirection())?" ASC":" DESC");
        if (!isId) order+=", id ASC";
        return order;
    }

    private String getFilterQuery() {
        FilterQuery filterQuery = FilterHelper.getFilter(getFilterName(),settingsManager);
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
        FilterQuery filterQuery = FilterHelper.getFilter(getFilterName(),settingsManager);
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

    protected String prepareQuery(String query){
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
            query = query.replace(TABLENAME_TAG,tableName)
                    .replace(ORDER_TAG, order)
                    .replace(WHERE_TAG,where);
        }
        return query;
    }

    protected abstract String replaceTagsGetAllQuery(String query);

    private void saveCacheRowToDataBase(Long id, RowFields rowValues) {
        Map<String,Object> items=rowValues.getCollection();
        Set<String> keys=items.keySet();
        StringJoiner queryValues=new StringJoiner(",");
        for(String key:keys){
            queryValues.add(getQueryForCacheUpdate(key,rowValues.get(key)));
        }
        String query=getQuery(QUERY_UPDATE_FROM_CACHE);
        query= Objects.requireNonNull(query)
                .replace(TABLENAME_TAG,this.tableName)
                .replace(FIELDS_SET_TAG,queryValues.toString())
                .replace(ID_TAG,id.toString());
        em.createNativeQuery(query).executeUpdate();
    }

    private CharSequence getQueryForCacheUpdate(String key, Object value) {
        Class<?> c = String.class;
        if (value!=null)
            c=getClassForFieldValue(value);
        else
            getClassForFieldAlias(key);

        String s="";
        try {
            value=value==null?"":value;
            s=SqlQueryPreparer.getCaptionFromValue((String) value,c, DateFormatter.INTERNATIONAL_DATE_FORMAT,"/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key+"="+s;
    }

    private Class<?> getClassForFieldAlias(String key) {
        int indx= getAliasIndex(key);
        return getClassForFieldType(FieldType.valueOf(aliases.get(indx).getFieldtype()));
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

    private Class<?> getClassForFieldValue(Object object) {
        Class<?> c=object.getClass();
        if (Integer.class.equals(c) || Long.class.equals(c)) {
            return Long.class;
        } else if (Double.class.equals(c)) {
            return Double.class;
        } else if (Float.class.equals(c)) {
            return Float.class;
        } else if (Date.class.equals(c) || Calendar.class.equals(c) || java.sql.Date.class.equals(c)) {
            return java.sql.Date.class;
        } else {
            return String.class;
        }
    }

    private boolean getFilterChecked(String fieldName, Object s) {
        FilterQuery filterQuery = FilterHelper.getFilter(getFilterName(),settingsManager);
        List list=filterQuery.get(fieldName);
        if (list!=null){
            return list.contains(s);
        }
        return false;
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
    public void setPaginationHelper(PaginationHelper paginationHelper) {
        this.paginationHelper = paginationHelper;
    }

    @Autowired
    public void setUsersTablesCache(UsersTablesCache usersTablesCache) {
        this.usersTablesCache = usersTablesCache;
    }

    @Autowired
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }


}
