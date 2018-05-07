package com.example.hospital_one.connection;

import com.google.gson.Gson;

import java.util.List;

public class DoctorCalendarConnection {

    public class DoctorCalendarMessage{
        public String departmentName;
        public int admissionNum;
        public String doctorName;
        public String admissionDate;
        public String hospitalId;
        public String doctorId;
        public String departmentId;
        public boolean isValid;
        public String admissionId;
        public String admissionPeriod;
        public String hospitalName;
        public int remainingNum;
        public DoctorCalendarMessage(
                String departmentName,
                int admissionNum,
                String doctorName,
                String admissionDate,
                String hospitalId,
                String doctorId,
                String departmentId,
                boolean isValid,
                String admissionId,
                String admissionPeriod,
                String hospitalName,
                int remainingNum
        ){
            this.departmentName = departmentName;
            this.admissionNum = admissionNum;
            this.doctorName = doctorName;
            this.admissionDate = admissionDate;
            this.hospitalId = hospitalId;
            this.doctorId = doctorId;
            this.departmentId = departmentId;
            this.isValid = isValid;
            this.admissionId = admissionId;
            this.admissionPeriod = admissionPeriod;
            this.hospitalName = hospitalName;
            this.remainingNum = remainingNum;
        }
    }

    public class JsonHead{
        public int total;
        public List<DoctorCalendarMessage> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<DoctorCalendarMessage> data,String message,boolean status){
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
