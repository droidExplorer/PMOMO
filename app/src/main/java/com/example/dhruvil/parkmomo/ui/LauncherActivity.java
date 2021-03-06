package com.example.dhruvil.parkmomo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dhruvil.parkmomo.R;
import com.example.dhruvil.parkmomo.helpers.APIClass;
import com.example.dhruvil.parkmomo.helpers.AppConstants;
import com.example.dhruvil.parkmomo.helpers.PrefUtils;
import com.example.dhruvil.parkmomo.model.UserClass;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;


public class LauncherActivity extends ActionBarActivity implements View.OnClickListener{

    private Button btnLogin;
    private EditText edMobile;
    private EditText edPassword;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        init();

        if(PrefUtils.getLogin(LauncherActivity.this)==true){
            Intent intentMap = new Intent(LauncherActivity.this,MapActivity.class);
            startActivity(intentMap);
            finish();
        }

    }

    private void init() {

        btnLogin = (Button)findViewById(R.id.btnLogin);
        edMobile = (EditText)findViewById(R.id.edMobile);
        edPassword = (EditText)findViewById(R.id.edPassword);
        btnLogin.setOnClickListener(this);
        edMobile.setText("9722973444");
        edPassword.setText("123456");


        Typeface typeface = PrefUtils.getTypeFace(LauncherActivity.this);
        btnLogin.setTypeface(typeface);
        edMobile.setTypeface(typeface);
        edPassword.setTypeface(typeface);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnLogin:

                if(checkValidation()){
                    processLogin();
                }else{
                    Toast.makeText(LauncherActivity.this,"Enter Valid Information",Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    public boolean checkValidation(){
        boolean isPassed = false;

        if(edPassword.getText() == null || edPassword.getText().toString().equalsIgnoreCase("") ||
                edMobile.getText() == null || edMobile.getText().toString().equalsIgnoreCase("")){
            isPassed = false;
        }else{
            isPassed = true;
        }

        return isPassed;
    }

    private void processLogin() {

        pd = ProgressDialog.show(LauncherActivity.this,"Please Wait","Loading..",true,false);

        JSONObject obj = null;
        try{
            obj = new JSONObject();
            obj.put("MobileCountryCode","44");
            obj.put("MobileNo",edMobile.getText().toString());
            obj.put("Password",edPassword.getText().toString());
            obj.put("UserRole",3);

        }catch(Exception e){
        }

        new APIClass(AppConstants.LOGIN, obj) {
            @Override
            public void response(String response) {

                pd.dismiss();

                Log.e("Response Login", response);
                UserClass currentUser = new GsonBuilder().create().fromJson(response, UserClass.class);
                PrefUtils.setLogin(true,LauncherActivity.this);
                PrefUtils.setCurrentUser(currentUser, LauncherActivity.this);

                Intent iMap = new Intent(LauncherActivity.this,MapActivity.class);
                startActivity(iMap);
                finish();

            }
            @Override
            public void error(String error) {
                pd.dismiss();
                Toast.makeText(LauncherActivity.this,error,Toast.LENGTH_SHORT).show();
            }
        }.call();
    }
}
