package com.ppsdevelopment.repos.res;

import com.ppsdevelopment.domain.reserv.ExTable;

import javax.persistence.NamedNativeQuery;
import java.util.List;

@NamedNativeQuery(name="extableselect",query="select * from zmm2021", resultClass = ExTable.class)
public interface ExTableRepo {
    public List<ExTable> extableselect();
}
