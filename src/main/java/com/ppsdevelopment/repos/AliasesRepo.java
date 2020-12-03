package com.ppsdevelopment.repos;

import com.ppsdevelopment.domain.Aliases;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AliasesRepo extends CrudRepository<Aliases,Long> {
    @Query("from Aliases as al where al.table_id=?1 order by id asc")
    List<Aliases> getAllByTable(Long id);
}

