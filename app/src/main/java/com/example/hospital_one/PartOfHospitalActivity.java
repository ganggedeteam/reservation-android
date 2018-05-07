package com.example.hospital_one;

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
import com.example.hospital_one.connection.DepartmentConnection;
import com.example.hospital_one.connection.DepartmentControllerConnection;
import com.example.hospital_one.connection.InternetConnection;
import com.example.hospital_one.adapter.DepartmentKindsAdapter;
import com.example.hospital_one.adapter.OnItemClickListener;
import com.example.hospital_one.adapter.PartOfHospitalAdapter;

import java.util.ArrayList;
import java.util.List;

public class PartOfHospitalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_of_hospital);
        initPartOfHospitalActivity();
    }
    DepartmentKindsTask kindsTask = null;
    PartOfHospitalTask task = null;
    String hospitalID = null;
    private void initPartOfHospitalActivity(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        Intent intent = getIntent();
        String keshi = intent.getStringExtra("keshiId");
        if(keshi.equals("YES")){
            kindsTask = new DepartmentKindsTask();
            kindsTask.execute((Void)null);
        }
        else if(keshi.equals("NO")){
            hospitalID = intent.getStringExtra("hospitalId");
            task = new PartOfHospitalTask("\"hospitalId\": \"" + hospitalID + "\"");
            task.execute((Void) null);
        }
    }

    List<DepartmentControllerConnection.DepartmentOfHos> connectResult = null;

    public void setAdapter(List<DepartmentControllerConnection.DepartmentOfHos> list){
        RecyclerView keshiListView = (RecyclerView) findViewById(R.id.keshiListView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        keshiListView.setLayoutManager(layoutManager);
        PartOfHospitalAdapter partOfHospitalAdapter = new PartOfHospitalAdapter(list);
        partOfHospitalAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent intent1 = new Intent(
                        PartOfHospitalActivity.this,DoctorActivity.class);
//                intent1.putExtra("hospitalId",hospitalID);
//                intent1.putExtra("departmentTypeId"
//                        ,connectResult.get(position).departmentTypeId);
//                intent1.putExtra("departmentName"
//                        ,connectResult.get(position).departmentName);
                startActivity(intent1);
            }
        });
        keshiListView.setAdapter(partOfHospitalAdapter);
    }

    List<DepartmentConnection.DepartmentKindsMes> connectKindsResult = null;
    public void keshiYes(){
        RecyclerView keshiListView = (RecyclerView) findViewById(R.id.keshiListView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        keshiListView.setLayoutManager(layoutManager);
        DepartmentKindsAdapter departmentKindsAdapter = new DepartmentKindsAdapter(connectKindsResult);
        departmentKindsAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent intent1 = new Intent(PartOfHospitalActivity.this,DoctorActivity.class);
                intent1.putExtra("Hospital","");
                intent1.putExtra("data",position);
                startActivity(intent1);
            }
        });
        keshiListView.setAdapter(departmentKindsAdapter);
    }

    public class PartOfHospitalTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;

        PartOfHospitalTask(String jsonData) {
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
                    Toast.makeText(PartOfHospitalActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
//                    showSearchResult();
                    Toast.makeText(PartOfHospitalActivity
                            .this,"查找不到结果",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    setAdapter(connectResult);
                }else{
                    Toast.makeText(PartOfHospitalActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }

    public class DepartmentKindsTask extends AsyncTask<Void, Void, Boolean>{
        private final String jsonData;
        private int message = 0;

        DepartmentKindsTask() {
            this.jsonData = "{\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0,size = 0;
            while(i < 1 || i < size/10 + 1) {
                DepartmentConnection.JsonHead result;
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String ip = reader.getString("ip", "");
                String last = reader.getString("departmentPage", "");
                result = DepartmentConnection.parseJsonData(
                        InternetConnection.ForInternetConnection(ip + last, jsonData + (i + 1) + "\"}"));
                if (result == null) {
                    this.message = 1;
                    return true;
                }
                if (result.message.equals("success")) {
                    size = result.total;
                    if (result.total == 0) {
                        message = 2;
                    } else if(result.data.size() != 0){
                        connectKindsResult.addAll(result.data);
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
                    Toast.makeText(PartOfHospitalActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
//                    showSearchResult();
                    Toast.makeText(PartOfHospitalActivity
                            .this,"查找不到结果",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    keshiYes();
                }else{
                    Toast.makeText(PartOfHospitalActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            kindsTask = null;
        }
    }

}
