package com.example.hospital_one;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.hospital_one.adapter.DoctorCalendarAdapter;
import com.example.hospital_one.adapter.OnCancelButton;
import com.example.hospital_one.adapter.OnRegisterNoteAdaterButtonClicked;
import com.example.hospital_one.adapter.RegisterDoctorNoteAdapter;
import com.example.hospital_one.connection.DoctorCalendarConnection;
import com.example.hospital_one.connection.DoctorConnection;
import com.example.hospital_one.connection.InternetConnection;
import com.example.hospital_one.connection.ReservationConnection;

import java.util.ArrayList;
import java.util.List;

public class RegisterDoctorNoteActivity extends AppCompatActivity {

    private RegisterGetNoteTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor_note);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        task = new RegisterGetNoteTask();
        task.execute((Void)null);

    }

    private void showMessage(String string){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息");
        builder.setMessage(string);
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
    private void setAdapter(final List<RegisterDoctorNoteAdapter
            .RegisterNoteItemDetailMessage> registerNoteItemDetailMessageList){
        LinearLayout registerNoteNULL = (LinearLayout)findViewById(R.id.RegisterNoteNULL);
        LinearLayout registerNoteSome = (LinearLayout)findViewById(R.id.RegisterNoteSome);
        if(registerNoteItemDetailMessageList == null || registerNoteItemDetailMessageList.size() == 0){
            registerNoteNULL.setVisibility(View.VISIBLE);
            registerNoteSome.setVisibility(View.GONE);
        }else{
            registerNoteNULL.setVisibility(View.GONE);
            registerNoteSome.setVisibility(View.VISIBLE);
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RegisterNoteRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            RegisterDoctorNoteAdapter registerDoctorNoteAdapter=
                    new RegisterDoctorNoteAdapter(registerNoteItemDetailMessageList);
            registerDoctorNoteAdapter.setOnCancelButton(new OnCancelButton() {
                @Override
                public void OnCancelButtonClicked(int position) {

                    RegisterCancelNoteTask cancelNoteTask = new RegisterCancelNoteTask(
                            registerNoteItemDetailMessageList.get(position).reservationId);
                    cancelNoteTask.execute((Void)null);
                    //取消挂号

                }
            });

            recyclerView.setAdapter(registerDoctorNoteAdapter);
        }
    }



    private class RegisterCancelNoteTask extends AsyncTask<Void, Void, Boolean>{

        private final String jsonData;
        private int message;
        private List<ReservationConnection.ReservationMessage> reservationMessages;

        public RegisterCancelNoteTask(String jsonData){
            this.jsonData = "{\"reservationId\": \"" + jsonData + "\"}";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");

            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("reservationCancel","");
            String response = InternetConnection.ForInternetHeaderConnection(url,key,token,jsonData);
            if(response == null || response.equals("")){
                this.message = 1;
                return true;
            }
            Log.e("5555555555555555555", "doInBackground: " + response);

            this.message = ReservationConnection.parseReservationCanecl(response);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                if(this.message == 0){
                    //成功的操作
                    showMessage("取消挂号成功！");
                    task = new RegisterGetNoteTask();
                    task.execute((Void)null);
                }else if(this.message == 1){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "网络连接错误！",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "JSon数据解析错误！",Toast.LENGTH_LONG).show();
                }
                else if(this.message == 3){
                    showMessage("取消挂号失败，请提前一天取消");
                }else{
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "未知错误，请与开发者联系！",Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(RegisterDoctorNoteActivity.this,
                        "网络连接错误！",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    private class RegisterGetNoteTask extends AsyncTask<Void, Void, Boolean>{

        private final String jsonData;
        private int message;
        private List<RegisterDoctorNoteAdapter
                .RegisterNoteItemDetailMessage> reservationMessages;

        public RegisterGetNoteTask(){
            SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
            jsonData = "{\"userPhone\": \"" +
                    reader.getString("account","") + "\",\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.getString("ip","")
                    + reader.getString("reservationList","");
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");

            int i = 0,size = 0;
            while(i < 1 || i < size / 10 + 1){
                ReservationConnection.JsonHead result;
                String response = InternetConnection.ForInternetHeaderConnection(url,key,token,jsonData + (i+1)+"\"}");
                if(response == null || response.equals(""))return false;
                if(ReservationConnection.parseAddReservationJson(response) == 3
                        || ReservationConnection.parseAddReservationJson(response) == 2 ){
                    this.message = 2;
                    return true;
                }
                result = ReservationConnection.parseJson(response);
                if(result == null){
                    this.message = 1;
                }else{
                    if(i == 0)reservationMessages = new ArrayList<>();
                    if (i == 0) size = result.data.size();
                    else size += result.data.size();
                    List<RegisterDoctorNoteAdapter
                            .RegisterNoteItemDetailMessage> list = new ArrayList<>();
                    for (ReservationConnection.ReservationMessage now : result.data) {
                        RegisterDoctorNoteAdapter
                                .RegisterNoteItemDetailMessage nowItem = new RegisterDoctorNoteAdapter
                                .RegisterNoteItemDetailMessage();

                        getCalendarMessage(nowItem,now.admissionId);
                        nowItem.reservationId = now.reservationId;
                        nowItem.patientName = now.patientName;
                        nowItem.noteNumber = now.reservationId;
                        nowItem.patientStatus = now.isAdmission;
                        list.add(nowItem);
                    }
                    reservationMessages.addAll(list);
                }
                i++;
            }
            return true;
        }

        private void getCalendarMessage(RegisterDoctorNoteAdapter
                                                .RegisterNoteItemDetailMessage item,String jsonData){
            DoctorCalendarConnection.JsonHead result;
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String url = reader.getString("ip", "") +
                    reader.getString("calendarList", "");
            SharedPreferences readerHeader = getSharedPreferences("start_file",MODE_PRIVATE);
            String key = readerHeader.getString("key","");
            String token = readerHeader.getString("token","");
            String response = InternetConnection.ForInternetHeaderConnection(url,key,token, "{\"admissionId\": \"" + jsonData + "\"}");
            result = DoctorCalendarConnection.parseJsonData(response);
            if (result == null) {
                showMessage("没有该挂号的医生信息");
            }else if(!result.status){
                showMessage("没有该挂号的医生信息");
            } else {
                if (result.data.size() == 0) {
                    showMessage("没有该挂号的医生信息");
                } else {
                    DoctorCalendarConnection.
                            DoctorCalendarMessage doctorMessage = result.data.get(0);
                    item.doctorName = doctorMessage.doctorName;
                    item.departmentName = doctorMessage.departmentName;
                    item.hospitalName = doctorMessage.hospitalName;
                    item.noteTime = doctorMessage.admissionDate + " "
                            + (doctorMessage.admissionPeriod==null||
                            doctorMessage.admissionPeriod.equals("")?"暂无":
                            (Character.digit(doctorMessage.admissionPeriod.charAt(0),
                                    10) == 0 ?"上午8:00~12:00":"下午2:00~5：30"));
                }
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                if(this.message == 0){
                    //成功的操作
                    setAdapter(reservationMessages);
                }else if(this.message == 1){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "网络连接错误！",Toast.LENGTH_LONG).show();
                }else if(this.message == 2){
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "JSon数据解析错误！",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RegisterDoctorNoteActivity.this,
                            "未知错误，请与开发者联系！",Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(RegisterDoctorNoteActivity.this,
                        "网络连接错误！",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }
}
