package com.ppsdevelopment.repos;

import com.ppsdevelopment.domain.Aliases;
import com.ppsdevelopment.domain.dictclasses.AliasesSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/*
public interface AliasesSettingsRepo extends CrudRepository<AliasesSettings,Long>{
        @Query("select al.fieldalias, al.fieldname from Aliases as al left join ColumnSettings cs on al.id=cs.alias_id where al.table_id=?1 order by id asc")
        List<AliasesSettings> getAllByTableAndUser(Long id);
    }
*/
