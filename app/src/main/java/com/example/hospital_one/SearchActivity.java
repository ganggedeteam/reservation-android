package com.example.hospital_one;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.example.hospital_one.connection.DoctorConnection;
import com.example.hospital_one.connection.HospitalConnection;
import com.example.hospital_one.connection.InternetConnection;
import com.example.hospital_one.adapter.HospitalAdapter;
import com.example.hospital_one.adapter.OnItemClickListener;
import com.example.hospital_one.adapter.DoctorAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AcitivityBase {

    private SearchTask searchDoctorTask = null;
    private SearchTask searchHospitalTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initSearchActivity();

    }

    EditText editSearchText;
    TextView search;    //“搜索”文字
    SearchView searchView; //搜索框

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
        editSearchText = (EditText)searchView.findViewById(searchView.getContext()
                .getResources().getIdentifier("android:id/search_src_text", null, null));
        searchView.setQueryHint("搜索");//设置默认无内容时的文字提示
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
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
                setAllList();
            }
        });
    }

    private void setAllList(){
        if(editSearchText.getText().toString().length()>10)showMessageDialog("字符串过长");
        searchHospitalTask = new SearchTask(0,
                "\"hospitalName\":\"" + editSearchText.getText().toString() +"\"");
        searchHospitalTask.execute((Void)null);

        searchDoctorTask = new SearchTask(
                1,"\"doctorName\": \"" + editSearchText.getText().toString() +"\"");
        searchDoctorTask.execute((Void)null);
    }

    private void setDoctorAdapter(List<DoctorConnection.DoctorMessage> list){
        RecyclerView doctorListView = (RecyclerView) findViewById(R.id.search_result_doctor_list);
        LinearLayout noResult = (LinearLayout)findViewById(R.id.DoctorSearchNoResult);
        if(list.size() == 0){
            doctorListView.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
        }else{
            doctorListView.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        doctorListView.setLayoutManager(layoutManager);
        DoctorAdapter doctorAdapter = new DoctorAdapter(list);
        doctorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        doctorListView.setAdapter(doctorAdapter);
    }
    private void setDoctorInAllAdapter(List<DoctorConnection.DoctorMessage> list){
        RecyclerView doctorListView = (RecyclerView) findViewById(R.id.search_doctor_result_in_all_list);
        if(list.size() == 0){
            doctorListView.setVisibility(View.GONE);
        }else{
            doctorListView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        doctorListView.setLayoutManager(layoutManager);
        DoctorAdapter doctorAdapter = new DoctorAdapter(list);
        doctorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        doctorListView.setAdapter(doctorAdapter);
    }

    private void setHospitalAdapter(final List<HospitalConnection.HospitalMes> list){
        RecyclerView hospitalRecycler = (RecyclerView) findViewById(R.id.search_result_hospital_list);
        LinearLayout noResult = (LinearLayout)findViewById(R.id.HospitalSearchNoResult);
        if(list.size() == 0){
            hospitalRecycler.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
        }else{
            hospitalRecycler.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        hospitalRecycler.setLayoutManager(layoutManager);
        HospitalAdapter hospitalAdapter = new HospitalAdapter(list);
        hospitalAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent intent1 = new Intent(
                        SearchActivity.this,HospitalDetailMessageActivity.class);
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
    private void setHospitalInAllAdapter(final List<HospitalConnection.HospitalMes> list){
        RecyclerView hospitalRecycler = (RecyclerView) findViewById(R.id.search_hospital_result_in_all_list);
        if(list.size() == 0){
            hospitalRecycler.setVisibility(View.GONE);
        }else{
            hospitalRecycler.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        hospitalRecycler.setLayoutManager(layoutManager);
        HospitalAdapter hospitalAdapter = new HospitalAdapter(list);
        hospitalAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent intent1 = new Intent(
                        SearchActivity.this,HospitalDetailMessageActivity.class);
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

    public void showMessageDialog(String message){
        AlertDialog.Builder builder = new
                AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("提示信息");
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

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

        private int DoctorSearch(int size,String jsonData,int i){
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

            if(i==0)doctorResult = new ArrayList<>();

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
            return size;
        }

        private int HospitalSearch(int size,String jsonData,int i){
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
            if(i == 0)hospitalResult = new ArrayList<>();

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
            return size;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0;
            int size = 0;
            while(i < 1 || i < size/10 + 1) {
                if(cursor == 0) {
                    size = HospitalSearch(size,jsonData,i);
                }else if(this.cursor == 1){
                    size = DoctorSearch(size,jsonData,i);
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
                        setHospitalAdapter(hospitalResult);
                        setHospitalInAllAdapter(hospitalResult);
                    }else if(this.cursor == 1){
                        setDoctorAdapter(doctorResult);
                        setDoctorInAllAdapter(doctorResult);
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
            switch (this.cursor){
                case 0 : searchHospitalTask = null; break;
                case 1 : searchDoctorTask = null; break;
                default:
            }
        }
    }
//    public void SearchThing(){
//        List<Doctor> doctors = new ArrayList<>();
//        for(int i = 0;i < 10;i++){
//            Doctor doctor = new Doctor("我是谁","儿科","专家","心脏手术专家",R.drawable.account);
//            doctors.add(doctor);
//        }
//        //向医生RecyclerView添加搜索结果
//        doctorListView = (RecyclerView) findViewById(R.id.search_result_doctor_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
//        doctorListView.setLayoutManager(layoutManager);
//        DoctorAdapter doctorAdapter = new DoctorAdapter(doctors);
//        doctorListView.setAdapter(doctorAdapter);
//
//    }
}
