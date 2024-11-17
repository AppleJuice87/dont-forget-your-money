package com.example.dontforgetyourmoney.di;

import com.example.dontforgetyourmoney.data.DAO.ConditionDao;
import com.example.dontforgetyourmoney.data.DAO.PostDao;
import com.example.dontforgetyourmoney.data.repository.ConditionRepository.ConditionRepository;
import com.example.dontforgetyourmoney.data.repository.ConditionRepository.ConditionRepositoryImpl;
import com.example.dontforgetyourmoney.data.repository.PostRepository.PostRepository;
import com.example.dontforgetyourmoney.data.repository.PostRepository.PostRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RepositoryModule {

    @Provides
    public PostRepository providePostRepository(PostDao postDao) {
        return new PostRepositoryImpl(postDao);
    }

    @Provides
    public ConditionRepository provideConditionRepository(ConditionDao conditionDao) {
        return new ConditionRepositoryImpl(conditionDao);
    }
}
