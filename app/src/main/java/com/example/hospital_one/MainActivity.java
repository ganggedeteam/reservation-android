package com.example.hospital_one;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AcitivityBase {


    LinearLayout personCenter,personCenterElse;
    public static final String[] keshi_title = { "儿科","妇产科","泌尿外科、男科",
            "中医科","皮肤性病科","全科","心理科","普外科","骨科","心内科","普内科",
            "呼吸内科","神经内科","肿瘤内科","消化内科","内分泌科","肾内科","眼科","耳鼻喉科",
            "口腔科","营养科"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        personCenter = (LinearLayout)findViewById(R.id.PersonCenter);
        personCenterElse = (LinearLayout)findViewById(R.id.PersonCenterElse);
        initMain();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences reader =
                getSharedPreferences("start_file", MODE_PRIVATE);
        boolean status = reader.getBoolean("status", false);
        if (status) {
            showPerson();
        } else {
            closePerson();
        }
    }

    public void setHostFile(){
        SharedPreferences.Editor editor = getSharedPreferences("host",MODE_PRIVATE).edit();
        editor.putString("ip","http://192.168.191.1:8080");
        editor.putString("menuAdd","/system/menu/add");
        editor.putString("departmentPage","/code/departmenttype/pagelist");
        editor.putString("hospitalPage","/hospital/hospital/pagelist");
        editor.putString("addressPage","/code/address/pagelist");
        editor.putString("loginBuser","/login/buser");
        editor.putString("buserPage","/system//buser/pagelist");
        editor.putString("patientAdd","/patient/add");
        editor.putString("patientPage","/patient/pagelist");
        editor.putString("userPage","/user/pagelist");
        editor.putString("userUpdate","/user/update");
        editor.apply();

    }


    private void initPersonCenter(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        LinearLayout personInformation = (LinearLayout)findViewById(R.id.personInformation);
        personInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this,);
//                startActivity(intent);
            }
        });

        Button paientManger = (Button)findViewById(R.id.paientManger);
        paientManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this,);
//                startActivity(intent);
            }
        });

        Button MyDoctor = (Button)findViewById(R.id.MyDoctor);
        MyDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this,);
//                startActivity(intent);
            }
        });

        LinearLayout RegisterNote = (LinearLayout)findViewById(R.id.RegisterNote);
        RegisterNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this,);
//                startActivity(intent);
            }
        });
        LinearLayout ChangePassWord = (LinearLayout)findViewById(R.id.ChangePassWord);
        ChangePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this,);
//                startActivity(intent);
            }
        });

        Button DownAccount = (Button)findViewById(R.id.DownAccount);
        DownAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editorStartFileFail =
                        getSharedPreferences("start_file",MODE_PRIVATE).edit();
                editorStartFileFail.putBoolean("status",false);
                editorStartFileFail.apply();
                closePerson();
            }
        });

        Button PlaeaseLogin = (Button)findViewById(R.id.PlaeaseLogin);
        PlaeaseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,LoginHospitalActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout personInformation1 = (LinearLayout)findViewById(R.id.personInformation);
        personInformation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,UserInformationActivity.class);
                startActivity(intent);
            }
        });

    }
    void showPerson(){
        initPersonCenter();
        SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
        TextView userName = (TextView)findViewById(R.id.userAccount);
        userName.setText(reader.getString("userName",""));
        personCenter.setVisibility(View.VISIBLE);
        personCenterElse.setVisibility(View.GONE);
    }
    void closePerson(){
        personCenter.setVisibility(View.GONE);
        personCenterElse.setVisibility(View.VISIBLE);
    }

    private void initMain(){
        setHostFile();
        SharedPreferences.Editor keshi_file = getSharedPreferences("keshi_name",MODE_PRIVATE).edit();
        for(int i = 0;i < 20;i++){
            keshi_file.putString("" + i, keshi_title[i]);
        }
        keshi_file.apply();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.hide();

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("home");
        tabSpec1.setIndicator("Home",getDrawable(R.drawable.home));
        tabSpec1.setContent(R.id.tab1);
        tabHost.addTab(tabSpec1);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("message");
        tabSpec2.setIndicator("Message",getDrawable(R.drawable.email));
        tabSpec2.setContent(R.id.tab2);
        tabHost.addTab(tabSpec2);
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("person");
        tabSpec3.setIndicator("Person",getDrawable(R.drawable.account));
        tabSpec3.setContent(R.id.tab3);
        tabHost.addTab(tabSpec3);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("Person")) {
                    SharedPreferences reader =
                            getSharedPreferences("start_file", MODE_PRIVATE);
                    boolean status = reader.getBoolean("status", true);
                    if (status) {
                        showPerson();
                    } else {
                        closePerson();
                    }
                }
            }
        });

        //搜索框事件

        TextView textView = (TextView)findViewById(R.id.searchTarget);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout search = (LinearLayout)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        //快速找医生事件
        LinearLayout askDoctor = (LinearLayout)findViewById(R.id.askDoctor);
        askDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,DoctorActivity.class);
//                intent.putExtra("data",DoctorActivity.quickFindDoctor);
//                startActivity(intent);

                Intent intent = new Intent(
                        MainActivity.this, LoginHospitalActivity.class);
                intent.putExtra("keshi","NO");
                startActivity(intent);

            }
        });

        //预约挂号事件

        LinearLayout yuYue = (LinearLayout)findViewById(R.id.yuyue);
        yuYue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HospitalListActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout today = (LinearLayout)findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,PartOfHospitalActivity.class);
//                intent.putExtra("data",1);
//                startActivity(intent);
            }
        });

        LinearLayout fuZhen = (LinearLayout)findViewById(R.id.fuzhen);
        fuZhen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,);
//                startActivity(intent);
            }
        });

        //选科室事件
        int[] keshiName = {R.id.baby,R.id.woman,R.id.skin,R.id.chineseDoctor,R.id.punei,R.id.xiaohua,R.id.guke,R.id.moreke};
        String[] zifu = {"儿科","妇产科","皮肤科","中医科","普内科","消化内科","骨科","更多"};
        int[] keshi = {
                DoctorActivity.erke ,
                DoctorActivity.fuchan,
                DoctorActivity.nanke,
                DoctorActivity.zhongyi,
                DoctorActivity.pifu,
                DoctorActivity.quanke,
                DoctorActivity.xinli,
                DoctorActivity.puwai ,
                DoctorActivity.guke ,
                DoctorActivity.xinneike,
                DoctorActivity.puneike ,
                DoctorActivity.huxineike ,
                DoctorActivity.shenjingneike,
                DoctorActivity.zhongliuneike,
                DoctorActivity.xiaohuneike ,
                DoctorActivity.neifenmike ,
                DoctorActivity.shenneike ,
                DoctorActivity.yanke ,
                DoctorActivity.erbihouke ,
                DoctorActivity.kouqiang ,
                DoctorActivity.yingyang };

        for(int i = 0; i < 7;i++){
            //为七个显示在界面上的科室添加事件
            LinearLayout linearLayout = (LinearLayout)findViewById(keshiName[i]);
            final int name = keshi[i];
            linearLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            MainActivity.this,DoctorActivity.class);
                    intent.putExtra("departmentTypeId","");
                    intent.putExtra("data",name);
                    startActivity(intent);
                }
            });
        }
        LinearLayout linearLayout = (LinearLayout)findViewById(keshiName[7]);
        linearLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PartOfHospitalActivity.class);
                intent.putExtra("keshiId","YES");
                startActivity(intent);
            }
        });
    }

}
