package com.ppsdevelopment.viewlib.dataprepare.statichelpers;

import com.ppsdevelopment.domain.Aliases;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;

public class HeaderGenerator {
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

    public static String getColumnsList(List<Aliases> aliases){
        Collector<Aliases, StringJoiner, String> aliasesCollector =
                Collector.of(
                        () -> new StringJoiner(" , "),          // supplier
                        (j, p) -> j.add(p.getFieldalias()),  // accumulator
                        (j1, j2) -> j1.merge(j2),               // combiner
                        StringJoiner::toString);                // finisher

        return aliases
                .stream()
                .collect(aliasesCollector);
    }

   /* public String getCheckTypesLine(List<Aliases> aliases) {
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
    }*/


}
