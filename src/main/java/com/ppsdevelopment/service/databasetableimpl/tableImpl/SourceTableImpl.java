package com.ppsdevelopment.service.databasetableimpl.tableImpl;

import com.ppsdevelopment.service.viewservices.Pagination;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service("SourceTableImpl")
@Repository

public class SourceTableImpl  extends TableClass{

    @Override
    protected void fillQueriesCollection() {
        queries.put(QUERY_SELECT_ALL, propertiesService.get("tableselectpageble"));
        queries.put(QUERY_COUNT, propertiesService.get("sourceTableCountQuery"));
        queries.put(QUERY_UPDATE, propertiesService.get("fieldupdate"));
        queries.put(QUERY_UPDATE_FROM_CACHE, propertiesService.get("fieldupdatefromcache"));
        queries.put(QUERY_SELECT_TOP, propertiesService.get("tableselecttop"));

    }

    @Override
    protected String replaceTagsGetAllQuery(String query, String aliasesStringList) {
        Pagination pagination= paginationHelper.getPagination(this.getPaginationName());
        return query
                .replace(FIELDS_TAG, aliasesStringList)
                .replace(FROM_TAG,getLimitsQuery())
                .replace(COUNT_TAG,Long.valueOf(pagination.getPageSize()).toString());
    }

}
