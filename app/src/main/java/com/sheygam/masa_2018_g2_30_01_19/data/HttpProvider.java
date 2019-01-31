package com.sheygam.masa_2018_g2_30_01_19.data;

import android.util.Log;

import com.google.gson.Gson;
import com.sheygam.masa_2018_g2_30_01_19.data.dto.AuthRequestDto;
import com.sheygam.masa_2018_g2_30_01_19.data.dto.AuthResponseDto;
import com.sheygam.masa_2018_g2_30_01_19.data.dto.ResponseErrorDto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpProvider {
    private static final String BASE_URL = "https://contacts-telran.herokuapp.com/";
    private static final HttpProvider instance = new HttpProvider();
    private Gson gson;

    private HttpProvider(){
        gson = new Gson();
    }

    public static HttpProvider getInstance(){
        return instance;
    }

    public AuthResponseDto registration(String email, String password) throws Exception {
        AuthRequestDto authRequestDto = new AuthRequestDto(email,password);
        String requestBody = gson.toJson(authRequestDto);

        URL url = new URL(BASE_URL + "api/registration");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Content-Type","application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

//        connection.connect();//for GET
        OutputStream os = connection.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.write(requestBody);
        bw.flush();
        bw.close();

        int code = connection.getResponseCode();
        BufferedReader br;
        String line;
        String responseBody = "";

        if(code < 300){
            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while((line = br.readLine()) != null){
                responseBody += line;
            }
            AuthResponseDto authResponseDto = gson.fromJson(responseBody,AuthResponseDto.class);
            br.close();
            return authResponseDto;
        }else if(code == 400 || code == 409){
            InputStream is = connection.getErrorStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null){
                responseBody += line;
            }
            ResponseErrorDto error = gson.fromJson(responseBody,ResponseErrorDto.class);
            br.close();
            throw new Exception(error.getMessage());
        }else{
            InputStream is = connection.getErrorStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine())!=null){
                responseBody += line;
            }
            Log.e("MY_TAG", "registration: " + responseBody);
            throw new Exception("Server error! Call to support!");
        }
    }
}
