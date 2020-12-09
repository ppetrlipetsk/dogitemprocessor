package com.ppsdevelopment.service;

import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.converters.DateFormatter;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.repos.AliasesRepo;
import com.ppsdevelopment.repos.TablesRepo;
import com.ppsdevelopment.tmctypeslib.DetectType;
import com.ppsdevelopment.tmctypeslib.FieldType;
import com.ppsdevelopment.viewlib.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;

@Service("SourceTableImpl")
@Repository

public class SourceTableImpl {

    @Autowired
    TablesRepo tablesRepo;

    @Autowired
    AliasesRepo aliasesRepo;

    @Autowired
    PropertiesService propertiesService;

    @Qualifier("AliasesImpl")
    @Autowired
    AliasesImpl aliasesImpl;

    @PersistenceContext
    private EntityManager em;

    private  String tableName;
    private  Long tableId;
    private static List<Aliases> aliases;
    private static String aliasesStringList;


    @PostConstruct
    private void init(){
        tableName=propertiesService.properties().getProperty("tablename");
        List<Tables> table=tablesRepo.findByTablename(tableName);
        this.tableId=table.get(0).getId();
        aliases=aliasesRepo.getAllByTable(tableId);
        aliasesStringList="id,".concat(getColumnsList(aliases));
    }

    /**
     * Возвращает строку, содержащую набор значений полей из коллекции aliases, для вывода в представление.
     */
    public String getFieldsValuesLine() {
        //String fieldsLine= "id,".concat(getColumnsList(aliases));
        String queryString=propertiesService.properties().getProperty("tableselect");
        queryString=queryString.replace("%fields%",this.aliasesStringList).replace("%tablename%",tableName);
//        String q="select "+fieldsLine+" from  zmm2021 c";
        List result = em.createNativeQuery(queryString).getResultList();
        return getDataString(result);
    }

    private String getDataString(List lines){
        Collector<Object, StringJoiner, String> lineCollector =
                Collector.of(
                        () -> new StringJoiner(" , "),  // supplier
                        (j, p) -> j.add(getQueryValues(p)),           // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher
        String s="["+lines.stream().collect(lineCollector).toString()+"]";
    return s;
    }

    private String getQueryValues(Object item) {
        List lines=Arrays.asList(((Object[]) item));
        Collector<Object, StringJoiner, String> valuesCollector =
                Collector.of(
                        () -> new StringJoiner(" , "),          // supplier
                        (j, p) -> j.add(getFieldValueAsString(p)),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String fieldsLine = lines
                .stream()
                .collect(valuesCollector).toString();
        System.out.println(ClassRepository.classesRepository.toString());
        return "["+fieldsLine+"]";
    }


    private String getFieldValueAsString(Object field) {
        try {
            return SqlQueryPreparer.getCaptionFromValue(field,DateFormatter.INTERNATIONAL_DATE_FORMAT,"/","\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getHeaderDataList() {
        StringBuilder resultStr=new StringBuilder();
        resultStr.append("[");

        Collector<Aliases, StringJoiner, String> aliasesCollector =
                Collector.of(
                        () -> new StringJoiner(" , "),          // supplier
                        (j, p) -> j.add(p.toCellString()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String fieldsLine = aliases
                .stream()
                .collect(aliasesCollector);
        resultStr.append(fieldsLine).append("]");
        return resultStr.toString();
    }

    public String getColumnsList(List<Aliases> aliases){
        Collector<Aliases, StringJoiner, String> aliasesCollector =
                Collector.of(
                        () -> new StringJoiner(" , "),          // supplier
                        (j, p) -> j.add(p.getFieldalias()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String fieldsLine = aliases
                .stream()
                .collect(aliasesCollector);
        return fieldsLine;
    }

    public List<Aliases> getAliases() {
        return aliases;
    }

    public void setAliases(List<Aliases> aliases) {
        SourceTableImpl.aliases = aliases;
    }

    public boolean isDataValid(String value, String fieldType) {
        return DetectType.isValueValid(FieldType.valueOf(fieldType),value);
    }

    @Transactional
    public void updateFieldValue(Long id, String value, String fieldName, FieldType fieldType) {
        String query=propertiesService.properties().getProperty("fieldupdate");
        Class<?> c=getClassForFieldType(fieldType);
        String s=null;
        try {
            s=SqlQueryPreparer.getCaptionFromValue(value,c, DateFormatter.INTERNATIONAL_DATE_FORMAT,"/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        query=query.replace("%tablename%",this.tableName).replace("%fieldname%",fieldName).replace("%value%",s).replace("%id%",id.toString());
        em.createNativeQuery(query).executeUpdate();

    }

    private Class<?> getClassForFieldType(FieldType t) {
        switch(t){
            case BIGINTTYPE: return Long.class;
            case INTTYPE: return Double.class;
            case DECIMALTYPE: return Double.class;
            case FLOATTYPE: return Float.class;
            case DATETYPE: return java.sql.Date.class;
            default: return String.class;
        }
    }

    public String getCheckTypesLine() {
        StringBuilder resultStr=new StringBuilder();
        resultStr.append("[");

        Collector<Aliases, StringJoiner, String> aliasesCollector =
                Collector.of(
                        () -> new StringJoiner(" , "),          // supplier
                        (j, p) -> j.add(p.toCellString()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String fieldsLine = aliases
                .stream()
                .collect(aliasesCollector);
        resultStr.append(fieldsLine).append("]");
        return resultStr.toString();
    }

    public String getFieldsValuesLine(Integer pageNumber, Integer pageSize) {
        String from=String.valueOf((pageNumber-1)*pageSize);
        String queryString=propertiesService.properties().getProperty("tableselectpageble");
        queryString=queryString.replace("%fields%",this.aliasesStringList).replace("%tablename%",tableName).replace("%from%",from).replace("%count%",pageSize.toString());
        List result = em.createNativeQuery(queryString).getResultList();
        return getDataString(result);
    }
}
