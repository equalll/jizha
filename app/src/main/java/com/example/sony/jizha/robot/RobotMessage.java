package com.example.sony.jizha.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;

public class RobotMessage {

    private String APIKEY = "640717b5c1c458d12ba5ebc06c3fa0b2";
    private StringBuffer sb;
    private jsonData jsData;


    public void main(String userStr) throws IOException {

        String INFO = URLEncoder.encode(userStr, "utf-8");
        String getURL = "http://www.tuling123.com/openapi/api?key=" + APIKEY + "&info=" + INFO;
        URL getUrl = new URL(getURL);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        sb = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        connection.disconnect();
        System.out.println(sb);

    }

    public String getString() {
        Gson gson = new Gson();
        jsData = new jsonData();
        jsData = gson.fromJson(sb.toString(), jsonData.class);
        return jsData.getText();
    }
}
