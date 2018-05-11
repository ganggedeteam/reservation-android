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
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.hospital_one.connection.InternetConnection;
import com.example.hospital_one.connection.LoginBackMessage;
import com.example.hospital_one.connection.PatientConnection;
import com.example.hospital_one.adapter.OnButtonClickListener;
import com.example.hospital_one.adapter.PatientAdapter;

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
        final View view = View.inflate(this,R.layout.patient_add_layout,null);
        builder.setView(view);
        builder.setTitle("添加就诊人");
        final RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.RealtionGroup);
//        final RadioGroup anoRadioGroup = (RadioGroup)view.findViewById(R.id.AnoRealtionGroup);

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.e("radioGroup" , "onCheckedChanged: " + checkedId + "  " + radioGroup.getCheckedRadioButtonId());
//                if(checkedId != -1 && checkedId != radioGroup.getCheckedRadioButtonId()) {
//                    radioButtonNum = checkedId;
//                    if(anoRadioGroup.getCheckedRadioButtonId() != -1)
//                        anoRadioGroup.check(-1);
//                }
//            }
//        });
//        anoRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.e("anoRadioGroup" , "onCheckedChanged: " + checkedId+ "  " + anoRadioGroup.getCheckedRadioButtonId());
//                if(checkedId != -1 && checkedId != anoRadioGroup.getCheckedRadioButtonId()) {
//                    radioButtonNum = checkedId;
//                    if(radioGroup.getCheckedRadioButtonId() != -1)
//                        radioGroup.check(-1);
//                }
//            }
//        });
        final EditText patientNameEditText = view.findViewById(R.id.PatientNameEditText);
        final EditText patientIdCardEditText = (EditText)view.findViewById(R.id.PatientIdCardEditText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (patientNameEditText.getText().toString().equals("") ||
                        patientNameEditText.getText().toString().split(" ") == null
                        || patientNameEditText.getText().toString().length() < 3) {
                    showMessageDialog("请检查就诊人姓名信息是否正确");
                    return;
                } else if (patientIdCardEditText.getText().equals("") ||
                        patientIdCardEditText.getText().toString().split(" ") == null ||
                        patientIdCardEditText.getText().toString().length() != 18) {
                    showMessageDialog("请检查身份证号信息是否正确");
                    return;
                } else {
                    SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
                    int theRelation;
                    if(radioGroup.getCheckedRadioButtonId() != -1) {
                        RadioButton radioButton = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                        switch (radioButton.getText().toString()){
                            case "本人":theRelation = 0;break;
                            case "父母":theRelation = 1;break;
                            case "夫妻":theRelation = 2;break;
                            case "子女":theRelation = 3;break;
                            case "亲戚":theRelation = 4;break;
                            case "其他":theRelation = 5;break;
                            default: theRelation = 6;
                        }
                    }else{
                        theRelation = -1;
                    }
                    Log.e("theRelation", "onClick: " + theRelation );
                    if (theRelation == -1) {
                        patientAddTask = new PatientTask(0, "{\"patientName\": \"" + patientNameEditText.getText().toString() + "\","
                                +"\"userId\": \"" + reader.getString("account","") + "\","
                                +"\"idCard\": \"" + patientIdCardEditText.getText().toString() + "\"}");
                        patientAddTask.execute((Void)null);
                    } else if (theRelation <= 5 && theRelation >= 0) {
                        patientAddTask = new PatientTask(0, "{\"patientName\": \"" + patientNameEditText.getText().toString() + "\","
                                +"\"userId\": \""+ reader.getString("account","") + "\","
                                + "\"idCard\": \"" + patientIdCardEditText.getText().toString() + "\","
                                + "\"relation\": \"" + theRelation + "\",\"cardType\":\"" + 0 + "\"}");
                        patientAddTask.execute((Void)null);
                    }
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
        LinearLayout patientNumberNULL = (LinearLayout)findViewById(R.id.PatientNumberNULL);
        if(list == null || list.size() == 0){
            patientNumberNULL.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            patientNumberNULL.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");
            if(cursor == 0){
                String url2 = ip +
                        reader.getString("patientAdd","");
                PatientConnection.PatientAddMessageHead resultAdd;
                String response = InternetConnection.ForInternetHeaderConnection(url2,key,token,jsonData);
                SharedPreferences.Editor editor = getSharedPreferences("error_file",MODE_PRIVATE).edit();
                editor.putString("addPatient",response);
                editor.apply();
                Log.e("response", "doInBackground: " + response );
                resultAdd = PatientConnection.parsePatientAddMessageHead(response);
                if (resultAdd == null) {
                    this.message = 1;
                } else if(resultAdd.message.equals("success")){
                    return true;
                }else{
                    return false;
                }

            }else if(cursor == 1){
                String url1 = ip + reader.getString("patientPage","");

                result = PatientConnection.parseJsonData(
                        InternetConnection.ForInternetHeaderConnection(url1,key,token,jsonData));
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
                String respone = InternetConnection.ForInternetHeaderConnection(url2,key,token,jsonData);
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
                    String response = InternetConnection.ForInternetHeaderConnection(url, key,token,jsonData + (i + 1) + "\"}");
                    result = PatientConnection.parseJsonData(response);
                    if(i == 0)patientMessageList = new ArrayList<>();

                    if (result == null) {
                        this.message = 1;
                    } else {
                        if (i == 0) size = result.total;
                        else size += result.total;
                        patientMessageList.addAll(result.data);
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
                            .this,"删除失败",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    if(this.cursor == 0){
                        showMessageDialog("添加成功！");
                        SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
                        patientSelectTask = new PatientTask(2,reader.getString("account",""));
                        patientSelectTask.execute((Void)null);
                    }else if(this.cursor == 1){
                        view.setVisibility(View.GONE);
                        showMessageDialog("删除成功！");
                        SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
                        patientSelectTask = new PatientTask(2,reader.getString("account",""));
                        patientSelectTask.execute((Void)null);
                    }else if(this.cursor == 2){
                        setRecyclerView(patientMessageList);
                    }
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
