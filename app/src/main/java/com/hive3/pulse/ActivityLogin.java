package com.hive3.pulse;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.dd.processbutton.iml.ActionProcessButton;
import com.hive3.pulse.Utils.Utils;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {

    private EditText et_emailId, et_password;
    private TextInputLayout inputLayoutEmail, inputLayoutStudentPassword;
    private ActionProcessButton signIn;

    public static String emailId, password;


    public static Map<String, String> loginCookies = new HashMap<>();
    public static Map<String, String> sessionID = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutStudentPassword = (TextInputLayout) findViewById(R.id.input_layout_studentID);
        et_emailId = (EditText) findViewById(R.id.et_emailId);
        et_password = (EditText) findViewById(R.id.et_password);

        et_emailId.addTextChangedListener(new MyTextWatcher(et_emailId));
        et_password.addTextChangedListener(new MyTextWatcher(et_password));


        signIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        signIn.setMode(ActionProcessButton.Mode.ENDLESS);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideKeyboard(v);

                ActivityMain.tinyDB.clear();

                if (isOnline()) {
                    signIn.setProgress(1);
                    submitForm();

                    if (validateEmail() && validatePassword()) {

                        emailId = et_emailId.getText().toString();
                        password = et_password.getText().toString();


                        try {

                            CheckLogin checkLogin = new CheckLogin();
                            checkLogin.execute();

                        } catch (Exception e) {

                            signIn.setProgress(-1);
                        }

                    } else {

                        signIn.setProgress(-1);

                    }
                }else {
                    Snackbar.make(v, "3G/4G/wifi connection is required ", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });


        //    tinyDB = new TinyDB(getApplicationContext());


    }


    private void submitForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateEmail() {

        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "srmuniv.edu.in";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(et_emailId.getText().toString());

        if (!matcher.matches()) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(et_emailId);
            return false;
        } else {

            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (et_password.getText().toString().trim().isEmpty()) {
            inputLayoutStudentPassword.setError(getString(R.string.err_msg_password));
            requestFocus(et_password);
            return false;
        } else {

            inputLayoutStudentPassword.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_emailId:
                    validateEmail();
                    break;
                case R.id.et_password:
                    validatePassword();
                    break;
            }
        }
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);


        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }


    public  boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(getApplicationContext(), "No Internet connection! We need 2G/3G/wifi connection", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    public class CheckLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {


                Connection.Response login1 = Jsoup.connect("https://academia.srmuniv.ac.in/")
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .method(Connection.Method.GET)
                        .execute();

                loginCookies.clear();
                loginCookies = login1.cookies();


                Connection.Response login2 = Jsoup.connect("https://academia.srmuniv.ac.in/accounts/signin.ac")
                        .userAgent("Mozilla/5.0")
                        .referrer("https://academia.srmuniv.ac.in/accounts/signin?_sh=false&hideidp=true&portal=10002227248&client_portal=true&servicename=ZohoCreator&serviceurl=https://academia.srmuniv.ac.in/&service_language=en")
                        .timeout(10000)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .method(Connection.Method.POST)
                        .data("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .cookies(login1.cookies())
                        .data("client_portal", "true")
                        .data("grant_type", "password")
                        .data("is_ajax", "true")
                        .data("portal", "10002227248")
                        .data("servicename", "ZohoCreator")
                        .data("serviceurl", "https://academia.srmuniv.ac.in/")
                        .data("username", emailId)
                        .data("password", password)
                        .execute();


                sessionID.clear();
                sessionID = login2.cookies();


                if (sessionID.get("clientauthtoken") == null) {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                } else {

                }


            } catch (Exception e) {
                e.printStackTrace();


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (sessionID.get("clientauthtoken") != null) {

                ArrayList<String> cookies = new ArrayList<>(sessionID.keySet());
                ArrayList<String> cookiesData = new ArrayList<>(sessionID.values());

                ActivityMain.tinyDB.putListString("cookies", cookies);
                ActivityMain.tinyDB.putListString("cookiesData", cookiesData);
                ActivityMain.tinyDB.putString("emailId",emailId);
                ActivityMain.tinyDB.putString("password",password);

                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null) {
                            Toast.makeText(getApplicationContext(),"Error Your account is not created!",Toast.LENGTH_LONG).show();
                        } else {


                            ParseUser.getCurrentUser().put("requests", false);
                            ParseUser.getCurrentUser().put("status","idle");
                            ParseUser.getCurrentUser().saveInBackground();

                            Intent i = new Intent(getApplicationContext(), ActivityMain.class);
                            i.putExtra("emailId", emailId);
                            i.putExtra("password", password);
                            i.putExtra("sessionID", sessionID.toString());
                            i.putExtra("requests",false);
                            finish();
                            startActivity(i);

                        }
                    }
                });


            } else {
                signIn.setProgress(-1);
            }


        }


    }


}

































