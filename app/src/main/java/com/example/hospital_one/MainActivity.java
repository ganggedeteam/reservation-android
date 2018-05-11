package com.example.hospital_one;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.*;

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
        initPersonCenter();
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
        editor.putString("ip","http://10.236.233.7:8080");
        editor.putString("pictureDownloadIp","http://10.236.207.109:9999");
        editor.putString("pictureUploadIP","http://10.236.207.109:8888");
        editor.putString("menuAdd","/system/menu/add");
        editor.putString("departmentPage","/code/departmenttype/pagelist");
        editor.putString("hospitalPage","/hospital/hospital/pagelist");
        editor.putString("addressPage","/code/address/pagelist");
        editor.putString("loginBuser","/login/buser");
        editor.putString("register","/user/register");
        editor.putString("login","/user/login");
        editor.putString("doctorPage","/hospital/doctor/pagelist");
        editor.putString("buserPage","/system//buser/pagelist");
        editor.putString("patientAdd","/patient/add");
        editor.putString("patientDelete","/patient/delete");
        editor.putString("patientPage","/patient/pagelist");
        editor.putString("userAdd","/user/add");
        editor.putString("userPage","/user/pagelist");
        editor.putString("userUpdate","/user/update");
        editor.putString("changePwd","/user/changepwd");
        editor.putString("hospitalDepartment","/hospital/department/pagelist");
        editor.putString("reservationCancel","/reservation/cancel");
        editor.putString("reservationAdd","/reservation/add");
        editor.putString("reservationList","/reservation/list");
        editor.putString("calendarList","/hospital/calendar/list");
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

        LinearLayout paientManger = (LinearLayout)findViewById(R.id.paientManger);
        paientManger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PatientManagerActivity.class);
                startActivity(intent);
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


                Intent intent = new Intent(MainActivity.this,
                        RegisterDoctorNoteActivity.class);
                startActivity(intent);
            }
        });
        final LinearLayout ChangePassWord = (LinearLayout)findViewById(R.id.ChangePassWord);
        ChangePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        Button DownAccount = (Button)findViewById(R.id.DownAccount);
        DownAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editorStartFileFail =
                        getSharedPreferences("start_file",MODE_PRIVATE).edit();
                editorStartFileFail.putBoolean("status",false);
                editorStartFileFail.putString("key",null);
                editorStartFileFail.putString("token",null);
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
                startActivityForResult(intent,1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String result = data.getStringExtra("userName");
                    TextView userName = (TextView)findViewById(R.id.userAccount);
                    userName.setText(result);
                }
                break;
                default:
        }
    }

    void showPerson(){
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
        tabSpec1.setContent(R.id.tab4);
        tabHost.addTab(tabSpec1);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("message");
        tabSpec2.setIndicator("Message",getDrawable(R.drawable.email));
        tabSpec2.setContent(R.id.tab5);
        tabHost.addTab(tabSpec2);
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
                Intent intent = new Intent(
                        MainActivity.this,HospitalListActivity.class);
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
