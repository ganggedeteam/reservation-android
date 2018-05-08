package com.example.hospital_one;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.example.hospital_one.adapter.DoctorCalendarAdapter;
import com.example.hospital_one.adapter.OnItemClickListener;
import com.example.hospital_one.connection.*;

import java.util.ArrayList;
import java.util.List;

public class DoctorCalendarActivity extends AppCompatActivity {

    CalendarMessageTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_calendar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        Intent intent = getIntent();
        int year = intent.getIntExtra("year",0);
        int month = intent.getIntExtra("month",0);
        int day = intent.getIntExtra("day",0);
        String hospitalId = intent.getStringExtra("hospitalId");
        String departmentId = intent.getStringExtra("departmentId");
        SharedPreferences.Editor editor = getSharedPreferences("error_file",MODE_PRIVATE).edit();
        editor.putString("theJSonData","{\"hospitalId\": \"" + hospitalId + "\"," +
                "\"admissionDate\": \""+ year + "-" + (month < 10?"0" + month:"" + month) + "-" + (day < 10?"0" + day: "" + day) +"\"," +
                "\"departmentId\": \"" + departmentId + "\"}");
        editor.apply();

        task = new CalendarMessageTask("{\"hospitalId\": \"" + hospitalId + "\"," +
                "\"admissionDate\": \""+ year + "-" + (month < 10?"0" + month:"" + month) + "-" + (day < 10?"0" + day: "" + day) +"\"," +
                "\"departmentId\": \"" + departmentId + "\"");
        task.execute((Void)null);

    }

    private void setAdapter(final List<DoctorCalendarAdapter.DoctorCalendarView> list){
        RecyclerView doctorDuty = (RecyclerView) findViewById(R.id.DoctorDuty);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        doctorDuty.setLayoutManager(layoutManager);
        DoctorCalendarAdapter doctorCalendarAdapter = new DoctorCalendarAdapter(list);
        doctorCalendarAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PatientTask patientTask = new PatientTask(list.get(position).admissionId);
                patientTask.execute((Void)null);
            }
        });
        doctorDuty.setAdapter(doctorCalendarAdapter);
    }

    int patientChoice;
    private void showPatientDialog(final List<PatientConnection.PatientMessage> list,
                                   final String admissionId){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择就诊人");

        List<String> patientName = new ArrayList<>();
        for(int i = 0; i < list.size();i++){

            PatientConnection.PatientMessage message = list.get(i);
            patientName.add(message.getPatientName() + " " + message.getRelation());

        }

        builder.setSingleChoiceItems(patientName.toArray(new String[1]),0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                patientChoice = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ReservationTask reservationTask = new ReservationTask("{\"isAdmission\": \"0\",\n" +
                        "      \"patientId\": \"" + list.get(patientChoice).getPatientId() + "\",\n" +
                        "\"userPhone\": \"" + list.get(patientChoice).getUserId() +"\",\n" +
                        "\"admissionId\": \"" + admissionId + "\"\n" +
                        "}");
                reservationTask.execute((Void)null);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void ShowMessage(String string){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息");
        builder.setMessage(string);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class ReservationTask extends AsyncTask<Void, Void, Boolean>{
        private int message;
        private final String jsonData;
        public ReservationTask(String jsonData){
            this.jsonData = jsonData;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("reservationAdd","");
            String response = InternetConnection.ForInternetConnection(url,jsonData);
            if(response == null || response.equals("")){
                this.message = 1;
            }else{
                this.message = ReservationConnection.parseAddReservationJson(response);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if(this.message == 0){
                    ShowMessage("挂号成功");
                }else if(this.message == 1){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "网络连接错误",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "Json数据解析错误！",Toast.LENGTH_LONG).show();
                }else if(this.message == 3){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "您已挂过号",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(DoctorCalendarActivity.this,
                            "未知错误，请与开发者联系!",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {

        }

    }

    public class CalendarMessageTask extends AsyncTask<Void, Void, Boolean> {
        private int message;
        private final String jsonData;
        public CalendarMessageTask(String jsonData){
            this.jsonData = jsonData + ",\"pageNo\":\"";
        }

        DoctorCalendarConnection.JsonHead result;
        List<DoctorCalendarConnection.DoctorCalendarMessage> doctorCalendarMessage;

        @Override
        protected Boolean doInBackground(Void... voids) {
            int i = 0,size = 0;
            while(i < 1 || i < size/10 + 1) {
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String url = reader.getString("ip", "") +
                        reader.getString("calendarList", "");
                String response = InternetConnection.ForInternetConnection(url, jsonData + (i + 1) + "\"}");
                SharedPreferences.Editor editor = getSharedPreferences("error_file",MODE_PRIVATE).edit();
                editor.putString("response",response);
                editor.apply();
                result = DoctorCalendarConnection.parseJsonData(response);
                if(i==0)doctorCalendarMessage = new ArrayList<>();
                if (result == null) {
                    this.message = 1;
                }else if(!result.status){
                    this.message = 3;
                } else {
                    if (result.data.size() == 0 && i == 0) {
                        this.message = 2;
                        return true;
                    } else {
                        doctorCalendarMessage.addAll(result.data);
                        this.message = 0;
                    }
                }
                i++;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if(this.message == 0){
                    //成功进行的操作
                    SharedPreferences.Editor editor = getSharedPreferences("error_file",MODE_PRIVATE).edit();
                    editor.putInt("doctorReservationMessage",doctorCalendarMessage.size());
                    editor.apply();
                    List<DoctorCalendarAdapter.DoctorCalendarView> list = new ArrayList<>();
                    for(DoctorCalendarConnection.
                            DoctorCalendarMessage doctorCalendarMessage:doctorCalendarMessage){
                        DoctorCalendarAdapter.DoctorCalendarView view =
                                new DoctorCalendarAdapter.DoctorCalendarView();
                        DoctorTask doctorTask = new DoctorTask(view,doctorCalendarMessage.doctorId);
                        doctorTask.execute((Void)null);
                        view.admissionNum = doctorCalendarMessage.admissionNum;
                        view.admissionPeriod = doctorCalendarMessage.admissionPeriod;
                        view.remainingNum = doctorCalendarMessage.remainingNum;
                        view.admissionId = doctorCalendarMessage.admissionId;
                        list.add(view);
                    }
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setAdapter(list);
                }else if(this.message == 1){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "网络连接错误",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "查找不到相关的信息！",Toast.LENGTH_LONG).show();
                }else if(this.message == 3){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "Json信息错误!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(DoctorCalendarActivity.this,
                            "未知错误，请与开发者联系!",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }

    }

    public class DoctorTask extends AsyncTask<Void, Void, Boolean>{
        private int message;
        private final String jsonData;
        DoctorCalendarAdapter.DoctorCalendarView view;
        public DoctorTask(DoctorCalendarAdapter.DoctorCalendarView view,
                          String jsonData){
            this.jsonData = "{ \"doctorId\":\"" + jsonData + "\"}";
            this.view = view;
        }

        DoctorConnection.JsonHead result;

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("doctorPage","");
            String response = InternetConnection.ForInternetConnection(url,jsonData);
            result = DoctorConnection.parseJsonData(response);
            if(result == null){
                this.message = 1;
            }else{
                if(result.total == 0){
                    this.message = 2;
                }else {
                    this.message = 0;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if(this.message == 0){
                    //成功进行的操作
                    if(result.total != 0) {
                        view.skill = result.data.get(0).skill;
                        view.doctorPhoto = result.data.get(0).doctorPhoto;
                        view.doctorTitle = result.data.get(0).doctorTitle;
                    }
                }else if(this.message == 1){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "网络连接错误",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "查找不到相关的信息！",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(DoctorCalendarActivity.this,
                            "未知错误，请与开发者联系!",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {

        }

    }

    public class PatientTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;
        private final String admissionId;
        PatientConnection.JsonHead result = null;
        List<PatientConnection.PatientMessage> patientMessageList;

        PatientTask(String admissionId){
            this.admissionId = admissionId;
            SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
            this.jsonData = "{\"userId\": \""
                    + reader.getString("account","") + "\"," + "\"pageNo\":\"";
            SharedPreferences.Editor editor = getSharedPreferences("error_file",MODE_PRIVATE).edit();
            editor.putString("account",reader.getString("account",""));
            editor.apply();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            int i = 0;
            int size = 0;
            while (i < 1 || i < size / 10 + 1) {
                String url = reader.getString("ip","")
                        + reader.getString("patientPage", "");
                String response = InternetConnection.ForInternetConnection(url, jsonData + (i + 1) + "\"}");
                result = PatientConnection.parseJsonData(response);
                if(i == 0)patientMessageList = new ArrayList<>();
                if (result == null) {
                    this.message = 1;
                } else {
                    if (i == 0) size = result.total;
                    if (result.total == 0 && i == 0) {
                        this.message = 2;
                    } else if (result.data.size() != 0) {
                        patientMessageList.addAll(result.data);
                        this.message = 0;
                    }else{
                        this.message = 0;
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
                    Toast.makeText(DoctorCalendarActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
                    ShowMessage("您的就诊人列表为空，请移步个人中心->就诊人管理添加就诊人");
                }else if(message == 0) {
                    showPatientDialog(patientMessageList,admissionId);
                }else{
                    Toast.makeText(DoctorCalendarActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }else{
                ShowMessage("网络传输代码错误，请与开发者联系");
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

}
