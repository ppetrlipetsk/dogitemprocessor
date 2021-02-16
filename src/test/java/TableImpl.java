import com.ppsdevelopment.service.databasetableimpl.tableImpl.TableClass;
import com.ppsdevelopment.service.viewservices.Pagination;

public class TableImpl extends TableClass {
    @Override
    protected void fillQueriesCollection() {
        queries.put("QUERY_SELECT_ALL", "tableselectpageble");
        queries.put("QUERY_COUNT","sourceTableCountQuery");
        queries.put("QUERY_UPDATE", "fieldupdate");
        queries.put("QUERY_UPDATE_FROM_CACHE", "fieldupdatefromcache");
    }

    @Override
    protected String replaceTagsGetAllQuery(String query, String aliasesStringList) {
        return "";
    }
    }
