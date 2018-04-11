package com.example.hospital_one.intenet_connection;

public class PatientConnection {
    public class PatientMessage{
        String patientId;
        String patientName;
        String userId;
        String idCard;
        String relation;
        String gmtCreate;
        String gmtModified;
        final String[] relationName = {"本人","父母","夫妻","子女","亲戚","其他"};
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
                String patientId,
                String patientName,
                String userId,
                String idCard,
                String relation,
                String gmtCreate,
                String gmtModified){
            this.patientId = patientId;
            this.patientName = patientName;
            this.userId = userId;
            this.idCard = idCard;
            this.relation = relation;
            this.gmtCreate = gmtCreate;
            this.gmtModified = gmtModified;
        }
    }
}
