package com.example.hospital_one.intenet_connection;

import com.google.gson.Gson;

import java.util.List;

public class DepartmentControllerConnection {
    public class DepartmentOfHos{
        String departmentName;
        String hospitalId;
        String departmentId;
        String typeName;
        int typeId;

        public int getTypeId(){
            return this.typeId;
        }

        public String getDepartmentName(){
            return this.departmentName;
        }

        public String getHospitalId(){
            return this.hospitalId;
        }

        public String getDepartmentId(){
            return this.departmentId;
        }

        public String getTypeName(){
            return this.typeName;
        }

        public DepartmentOfHos(
                String departmentName,
                String hospitalId,
                String departmentId,
                String typeName,
                int typeId){
            this.departmentName = departmentName;
            this.hospitalId = hospitalId;
            this.departmentId = departmentId;
            this.typeName = typeName;
            this.typeId = typeId;
        }
    }

    public class JsonHead{
        public int total;
        public List<DepartmentOfHos> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<DepartmentOfHos> data,String message,boolean status){
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
