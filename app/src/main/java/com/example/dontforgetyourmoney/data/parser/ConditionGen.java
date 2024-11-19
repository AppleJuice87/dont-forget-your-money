package com.example.dontforgetyourmoney.data.parser;

import com.example.dontforgetyourmoney.data.model.Post;

public class ConditionGen {
    public static String getConditions(Post post) {
        StringBuilder sb = new StringBuilder();
        // post 의 학년, 소득구간, 평점이 확인불가가 아닌 경우에 sb에 append
        if (post.getGrade() != null && !post.getGrade().trim().equals("판별불가")) {
            sb.append("학년:").append(post.getGrade()).append(" ");
        }
        if (post.getIncomeBracket() != null && !post.getIncomeBracket().trim().equals("판별불가")) {
            sb.append("소득구간:").append(post.getIncomeBracket()).append(" ");
        }
        if (post.getRating() != null && !post.getRating().trim().equals("판별불가")) {
            sb.append("평점:").append(post.getRating());
        }

        return sb.toString();
    }
}
