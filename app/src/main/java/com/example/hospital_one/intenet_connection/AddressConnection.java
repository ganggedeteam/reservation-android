package com.example.hospital_one.intenet_connection;

import com.google.gson.Gson;

import java.util.List;

public class AddressConnection {

    public class AddressMessage{
        public String preId;
        public String level;
        public String addressName;
        public int id;
        public String addressId;
        public AddressMessage(
                String preId,
                String level,
                String addressName,
                int id,
                String addressId){
            this.preId = preId;
            this.level = level;
            this.addressName = addressName;
            this.id = id;
            this.addressId = addressId;
        }
    }

    public class JsonHead{
        public int total;
        public List<AddressMessage> data;
        public String message;
        public boolean status;
        public JsonHead(int total, List<AddressMessage> data, String message, boolean status){
            this.total = total;
            this.data = data;
            this.message = message;
            this.status = status;
        }
    }

    public static JsonHead parseJsonData(String jsonData){
        Gson gson = new Gson();
        JsonHead list = gson.fromJson(jsonData, JsonHead.class);
        return list;
    }

}
