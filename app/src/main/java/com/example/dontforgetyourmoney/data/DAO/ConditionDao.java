package com.example.dontforgetyourmoney.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dontforgetyourmoney.data.model.Condition;

import java.util.List;

@Dao
public interface ConditionDao {
    @Insert
    void insert(Condition condition);

    @Update
    void update(Condition condition);

    @Delete
    void delete(Condition condition);

    @Query("SELECT * FROM conditions WHERE id = :id")
    Condition getConditionById(int id);

    @Query("SELECT * FROM conditions")
    List<Condition> getAllConditions();

    @Query("DELETE FROM conditions")
    void deleteAllConditions();
}