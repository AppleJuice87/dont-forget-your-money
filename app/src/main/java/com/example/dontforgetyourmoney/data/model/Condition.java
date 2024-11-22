package com.example.dontforgetyourmoney.data.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Entity(tableName = "conditions") // 테이블 이름 지정
public class Condition {
    
    @PrimaryKey(autoGenerate = true) // 자동 증가하는 기본 키
    private int id;

    private String keyword; // 키워드 (nullable)
    private Integer grade; // 학년 (nullable)
    private Integer incomeBracket; // 소득구간 (nullable)
    private Double rating; // 평점 (nullable)

    // 생성자, getter 및 setter 메서드
    public Condition(String keyword, Integer grade, Integer incomeBracket, Double rating) {
        this.keyword = keyword;
        this.grade = grade;
        this.incomeBracket = incomeBracket;
        this.rating = rating;
    }

    public void clear() {
        this.keyword = null;
        this.grade = null;
        this.incomeBracket = null;
        this.rating = null;
    }

    public void saveAsMyCondition(SharedPreferences sharedPreferences, Gson gson) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("my-condition", gson.toJson(this));
        editor.apply();
    }

    @Override
    public String toString() {
        return "Condition{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                ", grade=" + grade +
                ", incomeBracket=" + incomeBracket +
                ", rating=" + rating +
                '}';
    }

    public String getConditions() {
        // 있으면 조건 출력, 없으면 아예 안넣음
        StringBuilder sb = new StringBuilder();
        if (keyword != null && !keyword.isEmpty()) {
            sb.append("키워드:").append(keyword).append(" ");
        }
        if (grade != null && grade != 0) {
            sb.append("학년:").append(grade).append(" ");
        }
        if (incomeBracket != null && incomeBracket != 0) {
            sb.append("소득구간:").append(incomeBracket).append(" ");
        }
        if (rating != null) {
            sb.append("평점:").append(rating);
        }
        return sb.toString().isEmpty() ? ": 선택된 검색조건 없음" : sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getIncomeBracket() {
        return incomeBracket;
    }

    public void setIncomeBracket(Integer incomeBracket) {
        this.incomeBracket = incomeBracket;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
