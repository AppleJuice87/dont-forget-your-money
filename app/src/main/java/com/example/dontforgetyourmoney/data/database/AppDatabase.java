package com.example.dontforgetyourmoney.data.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.dontforgetyourmoney.data.DAO.ConditionDao;
import com.example.dontforgetyourmoney.data.DAO.PostDao;
import com.example.dontforgetyourmoney.data.model.Condition;
import com.example.dontforgetyourmoney.data.model.Post;

@Database(entities = {Post.class, Condition.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    
    public abstract PostDao postDao(); // PostDao를 반환하는 메서드
    public abstract ConditionDao conditionDao(); // ConditionDao를 반환하는 메서드

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
