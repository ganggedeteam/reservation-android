package com.example.hospital_one.connection;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ReservationConnection {

    public static String[] reservationStatus = {"未就诊","已就诊","未按时"};

    public class ReservationMessage{
        public String isAdmission;
        public String patientName;
        public String reservationId ;
        public String patientId;
        public String userPhone ;
        public String admissionId;
        public ReservationMessage(
                String isAdmission,
                String patientName,
                String reservationId ,
                String patientId,
                String userPhone ,
                String admissionId
        ){

            this.isAdmission = isAdmission;
            this.patientName = patientName;
            this.reservationId  = reservationId ;
            this.patientId = patientId;
            this.userPhone  = userPhone ;
            this.admissionId = admissionId;

        }
    }

    public class JsonHead{
        public List<ReservationMessage> data;
        public String message;
        public boolean status;
        public JsonHead(List<ReservationMessage> data,String message,boolean status){
            this.data = data;
            this.message = message;
            this.status = status;
        }
    }

    public static JsonHead parseJson(String jsonData){
        Gson gson = new Gson();
        JsonHead list = gson.fromJson(jsonData,JsonHead.class);
        return list;
    }

    public class TimeMessage{
         public String timestamp;
         public int status;
         public String error;
         public String message;
         public String path;
         public TimeMessage(
                 String timestamp,
                 int status,
                 String error,
                 String message,
                 String path
         ){
             this.timestamp = timestamp;
             this.status = status;
             this.error = error;
             this.message = message;
             this.path = path;
         }
    }
    public static TimeMessage parseTimeMessage(String jsonData){
        Gson gson = new Gson();
        TimeMessage list = gson.fromJson(jsonData, TimeMessage.class);
        return list;
    }
    public static int parseAddReservationJson(String jsonData){
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if(jsonObject.getBoolean("status"))return 0;
            else{
                return 3;
            }
        } catch (JSONException e) {
//            e.printStackTrace();
            return 2;
        }
    }
}
