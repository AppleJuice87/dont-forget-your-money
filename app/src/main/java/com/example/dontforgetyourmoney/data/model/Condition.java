package com.example.dontforgetyourmoney.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conditions") // 테이블 이름 지정
public class Condition {
    
    @PrimaryKey(autoGenerate = true) // 자동 증가하는 기본 키
    private int id;

    private String keyword; // 키워드 (nullable)
    private Integer grade; // 학년 (nullable)
    private Integer incomeBracket; // 소득구간 (nullable)
    private Float rating; // 평점 (nullable)

    // 생성자, getter 및 setter 메서드
    public Condition(String keyword, Integer grade, Integer incomeBracket, Float rating) {
        this.keyword = keyword;
        this.grade = grade;
        this.incomeBracket = incomeBracket;
        this.rating = rating;
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

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
