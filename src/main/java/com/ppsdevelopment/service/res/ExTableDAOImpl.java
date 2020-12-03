package com.ppsdevelopment.service.res;

import com.ppsdevelopment.domain.reserv.ExTable;
import com.ppsdevelopment.repos.res.ExTableRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class ExTableDAOImpl implements ExTableRepo {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<ExTable> extableselect() {
        List<ExTable> items=manager.createNativeQuery("select * from zmm2021").getResultList();
        return items;
    }

    public ExTableDAOImpl() {
    }
}
