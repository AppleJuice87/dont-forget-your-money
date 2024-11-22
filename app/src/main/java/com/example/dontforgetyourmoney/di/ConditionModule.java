package com.example.dontforgetyourmoney.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dontforgetyourmoney.data.model.Condition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(SingletonComponent.class)
public class ConditionModule {

    @Provides
    @Singleton
    @Named("SearchCondition")
    public Condition provideSearchCondition() {
        return new Condition(null, null, null, null);
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return context.getSharedPreferences("my-condition", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    @Named("MyCondition")
    public Condition provideMyCondition(SharedPreferences sharedPreferences, Gson gson) {
        String json = sharedPreferences.getString("my-condition", null);
        if (json != null) {
            return gson.fromJson(json, Condition.class);
        }
        return new Condition(null, null, null, null); // 기본값 반환
    }
}
