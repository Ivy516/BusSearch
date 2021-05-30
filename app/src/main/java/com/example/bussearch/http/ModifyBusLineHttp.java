package com.example.bussearch.http;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyBusLineHttp {
    private static int position;
    private static String name,busName;
    private String url;
    private static ModifyBusLineHttp mModifyBusLineHttp;
    public static final String TAG = "ModifyBusLineHttp";
    private ModifyBusLineHttp(String url, int position, String name, String busName) {
        this.url = url;
        this.position = position;
        this.name = name;
        this.busName = busName;
    }

    public interface Callback{
        void response(String data);
    }

    public static ModifyBusLineHttp getInstance(String url, int position, String name, String busName) {
        if (mModifyBusLineHttp == null) {
            mModifyBusLineHttp = new ModifyBusLineHttp(url, position, name, busName);
        } else {
            ModifyBusLineHttp.position = position;
            ModifyBusLineHttp.name = name;
            ModifyBusLineHttp.busName = busName;
        }
        return mModifyBusLineHttp;
    }

    public void sendHttp(final Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("position", String.valueOf(position))
                .add("name", name)
                .add("busName", busName)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (callback != null) {
                    callback.response(response.body().string());
                }
            }
        });
    }
}
