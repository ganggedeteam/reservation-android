package com.example.hospital_one.connection;

import com.google.gson.Gson;

import java.util.List;

public class HospitalDepartmentTypeConnection {

    public class HospitalDepartmentType{
        public String name;
        public int id;
    }

    public class JsonHead{
        public List<HospitalDepartmentType> data;
        public String message;
        public boolean status;
    }

    public static JsonHead parseJsonData(String jsonData){
        Gson gson = new Gson();
        JsonHead list = gson.fromJson(jsonData,JsonHead.class);
        return list;
    }

    public class HospitalDetailDepartment{
        public String departmentName;
        public String hospitalId;
        public String departmentId;
        public String typeName;
        public int typeId;
    }

    public class DetailJsonHead{
        public List<HospitalDetailDepartment> data;
        public String message;
        public boolean status;
    }

    public static DetailJsonHead parseJsonHead(String jsonData){
        Gson gson = new Gson();
        DetailJsonHead list = gson.fromJson(jsonData,DetailJsonHead.class);
        return list;
    }

}
