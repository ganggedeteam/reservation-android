package com.example.hospital_one;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.intenet_connection.LoginBackMessage;
import com.example.hospital_one.intenet_connection.PatientConnection;
import com.example.hospital_one.part_hospital.OnButtonClickListener;
import com.example.hospital_one.part_hospital.PatientAdapter;

import java.util.ArrayList;
import java.util.List;

public class PatientManagerActivity extends AppCompatActivity {

    private PatientTask patientSelectTask = null;
    private PatientTask patientDeleteTask = null;
    private PatientTask patientAddTask = null;
    Button addPatient;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_manager);
        recyclerView = (RecyclerView)findViewById(R.id.PatientRecyclerView);
        addPatient = (Button)findViewById(R.id.AddPatientButton);
        initPatientManagerActivity();
    }

    List<PatientConnection.PatientMessage> patientMessageList = null;
    void initPatientManagerActivity(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPatientDialog();
            }
        });
        SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
        patientSelectTask = new PatientTask(2,reader.getString("account",""));
        patientSelectTask.execute((Void)null);
    }

    public void showAddPatientDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.patient_add_layout,null);
        builder.setView(view);
        final Spinner spinner = (Spinner)findViewById(R.id.PatientRelationSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,PatientConnection.relationName);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        final EditText patientNameEditText = (EditText)findViewById(R.id.PatientNameEditText);
        final EditText patientIdCardEditText = (EditText)findViewById(R.id.PatientIdCardEditText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
                if(spinner.isSelected() && !patientIdCardEditText.
                        getText().toString().equals("") && !patientNameEditText.getText().toString().equals("")
                        && !patientIdCardEditText.getText().toString().contains(" ") && !patientNameEditText.getText().toString().contains(" ")){
                    patientAddTask = new PatientTask(0,"{\n" +
                            "      \"patientName\": \""+ patientNameEditText.getText().toString() +"\",\n" +
                            "      \"idCard\": \""+ patientIdCardEditText.getText().toString() +"\",\n" +
                            "      \"userId\": \"" + reader.getString("account","") + "\",\n" +
                            "      \"relation\": \"" + spinner.getSelectedItemPosition() +"\"\n" +
                            "    }");
                    patientAddTask.execute((Void)null);
                    dialog.dismiss();
                }else if(!spinner.isSelected()){
                    spinner.requestFocus();
                }else if(patientIdCardEditText.
                        getText().toString().equals("") || patientIdCardEditText.getText().toString().contains(" ")){
                    patientIdCardEditText.requestFocus();
                }else if(patientNameEditText.getText().toString().equals("") ||
                        patientNameEditText.getText().toString().contains(" ")){
                    patientNameEditText.requestFocus();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.show();

    }

    public void setRecyclerView(final List<PatientConnection.PatientMessage> list){
        LinearLayoutManager layoutManager = new LinearLayoutManager(PatientManagerActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        PatientAdapter patientAdapter = new PatientAdapter(list);
        patientAdapter.setOnButtonClickListener(new OnButtonClickListener(){
            @Override
            public void onButtonClick(View view,String patientId){
                patientDeleteTask = new PatientTask(1,patientId,view);
                patientDeleteTask.execute((Void)null);
            }
        });
        recyclerView.setAdapter(patientAdapter);
    }

    public void showMessageDialog(String message){
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
        private final View view;
        PatientConnection.JsonHead result = null;

        PatientTask(int cursor,String jsonData,View view){
            this.cursor = cursor;
            if(cursor == 1){
                this.jsonData = "{\"patientId\":\""+jsonData+"\"}";
                this.view = view;
            }else {
                this.jsonData = "{\"pageNo\":\"";
                this.view = null;
            }
        }

        PatientTask(int cursor,String jsonData) {
            this.cursor = cursor;
            if(cursor == 0){//0代表添加就诊人
                this.jsonData = jsonData;
            }else if(cursor == 1){//1代表删除就诊人
                this.jsonData = "{\"patientId\":\""+jsonData+"\"}";
            }else if(cursor == 2) {//2代表查看所有就诊人
                this.jsonData = "{\"userId\":\"" + jsonData + "\",\"pageNo\":\"";
            }else{
                this.jsonData = jsonData;
            }
            this.view = null;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String ip = reader.
                    getString("ip", "");
            if(cursor == 0){
                String url2 = ip +
                        reader.getString("patientAdd","");
                String respone = InternetConnection.ForInternetConnection(url2,jsonData);
                result = PatientConnection.parseJsonData(respone);
                if (result == null) {
                    this.message = 1;
                } else if(result.message.equals("success")){
                    if (result.total == 0) {
                        this.message = 2;
                    } else if (result.data.size() != 0) {
//                            listResult.addAll(result.data);
                    }
                }else{
                    return false;
                }

            }else if(cursor == 1){
                String url1 = ip + reader.getString("patientPage","");
                result = PatientConnection.parseJsonData(
                        InternetConnection.ForInternetConnection(url1,jsonData));
                if(result == null){
                    this.message = 1;
                    return true;
                }else{
                    if(result.total == 0){
                        this.message = 2;
                        return true;
                    }
                }
                String url2  = ip +
                        reader.getString("patientDelete","");
                String respone = InternetConnection.ForInternetConnection(url2,jsonData);
                if (respone == null) {
                    this.message = 1;
                }
                this.message = LoginBackMessage.parseJson(respone);

            }else if(cursor == 2) {
                int i = 0;
                int size = 0;
                while (i < 1 || i < size / 10 + 1) {
                    String url = ip
                            + reader.getString("patientPage", "");
                    String response = InternetConnection.ForInternetConnection(url, jsonData + (i + 1) + "\"}");
                    result = PatientConnection.parseJsonData(response);
                    if(i == 0)patientMessageList = new ArrayList<>();

                    if (result == null) {
                        this.message = 1;
                    } else {
                        if (i == 0) size = result.total;
                        if (result.total == 0) {
                            this.message = 2;
                        } else if (result.data.size() != 0) {
                            patientMessageList.addAll(result.data);
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
                            .this,"查找不到此就诊人",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    if(this.cursor == 0){
                        showMessageDialog("添加成功！");
                    }else if(this.cursor == 1){
                        view.setVisibility(View.GONE);
                        showMessageDialog("删除成功！");
                    }else if(this.cursor == 2){
                        setRecyclerView(patientMessageList);
                    }
//                    task = null;
//                    finish();
                }else{
                    Toast.makeText(PatientManagerActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }else{
                showMessageDialog("网络传输代码错误，请与开发者联系");
            }
        }

        @Override
        protected void onCancelled() {
            if(cursor == 0){
                patientAddTask = null;
            }else if(cursor == 1){
                patientDeleteTask = null;
            }else if(cursor == 2){
                patientSelectTask = null;
            }
        }
    }

}
