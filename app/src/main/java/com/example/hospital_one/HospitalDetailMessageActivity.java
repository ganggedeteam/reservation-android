package com.example.hospital_one;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.example.hospital_one.adapter.HospitalPictureAdapter;
import com.example.hospital_one.connection.*;
import com.example.hospital_one.adapter.OnItemClickListener;
import com.example.hospital_one.adapter.PartOfHospitalAdapter;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.hospital_one.connection.HospitalConnection.hospitalLevel;

public class HospitalDetailMessageActivity extends AppCompatActivity {

    private TextView hospitalDetailName;
    private TextView hospitalDetailGrade;
    private TextView hospitalDetailPhone;
    private TextView hospitalDetailManager;
    private TextView detailIntroduction;
    private TextView detailAddress;
    private DepartmentTask task = null;
    private HospitalPictureTask pictureTask;

    @Override
    protected void onResume(){
        super.onResume();
    }
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
        hospitalDetailGrade.setText(hospitalGrade);
        hospitalDetailPhone.setText(hospitalPhone);
        hospitalDetailManager.setText("管理员：" + hospitalManager);
        detailIntroduction.setText(introduction);
        detailAddress.setText("详细地址：" + address);

        task = new DepartmentTask("\"hospitalId\": \"" + hospitalId + "\"");
        task.execute((Void)null);

        pictureTask = new HospitalPictureTask(hospitalId);
        pictureTask.execute((Void)null);
    }

    private void setKindsAdapter(final List<DepartmentControllerConnection.DepartmentOfHos> list){
        LinearLayout result = (LinearLayout)findViewById(R.id.hospital_detail_message);
        LinearLayout noResult = (LinearLayout)findViewById(R.id.hospital_detail_message_noResult);
        Log.e("6666666666666666666: ", "setKindsAdapter: " + list.size() );
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
                        Date d = new Date();
                        System.out.println(d);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateNowStr = sdf.format(d);

                        String time = dateNowStr.substring(0,10);
                        String[] dateDetail = time.split("-");
                        int year,month,day;
                        year = stringToInt(dateDetail[0]);
                        month = stringToInt(dateDetail[1]);
                        day = stringToInt(dateDetail[2]);
                        showDateDialog(getDate(year,month,day));
                    }

                }
            });
            departmentListView.setAdapter(partOfHospitalAdapter);
        }
    }

    private void setHospitalAdapter(List<DepartmentControllerConnection.DepartmentOfHos> list){
       RecyclerView departmentListView = (RecyclerView) findViewById(R.id.HospitalDetailDepartmentName);
       LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       departmentListView.setLayoutManager(layoutManager);
       PartOfHospitalAdapter partOfHospitalAdapter = new PartOfHospitalAdapter(list);
       partOfHospitalAdapter.setOnItemClickListener(new OnItemClickListener() {
           @Override
           public void onItemClick(View view, int position) {

               /*SharedPreferences reader
                       = getSharedPreferences("start_file",MODE_PRIVATE);
               boolean status = reader.getBoolean("status", false);
               if(!status){
                   showMessage("请登陆账号");
               }else {
                   departmentId = list.get(position).getDepartmentId();
                   Date d = new Date();
                   System.out.println(d);
                   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   String dateNowStr = sdf.format(d);

                   String time = dateNowStr.substring(0,10);
                   String[] dateDetail = time.split("-");
                   int year,month,day;
                   year = stringToInt(dateDetail[0]);
                   month = stringToInt(dateDetail[1]);
                   day = stringToInt(dateDetail[2]);
                   showDateDialog(getDate(year,month,day));
               }*/

           }
       });
       departmentListView.setAdapter(partOfHospitalAdapter);
    }


    private void setList(List<HospitalDepartmentTypeConnection.HospitalDepartmentType> list){
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.TestHospitalView);
        LinearLayout result = (LinearLayout)findViewById(R.id.hospital_detail_message);
        LinearLayout noResult = (LinearLayout)findViewById(R.id.hospital_detail_message_noResult);
        if(list.size() == 0){
            result.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
        } else {
            result.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);
        }
        for(HospitalDepartmentTypeConnection.HospitalDepartmentType mes : list) {
            TextView textView = new TextView(this);
            textView.setTextSize(24);
            textView.setText(mes.name);
            textView.setTextColor(Color.BLACK);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            final HospitalDepartmentTypeConnection.HospitalDepartmentType now = mes;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DepartmentKindTask departmentKindTask = new DepartmentKindTask("\"hospitalId\": \""
                            + hospitalId + "\",\"typeId\":\"" + now.id + "\"");
                    departmentKindTask.execute((Void)null);
                }
            });
            linearLayout.addView(textView);
        }

    }
    private void setDetailList(List<HospitalDepartmentTypeConnection.HospitalDetailDepartment> list){
        LinearLayout hospitalDetailDep = (LinearLayout)findViewById(R.id.HospitalDetailDep);
        hospitalDetailDep.removeAllViews();
        if(list.size() == 0){
            TextView textView = new TextView(this);
            textView.setText("暂无");
            textView.setTextColor(Color.BLACK);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            hospitalDetailDep.addView(textView);
        }
        for(HospitalDepartmentTypeConnection.
                    HospitalDetailDepartment mes : list){
            TextView textView = new TextView(this);
            textView.setText(mes.departmentName);
            textView.setTextColor(Color.BLACK);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            final HospitalDepartmentTypeConnection.
                    HospitalDetailDepartment now = mes;
            textView.setTextSize(24);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences reader
                            = getSharedPreferences("start_file",MODE_PRIVATE);
                    boolean status = reader.getBoolean("status", false);
                    if(!status){
                        showMessage("请登陆账号");
                    }else {
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateNowStr = sdf.format(d);
                        String time = dateNowStr.substring(0,10);
                        Intent intent = new Intent(
                                HospitalDetailMessageActivity.this, DoctorCalendarActivity.class);
                        intent.putExtra("date",time);
                        intent.putExtra("hospitalId",hospitalId);
                        intent.putExtra("departmentId",now.departmentId);
                        startActivity(intent);

                    }
                }
            });
            hospitalDetailDep.addView(textView);
        }
    }

    private void setHospitalPicture(List<String> url){
        LinearLayout NullView = (LinearLayout)findViewById(R.id.HospitalPictureNull);
        RecyclerView hospitalPicRecyclerView = (RecyclerView)findViewById(R.id.HospitalPictureRecyclerView);
        if(url.size() == 0) {
            NullView.setVisibility(View.VISIBLE);
            hospitalPicRecyclerView.setVisibility(View.GONE);
        }
        else{
            NullView.setVisibility(View.GONE);
            hospitalPicRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            hospitalPicRecyclerView.setLayoutManager(linearLayoutManager);
            HospitalPictureAdapter adapter = new HospitalPictureAdapter(url);
            hospitalPicRecyclerView.setAdapter(adapter);
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
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public class HospitalPictureTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;

        HospitalPictureTask(String jsonData) {
            this.jsonData = "{\"hospitalId\": \"" + jsonData + "\"}";
        }
        String pictureUrls;

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            HospitalConnection.JsonHead result;
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String ip = reader.getString("ip", "");
            String last = reader.getString("hospitalPage", "");
            String response = InternetConnection.ForInternetConnection(ip + last, jsonData);
            result = HospitalConnection.parseJsonData(response);
//            Log.e("99999999999999", "doInBackground: " + response);
            if (result == null) {
                this.message = 1;
                return true;
            }
            if (result.message.equals("success")) {
//                Log.e("99999999999999", "doInBackground: " + result.data.size());
                if (result.total == 0) {
                    message = 2;
                } else if(result.data.size() != 0){
                    pictureUrls = result.data.get(0).hospitalPicture;
                }
            } else {
                message = 3;
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
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"查找不到结果",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
//                    Log.e("888888888888888888:", "onPostExecute: " + pictureUrls );
                    if(pictureUrls == null || pictureUrls.equals("")){
//                        showMessage("暂无图片");
                    }
                    else {
                        setHospitalPicture(getUrlList(pictureUrls));
                    }
                }
                else{
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {

        }


        private List<String> getUrlList(String urls){
            List<String> list = new ArrayList<>();
            String[] url = urls.split(";");
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String ip = reader.getString("pictureDownloadIp","");
            for(String picUrl : url){
                list.add(ip + picUrl);
            }
            return list;
        }

    }

    public class DepartmentTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;
        private List<HospitalDepartmentTypeConnection.HospitalDepartmentType> connectResult = null;

        DepartmentTask(String jsonData) {
            this.jsonData = "{" + jsonData + ",\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0,size = 0;
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");
            if(key == null || token == null || key.equals("") || token.equals(""))return false;
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String ip = reader.getString("ip", "");
            String last = reader.getString("hospitalDepType", "");
            while(i < 1 || i < size/10 + 1) {
                HospitalDepartmentTypeConnection.JsonHead result;
                String response = InternetConnection.ForInternetHeaderConnection(ip + last,key,token, jsonData + (i + 1) + "\"}");
                Log.e("66666666666666666: ", "doInBackground: " + response );
                result = HospitalDepartmentTypeConnection.parseJsonData(response);
                if(i==0)connectResult = new ArrayList<>();
                if (result == null) {
                    this.message = 1;
                    return true;
                }
                if (result.message.equals("success")) {
                    size = result.data.size();
                    if (result.data.size() == 0 && i == 0) {
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
                            .this,"科室信息暂时查找不到结果",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    setList(connectResult);
                }else{
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }else{
                showMessage("请登录账号");
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }

    public class DepartmentKindTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;
        private List<HospitalDepartmentTypeConnection.HospitalDetailDepartment> connectResult = null;

        DepartmentKindTask(String jsonData) {
            this.jsonData = "{" + jsonData + ",\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0,size = 0;
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");
            if(key == null || token == null || key.equals("") || token.equals(""))return false;
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String ip = reader.getString("ip", "");
            String last = reader.getString("departmentDetailPage", "");
            while(i < 1 || i < size/10 + 1) {
                HospitalDepartmentTypeConnection.DetailJsonHead result;
                String response = InternetConnection.ForInternetHeaderConnection(ip + last, key,token,jsonData + (i + 1) + "\"}");
                Log.e("sdfjsadfjsaalfjsaldfjsakaldjfaskldfjsaalkdfjslkf:", "doInBackground: " + response );
                result = HospitalDepartmentTypeConnection.parseJsonHead(response);
                if(i==0)connectResult = new ArrayList<>();
                if (result == null) {
                    this.message = 1;
                    return true;
                }
                if (result.message.equals("success")) {
                    size = result.data.size();
                    if (result.data.size() == 0) {
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
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"查找不到结果",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    setDetailList(connectResult);
                }else{
                    Toast.makeText(HospitalDetailMessageActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }else{
                showMessage("请登录账号");
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
                    reader.getString("hospitalDepartment","");

            String response = InternetConnection.ForInternetConnection(url,"{,}");
            Log.e("0000000000000: ", "doInBackground: " + response);
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
