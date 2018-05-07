package com.example.hospital_one.connection;

import com.google.gson.Gson;
import org.json.JSONObject;

public class ReservationConnection {

//    public class{}

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

    public int parseJson(String jsonData){
        JSONObject jsonObject =
    }
}
