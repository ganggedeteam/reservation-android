package com.example.hospital_one;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
    private List<AddressConnection.AddressMessage> addressMessageList = null;
    private UserInformationTask task;
    private Button saveInformation;
    private TextView userNameText;
    private TextView telephoneText;
    private TextView sexText;
    private TextView provinceText;
    private TextView cityText;
    private TextView quText;
    private TextView addrText;

    String provinceAddressId = "-1";
    String cityAddressId = "-1";
    String quAddressId = "-1";
    int provinceId = -1;
    int cityId = -1;
    int quId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        saveInformation = (Button)findViewById(R.id.InformationSave);
        userNameText = (TextView)findViewById(R.id.InformationUserNameText);
        telephoneText = (TextView)findViewById(R.id.InformationTelephoneText);
        sexText = (TextView)findViewById(R.id.InformationSexText);
        provinceText = (TextView)findViewById(R.id.InformationProvinceText);
        cityText = (TextView)findViewById(R.id.InformationCityText);
        quText = (TextView)findViewById(R.id.InformationQuText);
        addrText = (TextView)findViewById(R.id.InformationAddrText);
        initUserInformationActity();
        SharedPreferences reader =
                getSharedPreferences("start_file",MODE_PRIVATE);
        SharedPreferences readerHost = getSharedPreferences("host",MODE_PRIVATE);
        task = new UserInformationTask(readerHost.getString("userPage",""),
                "{\"userPhone\": \""+ reader.getString("account","") + "\"}");
        task.execute((Void)null);
    }

    private void initUserInformationActity(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();
        saveInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences readerHost =
                        getSharedPreferences("host",MODE_PRIVATE);
                task = task = new UserInformationTask(readerHost.getString("userUpdate",""),
                        "{   \"city\": ,\n" +
                                "      \"userPhone\": \"\",\n" +
                                "      \"sex\": \"\",\n" +
                                "      \"county\": ,\n" +
                                "      \"userPwd\": \"\",\n" +
                                "      \"userName\": \"\",\n" +
                                "      \"detailAddr\": \"\",\n" +
                                "      \"province\": ,}");
                task.execute((Void)null);
            }
        });

        LinearLayout province = (LinearLayout)findViewById(R.id.InformationProvince);
        province.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressTask = new AddressTask(0,"\"preId\": \"0\"");
                addressTask.execute((Void)null);
            }
        });

        LinearLayout city = (LinearLayout)findViewById(R.id.InformationCity);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!provinceText.getText().equals("无")) {
                    addressTask = new AddressTask(1, "\"preId\": \"" + provinceAddressId + "\"");
                    addressTask.execute((Void) null);
                }else{
                    showErrorMessage("请检查省分信息是否填写");
                }
            }
        });

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
        addrText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public List<String> mesToList(List<AddressConnection.AddressMessage> list){
        List<String> result = new ArrayList<>();
        for(int i = 0 ;i < list.size();i++ ){
            result.add(i,list.get(i).addressName);
        }
        return result;
    }

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
                        provinceAddressId = addressMessageList.get(itemsNum).addressId;
                        provinceId = addressMessageList.get(itemsNum).id;
                        cityText.setText("无");
                        quText.setText("无");
                    }
                }else if(cursor == 1){
                    if(!string.equals(list.get(itemsNum))){
                        cityId = addressMessageList.get(itemsNum).id;
                        cityAddressId = addressMessageList.get(itemsNum).addressId;
                        quText.setText("无");
                    }
                }else if(cursor == 2){
                    if(!string.equals(list.get(itemsNum))){
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

    private void setData(UserInformationConnection.UserInformation infor){

        userNameText.setText(infor.userName);
        telephoneText.setText(infor.userPhone);
        if(infor.sex == null || infor.sex.equals("")){
            sexText.setText("无");
        }else {
            if (infor.sex.equals("0")) {
                sexText.setText("男");
            } else if (infor.sex.equals("1")) {
                sexText.setText("女");
            }
        }
        if(infor.provinceName == null || infor.provinceName.equals("")) {
            provinceText.setText("无");
        }else {
            provinceId = infor.province;
            provinceText.setText(infor.provinceName);
        }
        if(infor.cityName == null || infor.cityName.equals(""))
            cityText.setText("无");
        else {
            cityId = infor.city;
            cityText.setText(infor.cityName);
        }
        if(infor.countyName== null || infor.countyName.equals(""))
            quText.setText("无");
        else {
            quId = infor.country;
            quText.setText(infor.countyName);
        }
        if(infor.detailAddr == null || infor.detailAddr.equals(""))
            addrText.setText("无");
        else
            addrText.setText(infor.detailAddr);
    }

    public class UserInformationTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private final String urlLast;
        private int message = 0;
        UserInformationConnection.JsonHead result = null;

        UserInformationTask(String url,String jsonData) {
            this.urlLast = url;
            this.jsonData = jsonData;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String url = reader.
                    getString("ip","") + this.urlLast;
            String response = InternetConnection.ForInternetConnection(url,jsonData);
            result = UserInformationConnection.parseJsonData(response);
            if(result == null){
                this.message = 1;
            }else{
                if(result.data.size() == 0){
                    this.message = 2;
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
//                    showSearchResult();
                    Toast.makeText(UserInformationActivity
                            .this,"查找不到本用户",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    setData(result.data.get(0));
//                    task = null;
//                    finish();
                }else{
                    Toast.makeText(UserInformationActivity
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


    public class AddressTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private final int cursor;
        private int message = 0;
        AddressConnection.JsonHead result = null;
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
                SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
                String url = reader.
                        getString("ip", "")
                        + reader.getString("addressPage", "");
                String response = InternetConnection.ForInternetConnection(url, jsonData + (i+1)+ "\"}");
                SharedPreferences.Editor editor = getSharedPreferences("error_file", MODE_PRIVATE).edit();
                editor.putString("messsage", response);
                editor.apply();
                result = AddressConnection.parseJsonData(response);
                if (result == null) {
                    this.message = 1;
                } else {
                    if(i == 0)size = result.total;
                    if (result.total == 0) {
                        this.message = 2;
                    }else if(result.data.size() != 0){
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
                    }else{
                        Toast.makeText(UserInformationActivity
                                .this,"标识代码错误",Toast.LENGTH_LONG).show();
                    }
//                    task = null;
//                    finish();
                }else{
                    Toast.makeText(UserInformationActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
//            setRecyclerView(getConnect());
            addressTask = null;
        }
    }

}
