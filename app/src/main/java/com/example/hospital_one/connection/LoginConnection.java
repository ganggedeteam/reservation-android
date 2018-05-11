package com.example.hospital_one.connection;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoginConnection {

    String jsonResult;
    public LoginConnection(String data){
        jsonResult = makeJson(data);
    }

    public String getJsonResult(){
        return this.jsonResult;
    }

    public String makeJson(String data){
        String jsonData = null;
        try {
            jsonData = new JSONObject().put("userName", data).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

    public class JsonHead{
        public int total;
        public List<BuserInfoData> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<BuserInfoData> data,String message,boolean status){
            this.total = total;
            this.data = data;
            this.message = message;
            this.status = status;
        }
    }

    public class BuserInfoData{
        public String userPhone,userName;
        public BuserInfoData(String loginId, String
                userName){
            this.userPhone = loginId;
            this.userName = userName;
        }
    }

    public static JsonHead parseJsonData(String jsonData){
        Gson gson = new Gson();
        JsonHead list = gson.fromJson(jsonData, JsonHead.class);
        return list;
    }

    public class LoginJsonHead{
        public UserMessage data;
        public String message;
        public boolean status;
        public LoginJsonHead(UserMessage data,String message,boolean status){

            this.data = data;
            this.message = message;
            this.status = status;

        }
    }

    public static LoginJsonHead parseLoginJsonData(String jsonData){
        Gson gson = new Gson();
        LoginJsonHead list = gson.fromJson(jsonData, LoginJsonHead.class);
        return list;
    }

    public class UserMessage{
        public String key,token;
        public UserMessage(String key, String
                token){
            this.key = key;
            this.token = token;
        }
    }
}
