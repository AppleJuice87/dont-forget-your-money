package com.example.dontforgetyourmoney.data.repository.ConditionRepository;

import com.example.dontforgetyourmoney.data.model.Condition;

import java.util.List;

public interface ConditionRepository {
    
    // 조건 추가
    void insert(Condition condition);

    // 조건 업데이트
    void update(Condition condition);

    // 조건 삭제
    void delete(Condition condition);

    // ID로 조건 조회
    Condition getConditionById(int id);

    // 모든 조건 조회
    List<Condition> getAllConditions();

    // 모든 조건 삭제
    void deleteAllConditions();
}
