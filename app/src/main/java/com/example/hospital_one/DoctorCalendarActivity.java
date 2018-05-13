package com.example.hospital_one;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hospital_one.adapter.DoctorCalendarAdapter;
import com.example.hospital_one.adapter.OnItemClickListener;
import com.example.hospital_one.adapter.SpecialDateAdapter;
import com.example.hospital_one.connection.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.hospital_one.connection.DoctorConnection.DoctorLeverl;

public class DoctorCalendarActivity extends AppCompatActivity {

    CalendarMessageTask task = null;

    public int stringToInt(String target){
        int sum = 0;
        for(int i = 0; i < target.length();i++){
            if(Character.isDigit(target.charAt(i))){
                sum = sum *10 + Character.digit(target.charAt(i),10);
            }else break;
        }
        return sum;
    }

    public String[] getDate(int year,int month,int day){
        String[] date = new String[7];
        Set<Integer> month31 = new HashSet<>();
        month31.add(1);
        month31.add(3);
        month31.add(5);
        month31.add(7);
        month31.add(8);
        month31.add(10);
        month31.add(12);
        for(int i = 0;i < 7; i++){
            if(month == 2){
                if(isLeapYear(year)){
                    if(day < 29){
                        day++;
                        date[i] = "" + year + "-" + month + "-" + day;
                        continue;
                    }else{
                        month ++;
                        if(month == 13){
                            year ++;
                            month = 1;
                        }
                        day = 1;
                        date[i] = "" + year + "-" + month + "-" + day;
                        continue;
                    }
                }else{
                    if(day < 28){
                        day++;
                        date[i] = "" + year + "-" + month + "-" + day;
                        continue;
                    }else{
                        month ++;
                        if(month == 13){
                            year ++;
                            month = 1;
                        }
                        day = 1;
                        date[i] = "" + year + "-" + month + "-" + day;
                        continue;
                    }
                }
            }
            if(!month31.contains(month)){
                if(day < 30){
                    day++;
                    date[i] = "" + year + "-" + month + "-" + day;
                    continue;
                }else{
                    month ++;
                    if(month == 13){
                        year ++;
                        month = 1;
                    }
                    day = 1;
                    date[i] = "" + year + "-" + month + "-" + day;
                    continue;
                }
            }
            if(month31.contains(month)){

                if(day < 31){
                    day++;
                    date[i] = "" + year + "-" + month + "-" + day;
                    continue;
                }else{
                    month ++;
                    if(month == 13){
                        year ++;
                        month = 1;
                    }
                    day = 1;
                    date[i] = "" + year + "-" + month + "-" + day;
                    continue;
                }
            }
        }
        return date;
    }

    public boolean isLeapYear(int dateNum){
        if(dateNum % 100 == 0){
            if(dateNum%4 == 0)return true;
        }else if(dateNum % 4 == 0){
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_calendar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        Intent intent = getIntent();
        int year;
        int month;
        int day;
        String nowDate = intent.getStringExtra("date");
        String[] dateDetail = nowDate.split("-");
        year = stringToInt(dateDetail[0]);
        month = stringToInt(dateDetail[1]);
        day = stringToInt(dateDetail[2]);
        String[] date = getDate(year,month,day);
        dateDetail = date[0].split("-");
        List<String> list = new ArrayList<>();
        for(String string : date){
            list.add(string);
        }
        setDate(list);
        year = stringToInt(dateDetail[0]);
        month = stringToInt(dateDetail[1]);
        day = stringToInt(dateDetail[2]);
        hospitalId = intent.getStringExtra("hospitalId");
        departmentId = intent.getStringExtra("departmentId");
        task = new CalendarMessageTask("{\"hospitalId\": \"" + hospitalId + "\"," +
                "\"admissionDate\": \""+ year + "-" + (month < 10?"0" + month:"" + month) + "-" + (day < 10?"0" + day: "" + day) +"\"," +
                "\"departmentId\": \"" + departmentId + "\"");
        task.execute((Void)null);
    }

    String hospitalId;
    String departmentId;

    private void setDate(final List<String> date){

        RecyclerView dateView = (RecyclerView)findViewById(R.id.DateView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dateView.setLayoutManager(linearLayoutManager);
        SpecialDateAdapter adapter = new SpecialDateAdapter(date);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int year,month,day;
                String[] dateDetail = date.get(position).split("-");
                year = stringToInt(dateDetail[0]);
                month = stringToInt(dateDetail[1]);
                day = stringToInt(dateDetail[2]);
                task = new CalendarMessageTask("{\"hospitalId\": \"" + hospitalId + "\"," +
                        "\"admissionDate\": \""+ year + "-" + (month < 10?"0" + month:"" + month) + "-" + (day < 10?"0" + day: "" + day) +"\"," +
                        "\"departmentId\": \"" + departmentId + "\"");
                task.execute((Void)null);
            }
        });
        dateView.setAdapter(adapter);
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
        String backMes;
        public ReservationTask(String jsonData){
            this.jsonData = jsonData;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("reservationAdd","");
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");
            String response = InternetConnection.ForInternetHeaderConnection(url,key,token,jsonData);
            backMes = response;
            Log.e("fjfhwiuafhsaufhasjdfhsakjdfhsakjfdhsa:", "doInBackground: "+response );
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
                }else{
//                    ShowMessage();
                    Toast.makeText(DoctorCalendarActivity.this,
                            ReservationConnection.paeseAddMesage(backMes),Toast.LENGTH_LONG).show();
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
        List<DoctorCalendarAdapter.DoctorCalendarView> doctorCalendarMessage;

        @Override
        protected Boolean doInBackground(Void... voids) {
            int i = 0,size = 0;
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");
            while(i < 1 || i < size/10 + 1) {
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String url = reader.getString("ip", "") +
                        reader.getString("calendarList", "");
                String response = InternetConnection.ForInternetHeaderConnection(url,key,token, jsonData + (i + 1) + "\"}");
                Log.e("777777777777777:", "doInBackground: " + response );
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
                        List<DoctorCalendarAdapter.DoctorCalendarView> list = new ArrayList<>();
                        for(DoctorCalendarConnection.
                                DoctorCalendarMessage doctorCalendarMessage:result.data){
                            DoctorCalendarAdapter.DoctorCalendarView view =
                                    new DoctorCalendarAdapter.DoctorCalendarView();
                            getDoctorMessage(view,doctorCalendarMessage.doctorId);
                            view.doctorName = doctorCalendarMessage.doctorName;
                            view.admissionNum = doctorCalendarMessage.admissionNum;
                            view.admissionPeriod = doctorCalendarMessage.admissionPeriod;
                            view.remainingNum = doctorCalendarMessage.remainingNum;
                            view.admissionId = doctorCalendarMessage.admissionId;
                            list.add(view);
                        }
                        doctorCalendarMessage.addAll(list);
                        this.message = 0;
                    }
                }
                i++;
            }
            return true;
        }

        public void getDoctorMessage(DoctorCalendarAdapter.DoctorCalendarView view,String jsonData){
            DoctorConnection.JsonHead result;
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("doctorPage","");
            String response = InternetConnection.ForInternetHeaderConnection
                    (url,key,token,"{ \"doctorId\":\"" + jsonData + "\"}");
            Log.e("555555555555:", "getDoctorMessage: " + response );
            result = DoctorConnection.parseJsonData(response);
            if(result == null){
                view.skill = "暂无";
                view.doctorPhoto = "暂无";
                view.doctorTitle = "暂无";
            }else{
                if(result.data.size() == 0){
                    view.skill = "暂无";
                    view.doctorPhoto = "暂无";
                    view.doctorTitle = "暂无";
                }else {
                    SharedPreferences readerPictureUrl = getSharedPreferences("host",MODE_PRIVATE);
                    view.skill = result.data.get(0).skill;
                    String doctorPhoto = result.data.get(0).doctorPhoto;
                    view.doctorPhoto = doctorPhoto == null || doctorPhoto.equals("") ? "暂无" : readerPictureUrl.getString(
                            "pictureDownloadIp","") + doctorPhoto;
                    String doctorTitle = result.data.get(0).doctorTitle;
                    view.doctorTitle = doctorTitle == null||doctorTitle.equals("")?
                            "暂无":DoctorLeverl[Character.digit(doctorTitle.charAt(0),10)] ;
                }
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if(this.message == 0){
                    //成功进行的操作
                    setAdapter(doctorCalendarMessage);
                }else if(this.message == 1){
                    Toast.makeText(DoctorCalendarActivity.this,
                            "网络连接错误",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    setAdapter(new ArrayList<DoctorCalendarAdapter.DoctorCalendarView>());
                    ShowMessage("查找不到相关的信息！");
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
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            int i = 0;
            int size = 0;
            while (i < 1 || i < size / 10 + 1) {
                String url = reader.getString("ip","")
                        + reader.getString("patientPage", "");
                String response = InternetConnection.ForInternetHeaderConnection(url,key,token, jsonData + (i + 1) + "\"}");
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
