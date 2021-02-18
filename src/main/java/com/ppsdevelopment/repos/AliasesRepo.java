package com.ppsdevelopment.repos;

import com.ppsdevelopment.domain.Aliases;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AliasesRepo extends CrudRepository<Aliases,Long> {
    @Query("from Aliases as al where al.table_id=?1 order by id asc")
    List<Aliases> getAllByTable(Long id);
    Aliases findFirstById(Long id);


/*
    @Query("select al, c.width from Aliases as al left join ColumnSettings c on c.userId=?1 and c.aliasId=al.id where al.table_id=?2 order by al.id asc")
    //where al.table_id=?1 and al.columnstyle.user_id=11   order by id asc")
    List getAllByTableUser(Long id, Long t);
*/


}

