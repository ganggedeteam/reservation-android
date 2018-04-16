package com.example.hospital_one;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.example.hospital_one.intenet_connection.DoctorConnection;
import com.example.hospital_one.intenet_connection.HospitalConnection;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.searchresult.Doctor;
import com.example.hospital_one.part_hospital.DoctorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AcitivityBase {

    private SearchTask searchTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initSearchActivity();

    }
    TextView search;    //“搜索”文字
    SearchView searchView; //搜索框
    RecyclerView doctorListView;//搜索结果 --- 医生

    public void initSearchActivity(){
        //屏蔽标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        //初始化TabHost
        TabHost tabHost = (TabHost) findViewById(R.id.searchTabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("zonghe");
        tabSpec1.setIndicator("综合");
        tabSpec1.setContent(R.id.searchtab1);
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("hospital");
        tabSpec2.setIndicator("医院");
        tabSpec2.setContent(R.id.searchtab2);
        tabHost.addTab(tabSpec2);

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("medical");
        tabSpec3.setIndicator("药品");
        tabSpec3.setContent(R.id.searchtab3);
        tabHost.addTab(tabSpec3);

        TabHost.TabSpec tabSpec4 = tabHost.newTabSpec("doctor");
        tabSpec4.setIndicator("医生");
        tabSpec4.setContent(R.id.searchtab4);
        tabHost.addTab(tabSpec4);

        TabHost.TabSpec tabSpec5 = tabHost.newTabSpec("sick");
        tabSpec5.setIndicator("疾病");
        tabSpec5.setContent(R.id.searchtab5);
        tabHost.addTab(tabSpec5);


        search = (TextView) findViewById(R.id.searchResult);
        search.setBackgroundColor(Color.WHITE);

        searchView = (SearchView)findViewById(R.id.searchTarget);
        searchView.setQueryHint("搜索");//设置默认无内容时的文字提示
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchThing();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchThing();
            }
        });
    }

    public List<Doctor> getJson(String jsonData){
        List<Doctor> list = new ArrayList<>();
        return list;
    }
    public void SearchThing(){
        List<Doctor> doctors = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            Doctor doctor = new Doctor("我是谁","儿科","专家","心脏手术专家",R.drawable.account);
            doctors.add(doctor);
        }
        //向医生RecyclerView添加搜索结果
        doctorListView = (RecyclerView) findViewById(R.id.search_result_doctor_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        doctorListView.setLayoutManager(layoutManager);
        DoctorAdapter doctorAdapter = new DoctorAdapter(doctors);
        doctorListView.setAdapter(doctorAdapter);

    }


    //地址信息查询线程类
    public class SearchTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData; //传输到服务器的接送数据

        //用户操作标识（0代表医院查询，1代表医生查询，）
        private final int cursor;
        //与服务器连接信息
        private int message = 0;

        private List<HospitalConnection.HospitalMes> hospitalResult = null;
        private List<DoctorConnection.DoctorMessage> doctorResult = null;

        SearchTask(int cursor,String jsonData) {
            this.cursor = cursor;
            this.jsonData = "{" + jsonData + ",\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0;
            int size = 0;
            while(i < 1 || i < size/10 + 1) {
                if(cursor == 0) {
                    HospitalConnection.JsonHead result = null;
                    //读取服务器地址
                    SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                    String url = reader.
                            getString("ip", "")
                            + reader.getString("hospitalPage", "");
                    //与服务器建立连接并接受返回消息
                    String response = InternetConnection.
                            ForInternetConnection(url, jsonData + (i + 1) + "\"}");
                    result = HospitalConnection.parseJsonData(response);
                    if(i == 1)hospitalResult = new ArrayList<>();

                    //将json数据转化为类
                    if (result == null) {
                        this.message = 1;
                    } else {
                        if (i == 0) size = result.total;
                        if (result.total == 0) {
                            this.message = 2;
                        } else if (result.data.size() != 0) {
                            //将接受的地址信息提取
                            hospitalResult.addAll(result.data);
                        }
                    }
                }else if(this.cursor == 1){
                    DoctorConnection.JsonHead result = null;
                    //读取服务器地址
                    SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                    String url = reader.
                            getString("ip", "")
                            + reader.getString("doctorPage", "");
                    //与服务器建立连接并接受返回消息
                    String response = InternetConnection.
                            ForInternetConnection(url, jsonData + (i + 1) + "\"}");
                    result = DoctorConnection.parseJsonData(response);
                    if(i == 1)doctorResult = new ArrayList<>();

                    //将json数据转化为类
                    if (result == null) {
                        this.message = 1;
                    } else {
                        if (i == 0) size = result.total;
                        if (result.total == 0) {
                            this.message = 2;
                        } else if (result.data.size() != 0) {
                            //将接受的地址信息提取
                            doctorResult.addAll(result.data);
                        }
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
                    Toast.makeText(SearchActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
                    Toast.makeText(SearchActivity
                            .this,"查找不到本用户",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    if(this.cursor == 0){

                    }else{
                        Toast.makeText(SearchActivity
                                .this,"标识代码错误",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SearchActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            searchTask = null;
        }
    }

}
