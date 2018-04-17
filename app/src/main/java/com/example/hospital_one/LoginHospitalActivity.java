package com.example.hospital_one;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.example.hospital_one.intenet_connection.InternetConnection;
import com.example.hospital_one.intenet_connection.LoginBackMessage;
import com.example.hospital_one.intenet_connection.LoginConnection;
import com.example.hospital_one.intenet_connection.UserInformationConnection;
import okhttp3.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginHospitalActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * /data/data/com.example.hospital_one/shared_prefs/start_file.xml
     */
    private UserLoginTask mAuthTask = null;
    private UserNameTask mUserNameTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;    //账号输入框
    private EditText mPasswordView;         //密码输入框
    private View mProgressView;             //进度条（旋转）
    private View mLoginFormView;        //登陆界面
    private CheckBox mPasswordCheckBox;     //记住密码复选框
    private ImageButton backButton; //返回按钮
    private Button forgetPassword;
    private Button registerButton;

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_hospital);
        // Set up the login form.
        forgetPassword = (Button)findViewById(R.id.FogetPassword);
        registerButton = (Button)findViewById(R.id.RegisterButton) ;
        backButton = (ImageButton)findViewById(R.id.backLoginButton);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPasswordCheckBox = (CheckBox)findViewById(R.id.PasswordRemeber);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.account);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        //当密码框聚焦时清除框内所有内容
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mPasswordView.setText("");
                //清除有关帐号的密码信息
                SharedPreferences.Editor editor = getSharedPreferences("user_file",MODE_PRIVATE).edit();
                editor.remove(mEmailView.getText().toString());
            }
        });
        intitLoginHospitalActivity();
        Button mEmailSignInButton = (Button) findViewById(R.id.Login_in);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void intitLoginHospitalActivity(){

        SharedPreferences.Editor editor = getSharedPreferences("start_file", MODE_PRIVATE).edit();
        editor.putBoolean("status", false);
        editor.apply();

        //屏蔽标题栏
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null)actionBar.hide();

        forgetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        LoginHospitalActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        LoginHospitalActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

                //当输入超过三个时，将为用户提供提示
                SharedPreferences reader = getSharedPreferences("user_file",MODE_PRIVATE);
                String[] strings = reader.getAll().keySet().toArray(new String[0]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (LoginHospitalActivity.
                                this,android.R.layout.simple_dropdown_item_1line,strings);
                mEmailView.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //当用户点击提示的账号时，为其自动输入密码
                mPasswordView.setText(getSharedPreferences
                        ("user_file",MODE_PRIVATE).getString
                        (mEmailView.getText().toString(),""));// 自动输入密码

            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length()>3 && !email.contains(" ");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginHospitalActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private int message = 0;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //String result = null;
            SharedPreferences reader = getSharedPreferences("host",MODE_PRIVATE);
            String ip = reader.getString("ip","");
            String last = reader.getString("login","");
            String response = InternetConnection.ForInternetConnection
                    (ip + last,"{ \"userPhone\": \"" + mEmail + "\",\n" +
                            "\"userPwd\": \"" + mPassword + "\"}");
            if(response == null || response.equals("")){
                this.message = 1;
                return true;
            }
            this.message = LoginBackMessage.parseJson(response);
            if(this.message == 0){
                if(mPasswordCheckBox.isChecked()) {
                    //保存密码
                    SharedPreferences.Editor editor = getSharedPreferences("user_file", MODE_PRIVATE).edit();
                    editor.putString(this.mEmail, this.mPassword);
                    editor.apply();

                    //将登录的账号密码写入startfile文件，以便下次启动时自动登录
                    SharedPreferences.Editor editor1 = getSharedPreferences("start_file",MODE_PRIVATE).edit();
                    editor1.putString("account",this.mEmail);
                    editor1.putString("password",this.mPassword);
                    editor1.apply();
                }else{
                    //将登录账号数据清空
                    SharedPreferences.Editor editor = getSharedPreferences("user_file", MODE_PRIVATE).edit();
                    editor.remove(this.mEmail);
                    editor.apply();

                    //将start_file文件中的数据清空，保护用户信息
                    SharedPreferences.Editor editor1 = getSharedPreferences("start_file",MODE_PRIVATE).edit();
                    editor1.putString("account","");
                    editor1.putString("password","");
                    editor1.apply();

                }
                SharedPreferences.Editor editorStartFile =
                        getSharedPreferences("start_file",MODE_PRIVATE).edit();
                editorStartFile.putBoolean("status",true);
                editorStartFile.putString("account",this.mEmail);
//                editorStartFile.putString("userName",result.data.get(0).userName);
                editorStartFile.apply();
                return true;
            }
            //密码错误的操作
            SharedPreferences.Editor editorStartFileFail =
                    getSharedPreferences("start_file",MODE_PRIVATE).edit();
            editorStartFileFail.putBoolean("status",false);
            editorStartFileFail.apply();
            return false;
//            for(int i = 0;i < DUMMY_CREDENTIALS.length;i++){
//                String string = DUMMY_CREDENTIALS[i];
//                String[] data = string.split(":");
//                if(data[0].equals(mEmail) && data[1].equals(mPassword)) {
//                    if (mPasswordCheckBox.isChecked()) {
//                        //保存密码
//                        SharedPreferences.Editor editor = getSharedPreferences("user_file", MODE_PRIVATE).edit();
//                        editor.putString(this.mEmail, this.mPassword);
//                        editor.apply();
//
//                        //将登录的账号密码写入startfile文件，以便下次启动时自动登录
//                        SharedPreferences.Editor editor1 = getSharedPreferences("start_file", MODE_PRIVATE).edit();
//                        editor1.putString("account", this.mEmail);
//                        editor1.putString("password", this.mPassword);
//                        editor1.apply();
//                    } else {
//
//                        //将登录账号数据清空
//                        SharedPreferences.Editor editor = getSharedPreferences("user_file", MODE_PRIVATE).edit();
//                        editor.remove(this.mEmail);
//                        editor.apply();
//
//                        //将start_file文件中的数据清空，保护用户信息
//                        SharedPreferences.Editor editor1 = getSharedPreferences("start_file", MODE_PRIVATE).edit();
//                        editor1.putString("account", "");
//                        editor1.putString("password", "");
//                        editor1.apply();
//
//                    }
//
//                    SharedPreferences.Editor editorStartFile =
//                            getSharedPreferences("start_file", MODE_PRIVATE).edit();
//                    editorStartFile.putBoolean("status", true);
//                    editorStartFile.apply();
//                    message = 0;
//                    return true;
//                }else{
//                    SharedPreferences.Editor editorStartFileFail =
//                            getSharedPreferences("start_file",MODE_PRIVATE).edit();
//                    editorStartFileFail.putBoolean("status",false);
//                    editorStartFileFail.apply();
//                }
//            }
//            return false;
            // TODO: register the new account here.
//            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                if(message == 1){
                    Toast.makeText(LoginHospitalActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG);
                }else if(message == 2){
                    Toast.makeText(LoginHospitalActivity
                            .this,"暂不存在此用户",Toast.LENGTH_LONG);
                }else if(message == 0) {

                    SharedPreferences reader =
                            getSharedPreferences("start_file", MODE_PRIVATE);
                    boolean status = reader.getBoolean("status", false);
                    if(status){
                        mUserNameTask = new UserNameTask(
                                "{\"userPhone\": \""+ reader.getString("account","") + "\"}");
                        mUserNameTask.execute((Void)null);
                    }
                    try{
                        Thread.sleep(2000);
                        showProgress(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    finish();
                }
                else{
                    Toast.makeText(LoginHospitalActivity
                            .this,"未知错误",Toast.LENGTH_LONG);
                }
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    public class UserNameTask extends AsyncTask<Void, Void, Boolean> {

        private final String jsonData;
        private int message = 0;
        UserInformationConnection.JsonHead result = null;
        UserNameTask(String jsonData) {
            this.jsonData = jsonData;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            SharedPreferences reader = getSharedPreferences("host", MODE_PRIVATE);
            String url = reader.
                    getString("ip", "") + reader.getString("userPage","");
            String response = InternetConnection.ForInternetConnection(url, jsonData);
            result = UserInformationConnection.parseJsonData(response);
            if (result == null) {
                this.message = 1;
            } else {
                if (result.data.size() == 0) {
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
                    Toast.makeText(LoginHospitalActivity
                            .this,"网络连接错误",Toast.LENGTH_LONG).show();
                }else if(message == 2){
//                    showSearchResult();
                    Toast.makeText(LoginHospitalActivity
                            .this,"查找不到本用户",Toast.LENGTH_LONG).show();
                }else if(message == 0) {
                    SharedPreferences.Editor editor = getSharedPreferences("start_file", MODE_PRIVATE).edit();
                    editor.putString("userName",result.data.get(0).userName);
                    editor.apply();
                    Intent intent = new Intent();
                    intent.putExtra("userName",result.data.get(0).userName);
                    setResult(RESULT_OK,intent);
                }else{
                    Toast.makeText(LoginHospitalActivity
                            .this,"未知错误",Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mUserNameTask = null;
        }
    }
}

