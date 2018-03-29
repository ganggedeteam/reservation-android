package com.example.hospital_one;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

    private void initPartOfHospitalActivity(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        Intent intent = getIntent();
        int data = intent.getIntExtra("data",2);

        List<String> partOfHospital = new ArrayList<>();

        for(int i = 0;i < DoctorActivity.title.length;i++){
            partOfHospital.add(DoctorActivity.title[i]);
        }
        RecyclerView keshiListView = (RecyclerView) findViewById(R.id.keshiListView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        keshiListView.setLayoutManager(layoutManager);
        PartOfHospitalAdapter partOfHospitalAdapter = new PartOfHospitalAdapter(partOfHospital);
        partOfHospitalAdapter.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Intent intent1 = new Intent(PartOfHospitalActivity.this,DoctorActivity.class);
                intent1.putExtra("data",position);
                startActivity(intent1);
            }
        });
        keshiListView.setAdapter(partOfHospitalAdapter);
    }

}
