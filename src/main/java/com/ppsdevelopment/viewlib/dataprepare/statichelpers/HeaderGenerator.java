package com.ppsdevelopment.viewlib.dataprepare.statichelpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.dictclasses.AliasSettings;
import com.ppsdevelopment.domain.dictclasses.ColumnItem;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collector;

public class HeaderGenerator {

    public static String getHeaderDataList(List<Aliases> aliases, Map<Long, AliasSettings> settingsMap) {
        LinkedList<ColumnItem> list=new LinkedList<>();
        for (Aliases alias : aliases){
            Long id=alias.getId();
            AliasSettings settings=settingsMap.get(id);
            if (settings == null || (settings.isVisibility())){
                Integer cWidth=getColumnWith(alias, settings);
                if (settings.getColumnWidth()!=cWidth) settings.setColumnWidth(cWidth);
                ColumnItem item=new ColumnItem();
                item.setId(alias.getId());
                //item.setColumnStyle(cWidth);
                item.setColumnWidth(cWidth);
                item.setFieldName(alias.getFieldname());
                item.setFieldAlias(alias.getFieldalias());
                item.setFieldType(alias.getFieldtype());
                item.setColumnStyle(getColumnStyle(alias, settings));
                item.setColumnClass(getColumnClass(alias,settings));
                list.add(item);
            }
            //s.add(alias.toCellJSon());
        }
        //resultStr.append(s).append("]");
        ObjectMapper mapper=new ObjectMapper();
        StringWriter writer=new StringWriter();
        String line="";
        try {
            mapper.writeValue(writer,list.toArray());
            line=writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;


    }

    private static String getColumnStyle(Aliases alias, AliasSettings settings) {
        String cClass=null;
        if (settings!=null)
            cClass=settings.getColumnStyle();
        if (isEmpty(cClass))
            cClass=alias.getColumnstyle();
        if (isEmpty(cClass)) {
            cClass ="";
        }
        return cClass;
    }

    private static String getColumnClass(Aliases alias, AliasSettings settings) {
        String cClass=null;
        if (settings!=null)
            cClass=settings.getColumnClass();
        if (isEmpty(cClass))
            cClass=alias.getColumnclass();
        if (isEmpty(cClass)) {
            cClass ="";
        }
        return cClass;
    }

    private static boolean isEmpty(String cClass){
        return (cClass==null)||(cClass.length()==0);
    }

    private static boolean isEmpty(Integer cStyle){
        return (cStyle==null)||(cStyle==0)||(cStyle==-1);
    }

    private static Integer getColumnWith(Aliases alias, AliasSettings settings){
        Integer cWidth=null;
        if (settings!=null)
            cWidth=settings.getColumnWidth();
        if (isEmpty(cWidth))
            cWidth=alias.getColumnwidth();
        if (isEmpty(cWidth)) {
            cWidth =settings.getDefaultWidth(alias);
        }
        return cWidth;
    }

   /* public static String getHeaderDataListJson(List<Aliases> aliases, Map<Long, AliasSettings> settingsMap) {
        //StringBuilder resultStr=new StringBuilder();
        //resultStr.append("[");
        //StringJoiner s=new StringJoiner(",");
        LinkedList<ColumnItem> list=new LinkedList<>();
        for (Aliases alias : aliases){
            Long id=alias.getId();
            AliasSettings settings=settingsMap.get(id);
            if (settings == null || (settings.isVisibility())){
                Integer cWidth=getColumnWith(alias, settings);
                settings.setColumnWidth(cWidth);
                ColumnItem item=new ColumnItem();
                item.setId(alias.getId());
                //item.setColumnStyle(cWidth);
                item.setColumnWidth(cWidth);
                item.setFieldName(alias.getFieldname());
                item.setFieldAlias(alias.getFieldalias());
                list.add(item);
            }
                //s.add(alias.toCellJSon());
        }
        //resultStr.append(s).append("]");
        ObjectMapper mapper=new ObjectMapper();
        StringWriter writer=new StringWriter();
        String line="";
        try {
            mapper.writeValue(writer,list.toArray());
            line=writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
                //resultStr.toString();
    }
*/
   /* private static Integer getDefaultStyle(Aliases alias) {
        Integer w=null;
        if (alias!=null){
        if ( (alias.getFieldtype().equals(BIGINTTYPE.toString()))
                || (alias.getFieldtype().equals(INTTYPE.toString()))
                || (alias.getFieldtype().equals(DECIMALTYPE.toString()))
                || (alias.getFieldtype().equals(FLOATTYPE.toString()))
                || (alias.getFieldtype().equals(DATETYPE.toString()))
        )    w=80;
        else
             w=200;
        }
        return w;
    }*/

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
