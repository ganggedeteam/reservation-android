package com.example.hospital_one.intenet_connection;

import com.google.gson.Gson;

import java.util.List;

public class DoctorConnection {
    public class DoctorMessage{
        public String doctorName;
        public String doctorPhoto;
        public String doctorTitle;
        public String doctorId;
        public String hospitalId;
        public String sex;
        public String skill;
        public String typeName;
        public int typeId;
        public String hospitalName;
        public String introduction;
        public DoctorMessage(
                String doctorName,
                String doctorPhoto,
                String doctorTitle,
                String doctorId,
                String hospitalId,
                String sex,
                String skill,
                String typeName,
                int typeId,
                String hospitalName,
                String introduction){
            this.doctorName = doctorName;
            this.doctorPhoto = doctorPhoto;
            this.doctorTitle = doctorTitle;
            this.doctorId = doctorId;
            this.hospitalId = hospitalId;
            this.sex = sex;
            this.skill = skill;
            this.typeName = typeName;
            this.typeId = typeId;
            this.hospitalName = hospitalName;
            this.introduction = introduction;

        }
    }

    public class JsonHead{
        public int total;
        public List<DoctorMessage> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<DoctorMessage> data,String message,boolean status){
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
