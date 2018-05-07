package com.example.hospital_one;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.example.hospital_one.adapter.DoctorCalendarAdapter;
import com.example.hospital_one.adapter.OnItemClickListener;
import com.example.hospital_one.connection.DoctorCalendarConnection;
import com.example.hospital_one.connection.DoctorConnection;
import com.example.hospital_one.connection.InternetConnection;

import java.util.ArrayList;
import java.util.List;

public class DoctorCalendarActivity extends AppCompatActivity {

    CalendarMessageTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_calendar);
        ActionBar actionBar = getActionBar();
        if(actionBar!=null)actionBar.hide();
        Intent intent = getIntent();
        int year = intent.getIntExtra("year",0);
        int month = intent.getIntExtra("month",0);
        int day = intent.getIntExtra("day",0);
        String hospitalId = intent.getStringExtra("hospitalId");
        String departmentId = intent.getStringExtra("departmentId");

        task = new CalendarMessageTask("{\"hospitalId\": \"" + hospitalId + "\"," +
                "\"admissionDate\": \"" + year+ "-" + (month<10?"0"+month:month) + "-" + (day<10?"0"+day:day)+ "\"," +
                "\"departmentId\": \"" + departmentId + "\"}");

    }

    private void setAdapter(List<DoctorCalendarAdapter.DoctorCalendarView> list){
        RecyclerView doctorDuty = (RecyclerView) findViewById(R.id.DoctorDuty);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        doctorDuty.setLayoutManager(layoutManager);
        DoctorCalendarAdapter doctorCalendarAdapter = new DoctorCalendarAdapter(list);
        doctorCalendarAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        doctorDuty.setAdapter(doctorCalendarAdapter);
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

        DoctorConnection.JsonHead result;

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("reservationAdd","");
            String respone = InternetConnection.ForInternetConnection(url,jsonData);



            if(result == null){
                this.message = 1;
            }else{
                this.message = 0;
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

    public class CalendarMessageTask extends AsyncTask<Void, Void, Boolean> {
        private int message;
        private final String jsonData;
        public CalendarMessageTask(String jsonData){
            this.jsonData = jsonData;
        }

        DoctorCalendarConnection.JsonHead result;

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);

            String url = reader.getString("ip","") +
                    reader.getString("calendarList","");

            String response = InternetConnection.ForInternetConnection(url,jsonData);
            result = DoctorCalendarConnection.parseJsonData(response);
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
                    List<DoctorCalendarAdapter.DoctorCalendarView> list = new ArrayList<>();
                    for(DoctorCalendarConnection.
                            DoctorCalendarMessage doctorCalendarMessage:result.data){
                        DoctorCalendarAdapter.DoctorCalendarView view =
                                new DoctorCalendarAdapter.DoctorCalendarView();
                        DoctorTask doctorTask = new DoctorTask(view,doctorCalendarMessage.doctorId);
                        doctorTask.execute((Void)null);
                        view.admissionNum = doctorCalendarMessage.admissionNum;
                        view.admissionPeriod = doctorCalendarMessage.admissionPeriod;
                        view.remainingNum = doctorCalendarMessage.remainingNum;
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
            DoctorConnection.JsonHead result;
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("doctorPage","");
            String response = InternetConnection.ForInternetConnection(url,jsonData);
            result = DoctorConnection.parseJsonData(response);
            if(result == null){
                this.message = 1;
            }else{
                this.message = 0;
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

}
