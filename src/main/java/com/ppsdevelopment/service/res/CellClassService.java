package com.ppsdevelopment.service.res;

import com.ppsdevelopment.domain.reserv.CellClass;

import java.util.List;

public interface CellClassService {
    void create(CellClass cellClass);
    List<CellClass> readAll();
    CellClass read(int id);
    boolean update(CellClass cellClass, int id);
    boolean delete(int id);
}
