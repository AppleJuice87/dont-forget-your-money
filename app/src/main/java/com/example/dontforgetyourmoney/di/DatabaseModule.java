package com.example.dontforgetyourmoney.di;

import android.app.Application;
import androidx.room.Room;

import com.example.dontforgetyourmoney.data.DAO.ConditionDao;
import com.example.dontforgetyourmoney.data.DAO.PostDao;
import com.example.dontforgetyourmoney.data.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    public AppDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "app_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public PostDao providePostDao(AppDatabase database) {
        return database.postDao();
    }

    @Provides
    @Singleton
    public ConditionDao provideConditionDao(AppDatabase database) {
        return database.conditionDao();
    }
}
