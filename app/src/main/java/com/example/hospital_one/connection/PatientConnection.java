package com.example.hospital_one.connection;

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
    public static PatientAddMessageHead parsePatientAddMessageHead(String jsonData){
        Gson gson = new Gson();
        PatientAddMessageHead list = gson.fromJson(jsonData, PatientAddMessageHead.class);
        return list;
    }
    public class PatientAddMessage{
        public int total ;
        public String pageSize;
        public String pageNo;
        public String patientId;
        public String patientName;
        public String userId;
        public String idCard;
        public String relation;
        public String gmtCreate ;
        public String gmtModified ;
        public int startRecode;
        public PatientAddMessage(
                int total ,
                String pageSize,
                String pageNo,
                String patientId,
                String patientName,
                String userId,
                String idCard,
                String relation,
                String gmtCreate ,
                String gmtModified ,
                int startRecode){
            this.total  = total ;
            this.pageSize = pageSize;
            this.pageNo = pageNo;
            this.patientId = patientId;
            this.patientName = patientName;
            this.userId = userId;
            this.idCard = idCard;
            this.relation = relation;
            this.gmtCreate  = gmtCreate ;
            this.gmtModified  = gmtModified ;
            this.startRecode = startRecode;
        }
    }

    public class PatientAddMessageHead{
        public PatientAddMessage data;
        public String message;
        public boolean status;
        public PatientAddMessageHead(
                PatientAddMessage data,
                String message,
                boolean status){
            this.data = data;
            this.message = message;
            this.status = status;
        }
    }
}
