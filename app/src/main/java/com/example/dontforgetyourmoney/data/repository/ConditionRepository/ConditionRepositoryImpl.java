package com.example.dontforgetyourmoney.data.repository.ConditionRepository;

import com.example.dontforgetyourmoney.data.DAO.ConditionDao;
import com.example.dontforgetyourmoney.data.model.Condition;

import java.util.List;

public class ConditionRepositoryImpl implements ConditionRepository {
    private final ConditionDao conditionDao;

    public ConditionRepositoryImpl(ConditionDao conditionDao) {
        this.conditionDao = conditionDao;
    }

    @Override
    public void insert(Condition condition) {
        conditionDao.insert(condition);
    }

    @Override
    public void update(Condition condition) {
        conditionDao.update(condition);
    }

    @Override
    public void delete(Condition condition) {
        conditionDao.delete(condition);
    }

    @Override
    public Condition getConditionById(int id) {
        return conditionDao.getConditionById(id);
    }

    @Override
    public List<Condition> getAllConditions() {
        return conditionDao.getAllConditions();
    }

    @Override
    public void deleteAllConditions() {
        conditionDao.deleteAllConditions();
    }
}
