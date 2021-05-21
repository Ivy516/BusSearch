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

public class ModifyBusHttp {
    private String name,value;
    private String url;
    private static ModifyBusHttp mModifyBusHttp;
    public static final String TAG = "ModifyBusHttp";

    public interface callBack{
        void response(String data);
    }
    private ModifyBusHttp(String url, String name, String value) {
        this.url = url;
        this.name = name;
        this.value = value;
    }

    public static ModifyBusHttp getInstance(String url, String name,String value) {
        if (mModifyBusHttp == null) {
            mModifyBusHttp = new ModifyBusHttp(url,name,value);
        }
        return mModifyBusHttp;
    }

    public void sendHttp(final callBack callback) {
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add(name, value)
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
                if (callback != null) {
                    callback.response(response.body().toString());
                }
            }
        });
    }
}
