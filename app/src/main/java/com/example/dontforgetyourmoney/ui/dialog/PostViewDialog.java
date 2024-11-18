package com.example.dontforgetyourmoney.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dontforgetyourmoney.R.*;
import com.example.dontforgetyourmoney.*;
import com.example.dontforgetyourmoney.data.model.Post;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class PostViewDialog extends Dialog {
    private Post post;

    @SuppressLint("PrivateResource")
    public PostViewDialog(@NonNull Context context, Post post) {
        super(context, com.google.android.material.R.style.Base_Theme_Material3_Light_SideSheetDialog);
//        super(context, R.style.Custom_SideSheetDialog); // 사용자 정의 스타일 사용
        this.post = post;
        // 다이얼로그 레이아웃 설정
        setContentView(R.layout.dialog_postview);
        
        // 다이얼로그에 데이터 설정
        TextView title = findViewById(R.id.dialogTitle);
        TextView date = findViewById(R.id.dialogDate);
        TextView condition = findViewById(R.id.dialogCondition);

        //!TextView content = findViewById(R.id.dialogContent);
        WebView content = findViewById(id.dialogContent);

        Button viewWebButton = findViewById(R.id.buttonViewWeb);

        title.setText(post.getTitle());
        date.setText(post.getDate());
        condition.setText(getConditionText(post)); // 조건 텍스트 설정

        //!content.setText(post.getContent());

        // TODO WebView 에 링크 전달.
        loadContentInWebView(content, post.getLink());

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
            conditionText.append("학년:").append(post.getGrade()).append(" ");
        }
        if (post.getIncomeBracket() != null) {
            conditionText.append("소득구간:").append(post.getIncomeBracket()).append(" ");
        }
        if (post.getRating() != null) {
            conditionText.append("평점:").append(post.getRating()).append(" 이상");
        }
        return conditionText.toString().trim();
    }

    private void openWebPage(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        getContext().startActivity(browserIntent); // 웹 브라우저 열기
    }

    public void loadContentInWebView(WebView webView, String url) {
        // SSL 인증서 무시 설정
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            Log.e("KUCS_ParserImpl", "SSL setup failed: " + e.getMessage());
        }

        webView.getSettings().setJavaScriptEnabled(true); // JavaScript 활성화
        //webView.setWebViewClient(new WebViewClient()); // WebViewClient 설정
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Log.e("WebViewError", "Error: " + description + " (Code: " + errorCode + ")");
//            }
//        }); // WebViewClient 설정

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // SSL 오류를 무시 (개발 환경에서만 사용)
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("WebViewError", "Error: " + description + " (Code: " + errorCode + ")");
            }
        });
        webView.loadUrl(url); // URL 로드
    }
}