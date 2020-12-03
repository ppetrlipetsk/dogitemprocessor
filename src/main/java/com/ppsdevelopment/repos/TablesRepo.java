package com.ppsdevelopment.repos;

import com.ppsdevelopment.domain.Tables;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;
import java.util.List;

public interface TablesRepo extends CrudRepository<Tables,Long> {
    List<Tables> findByTablename(String tableName);
/*
    LinkedList<Tables> findByTablename(String tableName, Sort sort);
    List<Tables> findAllByOrderByIdAsc();
    Iterable<Tables> findAll(Sort id);
*/
    //List<Tables> findAll();
}
