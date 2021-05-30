package com.example.bussearch.http;

import android.util.Log;

import com.example.bussearch.data.BusDatas;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBusDataHttp {

    public interface callBack{
        void response(String data);
    }

    private BusDatas mBusData;
    private String mUrl;
    public static final String TAG = "AddBusDataHttp";
    public AddBusDataHttp(BusDatas mBusData,String url) {
        this.mBusData = mBusData;
        this.mUrl = url;
    }

    public void sendHttp(final callBack callBack) {
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("busName", mBusData.getName())
                .add("startTime",mBusData.getStartTime())
                .add("endTime",mBusData.getEndTime())
                .add("price", String.valueOf(mBusData.getPrice()))
                .add("distance", String.valueOf(mBusData.getDistance()))
                .build();

        final Request request = new Request.Builder()
                .url(mUrl)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (callBack != null) {
                    callBack.response(response.body().string());
                }
            }
        });
    }
}
