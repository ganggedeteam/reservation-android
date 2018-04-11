package com.example.hospital_one;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.example.hospital_one.intenet_connection.AddressConnection;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.intenet_connection.PatientConnection;
import com.example.hospital_one.part_hospital.OnButtonClickListener;
import com.example.hospital_one.part_hospital.OnItemClickListener;
import com.example.hospital_one.part_hospital.PatientAdapter;

import java.util.ArrayList;
import java.util.List;

public class PatientManagerActivity extends AppCompatActivity {

    public static PatientTask patientTask = null;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_manager);
        recyclerView = (RecyclerView)findViewById(R.id.PatientRecyclerView);
        initPatientManagerActivity();
    }

    void initPatientManagerActivity(){

    }

    public void setRecyclerView(final List<PatientConnection.PatientMessage> list){
        LinearLayoutManager layoutManager = new LinearLayoutManager(PatientManagerActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        PatientAdapter patientAdapter = new PatientAdapter(list);
        patientAdapter.setOnButtonClickListener(new OnButtonClickListener(){
            @Override
            public void onButtonClick(View view){

            }
        });
        recyclerView.setAdapter(patientAdapter);
    }

    public void shoeMessageDialog(String message){
        AlertDialog.Builder builder = new
                AlertDialog.Builder(PatientManagerActivity.this);
        builder.setTitle("提示信息");
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class PatientTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private final int cursor;
        private int message = 0;
        AddressConnection.JsonHead result = null;

        PatientTask(int cursor,String jsonData) {
            this.cursor = cursor;
            if(cursor == 0){//0代表添加就诊人
                this.jsonData = jsonData;
            }else if(cursor == 1){//1代表删除就诊人
                this.jsonData = "{\"patient_id\":\""+jsonData+"\"}";
            }else if(cursor == 2) {//2代表查看所有就诊人
                this.jsonData = "{" + jsonData + ",\"pageNo\":\"";
            }else{
                this.jsonData = jsonData;
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String ip = reader.
                    getString("ip", "");
            if(cursor == 0){
                String url = ip +
                        reader.getString("patientAdd","");
                String respone = InternetConnection.ForInternetConnection(url,jsonData);

            }else if(cursor == 1){
                String url  = ip +
                        reader.getString("","");
                String respone = InternetConnection.ForInternetConnection(url,jsonData);

            }else if(cursor == 2) {
                int i = 0;
                int size = 0;
                while (i < 1 || i < size / 10 + 1) {
                    String url = ip
                            + reader.getString("patientPage", "");
                    String response = InternetConnection.ForInternetConnection(url, jsonData + (i + 1) + "\"}");
                    result = AddressConnection.parseJsonData(response);
                    if (result == null) {
                        this.message = 1;
                    } else {
                        if (i == 0) size = result.total;
                        if (result.total == 0) {
                            this.message = 2;
                        } else if (result.data.size() != 0) {
//                            listResult.addAll(result.data);
                        }
                    }
                    i++;
                }
            }
            else{
                return false;
            }
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if(message == 1){
                    Toast.makeText(PatientManagerActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
                    Toast.makeText(PatientManagerActivity
                            .this,"查找不到本用户",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    if(this.cursor == 0){
                        shoeMessageDialog("添加成功！");
                    }else if(this.cursor == 1){
                        shoeMessageDialog("删除成功！");
                    }else if(this.cursor == 2){

                    }

//                    task = null;
//                    finish();
                }else{
                    Toast.makeText(PatientManagerActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }else{
                shoeMessageDialog("网络传输代码错误，请与开发者联系");
            }
        }

        @Override
        protected void onCancelled() {
//            setRecyclerView(getConnect());
//            addressTask = null;
        }
    }

}
