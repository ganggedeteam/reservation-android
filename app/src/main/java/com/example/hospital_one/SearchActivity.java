package com.example.hospital_one;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.example.hospital_one.searchresult.Doctor;
import com.example.hospital_one.searchresult.DoctorAdapter;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AcitivityBase {

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

}
