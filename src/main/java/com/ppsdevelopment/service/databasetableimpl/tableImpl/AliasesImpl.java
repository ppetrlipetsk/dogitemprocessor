package com.ppsdevelopment.service.databasetableimpl.tableImpl;

import com.ppsdevelopment.domain.Aliases;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Service("AliasesImpl")
@Repository
public class AliasesImpl {
/*
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public List<Aliases> getAliases(Long tableId, Integer userId) {
        entityManager
                .unwrap(Session.class)
                .enableFilter("userIdFilter")
                .setParameter("userid", userId);

        List<Aliases> list = entityManager.createQuery(
                "select al from Aliases al where al.table_id=?1 order by al.id asc", Aliases.class)
                .setParameter(1,tableId)
                .getResultList();
        return list;
    }*/
}
