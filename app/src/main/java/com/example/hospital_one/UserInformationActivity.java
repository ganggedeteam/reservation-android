package com.example.hospital_one;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.hospital_one.intenet_connection.AddressConnection;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.intenet_connection.UserInformationConnection;

import java.util.ArrayList;
import java.util.List;

public class UserInformationActivity extends AppCompatActivity {

    private AddressTask addressTask = null;
    private AddressTask provinceTask = null;     //地址查找线程
    private AddressTask cityTask = null;
    private AddressTask quTask = null;
    private List<AddressConnection.AddressMessage> addressMessageList = null; //地址信息类列表
    private UserInformationTask task; //用户信息查询列表
    private Button saveInformation;   //保存用户信息按钮
    private TextView userNameText;    //用户名显示
    private TextView telephoneText;   //电话号码显示
    private TextView sexText;          //性别显示
    private TextView provinceText;      //省地址显示
    private TextView cityText;          //城市地址显示
    private TextView quText;            //区地址显示
    private TextView addrText;          //详细地址显示
    private EditText userNameEditText;     //用户名编辑框
    private EditText addrEditText;          //详细地址编辑框

    String provinceAddressId = "-1";  //省地址标识ID
    String cityAddressId = "-1"; //城市地址标识ID
    String quAddressId = "-1";  //区地址地址标识ID
    String sex = "0";   //性别标识ID
    int provinceId = -1;  //省地质序号
    int cityId = -1;    //城市地址序号
    int quId = -1;  //区地址序号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        //界面按钮及TextView初始化
        userNameEditText = (EditText)findViewById(R.id.InformationUserNameEditText);
        saveInformation = (Button)findViewById(R.id.InformationSave);
        userNameText = (TextView)findViewById(R.id.InformationUserNameText);
        telephoneText = (TextView)findViewById(R.id.InformationTelephoneText);
        sexText = (TextView)findViewById(R.id.InformationSexText);
        provinceText = (TextView)findViewById(R.id.InformationProvinceText);
        cityText = (TextView)findViewById(R.id.InformationCityText);
        quText = (TextView)findViewById(R.id.InformationQuText);
        addrText = (TextView)findViewById(R.id.InformationAddrText);
        addrEditText = (EditText)findViewById(R.id.InformationAddrEditText);
        //各种控件事件初始化
        initUserInformationActity();
        //获取用户信息线程启动
        SharedPreferences reader =
                getSharedPreferences("start_file",MODE_PRIVATE);
        SharedPreferences readerHost = getSharedPreferences("host",MODE_PRIVATE);
        task = new UserInformationTask(0,
                "{\"userPhone\": \""+ reader.getString("account","") + "\"}");
        task.execute((Void)null);

        showProgress(false);
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            showProgress(true);
        }
    }

    private void initUserInformationActity(){
        //屏蔽标题栏
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        //修改用户信息按钮事件
        saveInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //输入栏聚焦消除
                addrEditText.clearFocus();
                userNameEditText.clearFocus();
                SharedPreferences readerHost =
                        getSharedPreferences("host",MODE_PRIVATE);
                String city = "\"city\": "+ cityId + ",";
                String province = "\"province\": " + provinceId + ",";
                String county = "\"county\": "+ quId +",";
                String detailAddr = "\"detailAddr\": \"" + addrText.getText().toString() + "\",";
                String userphone = "\"userPhone\": \"" + telephoneText.getText().toString() + "\"";
                String sexJson = "\"sex\":\""+ sex +"\",";
                //修改用户信息线程
                task = new UserInformationTask(1,
                        "{"+(provinceId == -1? "":province)
                                + (cityId == -1 ? "":city)+ (quId == -1?"":county)
                                + (addrText.getText().toString().equals("无")?"":detailAddr)
                                + (sexText.getText().toString().equals("无")?"":sexJson)+ userphone +"}");
                task.execute((Void)null);
            }
        });

        //用户名显示和用户名编辑事件
        userNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserNameText(true);
            }
        });
        userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!userNameText.getText().equals(userNameEditText
                            .getText()))saveInformation.setVisibility(View.VISIBLE);
                    setUserNameText(false);
                }else{
                    userNameEditText.setText(userNameText.getText());
                }
            }
        });

        //性别事件
        sexText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexSingleChoiceDialog();
            }
        });

        //省显示事件
        LinearLayout province = (LinearLayout)findViewById(R.id.InformationProvince);
        province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressTask = new AddressTask(0,"\"preId\": \"0\"");
                addressTask.execute((Void)null);
            }
        });

        //城市事件
        LinearLayout city = (LinearLayout)findViewById(R.id.InformationCity);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!provinceText.getText().equals("无")) {
                    addressTask = new AddressTask(1, "\"preId\": \"" + provinceAddressId + "\"");
                    addressTask.execute((Void) null);
                }else{
                    showErrorMessage("请检查省份信息是否填写");
                }
            }
        });

        //区事件
        LinearLayout qu = (LinearLayout)findViewById(R.id.InformationQu);
        qu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cityText.getText().toString().equals("无")) {
                    addressTask = new AddressTask(2, "\"preId\": \"" + cityAddressId + "\"");
                    addressTask.execute((Void) null);
                }else{
                    showErrorMessage("请检查省分和所在城市信息是否填写");
                }
            }
        });

        //详细地址显示及编辑事件
        addrText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDeatilAdress(true);
            }
        });
        addrEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!addrEditText.getText().equals(addrText
                            .getText()))saveInformation.setVisibility(View.VISIBLE);
                    setDeatilAdress(false);
                }else{
                    addrEditText.setText(addrText.getText());
                }
            }
        });
    }

    private void showAddressData(){
//        for(;;){
//            if(task == null)break;
//        }

        if(provinceId > 0){
            provinceTask = new AddressTask(6,"\"id\": " + provinceId + "");
            provinceTask.execute((Void)null);
        }

        if(cityId > 0){
            cityTask = new AddressTask(7,"\"id\": " + cityId + "");
            cityTask.execute((Void)null);
        }

        if(quId > 0){
            quTask = new AddressTask(8,"\"id\": " + quId + "");
            quTask.execute((Void)null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.


        final ScrollView messagePane = (ScrollView)findViewById(R.id.UserMessagePane);
        final LinearLayout progressBar = (LinearLayout)findViewById(R.id.UserInformationProgressBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            messagePane.setVisibility(show ? View.GONE : View.VISIBLE);
            messagePane.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    messagePane.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            messagePane.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    //设置详细地址的显示与编辑交替
    void setDeatilAdress(boolean cursor){
        if(cursor){
            addrText.setVisibility(View.GONE);
            addrEditText.setVisibility(View.VISIBLE);
            addrEditText.requestFocus();
        }else{
            addrText.setText(addrEditText.getText());
            addrText.setVisibility(View.VISIBLE);
            addrEditText.setVisibility(View.GONE);
        }
    }

    //设置用户名的显示与编辑交替
    void setUserNameText(boolean cursor){
        if(cursor){
            userNameText.setVisibility(View.GONE);
            userNameEditText.setVisibility(View.VISIBLE);
            userNameEditText.requestFocus();
        }else{
            userNameText.setText(userNameEditText.getText());
            userNameText.setVisibility(View.VISIBLE);
            userNameEditText.setVisibility(View.GONE);
        }
    }

    //将地址信息类列表转化为单纯的地址名称列表
    public List<String> mesToList(List<AddressConnection.AddressMessage> list){
        List<String> result = new ArrayList<>();
        for(int i = 0 ;i < list.size();i++ ){
            result.add(i,list.get(i).addressName);
        }
        return result;
    }

    //显示错误信息Dialog
    private void showErrorMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("友情提示");
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //显示地址单选界面
    int itemsNum = 0;
    private void showSingleChoiceDialog(final int cursor,final TextView textView, final List<String> list) {
        itemsNum = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("");

        String[] string1 = (String[])list.toArray(new String[0]);
        builder.setSingleChoiceItems(string1, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemsNum = which;
            }
        });

        final String string = textView.getText().toString();
        //设置正面按钮
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveInformation.setVisibility(View.VISIBLE);
                textView.setText(list.get(itemsNum));
                if(cursor == 0){
                    if(!string.equals(list.get(itemsNum))){
                        saveInformation.setVisibility(View.VISIBLE);
                        provinceAddressId = addressMessageList.get(itemsNum).addressId;
                        provinceId = addressMessageList.get(itemsNum).id;
                        cityText.setText("无");
                        quText.setText("无");
                    }
                }else if(cursor == 1){
                    if(!string.equals(list.get(itemsNum))){
                        saveInformation.setVisibility(View.VISIBLE);
                        cityId = addressMessageList.get(itemsNum).id;
                        cityAddressId = addressMessageList.get(itemsNum).addressId;
                        quText.setText("无");
                    }
                }else if(cursor == 2){
                    if(!string.equals(list.get(itemsNum))){
                        saveInformation.setVisibility(View.VISIBLE);
                        quId = addressMessageList.get(itemsNum).id;
                        quAddressId = addressMessageList.get(itemsNum).addressId;
                    }
                }
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    //显示性别单选界面
    private void showSexSingleChoiceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] showString = {"男","女"};
        final String sexResult = sex;
        builder.setSingleChoiceItems(showString,
                Character.digit(sex.charAt(0),10), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex = "" + which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!sexResult.equals(sex))
                    saveInformation.setVisibility(View.VISIBLE);
                if (sex.equals("0")) {
                    sexText.setText("男");
                } else if (sex.equals("1")) {
                    sexText.setText("女");
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex = sexResult;
                dialog.dismiss();
            }
        });

        builder.setCancelable(true);
        AlertDialog dialog= builder.create();
        dialog.show();
    }

    //初始化开始的数据显示
    private void setData(UserInformationConnection.UserInformation infor){

        userNameText.setText(infor.userName);
        telephoneText.setText(infor.userPhone);
        if(infor.sex == null || infor.sex.equals("")){
            sexText.setText("无");
        }else {
            sex = infor.sex;
            if (infor.sex.equals("0")) {
                sexText.setText("男");
            } else if (infor.sex.equals("1")) {
                sexText.setText("女");
            }
        }
        if(infor.province == 0) {
            provinceText.setText("无");
        }else {
            provinceId = infor.province;
        }
        if(infor.city == 0)
            cityText.setText("无");
        else {
            cityId = infor.city;
        }
        if(infor.county == 0)
            quText.setText("无");
        else {
            quId = infor.county;
        }
        if(infor.detailAddr == null || infor.detailAddr.equals(""))
            addrText.setText("无");
        else
            addrText.setText(infor.detailAddr);

        showAddressData();
    }

    //用户信息修改、查询线程类
    public class UserInformationTask extends AsyncTask<Void, Void, Boolean> {

        private final int cursor; //用户操作标识（0代表查询，1代表更新）
        private final String jsonData; //传输到服务器的json数据
        private int message = 0;   //与服务器连接的信息（0代表成功，1代表无网络连接，2代表查无此用户）
        UserInformationConnection.JsonHead result = null;  //服务器返回信息转化的类
        UserInformationConnection.UserInformationUpdateBackMessage
                updateResult = null; //服务器传回的更矮用户信息的信息类

        UserInformationTask(int cursor,String jsonData) {
            this.cursor = cursor;
            this.jsonData = jsonData;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            if(this.cursor == 0){

                //读取本地文件的服务器地址
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String url = reader.
                        getString("ip", "") + reader.getString("userPage","");
                //与服务器连接并传回的信息
                String response = InternetConnection.ForInternetConnection(url, jsonData);
                //将json数据转化为类
                result = UserInformationConnection.parseJsonData(response);
                if (result == null) {
                    this.message = 1;
                } else {
                    if (result.data.size() == 0) {
                        this.message = 2;
                    }
                }
            }else if(this.cursor == 1) {
                //读取本地文件的服务器地址
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String url = reader.
                        getString("ip", "") + reader.getString("userUpdate","");
                //与服务器连接并传回的信息
                String response = InternetConnection.ForInternetConnection(url, jsonData);
                //将json数据转化为类
                updateResult = UserInformationConnection.parseUpdateMessageJsonData(response);
                if (updateResult == null) {
                    this.message = 1;
                } else {

                }
            }
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if(message == 1){
                    Toast.makeText(UserInformationActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
                    Toast.makeText(UserInformationActivity
                            .this,"查找不到本用户",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    if(cursor == 0)
                        setData(result.data.get(0));
                    else{
                        //更新成功弹出信息提示框
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserInformationActivity.this);
                        builder.setMessage("更新成功!");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setCancelable(true);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        saveInformation.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(UserInformationActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }

    //地址信息查询线程类
    public class AddressTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData; //传输到服务器的接送数据
        //用户操作标识（0代表省查询，1代表城市查询，2代表区查询，
        // 3代表省AddressId查询，4代表城市AddressId查询，5代表区AddressId查询
        // 6代表省名称查询，7代表城市名称查询，8代表区名称查询）
        private final int cursor;
        //与服务器连接信息
        private int message = 0;
        AddressConnection.JsonHead result = null;  //服务器返回信息转化的类

        //地址信息类列表
        List<AddressConnection.
                AddressMessage> listResult = new ArrayList<>();

        AddressTask(int cursor,String jsonData) {
            this.cursor = cursor;
            this.jsonData = "{" + jsonData + ",\"pageNo\":\"";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            int i = 0;
            int size = 0;
            while(i < 1 || i < size/10 + 1) {

                //读取服务器地址
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String url = reader.
                        getString("ip", "")
                        + reader.getString("addressPage", "");
                //与服务器建立连接并接受返回消息
                String response = InternetConnection.
                        ForInternetConnection(url, jsonData + (i+1)+ "\"}");

                //将json数据转化为类
                result = AddressConnection.parseJsonData(response);
                if (result == null) {
                    this.message = 1;
                } else {
                    if(i == 0)size = result.total;
                    if (result.total == 0) {
                        this.message = 2;
                    }else if(result.data.size() != 0){
                        //将接受的地址信息提取
                        listResult.addAll(result.data);
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
                    Toast.makeText(UserInformationActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
                    Toast.makeText(UserInformationActivity
                            .this,"查找不到本用户",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    addressMessageList = listResult;
                    if(this.cursor == 0){
                        //显示相应的信息
                        List<String> list = mesToList(addressMessageList);
                        showSingleChoiceDialog(0,provinceText,list);
                    }else if(this.cursor == 1){
                        List<String> list = mesToList(addressMessageList);
                        showSingleChoiceDialog(1,cityText,list);
                    }else if(this.cursor == 2){
                        List<String> list = mesToList(addressMessageList);
                        showSingleChoiceDialog(2,quText,list);
                    }else if(this.cursor == 3){
                        provinceAddressId = addressMessageList.get(0).addressId;
                    }else if(this.cursor == 4){
                        cityAddressId = addressMessageList.get(0).addressId;
                    }else if(this.cursor == 5){
                        quAddressId = addressMessageList.get(0).addressId;
                    }else if(this.cursor == 6){
                        provinceText.setText(listResult.get(0).addressName);
                    }else if(this.cursor == 7){
                        cityText.setText(listResult.get(0).addressName);
                    }else if(this.cursor == 8){
                        quText.setText(listResult.get(0).addressName);
                    }else{
                        Toast.makeText(UserInformationActivity
                                .this,"标识代码错误",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(UserInformationActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            switch (this.cursor){
                case 6: provinceTask = null;break;
                case 7:cityTask = null;break;
                case 8:quTask = null;break;
                    default:
                        addressTask = null;
            }
        }
    }
}
