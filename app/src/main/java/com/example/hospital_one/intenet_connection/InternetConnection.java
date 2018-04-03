package com.example.hospital_one.intenet_connection;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
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
}
