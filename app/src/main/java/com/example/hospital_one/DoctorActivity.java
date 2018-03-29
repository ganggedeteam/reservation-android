package com.example.hospital_one;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.example.hospital_one.part_hospital.OnItemClickListener;
import com.example.hospital_one.searchresult.Doctor;
import com.example.hospital_one.searchresult.DoctorAdapter;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AcitivityBase {

    public static final int erke = 0;
    public static final int fuchan = 1;
    public static final int nanke = 2;
    public static final int zhongyi = 3;
    public static final int pifu = 4;
    public static final int quanke = 5;
    public static final int xinli = 6;
    public static final int puwai = 7;
    public static final int guke = 8;
    public static final int xinneike = 9;
    public static final int puneike = 10;
    public static final int huxineike = 11;
    public static final int shenjingneike = 12;
    public static final int zhongliuneike = 13;
    public static final int xiaohuneike = 14;
    public static final int neifenmike = 15;
    public static final int shenneike = 16;
    public static final int yanke = 17;
    public static final int erbihouke = 18;
    public static final int kouqiang = 19;
    public static final int yingyang = 20;
    public static final int quickFindDoctor = 100;

    public static final String[] title = { "儿科","妇产科","泌尿外科、男科",
            "中医科","皮肤性病科","全科","心理科","普外科","骨科","心内科","普内科",
            "呼吸内科","神经内科","肿瘤内科","消化内科","内分泌科","肾内科","眼科","耳鼻喉科",
            "口腔科","营养科"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        initDoctorActivity();

    }

    private void initDoctorActivity(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();
        Intent intent =getIntent();
        int num = intent.getIntExtra("data",2);
        TextView keshiName = (TextView)findViewById(R.id.keshiName);
        if(num == quickFindDoctor)
            keshiName.setText("快速找医生");
        else
            keshiName.setText(title[num]);


        List<Doctor> doctors = new ArrayList<>();
        for(int i = 0;i < 10;i++){
            Doctor doctor = new Doctor("我是谁","儿科","专家","心脏手术专家",R.drawable.account);
            doctors.add(doctor);
        }
        RecyclerView doctorListView = (RecyclerView) findViewById(R.id.doctor_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        doctorListView.setLayoutManager(layoutManager);
        DoctorAdapter doctorAdapter = new DoctorAdapter(doctors);
        doctorAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
//                Intent intent1 = new Intent();
//                intent1.putExtra("data",position);
//                startActivity(intent1);
            }
        });
        doctorListView.setAdapter(doctorAdapter);

    }
}
