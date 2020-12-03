import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.repos.AliasesRepo;
import com.ppsdevelopment.repos.TablesRepo;
import com.ppsdevelopment.service.SourceTableImpl;
import com.ppsdevelopment.service.res.ExTableDAOImpl;
import com.ppsdevelopment.viewlib.HeaderShaper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestSourceTable {
    @Autowired
    private TablesRepo tablesRepo;

    @Autowired
    AliasesRepo aliasesRepo;

    @Autowired
    private SourceTableImpl sourceTable;

    @Test
    public void test1() {
        List<Tables> table = tablesRepo.findByTablename("zmm2021");
        List<Aliases> aliases = aliasesRepo.getAllByTable(table.get(0).getId());

//        System.out.println(fieldsLine);

        String tableHeader = HeaderShaper.generateHeaderData(aliases);


        //model.put("tables4",aliases);

        sourceTable.displayAllContactSummary(aliases);

    }
}
