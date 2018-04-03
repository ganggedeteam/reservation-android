package com.example.hospital_one.intenet_connection;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HospitalConnection {

/*
    String jsonResult = null;

    private String[] keys = {
            "hospitalGrade",
            "hospitalId",
            "hospitalManager",
            "isValid",
            "hospitalName",
            "managerName"
    };

    public static final int hospitalGradeKey = 1,
                        hospitalIdKey = 2 ,
                        hospitalManagerKey = 3 ,
                        hospitalNameKey = 4 ,
                        managerNameKey = 5;
    public HospitalConnection(int key,String data){
        switch (key){
            case hospitalGradeKey:break;
            case hospitalIdKey:break;
            case hospitalManagerKey:break;
            case hospitalNameKey:break;
            case managerNameKey:break;
            default:jsonResult = makeJson(0,null);return;
        }
        jsonResult = makeJson(key,data);
    }

    public String getJsonResult(){
        return this.jsonResult;
    }

    public String makeJson(int key,String data){
        String jsonData = null;
        try {
            if(key == 0 || data == null){
                jsonData = new JSONObject().toString();
            }else
                jsonData = new JSONObject().put(keys[key], data).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData;
    }
*/

    public class JsonHead {
        public int total;
        public List<HospitalMes> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<HospitalMes> data,String message,boolean status){
            this.total = total;
            this.data = data;
            this.message = message;
            this.status = status;
        }
    }

    public class HospitalMes{
        public String hospitalGrade;
        public String hospitalId;
        public String hospitalManager;
        public String isValid;
        public String hospitalName;
        public String managerName;

        public HospitalMes(String hospitalGrade,
                           String hospitalId,
                           String hospitalManager,
                           String isValid,
                           String hospitalName,
                           String managerName){
            this.hospitalGrade = hospitalGrade;
            this.hospitalId = hospitalId;
            this.hospitalManager = hospitalManager;
            this.isValid = isValid;
            this.hospitalName = hospitalName;
            this.managerName = managerName;
        }

    }
    public static JsonHead parseJsonData(String jsonData){
        Gson gson = new Gson();
        JsonHead list = gson.fromJson(jsonData, JsonHead.class);
        return list;
    }

}
