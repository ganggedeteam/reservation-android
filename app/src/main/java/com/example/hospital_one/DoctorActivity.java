package com.example.hospital_one;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.example.hospital_one.intenet_connection.DepartmentConnection;
import com.example.hospital_one.intenet_connection.DoctorConnection;
import com.example.hospital_one.intenet_connection.HospitalConnection;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.part_hospital.OnItemClickListener;
import com.example.hospital_one.part_hospital.DoctorAdapter;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AcitivityBase {

    public static final int erke = 0;
    public static final int fuchan = 1;
    public static final int nanke = 2;
    public static final int zhongyi = 3;
    public static final int pifu = 4;
    public static final int quanke = 5;
    public static final int xinli = 6;
    public static final int puwai = 7;
    public static final int guke = 8;
    public static final int xinneike = 9;
    public static final int puneike = 10;
    public static final int huxineike = 11;
    public static final int shenjingneike = 12;
    public static final int zhongliuneike = 13;
    public static final int xiaohuneike = 14;
    public static final int neifenmike = 15;
    public static final int shenneike = 16;
    public static final int yanke = 17;
    public static final int erbihouke = 18;
    public static final int kouqiang = 19;
    public static final int yingyang = 20;
    public static final int quickFindDoctor = 100;

    public static final String[] title = { "儿科","妇产科","泌尿外科、男科",
            "中医科","皮肤性病科","全科","心理科","普外科","骨科","心内科","普内科",
            "呼吸内科","神经内科","肿瘤内科","消化内科","内分泌科","肾内科","眼科","耳鼻喉科",
            "口腔科","营养科"};


    private DoctorTask doctorTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        Intent intent = getIntent();
        String departmentId = intent.getStringExtra("departmentId");
        doctorTask = new DoctorTask(0,"\"hospitalId\": \"" + departmentId + "\"");
        doctorTask.execute((Void)null);
    }

    private void setAdapter(List<DoctorConnection.DoctorMessage> list){
        RecyclerView doctorListView = (RecyclerView) findViewById(R.id.doctor_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        doctorListView.setLayoutManager(layoutManager);
        DoctorAdapter doctorAdapter = new DoctorAdapter(list);
        doctorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        doctorListView.setAdapter(doctorAdapter);
    }

    public class DoctorTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData; //传输到服务器的接送数据

        //用户操作标识（0代表根据医院科室查询，1代表可是种类查询，2代表医院查询）
        private final int cursor;
        //与服务器连接信息
        private int message = 0;

        private List<DoctorConnection.DoctorMessage> doctorResult = new ArrayList<>();

        DoctorTask(int cursor,String jsonData) {
            this.cursor = cursor;
            this.jsonData = "{" + jsonData + ",\"pageNo\":\"";
        }

        private int DoctorSearch(int size,String jsonData,int i){
            DoctorConnection.JsonHead result = null;
            //读取服务器地址
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String url = reader.
                    getString("ip", "")
                    + reader.getString("doctorPage", "");
            //与服务器建立连接并接受返回消息
            String response = InternetConnection.
                    ForInternetConnection(url, jsonData + (i + 1) + "\"}");
            result = DoctorConnection.parseJsonData(response);

            //将json数据转化为类
            if (result == null) {
                this.message = 1;
            } else {
                if (i == 0) size = result.total;
                if (result.total == 0) {
                    this.message = 2;
                } else if (result.data.size() != 0) {
                    //将接受的地址信息提取
                    doctorResult.addAll(result.data);
                }
            }
            return size;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0;
            int size = 0;
            while(i < 1 || i < size/10 + 1) {
                if(cursor == 0) {
                    size = DoctorSearch(size,this.jsonData,i);
                }else if(this.cursor == 1){
                    DepartmentConnection.JsonHead result = null;
                    //读取服务器地址
                    SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                    String url = reader.
                            getString("ip", "")
                            + reader.getString("departmentPage", "");
                    //与服务器建立连接并接受返回消息
                    String response = InternetConnection.
                            ForInternetConnection(url, jsonData + (i + 1) + "\"}");
                    result = DepartmentConnection.parseJsonData(response);
                    if(i == 1)doctorResult = new ArrayList<>();

                    //将json数据转化为类
                    if (result == null) {
                        this.message = 1;
                    } else {
                        if (i == 0) size = result.total;
                        if (result.total == 0) {
                            this.message = 2;
                        } else if (result.data.size() != 0) {
                            for(int j = 0; j < result.data.size(); j++) {
                                String jsonDataDepartment = "{\"typeId\":" +
                                        result.data.get(j).getDepartmentTypeId() + ",\"pageNo\":\"";
                                int departmentSize = 0,k=0;
                                while(k<1 || k<departmentSize/10 + 1){
                                    departmentSize = DoctorSearch(departmentSize,jsonDataDepartment,k);
                                    k++;
                                }
                            }
                        }
                    }
                }else if(this.cursor == 2){
                    HospitalConnection.JsonHead result = null;
                    //读取服务器地址
                    SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                    String url = reader.
                            getString("ip", "")
                            + reader.getString("departmentPage", "");
                    //与服务器建立连接并接受返回消息
                    String response = InternetConnection.
                            ForInternetConnection(url, jsonData + (i + 1) + "\"}");
                    result = HospitalConnection.parseJsonData(response);
                    if(i == 1)doctorResult = new ArrayList<>();

                    //将json数据转化为类
                    if (result == null) {
                        this.message = 1;
                    } else {
                        if (i == 0) size = result.total;
                        if (result.total == 0) {
                            this.message = 2;
                        } else if (result.data.size() != 0) {
                            for(int j = 0; j < result.data.size(); j++) {
                                String jsonDataDepartment = "{\"hospitalId\":" +
                                        result.data.get(j).hospitalId + ",\"pageNo\":\"";
                                int hospitalSize = 0,k=0;
                                while(k<1 || k<hospitalSize/10 + 1){
                                    hospitalSize = DoctorSearch(hospitalSize,jsonDataDepartment,k);
                                    k++;
                                }
                            }
                        }
                    }
                }
                i++;
            }
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if(message == 1){
                    Toast.makeText(DoctorActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
                    Toast.makeText(DoctorActivity
                            .this,"查找不到本用户",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    if(this.cursor > 2 || this.cursor < 0){
                        Toast.makeText(DoctorActivity
                                .this,"标识代码错误",Toast.LENGTH_LONG).show();
                    }else{
                        setAdapter(doctorResult);
                    }
                }else{
                    Toast.makeText(DoctorActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            doctorTask = null;
        }
    }

//    private String departmentTypeId = null;
   /* private void initDoctorActivity(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
        Intent intent = getIntent();
        *//*if(intent.getStringExtra("departmentTypeId").equals("")) {
            int num = intent.getIntExtra("data", 2);
            TextView keshiName = (TextView) findViewById(R.id.keshiName);
            if (num == quickFindDoctor)
                keshiName.setText("快速找医生");
            else
                keshiName.setText(title[num]);
//            testExample();
        }
        else{
            TextView keshiName = (TextView) findViewById(R.id.keshiName);
            keshiName.setText(intent.getStringExtra("departmentName"));
            departmentTypeId = intent.getStringExtra("departmentTypeId");
        }*//*
    }*/

    /*public void testExample(){
        List<Doctor> doctors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Doctor doctor = new Doctor("我是谁", "儿科", "专家", "心脏手术专家", R.drawable.account);
            doctors.add(doctor);
        }
        RecyclerView doctorListView = (RecyclerView) findViewById(R.id.doctor_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        doctorListView.setLayoutManager(layoutManager);
        DoctorAdapter doctorAdapter = new DoctorAdapter(doctors);
        doctorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent1 = new Intent();
//                intent1.putExtra("data",position);
//                startActivity(intent1);
            }
        });
        doctorListView.setAdapter(doctorAdapter);
    }*/
}
