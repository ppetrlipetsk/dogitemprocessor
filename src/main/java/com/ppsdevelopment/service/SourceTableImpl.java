package com.ppsdevelopment.service;

import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.converters.DateFormatter;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.viewlib.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
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
    TablesService tablesService;

    @PersistenceContext
    private EntityManager em;

    /**
     * Возвращает строку, содержащую набор полей из коллекции aliases, для генерирования запроса в БД.
     * @param aliases - коллекция полей
     */
    public String getFieldsValuesLine(List<Aliases> aliases) {
        String fieldsLine= "id,".concat(tablesService.getColumnsList(aliases));
        String q="select "+fieldsLine+" from  zmm2021 c";
        List result = em.createNativeQuery(q).getResultList();
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
            return SqlQueryPreparer.getCaptionFromValue(field,DateFormatter.INTERNATIONAL_DATE_FORMAT,"/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getHeaderDataList(List<Aliases> aliases) {
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


}
