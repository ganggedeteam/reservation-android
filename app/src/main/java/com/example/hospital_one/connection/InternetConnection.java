package com.example.hospital_one.connection;

import android.util.Log;
import okhttp3.*;

import java.io.IOException;


public class InternetConnection {

    public static String ForInternetConnection(String url,String jsonData){
        String result = null;
        try {
            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create
                    (JSON, jsonData);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            Log.d(null, "ForInternetConnection: internet Error Mr Wu");
        }
        return result;
    }

    public static String ForInternetConnection(String url){
        OkHttpClient client = new OkHttpClient();
        String response = null;
        try{
            Request request = new Request.Builder().url(url).build();
            response = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
