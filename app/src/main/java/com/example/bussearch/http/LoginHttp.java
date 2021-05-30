package com.example.bussearch.http;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginHttp {
    private String url;
    private String userName;
    private String userPass;
    public interface callBack{
        void response(String data);
    }
    public static final String TAG = "LoginHttp";
    private LoginHttp(String url,String userName, String userPass){
        this.url = url;
        this.userName = userName;
        this.userPass = userPass;
    }
    private static LoginHttp mLoginHttp = null;

    public static LoginHttp getInstance(String url, String userName, String userPass) {
        if(mLoginHttp == null) {
            mLoginHttp = new LoginHttp(url,userName,userPass);
        }
        return mLoginHttp;
    }

    public void login(final callBack callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userName", userName)
                .add("userPass",userPass)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG, "onResponse: " + callback);
                if (callback != null) {
                    callback.response(response.body().string());
                }
            }
        });
    }


}
