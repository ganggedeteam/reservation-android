package com.example.hospital_one.intenet_connection;

import com.google.gson.Gson;

import java.util.List;

public class PatientConnection {
    public static final String[] relationName = {"本人","父母","夫妻","子女","亲戚","其他"};
    public class PatientMessage{
        String patientName;
        String gmtModified;
        String patientId;
        String idCard;
        String gmtCreate;
        String userId;
        String relation;
        public String getPatientId(){
            return this.patientId;
        }

        public String getPatientName(){
            return this.patientName;
        }

        public String getUserId(){
            return this.userId;
        }

        public String getIdCard(){
            return this.idCard;
        }

        public String getRelation(){
            return relationName[
                    Character.digit(this.relation.charAt(0),10)];
        }

        public String getGmtCreate(){
            return this.gmtCreate;
        }

        public String getGmtModified(){
            return this.gmtModified;
        }

        public PatientMessage(
                String patientName,
                String gmtModified,
                String patientId,
                String idCard,
                String userId,
                String gmtCreate,
                String relation){
            this.patientName = patientName;
            this.gmtModified = gmtModified;
            this.patientId = patientId;
            this.idCard = idCard;
            this.userId = userId;
            this.gmtCreate = gmtCreate;
            this.relation = relation;
        }
    }

    public class JsonHead{
        public int total;
        public List<PatientMessage> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<PatientMessage> data,String message,boolean status){
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
