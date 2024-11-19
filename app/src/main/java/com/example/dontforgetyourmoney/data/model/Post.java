package com.example.dontforgetyourmoney.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "posts") // 테이블 이름 지정
public class Post {
    
    @PrimaryKey(autoGenerate = true) // 자동 증가하는 기본 키
    private int id;

    private String title; // 글 제목
    private String date; // 작성일
    private String content; // 본문
    private String link; // 게시글 링크
    private String grade; // 학년 (nullable)
    private String incomeBracket; // 소득구간 (nullable)
    private String rating; // 평점 (nullable)

    // 생성자, getter 및 setter 메서드
    public Post(String title, String date, String content, String link, String grade, String incomeBracket, String rating) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.link = link;
        this.grade = grade;
        this.incomeBracket = incomeBracket;
        this.rating = rating;
    }

    @Ignore
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", grade=" + grade +
                ", incomeBracket=" + incomeBracket +
                ", rating=" + rating +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getIncomeBracket() {
        return incomeBracket;
    }

    public void setIncomeBracket(String incomeBracket) {
        this.incomeBracket = incomeBracket;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
