package com.ppsdevelopment.service;

import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.converters.DateFormatter;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.viewlib.ClassRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service("SourceTableImpl")
@Repository

public class SourceTableImpl {
    @Autowired
    TablesService tablesService;

    @PersistenceContext
    private EntityManager em;
    public void displayAllContactSummary(List<Aliases> aliases) {
        String fieldsLine= "id,".concat(tablesService.getColumnsList(aliases));
        String q="select "+fieldsLine+" from  zmm2021 c";

        List result = em.createNativeQuery(q).getResultList();

        int count = 0;
        //(Arrays.asList((Object[]) result.get(0))).get(3).getClass()

        for (Iterator i = result.iterator(); i.hasNext();) {
            Object[] values = (Object[]) i.next();
            System.out.println("#" + ++count + ": " +values[0] + ", " + values[1] +values[70]+ ", " +
                    values[2]);
        }
        String s=getDataString(result);
        System.out.println(s);
    }

    private String getDataString(List lines){

        Collector<Object, StringJoiner, String> lineCollector =
                Collector.of(
                        () -> new StringJoiner(" , "),          // supplier
                        (j, p) -> j.add(getValue(p)),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher



        String s="["+lines.stream().collect(lineCollector).toString()+"]";


/*
        for(Object line:lines){
            s.append(Arrays.asList(line).stream().collect(valuesCollector));
        }
*/

/*
        String fieldsLine = aliases
                .stream()
                .collect(aliasesCollector);
        return fieldsLine;
*/
    return s;
    }

    private String getValue(Object item) {
        List lines=Arrays.asList(((Object[]) item));
       /* Object[] fields=((Object[]) item);
        StringBuilder s=new StringBuilder();
        for (int i=0;i<fields.length;i++){
            s.append(getFieldValueStr(fields[i]));
        }*/

        Collector<Object, StringJoiner, String> valuesCollector =
                Collector.of(
                        () -> new StringJoiner(" , "),          // supplier
                        (j, p) -> j.add(getFieldValueStr(p)),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        String fieldsLine = lines
                .stream()
                .collect(valuesCollector).toString();
        System.out.println(ClassRepository.classesRepository.toString());
        return "["+fieldsLine+"]";
    }


    private String getFieldValueStr(Object field) {
/*
        String s="";
        System.out.println((field.getClass()));
*/
        Class<?> className=field.getClass();
        try {
            return SqlQueryPreparer.getCaptionFromValue(field,DateFormatter.INTERNATIONAL_DATE_FORMAT,"/");
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        //ClassRepository.classesRepository.add(field.getClass().getCanonicalName());
        if ("java.sql.Date".equals(className)) return DateFormatter.convertDateFormat();

                break;
        }
*/
        return "";
        //"class java.lang.Integer"
        //java.lang.Double, java.math.BigInteger, java.lang.String, java.lang.Integer, java.sql.Date
    }

}
