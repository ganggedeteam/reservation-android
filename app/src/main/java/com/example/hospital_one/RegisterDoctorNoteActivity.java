package com.example.hospital_one;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.example.hospital_one.adapter.DoctorCalendarAdapter;
import com.example.hospital_one.adapter.OnCancelButton;
import com.example.hospital_one.adapter.OnRegisterNoteAdaterButtonClicked;
import com.example.hospital_one.adapter.RegisterDoctorNoteAdapter;
import com.example.hospital_one.connection.DoctorCalendarConnection;
import com.example.hospital_one.connection.DoctorConnection;
import com.example.hospital_one.connection.InternetConnection;
import com.example.hospital_one.connection.ReservationConnection;

import java.util.ArrayList;
import java.util.List;

public class RegisterDoctorNoteActivity extends AppCompatActivity {

    private RegisterGetNoteTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor_note);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        task = new RegisterGetNoteTask();
        task.execute((Void)null);

    }


    private void setAdapter(List<RegisterDoctorNoteAdapter
            .RegisterNoteItemDetailMessage> registerNoteItemDetailMessageList){

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RegisterNoteRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RegisterDoctorNoteAdapter registerDoctorNoteAdapter=
                new RegisterDoctorNoteAdapter(registerNoteItemDetailMessageList);
        registerDoctorNoteAdapter.setOnCancelButton(new OnCancelButton() {
            @Override
            public void OnCancelButtonClicked(int position) {

                //取消挂号

            }
        });

        recyclerView.setAdapter(registerDoctorNoteAdapter);
    }


    private class RegisterGetNoteTask extends AsyncTask<Void, Void, Boolean>{

        private final String jsonData;
        private int message;
        private List<ReservationConnection.ReservationMessage> reservationMessages;

        public RegisterGetNoteTask(){
            SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
            jsonData = "{\"userPhone\": \"" +
                    reader.getString("account","") + "\",\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("reservationList","");

            int i = 0,size = 0;
            while(i < 1 || i < size / 10 + 1){
                ReservationConnection.JsonHead result;
                String response = InternetConnection.ForInternetConnection(url,jsonData + (i+1)+"\"}");
                if(response == null || response.equals(""))return false;
                if(ReservationConnection.parseAddReservationJson(response) == 3
                        || ReservationConnection.parseAddReservationJson(response) == 2 ){
                    this.message = 2;
                    return true;
                }
                result = ReservationConnection.parseJson(response);
                if(result == null){
                    this.message = 1;
                }else{
                    if(i == 0)reservationMessages = new ArrayList<>();
                    if (i == 0) size = result.data.size();
                    else size += result.data.size();
                    this.message = 0;
                    reservationMessages.addAll(result.data);
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                if(this.message == 0){
                    //成功的操作
                    List<RegisterDoctorNoteAdapter
                            .RegisterNoteItemDetailMessage> list = new ArrayList<>();
                    for(ReservationConnection.ReservationMessage now : reservationMessages){
                        RegisterDoctorNoteAdapter
                                .RegisterNoteItemDetailMessage nowItem = new RegisterDoctorNoteAdapter
                                .RegisterNoteItemDetailMessage();

                        CalendarMessageTask calendarMessageTask =
                                new CalendarMessageTask(nowItem,now.admissionId);
                        calendarMessageTask.execute((Void)null);

                        nowItem.admissionId = now.admissionId;
                        nowItem.patientName = now.patientName;
                        nowItem.noteNumber = now.reservationId;
                        nowItem.patientStatus = now.isAdmission;
                        list.add(nowItem);
                    }

                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setAdapter(list);


                }else if(this.message == 1){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "网络连接错误！",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "JSon数据解析错误！",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "未知错误，请与开发者联系！",Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(RegisterDoctorNoteActivity.this,
                        "网络连接错误！",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }

    public class CalendarMessageTask extends AsyncTask<Void, Void, Boolean> {
        private int message;
        private final String jsonData;
        private final RegisterDoctorNoteAdapter
                .RegisterNoteItemDetailMessage item;
        public CalendarMessageTask(RegisterDoctorNoteAdapter
                                           .RegisterNoteItemDetailMessage item,String jsonData){
            this.item = item;
            this.jsonData = "{\"admissionId\": \"" + jsonData + "\",\"pageNo\":\"";
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
                    DoctorCalendarConnection.
                            DoctorCalendarMessage doctorMessage = doctorCalendarMessage.get(0);
                    item.doctorName = doctorMessage.doctorName;
                    item.departmentName = doctorMessage.departmentName;
                    item.hospitalName = doctorMessage.hospitalName;
                    item.noteTime = doctorMessage.admissionPeriod;
                    DoctorTask doctorTask = new DoctorTask(item,doctorMessage.doctorId);
                    doctorTask.execute((Void)null);

                }else if(this.message == 1){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "网络连接错误",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "查找不到相关的信息！",Toast.LENGTH_LONG).show();
                }else if(this.message == 3){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "Json信息错误!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RegisterDoctorNoteActivity.this,
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
        RegisterDoctorNoteAdapter.
                RegisterNoteItemDetailMessage item;
        public DoctorTask(RegisterDoctorNoteAdapter
                                  .RegisterNoteItemDetailMessage item,String jsonData){
            this.jsonData = "{ \"doctorId\":\"" + jsonData + "\"}";
            this.item = item;
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
                        item.doctorLevel = result.data.get(0).doctorTitle;
                    }
                }else if(this.message == 1){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "网络连接错误",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "查找不到相关的信息！",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "未知错误，请与开发者联系!",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {

        }

    }

}
