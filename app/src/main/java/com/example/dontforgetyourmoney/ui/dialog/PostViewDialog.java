package com.example.dontforgetyourmoney.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dontforgetyourmoney.R;
import com.example.dontforgetyourmoney.data.model.Post;

public class PostViewDialog extends Dialog {
    private Post post;

    public PostViewDialog(@NonNull Context context, Post post) {
        super(context, com.google.android.material.R.style.Base_Theme_Material3_Light_SideSheetDialog);
        this.post = post;
        // 다이얼로그 레이아웃 설정
        setContentView(R.layout.dialog_postview);
        
        // 다이얼로그에 데이터 설정
        TextView title = findViewById(R.id.dialogTitle);
        TextView date = findViewById(R.id.dialogDate);
        TextView condition = findViewById(R.id.dialogCondition);
        TextView content = findViewById(R.id.dialogContent);
        Button viewWebButton = findViewById(R.id.buttonViewWeb);

        title.setText(post.getTitle());
        date.setText(post.getDate());
        condition.setText(getConditionText(post)); // 조건 텍스트 설정
        content.setText(post.getContent());

        viewWebButton.setOnClickListener(v -> {
            // 웹 페이지에서 보기 버튼 클릭 시 동작
            openWebPage(post.getLink()); // 링크 열기 메서드
        });

        // 전체 화면으로 설정
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT; // 높이를 match_parent로 설정
        getWindow().setAttributes(params);
    }

    private String getConditionText(Post post) {
        // 조건을 문자열로 변환하는 로직
        StringBuilder conditionText = new StringBuilder();
        if (post.getGrade() != null) {
            conditionText.append("학년: ").append(post.getGrade()).append(" ");
        }
        if (post.getIncomeBracket() != null) {
            conditionText.append("소득구간: ").append(post.getIncomeBracket()).append(" ");
        }
        if (post.getRating() != null) {
            conditionText.append("평점: ").append(post.getRating()).append(" 이상");
        }
        return conditionText.toString().trim();
    }

    private void openWebPage(String url) {
        // 웹 페이지를 여는 로직을 구현
        // 예: Intent를 사용하여 웹 브라우저를 열기
    }
}