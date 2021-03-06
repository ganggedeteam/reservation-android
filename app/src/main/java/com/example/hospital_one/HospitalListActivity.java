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
import com.example.hospital_one.connection.HospitalConnection;
import com.example.hospital_one.connection.InternetConnection;
import com.example.hospital_one.adapter.HospitalAdapter;
import com.example.hospital_one.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.hospital_one.connection.HospitalConnection.hospitalLevel;

public class HospitalListActivity extends AppCompatActivity {

    RecyclerView hospitalRecycler;
    private SearchView searchView;
    private EditText editText;
    private TextView searchResult;
    private HospitalTask task = null;
    LinearLayout personCenter,personCenterElse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);
        hospitalRecycler = (RecyclerView) findViewById(R.id.HospitalRecycler);
        searchView = (SearchView) findViewById(R.id.searchHospitalTarget);
        personCenter = (LinearLayout)findViewById(R.id.PersonCenter);
        personCenterElse = (LinearLayout)findViewById(R.id.PersonCenterElse);
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
        initPersonCenter();
    }

    @Override
    protected void onResume(){
        super.onResume();
        task = new HospitalTask("");
        task.execute((Void) null);
        SharedPreferences reader =
                getSharedPreferences("start_file", MODE_PRIVATE);

        boolean status = reader.getBoolean("status", false);
        if (status) {
            showPerson();
        } else {
            closePerson();
        }
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
                Intent intent = new Intent(HospitalListActivity.this,PatientManagerActivity.class);
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


                Intent intent = new Intent(HospitalListActivity.this,
                        RegisterDoctorNoteActivity.class);
                startActivity(intent);
            }
        });
        final LinearLayout ChangePassWord = (LinearLayout)findViewById(R.id.ChangePassWord);
        ChangePassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HospitalListActivity.this,ChangePasswordActivity.class);
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
                        HospitalListActivity.this,LoginHospitalActivity.class);
                startActivityForResult(intent,1);
            }
        });

        LinearLayout personInformation1 = (LinearLayout)findViewById(R.id.personInformation);
        personInformation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        HospitalListActivity.this,UserInformationActivity.class);
                startActivity(intent);

            }
        });

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
    public void setHostFile(){
        SharedPreferences.Editor editor = getSharedPreferences("host",MODE_PRIVATE).edit();
        editor.putString("ip","http://10.236.87.227:8080");
        editor.putString("pictureDownloadIp","http://10.236.45.147:9999");
        editor.putString("pictureUploadIP","http://10.236.87.227:8888");
        editor.putString("menuAdd","/system/menu/add");
        editor.putString("departmentPage","/code/departmenttype/pagelist");
        editor.putString("departmentDetailPage","/hospital/department/list");
        editor.putString("hospitalPage","/hospital/hospital/pagelist");
        editor.putString("hospitalDepType","/hospital/hospital/depttype");
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

    public void initHospitalListActivity(){
        setHostFile();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost1);
        tabHost.setup();
        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("home");
        tabSpec1.setIndicator("预约中心",getDrawable(R.drawable.home));
        tabSpec1.setContent(R.id.tab4);
        tabHost.addTab(tabSpec1);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("message");
        tabSpec2.setIndicator("个人中心",getDrawable(R.drawable.email));
        tabSpec2.setContent(R.id.tab5);
        tabHost.addTab(tabSpec2);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("个人中心")) {
                    SharedPreferences reader =
                            getSharedPreferences("start_file", MODE_PRIVATE);
                    boolean status = reader.getBoolean("status", true);
                    if (status) {
                        showPerson();
                    } else {
                        closePerson();
                    }
                }else if(tabId.equals("预约中心")){
                    task = new HospitalTask("");
                    task.execute((Void) null);
                }
            }
        });
        TextView noResultNull = (TextView)findViewById(R.id.NORESULTNULL);
        noResultNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new HospitalTask("");
                task.execute((Void) null);
            }
        });
    }


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
        List<HospitalConnection.HospitalMes> connectResult = null;

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
//                if (result.message.equals("success")) {
                    size = result.total;
                    if (result.total == 0) {
                        message = 2;
                    } else if(result.data.size() != 0){
                        connectResult.addAll(result.data);
                    }
//                } else {
//                    message = 3;
//                }
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
                    List<HospitalConnection.HospitalMes> list = new ArrayList<>();
                    for(int i = 0;i < connectResult.size();i++){
                        HospitalConnection.HospitalMes mes = connectResult.get(i);
                        Log.e("dfhasjkdfhskjdfhsakjdfhkjsadfhskajfdhsdj:", "onPostExecute: " + mes.isValid);
                        if(mes.isValid.equals("1")){
                            String hospitalGrade = mes.hospitalGrade;
                            mes.hospitalGrade = hospitalGrade == null || hospitalGrade.equals("")?"暂无":
                                    hospitalLevel[(Character.digit(hospitalGrade.charAt(0),10))];
                            list.add(mes);
                        }else {
                            continue;
                        }
                    }

                    if(list.size() == 0){
                        showSearchResult();
                    }else {
                        showRecycle();
                        setRecyclerView(list);
                    }
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
