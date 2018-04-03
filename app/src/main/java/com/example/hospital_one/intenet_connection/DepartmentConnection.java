package com.example.hospital_one.intenet_connection;

import com.example.hospital_one.part_hospital.PartOfHospitalAdapter;
import com.google.gson.Gson;

import java.util.List;

public class DepartmentConnection {

    public class JsonHead{
        public int total;
        public List<PartOfHospitalAdapter.PartOfHospitalMes> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<PartOfHospitalAdapter.
                PartOfHospitalMes> data,String message,boolean status){
            this.total = total;
            this.data = data;
            this.message = message;
            this.status = status;
        }
    }

    public static JsonHead paraseJson(String jsonData){
        Gson gson = new Gson();
        JsonHead list = gson.fromJson(jsonData, JsonHead.class);
        return list;
    }

}
