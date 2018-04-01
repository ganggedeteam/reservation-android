package com.example.hospital_one.intenet_connection;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HospitalConnection {

    String jsonResult = null;
    public final String hospitalGradeKey = "hospitalGrade",
                        hospitalIdKey = "hospitalId" ,
                        hospitalManagerKey = "hospitalManager" ,
                        hospitalNameKey = "hospitalName" ,
                        managerNameKey = "managerName";
    public HospitalConnection(String key,String data){
        switch (key){
            case hospitalGradeKey:break;
            case hospitalIdKey:break;
            case hospitalManagerKey:break;
            case hospitalNameKey:break;
            case managerNameKey:break;
            default:return;
        }
        jsonResult = makeJson(key,data);
    }

    public String getJsonResult(){
        return this.jsonResult;
    }

    public String makeJson(String key,String data){
        String jsonData = null;
        try {
            jsonData = new JSONObject().put(key, data).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData;
    }

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
