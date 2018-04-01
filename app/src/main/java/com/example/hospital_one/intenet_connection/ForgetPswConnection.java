package com.example.hospital_one.intenet_connection;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ForgetPswConnection {

    String jsonResult;
    public ForgetPswConnection(String data){
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
        public String loginId,userName,roleId,loginPwd;
        public BuserInfoData(String loginId, String
                userName,String roleId,String loginPwd){
            this.loginId = loginId;
            this.userName = userName;
            this.roleId = roleId;
            this.loginPwd = loginPwd;
        }
    }
    public static JsonHead parseJsonData(String jsonData){
        Gson gson = new Gson();
        JsonHead list = gson.fromJson(jsonData, JsonHead.class);
        return list;
    }
}
