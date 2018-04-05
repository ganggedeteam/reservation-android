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
import com.example.hospital_one.intenet_connection.DepartmentConnection;
import com.example.hospital_one.intenet_connection.HospitalConnection;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.part_hospital.OnItemClickListener;
import com.example.hospital_one.part_hospital.PartOfHospitalAdapter;

import java.util.ArrayList;
import java.util.List;

public class PartOfHospitalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_of_hospital);
        initPartOfHospitalActivity();
    }
    PartOfHospitalTask task = null;
    String hospitalID = null;
    private void initPartOfHospitalActivity(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        Intent intent = getIntent();
        String keshi = intent.getStringExtra("keshiId");
        if(keshi.equals("YES"))keshiYes();
        else if(keshi.equals("NO")){
            hospitalID = intent.getStringExtra("hospitalId");
            task = new PartOfHospitalTask("{}");
            task.execute((Void) null);
        }

    }

    List<PartOfHospitalAdapter.PartOfHospitalMes> connectResult = null;

    public void setAdapter(List<PartOfHospitalAdapter.PartOfHospitalMes> list){
        RecyclerView keshiListView = (RecyclerView) findViewById(R.id.keshiListView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        keshiListView.setLayoutManager(layoutManager);
        PartOfHospitalAdapter partOfHospitalAdapter = new PartOfHospitalAdapter(list);
        partOfHospitalAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent intent1 = new Intent(
                        PartOfHospitalActivity.this,DoctorActivity.class);
                intent1.putExtra("hospitalId",hospitalID);
                intent1.putExtra("departmentTypeId"
                        ,connectResult.get(position).departmentTypeId);
                intent1.putExtra("departmentName"
                        ,connectResult.get(position).departmentName);
                startActivity(intent1);
            }
        });
        keshiListView.setAdapter(partOfHospitalAdapter);
    }

    public void keshiYes(){

        List<PartOfHospitalAdapter.PartOfHospitalMes> partOfHospital = new ArrayList<>();

        for(int i = 0;i < DoctorActivity.title.length;i++){
            partOfHospital.add(new
                    PartOfHospitalAdapter.PartOfHospitalMes(DoctorActivity.title[i]));
        }
        RecyclerView keshiListView = (RecyclerView) findViewById(R.id.keshiListView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        keshiListView.setLayoutManager(layoutManager);
        PartOfHospitalAdapter partOfHospitalAdapter = new PartOfHospitalAdapter(partOfHospital);
        partOfHospitalAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent intent1 = new Intent(PartOfHospitalActivity.this,DoctorActivity.class);
                intent1.putExtra("Hospital","");
                intent1.putExtra("data",position);
                startActivity(intent1);
            }
        });
        keshiListView.setAdapter(partOfHospitalAdapter);
    }

    public class PartOfHospitalTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;

        PartOfHospitalTask(String jsonData) {
            this.jsonData = jsonData;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            DepartmentConnection.JsonHead result = null;
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String ip = reader.getString("ip","");
            String last = reader.getString("departmentPage","");
            result = DepartmentConnection.paraseJson(
                    InternetConnection.ForInternetConnection(ip + last,jsonData));

            if(result == null){
                this.message = 1;
            }else{
                connectResult = result.data;
                if(result.data.size() == 0){
                    this.message = 2;
                }
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
//                    task = null;
//                    finish();
                }else{
                    Toast.makeText(PartOfHospitalActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
//            setRecyclerView(getConnect());
            task = null;
        }
    }

}
