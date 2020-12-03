package com.ppsdevelopment.service.res;

import com.ppsdevelopment.domain.reserv.CellClass;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CellClassServiceImpl implements CellClassService {
    private static final Map<Integer, CellClass> CLIENT_REPOSITORY_MAP = new HashMap<>();
    private static final AtomicInteger CLIENT_ID_HOLDER = new AtomicInteger();

    @Override
    public void create(CellClass cellClass) {
        final int clientId = CLIENT_ID_HOLDER.incrementAndGet();
        cellClass.setId(clientId);
        CLIENT_REPOSITORY_MAP.put(clientId, cellClass);
    }

    @Override
    public List<CellClass> readAll() {
        return new ArrayList<>(CLIENT_REPOSITORY_MAP.values());
    }

    @Override
    public CellClass read(int id) {
        return CLIENT_REPOSITORY_MAP.get(id);
    }

    @Override
    public boolean update(CellClass cellClass, int id) {
        if (CLIENT_REPOSITORY_MAP.containsKey(id)) {
            cellClass.setId(id);
            CLIENT_REPOSITORY_MAP.put(id, cellClass);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        return CLIENT_REPOSITORY_MAP.remove(id) != null;
    }
}
