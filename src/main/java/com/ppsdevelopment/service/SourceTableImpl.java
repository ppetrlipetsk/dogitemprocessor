package com.ppsdevelopment.service;

import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.config.ConfigProperties;
import com.ppsdevelopment.converters.DateFormatter;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.envinronment.Pagination;
import com.ppsdevelopment.repos.AliasesRepo;
import com.ppsdevelopment.repos.TablesRepo;
import com.ppsdevelopment.tmctypeslib.FieldType;
import com.ppsdevelopment.viewlib.dataprepare.datalib.DataAdapter;
import com.ppsdevelopment.viewlib.dataprepare.datalib.HeaderAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Service("SourceTableImpl")
@Repository

public class SourceTableImpl {

    private TablesRepo tablesRepo;
    private AliasesRepo aliasesRepo;
    private PropertiesService propertiesService;
    //private Pagination pagination;
    private ConfigProperties configProperties;
    private HeaderAdapter headerAdapter;


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

    public List getAll(Pagination pagination){
        String from=String.valueOf((pagination.getCurrentPage()-1)*pagination.getPageSize());
        String queryString=propertiesService.properties().getProperty("tableselectpageble");
        String order=pagination.getSortColumnName();
        if (order==null) order="id";
        order+=((pagination.isSortDirection())?" ASC":" DESC");
        queryString=queryString.replace("%fields%", aliasesStringList).replace("%tablename%",tableName).replace("%from%",from).replace("%count%",Long.valueOf(pagination.getPageSize()).toString()).replace("%order%", order);
        return em.createNativeQuery(queryString).getResultList();
    }

    public Long getCount() {
        String queryString=propertiesService.properties().getProperty("sourceTableCountQuery");
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
    public Long getTableId() {
        return tableId;
    }
*/

    public String getPaginationJsonResponse(String datatable, Pagination pagination){
        String dataLine="\"datatable\":"+datatable;
        //.replace("\"","\\\"")
        String paging="\"pagination\":{"+pagination.toValueString()+"}";
        return "{"+dataLine+","+paging+"}";
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

    public String getResultAsStringLine(List all) {
        return DataAdapter.getResultAsArrayLine(all);
    }

    public String getResultAsJSONLine(List all) {
        return DataAdapter.getResultAsJSONLine(all);
    }

}
