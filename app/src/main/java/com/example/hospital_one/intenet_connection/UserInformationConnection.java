package com.example.hospital_one.intenet_connection;

import android.text.TextUtils;
import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserInformationConnection {

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public class UserInformation{
        public String gmtModified;
        public int city;
        public String userPhone;
        public String sex;
        public int country;
        public String userPwd;
        public String userName;
        public String gmtCreate;
        public String detailAddr;
        public int province;
        public String cityName;
        public String provinceName;
        public String countyName;
        public UserInformation(
                String gmtModified,
                int city,
                String userPhone,
                String sex,
                int country,
                String userPwd,
                String userName,
                String gmtCreate,
                String detailAddr,
                int province,
                String cityName,
                String provinceName,
                String countyName) {
            this.gmtModified = gmtModified;
            this.city = city;
            this.userPhone = userPhone;
            this.sex = sex;
            this.country = country;
            this.userPwd = userPwd;
            this.userName = userName;
            this.gmtCreate = gmtCreate;
            this.detailAddr = detailAddr;
            this.province = province;
            this.cityName = cityName;
            this.provinceName = provinceName;
            this.countyName = countyName;
        }
    }

    public class JsonHead{
        public int total;
        public List<UserInformation> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<UserInformation> data,String message,boolean status){
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

    public class UserInformationUpdateBackMessage{
        String total;
        String pageSize;
        String pageNo;
        String userPhone;
        String userName;
        String sex;
        String province;
        String provinceName;
        String city;
        String cityName;
        String county;
        String countyName;
        String detailAddr;
        String userPwd;
        String gmtCreate;
        String gmtModified;
        String startRecode;
        public UserInformationUpdateBackMessage(
                String total,
                String pageSize,
                String pageNo,
                String userPhone,
                String userName,
                String sex,
                String province,
                String provinceNam,
                String city,
                String cityName,
                String county,
                String countyName,
                String detailAddr,
                String userPwd,
                String gmtCreate,
                String gmtModified,
                String startRecode){
            this.total = total;
            this.pageSize = pageSize;
            this.pageNo = pageNo;
            this.userPhone = userPhone;
            this.userName = userName;
            this.sex = sex;
            this.province = province;
            this.provinceName = provinceName;
            this.city = city;
            this.cityName = cityName;
            this.county = county;
            this.countyName = countyName;
            this.detailAddr = detailAddr;
            this.userPwd = userPwd;
            this.gmtCreate = gmtCreate;
            this.gmtModified = gmtModified;
            this.startRecode = startRecode;
        }
    }

    public class UserInformationAddBackMessage{
        List<UserInformationUpdateBackMessage> data;
        String message;
        boolean status;
        public UserInformationAddBackMessage(
                List<UserInformationUpdateBackMessage> data,
                String message,
                boolean status){
            this.data = data;
            this.message = message;
            this.status = status;
        }
    }

    public static UserInformationUpdateBackMessage parseUpdateMessageJsonData(String jsonData){
        Gson gson = new Gson();
        UserInformationUpdateBackMessage list = gson.fromJson(jsonData, UserInformationUpdateBackMessage.class);
        return list;
    }

    public static UserInformationAddBackMessage parseAddMessageJsonData(String jsonData){
        Gson gson = new Gson();
        UserInformationAddBackMessage list = gson.fromJson(jsonData, UserInformationAddBackMessage.class);
        return list;
    }
}
