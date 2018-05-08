package com.example.hospital_one.connection;

import com.google.gson.Gson;

import java.util.List;

public class HospitalConnection {

    public static String[] hospitalLevel = {"三甲","三乙","三丙","三丁","二甲","二乙","二丙","一甲","一乙","一丙"};

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
        public int city;
        public String isValid;
        public int county;
        public String hospitalName;
        public String hospitalPhone;
        public String detailAddr;
        public int province;
        public String cityName;
        public String hospitalId;
        public String hospitalManager;
        public String provinceName;
        public String introduction;
        public String countyName;
        public HospitalMes(
                String hospitalGrade,
                int city,
                String isValid,
                int county,
                String hospitalName,
                String hospitalPhone,
                String detailAddr,
                int province,
                String cityName,
                String hospitalId,
                String hospitalManager,
                String provinceName,
                String introduction,
                String countyName){
            this.hospitalGrade = hospitalGrade == null || hospitalGrade.equals("")?"暂无":
                    hospitalLevel[(Character.digit(hospitalGrade.charAt(0),10))];
            this.city = city;
            this.isValid = isValid;
            this.county = county;
            this.hospitalName = hospitalName;
            this.hospitalPhone = hospitalPhone;
            this.detailAddr = detailAddr;
            this.province = province;
            this.cityName = cityName;
            this.hospitalId = hospitalId;
            this.hospitalManager = hospitalManager;
            this.provinceName = provinceName;
            this.introduction = introduction;
            this.countyName = countyName;
        }

    }

    public static JsonHead parseJsonData(String jsonData){
        Gson gson = new Gson();
        JsonHead list = gson.fromJson(jsonData, JsonHead.class);
        return list;
    }

}
