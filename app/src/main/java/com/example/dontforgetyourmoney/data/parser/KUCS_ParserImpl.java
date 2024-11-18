package com.example.dontforgetyourmoney.data.parser;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.dontforgetyourmoney.data.model.Post;
import com.example.dontforgetyourmoney.data.repository.PostRepository.PostRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class KUCS_ParserImpl implements Parser {

    private final PostRepository postRepository;

    @Inject
    public KUCS_ParserImpl(PostRepository postRepository) {
        this.postRepository = postRepository;

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
    }

    public void parseAndSavePosts() {
        for (int page = 1; page <= 5; page++) {
            try {
                String searchTerm = "장학";
                //String encodedSearchTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8.toString());
                String encodedSearchTerm = "Zm5jdDF8QEB8JTJGYmJzJTJGY2UlMkYzMzMlMkZhcnRjbExpc3QuZG8lM0ZiYnNDbFNlcSUzRCUyNmJic09wZW5XcmRTZXElM0QlMjZpc1ZpZXdNaW5lJTNEZmFsc2UlMjZzcmNoQ29sdW1uJTNEc2olMjZzcmNoV3JkJTNEJUVDJTlFJUE1JUVEJTk1JTk5JTI2";
                String url = String.format("https://www.kyungnam.ac.kr/ce/2394/subview.do?enc=%s&page=%d", encodedSearchTerm, page);
                Document document = Jsoup.connect(url).get();
                Elements postElements = document.select("tbody tr"); // tbody 안의 tr 요소 선택

                //? 제일 최신의 게시물이 DB 에 포함되어 있으면 모든 작업을 멈춤.
                String first = postElements.get(0).select(".td-subject strong").text(); // 제목 선택
                if(postRepository.existsByTitle(first))
                    break;

                for (Element postElement : postElements) {
                    String title = postElement.select(".td-subject strong").text(); // 제목 선택

                    // 같은 제목의 글이 이미 DB에 저장되어 있으면 추가하지 않고 넘어가는 코드
                    if (postRepository.existsByTitle(title)) {
                        int id = postRepository.getPostIdByTitle(title);
                        Log.d("Test", "[mj1234] ID:" + id + " TITLE:" + title + " 이미존재!!!");
                        continue; // 이미 존재하는 제목이면 다음으로 넘어감
                    }

                    String date = postElement.select(".td-date").text(); // 날짜 선택
                    String link = postElement.select(".td-subject a").attr("href"); // 링크 선택
                    link = "https://www.kyungnam.ac.kr" + link; // 절대 경로로 변환

                    // 링크를 사용해 글 하나에 대한 사이트에 접속하여 본문 파싱
                    //String content = fetchPostContent(link);
                    String content = fetchPostContent(link);

                    //String content = "예시 본문";

                    //Post post = new Post(title, date, content, link, null, null, null);
                    Post post = fillConditions(
                            new Post(title, date, content, link,
                                    null, null, null)
                    );

                    postRepository.insert(post); // DB에 게시글 저장

                    Log.d("Test", "[mj1234]" + post.toString());
                }
            } catch (IOException e) {
                Log.e("KUCS_ParserImpl", "Network error: " + e.getMessage());
            } catch (Exception e) {
                Log.e("KUCS_ParserImpl", "Unexpected error: " + e.getMessage());
            }
        }
    }

    private String fetchPostContent(String link) {
        try {
            Document postDocument = Jsoup.connect(link).get();
            // <div class="view-con"> 클래스 하위의 HTML 가져오기
            String contentHtml = postDocument.select(".view-con").html(); // HTML 내용 가져오기
            // HTML에서 줄바꿈을 포함한 텍스트로 변환
            return contentHtml.replaceAll("<br>", "\n").replaceAll("<[^>]+>", ""); // <br> 태그를 줄바꿈으로 변환하고, 나머지 HTML 태그 제거
        } catch (IOException e) {
            Log.e("KUCS_ParserImpl", "Error fetching post content: " + e.getMessage());
            return ""; // 본문이 없을 경우 빈 문자열 반환
        } catch (Exception e) {
            Log.e("KUCS_ParserImpl", "Unexpected error fetching post content: " + e.getMessage());
            return ""; // 본문이 없을 경우 빈 문자열 반환
        }
    }

    public void loadContentInWebView(WebView webView, String url) {
        webView.getSettings().setJavaScriptEnabled(true); // JavaScript 활성화
        webView.setWebViewClient(new WebViewClient()); // WebViewClient 설정
        webView.loadUrl(url); // URL 로드
    }

    public Post fillConditions(Post psot) {


        return null;
    }
}
