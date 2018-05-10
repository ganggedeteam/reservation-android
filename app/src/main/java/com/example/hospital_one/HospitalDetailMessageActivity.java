package com.example.hospital_one;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hospital_one.connection.DepartmentControllerConnection;
import com.example.hospital_one.connection.InternetConnection;
import com.example.hospital_one.connection.ReservationConnection;
import com.example.hospital_one.adapter.OnItemClickListener;
import com.example.hospital_one.adapter.PartOfHospitalAdapter;

import java.util.*;

public class HospitalDetailMessageActivity extends AppCompatActivity {

    private TextView hospitalDetailName;
    private TextView hospitalDetailGrade;
    private TextView hospitalDetailPhone;
    private TextView hospitalDetailManager;
    private TextView detailIntroduction;
    private TextView detailAddress;
    private DepartmentTask task = null;

    String hospitalId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_detail_message);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        Intent intent = getIntent();
        String hospitalName = intent.getStringExtra("hospitalName");
        String hospitalGrade = intent.getStringExtra("hospitalGrade");
        String hospitalPhone = intent.getStringExtra("hospitalPhone");
        String hospitalManager = intent.getStringExtra("hospitalManager");
        String introduction = intent.getStringExtra("introduction");
        String address = intent.getStringExtra("address");
        hospitalId = intent.getStringExtra("hospitalId");

        hospitalDetailName = (TextView)findViewById(R.id.HospitalDetailName);
        hospitalDetailGrade = (TextView)findViewById(R.id.HospitalDetailGrade);
        hospitalDetailPhone = (TextView)findViewById(R.id.HospitalDetailTelephone);
        hospitalDetailManager = (TextView)findViewById(R.id.HospitalDetailManagerName);
        detailIntroduction = (TextView)findViewById(R.id.HospitalDetailIntroduction);
        detailAddress = (TextView)findViewById(R.id.HospitalDetailAddress);

        hospitalDetailName.setText(hospitalName);
        hospitalDetailGrade.setText(hospitalGrade+ "级医院");
        hospitalDetailPhone.setText(hospitalPhone);
        hospitalDetailManager.setText("院长：" + hospitalManager);
        detailIntroduction.setText(introduction);
        detailAddress.setText("详细地址：" + address);

        task = new DepartmentTask("\"hospitalId\": \"" + hospitalId + "\"");
        task.execute((Void)null);
    }

    private void setAdapter(final List<DepartmentControllerConnection.DepartmentOfHos> list){
        LinearLayout result = (LinearLayout)findViewById(R.id.hospital_detail_message);
        LinearLayout noResult = (LinearLayout)findViewById(R.id.hospital_detail_message_noResult);
        if(list.size() == 0){
            result.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
        } else {
            result.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);

            final RecyclerView departmentListView = (RecyclerView) findViewById(R.id.HospitalDetailDepartmentList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            departmentListView.setLayoutManager(layoutManager);
            PartOfHospitalAdapter partOfHospitalAdapter = new PartOfHospitalAdapter(list);
            partOfHospitalAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    SharedPreferences reader
                            = getSharedPreferences("start_file",MODE_PRIVATE);
                    boolean status = reader.getBoolean("status", false);
                    if(!status){
                        showMessage("请登陆账号");
                    }else {
                        departmentId = list.get(position).getDepartmentId();
//                        SharedPreferences.Editor editor = getSharedPreferences("error_file",MODE_PRIVATE).edit();
//                        editor.putString("departmentId",departmentId);
//                        editor.apply();
                        TimeServer timeServer = new TimeServer();
                        timeServer.execute((Void)null);
                    }

                }
            });
            departmentListView.setAdapter(partOfHospitalAdapter);
        }
    }

    int dateNum = 0;
    String departmentId;
    private void showDateDialog(final String[] date){
        dateNum = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择日期");
        builder.setSingleChoiceItems(date, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dateNum = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        HospitalDetailMessageActivity.this, DoctorCalendarActivity.class);
                String[] target = date[dateNum].split("-");
                intent.putExtra("year",stringToInt(target[0]));
                intent.putExtra("month",stringToInt(target[1]));
                intent.putExtra("day",stringToInt(target[2]));
                intent.putExtra("hospitalId",hospitalId);
                intent.putExtra("departmentId",departmentId);
                startActivity(intent);
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

    private void showMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息");
        builder.setMessage(message);
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class DepartmentTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;
        private List<DepartmentControllerConnection.DepartmentOfHos> connectResult = null;

        DepartmentTask(String jsonData) {
            this.jsonData = "{" + jsonData + ",\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0,size = 0;
            while(i < 1 || i < size/10 + 1) {
                DepartmentControllerConnection.JsonHead result;
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String ip = reader.getString("ip", "");
                String last = reader.getString("hospitalDepartment", "");
                result = DepartmentControllerConnection.parseJsonData(
                        InternetConnection.ForInternetConnection(ip + last, jsonData + (i + 1) + "\"}"));
                if(i==0)connectResult = new ArrayList<>();
                if (result == null) {
                    this.message = 1;
                    return true;
                }
                if (result.message.equals("success")) {
                    size = result.total;
                    if (result.total == 0) {
                        message = 2;
                    } else if(result.data.size() != 0){
                        connectResult.addAll(result.data);
                    }
                } else {
                    message = 3;
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
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
//                    showSearchResult();
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"查找不到结果",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    setAdapter(connectResult);
                }else{
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }

    public class TimeServer extends AsyncTask<Void, Void, Boolean>{
        int message;
        ReservationConnection.TimeMessage result;

        public TimeServer(){}

        @Override
        protected Boolean doInBackground(Void... voids) {
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);

            String url = reader.getString("ip","") +
                    reader.getString("reservationList","");

            String response = InternetConnection.ForInternetConnection(url,"{,}");
            result = ReservationConnection.parseTimeMessage(response);
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
                if(message == 1){
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"网络连接失败!", Toast.LENGTH_LONG).show();
                }else if(message == 0){
                    String time = result.timestamp.substring(0,10);
                    String[] dateDetail = time.split("-");
                    int year,month,day;
                    year = stringToInt(dateDetail[0]);
                    month = stringToInt(dateDetail[1]);
                    day = stringToInt(dateDetail[2]);
                    showDateDialog(getDate(year,month,day));
                }
            }
        }

        @Override
        protected void onCancelled() {

        }

    }

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
}
