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
import android.widget.TextView;
import android.widget.Toast;
import com.example.hospital_one.intenet_connection.DepartmentControllerConnection;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.part_hospital.OnItemClickListener;
import com.example.hospital_one.part_hospital.PartOfHospitalAdapter;

import java.util.ArrayList;
import java.util.List;

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
        hospitalDetailGrade.setText(hospitalGrade);
        hospitalDetailPhone.setText(hospitalPhone);
        hospitalDetailManager.setText(hospitalManager);
        detailIntroduction.setText(introduction);
        detailAddress.setText(address);

        task = new DepartmentTask("\"hospitalId\": \"" + hospitalId + "\"");
        task.execute((Void)null);
    }

    private void setAdapter(List<DepartmentControllerConnection.DepartmentOfHos> list){

        RecyclerView departmentListView = (RecyclerView) findViewById(R.id.HospitalDetailDepartmentList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        departmentListView.setLayoutManager(layoutManager);
        PartOfHospitalAdapter partOfHospitalAdapter = new PartOfHospitalAdapter(list);
        partOfHospitalAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){

            }
        });
        departmentListView.setAdapter(partOfHospitalAdapter);

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
}
