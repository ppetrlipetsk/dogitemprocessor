package com.ppsdevelopment.service;

import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.repos.TablesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class TablesService {
    @Autowired
    TablesRepo tablesRepository;

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

}
