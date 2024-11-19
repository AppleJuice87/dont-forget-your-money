package com.example.dontforgetyourmoney.di;

import com.example.dontforgetyourmoney.data.model.Condition;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class ConditionModule {

    @Provides
    @Singleton
    public Condition provideCondition() {
        return new Condition(null, null, null, null);
    }
}