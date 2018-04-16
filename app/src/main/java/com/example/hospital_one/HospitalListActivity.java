package com.example.hospital_one;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.example.hospital_one.intenet_connection.HospitalConnection;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.part_hospital.HospitalAdapter;
import com.example.hospital_one.part_hospital.OnItemClickListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HospitalListActivity extends AppCompatActivity {

    RecyclerView hospitalRecycler;
    private SearchView searchView;
    private EditText editText;
    private TextView searchResult;
    private HospitalTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);
        hospitalRecycler = (RecyclerView) findViewById(R.id.HospitalRecycler);
        searchView = (SearchView) findViewById(R.id.searchHospitalTarget);
        editText = (EditText)searchView.findViewById(searchView.getContext()
                .getResources().getIdentifier("android:id/search_src_text", null, null));
        ImageView hospitalBack = (ImageView)findViewById(R.id.HospitalBack);
        hospitalBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchResult = (TextView)findViewById(R.id.searchHospitalResult);
        searchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new HospitalTask("\"hospitalName\":\"" + editText.getText().toString() +"\",");
                task.execute((Void) null);
            }
        });
        initHospitalListActivity();
    }

    public void initHospitalListActivity(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        task = new HospitalTask("");
        task.execute((Void) null);
    }

    List<HospitalConnection.HospitalMes> connectResult = null;

    public void showRecycle(){
        LinearLayout recycler = (LinearLayout)findViewById(R.id.HospitalListView),
                searchResullt = (LinearLayout)findViewById(R.id.HospitalResult);
        recycler.setVisibility(View.VISIBLE);
        searchResullt.setVisibility(View.GONE);
    }
    public void showSearchResult(){
        LinearLayout recycler = (LinearLayout)findViewById(R.id.HospitalListView),
                searchResullt = (LinearLayout)findViewById(R.id.HospitalResult);
        recycler.setVisibility(View.GONE);
        searchResullt.setVisibility(View.VISIBLE);
    }

    public void setRecyclerView(final List<HospitalConnection.HospitalMes> list){
        LinearLayoutManager layoutManager = new LinearLayoutManager(HospitalListActivity.this);
        hospitalRecycler.setLayoutManager(layoutManager);
        HospitalAdapter hospitalAdapter = new HospitalAdapter(list);
        hospitalAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent intent1 = new Intent(
                        HospitalListActivity.this,HospitalDetailMessageActivity.class);
                HospitalConnection.HospitalMes name = list.get(position);
                String province = name.provinceName;
                String city = name.cityName;
                String county = name.countyName;
                String detail = name.detailAddr;
                String addr = (province == null?"":province)
                        +(city == null?"":city)+(county == null?"":county)+(detail == null?"":detail);
                intent1.putExtra("hospitalName",name.hospitalName==null?"暂无":name.hospitalName);
                intent1.putExtra("hospitalGrade",name.hospitalGrade==null?"暂无":name.hospitalGrade);
                intent1.putExtra("hospitalPhone",name.hospitalPhone==null?"暂无":name.hospitalPhone);
                intent1.putExtra("hospitalManager",name.hospitalManager == null ?"暂无":name.hospitalManager);
                intent1.putExtra("introduction",name.introduction == null?"暂无":name.introduction);
                intent1.putExtra("address",addr.equals("")?"暂无":addr);
                intent1.putExtra("hospitalId",name.hospitalId);
                startActivity(intent1);
            }
        });
        hospitalRecycler.setAdapter(hospitalAdapter);
    }

    public class HospitalTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;

        HospitalTask(String jsonData) {
            this.jsonData = "{" + jsonData + "\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0,size = 0;
            while(i < 1 || i < size/10 + 1) {
                HospitalConnection.JsonHead result;
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String ip = reader.getString("ip", "");
                String last = reader.getString("hospitalPage", "");
                result = HospitalConnection.parseJsonData(
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
                    Toast.makeText(HospitalListActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
                    showSearchResult();
                    Toast.makeText(HospitalListActivity
                            .this,"查找不到结果",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    showRecycle();
                    setRecyclerView(connectResult);
//                    finish();
                }
                else{
                    Toast.makeText(HospitalListActivity
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
