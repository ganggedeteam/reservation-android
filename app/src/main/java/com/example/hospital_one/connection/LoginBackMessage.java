package com.example.hospital_one.connection;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginBackMessage {

    public static int parseJson(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject.getBoolean("status"))return 0;
            else if(jsonObject.getString("messagecode").equals("0")){
                return 2;
            }else{
                return 4;
            }
        } catch (JSONException e) {
//            e.printStackTrace();
            return 3;
        }
    }
    public static int parseChangePwd(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject.getBoolean("status"))return 0;
            else if(jsonObject.getString("message").equals("0")){
                return 2;
            }else{
                return 4;
            }
        } catch (JSONException e) {
//            e.printStackTrace();
            return 3;
        }
    }
}
