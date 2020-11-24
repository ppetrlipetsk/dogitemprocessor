package com.ppsdevelopment.service;

import com.ppsdevelopment.domain.CellClass;

import java.util.List;

public interface CellClassService {
    void create(CellClass cellClass);
    List<CellClass> readAll();
    CellClass read(int id);
    boolean update(CellClass cellClass, int id);
    boolean delete(int id);
}
