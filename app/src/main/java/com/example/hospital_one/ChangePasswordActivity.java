package com.example.hospital_one;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.intenet_connection.LoginBackMessage;
import com.example.hospital_one.intenet_connection.UserInformationConnection;

public class ChangePasswordActivity extends AppCompatActivity {

    ChangePasswordTask task = null;

    EditText oldPassword;
    EditText newPassword;
    EditText ensureNewPassword;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        oldPassword = (EditText)findViewById(R.id.OldPassword);
        newPassword = (EditText)findViewById(R.id.NewPassword);
        ensureNewPassword = (EditText)findViewById(R.id.EnsureNewPassword);
        submit = (Button)findViewById(R.id.EnsureSubmitButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!judgeOldPwd()){
                    oldPassword.requestFocus();
                }else{
                    if(!judgeNewPwd()){
                        newPassword.requestFocus();
                    }else{
                        if(!judgeEnsureNewpwd()){
                            ensureNewPassword.requestFocus();
                        }else{
                            task = new ChangePasswordTask(oldPassword.
                                    getText().toString(),newPassword.getText().toString());
                            task.execute((Void)null);
                        }
                    }
                }
            }
        });
    }

    private boolean judgeOldPwd(){
        if(oldPassword.getText().equals(""))
            return false;
        return true;
    }

    private boolean judgeNewPwd(){
        if(newPassword.getText().toString().equals("") || newPassword.getText().toString().length()<6){
            return false;
        }
        return true;
    }

    private boolean judgeEnsureNewpwd(){
        if(!ensureNewPassword.getText().toString().equals(newPassword.getText().toString()))
            return false;
        return true;
    }

    private void showMessageDialog(String message){
        //更新成功弹出信息提示框
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
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

    //用户信息修改、查询线程类
    public class ChangePasswordTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData; //传输到服务器的json数据
        private int message = 0;   //与服务器连接的信息（0代表成功，1代表无网络连接，2代表查无此用户）
        private final String urlLast;
        ChangePasswordTask(String oldPwd,String newPwd) {
            SharedPreferences reader = getSharedPreferences("start_file",MODE_PRIVATE);
            this.jsonData = "{\"userPhone\":\"" + reader
                    .getString("account","") +
                    "\",\"oldPwd\":\"" + oldPwd + "\",\"newPwd\":\"" + newPwd + "\"}";
            this.urlLast = "?userPhone="+reader.getString("account","")
                    +"&oldPwd="+ oldPwd+"&newPwd="+newPwd;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            //读取本地文件的服务器地址
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String url = reader.
                    getString("ip", "") + reader.getString("changePwd","");
            //与服务器连接并传回的信息
            String response = InternetConnection.ForInternetConnection(url+urlLast,jsonData);
            //将json数据转化为类
            if(response == null || response.equals("")){
                this.message = 1;
                return true;
            }
            SharedPreferences.Editor editor = getSharedPreferences("error_file",MODE_PRIVATE).edit();
            editor.putString("response",response);
            editor.apply();

            this.message = LoginBackMessage.parseJson(response);

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if(message == 1){
                    Toast.makeText(ChangePasswordActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
                    showMessageDialog("密码输入错误");
                }else if(message == 3){
                    showMessageDialog("json数据解析错误");
                }else if(message == 4){
                    showMessageDialog("");
                }else if(message == 0) {
                    showMessageDialog("密码更改成功");
                }else{
                    Toast.makeText(ChangePasswordActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }
}
