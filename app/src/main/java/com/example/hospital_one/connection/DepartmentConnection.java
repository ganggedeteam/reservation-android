package com.example.hospital_one.connection;

import com.google.gson.Gson;

import java.util.List;

public class DepartmentConnection {

    public class DepartmentKindsMes{
        int departmentTypeId;
        String departmentTypeName;
        String remark;

        public String getDepartmentTypeName(){
            return this.departmentTypeName;
        }

        public int getDepartmentTypeId(){
            return  this.departmentTypeId;
        }

        public String getRemark(){
            return this.remark;
        }

        public DepartmentKindsMes(
                int departmentTypeId,
                String departmentTypeName,
                String remark){
            this.departmentTypeId = departmentTypeId;
            this.departmentTypeName = departmentTypeName;
            this.remark = remark;
        }
    }

    public class JsonHead{
        public int total;
        public List<DepartmentKindsMes> data;
        public String message;
        public boolean status;
        public JsonHead(int total,List<
                DepartmentKindsMes> data,String message,boolean status){
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
