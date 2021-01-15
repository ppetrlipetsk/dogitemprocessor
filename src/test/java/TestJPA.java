import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.Tables;
import com.ppsdevelopment.domain.reserv.ETable;
import com.ppsdevelopment.domain.reserv.ExTable;
import com.ppsdevelopment.helpers.StringHelper;
import com.ppsdevelopment.repos.AliasesRepo;
import com.ppsdevelopment.repos.TablesRepo;
import com.ppsdevelopment.service.tableImpl.SourceTableImpl;
import com.ppsdevelopment.service.res.ExTableDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJPA {
    @Autowired
    private TablesRepo tablesRepo;


    @Autowired
    ExTableDAOImpl exTableDAO;


    @Autowired
    AliasesRepo aliasesRepo;

    private SourceTableImpl sourceTable;

    public void test(){
        Map<String, Object> model=new HashMap<>();
        model.put("tableitems", StringHelper.getTableItems(ETable.getCells()));
        List<Tables> table=tablesRepo.findByTablename("zmm2021");
        List<Aliases> aliases=aliasesRepo.getAllByTable(table.get(0).getId());
        model.put("tables",table);
        //sourceTable.displayAllContactSummary(aliases);

        List<ExTable> items=exTableDAO.extableselect();
        model.put("tables5",items);
    }
}
