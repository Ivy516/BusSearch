package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bussearch.R;
import com.example.bussearch.http.LoginHttp;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    protected EditText mUserName,mUserPass;
    protected Button mLogin;
    private String mUser,mPass;
    private LoginHttp mLoginHttp;
    private String loginUrl = "http://localhost:8080/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longin);

        initView();
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser = mUserName.getText().toString();
                mPass = mUserPass.getText().toString();
                mLoginHttp = LoginHttp.getInstance(loginUrl, mUser,mPass);
                mLoginHttp.login(new LoginHttp.callBack() {
                    @Override
                    public void response(String data) {
                        String status = showData(data);
                        if (status != null && status.equals("success")) {
                            Intent intent = new Intent(LoginActivity.this,UserMainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "请检查账号和密码是否正确",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });
    }

    public String showData(String data) {
        String status = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            status = jsonObject.getString("status");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    protected void initView() {
        mUserName = findViewById(R.id.et_user_name);
        mUserPass = findViewById(R.id.et_user_pass);
        mLogin = findViewById(R.id.bt_login);
    }
}