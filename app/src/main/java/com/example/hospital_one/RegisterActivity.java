package com.example.hospital_one;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospital_one.jsonclass.JsonHead;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
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
    private UserRegisterTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mUserNameView;
    private EditText mRessurePassword;
    private View mProgressView;
    private View mRegisterFormView;
//    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Set up the login form.
        mUserNameView = (EditText)findViewById(R.id.register_user_name);
        mEmailView = (EditText) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mRessurePassword = (EditText)findViewById(R.id.register_resure_password);
        populateAutoComplete();
//        mRessurePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//                    attemptRegister();
//                    return true;
//                }
//                return false;
//            }
//        });
        initRegisterActivity();
        Button mRegisterButton = (Button) findViewById(R.id.Register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
    }

    public void initRegisterActivity(){

        //屏蔽标题栏
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null)actionBar.hide();

//        backButton = (ImageButton)findViewById(R.id.backLoginButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

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
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mRessurePassword.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String resurepassword = mRessurePassword.getText().toString();

        if (TextUtils.isEmpty(userName) || !checkUserName(userName)) {
            mUserNameView.requestFocus();
            mUserNameView.setError("请检查用户名是否输入正确");
            return;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.requestFocus();
            mEmailView.setError("请检查邮箱是否输入正确");
            return;
        } else if (!checkEmail(email)) {
            mEmailView.requestFocus();
            mEmailView.setError("请检查邮箱格式是否正确");
            return;
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !checkPassword(password)) {
            mPasswordView.requestFocus();
            mPasswordView.setError("请按照要求输入密码");
            return;
        }

        if (!TextUtils.isEmpty(resurepassword) && !checkRessurePassword(resurepassword)) {
            mRessurePassword.requestFocus();
            mRessurePassword.setError("请确认此处输入正确");
            return;
        }



        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mAuthTask = new UserRegisterTask(userName,email, password);
        mAuthTask.execute((Void) null);
    }


    private void makeToast(String string){
        Toast.makeText(RegisterActivity.this,string,Toast.LENGTH_SHORT).show();
    }
    public boolean checkUserName(String userName){
        if(userName.equals("")){
            makeToast("请输入用户名");
            return false;
        }
        if(userName.startsWith(" ")){
            makeToast("请不要以空格开头");
            return false;
        }
        if(userName.length() < 7 || userName.length() > 15){
            makeToast("用户名长度不要小于7或者大于15");
            return false;
        }
        return true;
    }
    public boolean checkEmail(String email){
        if(email.equals("")){
            makeToast("请输入邮箱账号");
            return false;
        }
        if(email.contains(" ") || !email.contains("@") || email.charAt(0) == '@'){
            makeToast("您输入的邮箱格式不正确");
            return false;
        }
        return true;
    }
    public boolean checkPassword(String password){

        if(password.equals("")){
            makeToast("请输入您的密码");
            return false;
        }

        if(password.length() < 7 || password.length() > 20){
            makeToast("密码长度不得小于7或者大于20");
            return false;
        }

        return true;
    }
    public boolean checkRessurePassword(String resurePassword){
        if(!resurePassword.equals(mPasswordView.getText().toString())){
            makeToast("请确认您的密码正确并且统一");
            return false;
        }
        return true;
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), RegisterActivity.ProfileQuery.PROJECTION,

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
            emails.add(cursor.getString(RegisterActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private class RegisterID extends JsonHead {
        String username;
        String email;
        String password;
        public RegisterID(String username,String email,String password){
            super("register");
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    class RegisterResult extends JsonHead {
        public int message = 0;
        public RegisterResult(int message){
            super("RegisterResult");
            this.message = message;
        }
    }

    public String makeJson(String username,String email,String password){
        RegisterID id = new RegisterID(username,email,password);
        String json = new Gson().toJson(id);
        return json;
    }

    public RegisterResult getJson(String jsondata){
        try {
            JSONArray jsonArray = new JSONArray(jsondata);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String headMessage = jsonObject.getString("headMessage");
                if(!headMessage.equals("RegisterResult")){
                    return null;
                }
                return new RegisterResult(jsonObject.getInt("message"));
            }
        } catch (JSONException e) {
            return null;
        }
        return null;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {


        private final String mEmail;
        private final String mPassword;
        private final String mUserName;
        private int message = 0;
        private final int USERNAME = 1;
        private final int EMAIL = 2;

        UserRegisterTask(String userName,String email, String password) {
            mUserName = userName;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            String result = null;
            //连接服务器，注册账号
            try {
                // Simulate network access.
                Thread.sleep(2000);
                /*String url = "";
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON,makeJson(mUserName,mEmail,mPassword));
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response= client.newCall(request).execute();
                result = response.body().string();

            } catch (IOException e) {
                message = 1;
                return true;
            }
            if(result == null){
                Toast.makeText(RegisterActivity.this,"网络连接失败",Toast.LENGTH_SHORT);
                return true;
            }
            RegisterResult registerResult = getJson(result);
            if(registerResult == null){
                //
                this.message = 3;
                return true;
            }else{
                //
                this.message = registerResult.message;
            }*/
            }catch (Exception ex){
                ex.printStackTrace();
            }
            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                if(message == USERNAME) {
                    mUserNameView.requestFocus();
                    Toast.makeText(RegisterActivity.this, "用户名已存在，请更改", Toast.LENGTH_SHORT).show();
                }
                else if(message == EMAIL) {
                    mEmailView.requestFocus();
                    Toast.makeText(RegisterActivity.this, "该邮箱已注册，请更改", Toast.LENGTH_SHORT).show();
                }else if(message == 0) {
                    Toast.makeText(RegisterActivity.this, "请到邮箱验证信息", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"未知错误，接受代码出错",Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "请到邮箱验证信息", Toast.LENGTH_SHORT).show();
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
}

