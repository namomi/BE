package com.minizin.travel.tour.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minizin.travel.tour.domain.entity.TourAPIRequest;
import com.minizin.travel.tour.domain.entity.TourAPIResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import lombok.RequiredArgsConstructor;
//import lombok.Value;
import netscape.javascript.JSObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minizin.travel.tour.domain.entity.TourAPIResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minizin.travel.tour.domain.entity.TourAPIResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

/**
 * Class: TourService Project: travel Package: com.minizin.travel.tour.service
 * <p>
 * Description: TourService
 *
 * @author dong-hoshin
 * @date 6/5/24 01:04 Copyright (c) 2024 MiniJin
 * @see <a href="https://github.com/team-MiniJin/BE">GitHub Repository</a>
 */
@Service
@RequiredArgsConstructor
public class TourService {

    @Value("${api-tour.serviceKey_De}")
    public String serviceKey;

    public TourAPIResponse getTourAPIFromSite() {
        int[] areaCode = {1, 2, 3, 4, 5, 6, 7, 8, 31, 32};
        StringBuilder sb = new StringBuilder();

        for (int i : areaCode) {
            try {
                StringBuilder urlBuilder = new StringBuilder(
                    "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?");
                urlBuilder.append("?");
                urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8")
                    + "=" + URLEncoder.encode(serviceKey, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8")
                    + "=" + URLEncoder.encode("10", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8")
                    + "=" + URLEncoder.encode("2", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8")
                    + "=" + URLEncoder.encode("ETC", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8")
                    + "=" + URLEncoder.encode("AppTest", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8")
                    + "=" + URLEncoder.encode("json", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8")
                    + "=" + URLEncoder.encode("Y", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("areaCode", "UTF-8")
                    + "=" + URLEncoder.encode(String.valueOf(i), "UTF-8"));
                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                System.out.println("Response code: " + conn.getResponseCode());
                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

            } catch (UnsupportedEncodingException | MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(sb.toString());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(sb.toString(), TourAPIResponse.class);
        } catch (
            IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

    /*@GetMapping("/api/load")
    public TourAPIResponse getTourAPIFromSite () {
        String result = "";
        String MobileOS = "ETC";
        String MobileApp = "AppTest";
        try{
            URL url = new URL("https://apis.data.go.kr/B551011/KorService1/");
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-type","application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = br.readLine();

            JSONParser jsonParser = new JSONParser();

        }

        return
    }*/

