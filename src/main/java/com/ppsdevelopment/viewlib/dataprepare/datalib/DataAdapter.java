package com.ppsdevelopment.viewlib.dataprepare.datalib;

import com.ppsdevelopment.SqlQueryPreparer;
import com.ppsdevelopment.converters.DateFormatter;
import com.ppsdevelopment.viewlib.ClassRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collector;

public class DataAdapter {

    public static String getResultAsArrayLine(List lines){
        String s="["+lines.stream().collect(getCollector((p)->getQueryValues(p))).toString()+"]";
        return s;
    }

    private static String getQueryValues(Object item) {
        List lines= Arrays.asList(((Object[]) item));
        String fieldsLine = lines
                .stream()
                .collect(getCollector((p)->getFieldValueAsString(p))).toString();
        System.out.println(ClassRepository.classesRepository.toString());
        return "["+fieldsLine+"]";
    }

    private static Collector<Object, StringJoiner, String> getCollector(Function<Object,String> action){
        return Collector.of(
                () -> new StringJoiner(" , "),          // supplier
                (j, p) -> j.add(action.apply(p)),  // accumulator
                (j1, j2) -> j1.merge(j2),               // combiner
                StringJoiner::toString);                // finisher
    }

    private static String getFieldValueAsString(Object field) {
        try {
            return SqlQueryPreparer.getCaptionFromValue(field, DateFormatter.INTERNATIONAL_DATE_FORMAT,"/","\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
