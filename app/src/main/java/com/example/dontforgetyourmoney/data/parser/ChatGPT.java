package com.example.dontforgetyourmoney.data.parser;

import android.content.Context;
import android.util.Log;

import com.example.dontforgetyourmoney.DontForgetYourMoneyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;

public class ChatGPT {

    private String apiKey;

    public ChatGPT() {
        Properties properties = new Properties();
        try (InputStream input = DontForgetYourMoneyApplication.getAppContext().getAssets().open("config.properties")) {
            properties.load(input);
            apiKey = properties.getProperty("openai.api.key");
            //Log.d("[GPT]", "[GPT] API Key: " + apiKey);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String makePrompt(String content) {

        return "다음은 장학금 관련 공지사항이다. 본문에서 내가 원하는 값만 추출하여 사용하고자 한다.\n" +
                "지금부터 답변은 \"답변형식\"과 완전히 동일하게 한다.\n" +
                "\n" +
                "[답변 형식]\n" +
                "신청 가능 학년:(1이상 4이하의 정수가 올 수 있다. 여러개의 정수도 올 수 있으며, 각 정수는 콤마로 구분한다)\n" +
                "신청 가능 소득구간:(1 이상, 10 이하의 정수만 올 수 있다. 다음과 같이 제시하라. 소득구간 3 이상 6 이하 이면, 3-6 과 같은 형태로 답변하라.)\n" +
                "신청 가능 평점평균:(0.0이상 4.5이하의 실수가 하나만 올 수 있다. 만약 신청가능한 평점 평균이 3.5점 이상이라면, \"3.5이상\"이라고 답변하라)\n" +
                "\n" +
                "[조건]\n" +
                "만약 학년, 소득구간, 평점평균 중 해당 정보가 나와있지 않거나, 판별할 수 없는 경우, 다음과 같이 답변하라.\n" +
                "- 만약 신청가능 학년을 판별할 수 없으면\n" +
                "신청 가능 학년:판별불가\n" +
                "- 만약 신청 가능한 소득구간을 판별할 수 없으면\n" +
                "신청 가능 소득구간:판별불가\n" +
                "- 만약 신청가능한 평점 평균을 판별할 수 없으면\n" +
                "신청 가능 평점평균:판별불가\n" +
                "\n" +
                "[본문]\n" +
                content;
    }

    public String askQuestion(String question) {
        //Log.d("[GPT]", "[GPT] 받은 질의 텍스트:" + question);
        String responseText = "";

        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // GSON을 사용하여 JSON 요청 본문 작성
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("model", "gpt-4o");

            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", question);
            jsonObject.add("messages", new Gson().toJsonTree(Collections.singletonList(message)));

            String jsonInputString = gson.toJson(jsonObject);

            conn.setRequestProperty("Content-Length", String.valueOf(jsonInputString.getBytes("utf-8").length));

            Log.d("[GPT]", "[GPT] 생성된 JSON BODY:" + jsonInputString);

            // 요청 본문 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e) {
                Log.e("[GPT]", "[GPT] 전송 실패!!!");
            }

            // 응답 처리
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        responseBuilder.append(responseLine.trim());
                    }
                    String jsonResponse = responseBuilder.toString();

                    Log.d("[GPT]", "[GPT] 받은 JSON:" + jsonResponse);


                    // GSON을 사용하여 JSON 파싱
                    JsonObject jsonObject1 = gson.fromJson(jsonResponse, JsonObject.class);
                    JsonArray choices = jsonObject1.getAsJsonArray("choices");
                    if (choices != null && choices.size() > 0) {
                        JsonObject message1 = choices.get(0).getAsJsonObject().getAsJsonObject("message");
                        responseText = message1.get("content").getAsString(); // content 추출
                    }
                }
            } else {
                Log.e("[GPT]", "[GPT] 요청 실패, 응답 코드: " + responseCode);
                // 오류 응답 처리
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponseBuilder = new StringBuilder();
                    String errorLine;
                    while ((errorLine = br.readLine()) != null) {
                        errorResponseBuilder.append(errorLine.trim());
                    }
                    Log.e("[GPT]", "[GPT] 오류 응답: " + errorResponseBuilder.toString());
                }
            }
        } catch (IOException e) {
            Log.e("[GPT]", "[GPT] IO 예외 발생: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("[GPT]", "[GPT] 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }

        Log.d("[GPT]", "[GPT] 받은 Content:" + responseText);
        return responseText; // API 응답 반환
    }

}
