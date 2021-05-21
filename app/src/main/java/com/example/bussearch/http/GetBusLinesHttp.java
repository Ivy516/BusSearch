package com.example.bussearch.http;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetBusLinesHttp {
    private String url;
    private static GetBusLinesHttp mGetBusLinesHttp;
    public static final String TAG = "GetBusLinesHttp";

    public interface callBack{
        void response(String data);
    }

    private GetBusLinesHttp(String url){
        this.url = url;
    }
    public static GetBusLinesHttp getInstance(String url) {
        if (mGetBusLinesHttp == null) {
            mGetBusLinesHttp = new GetBusLinesHttp(url);
        }
        return mGetBusLinesHttp;
    }

    public void sendHttp(final callBack callBack) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (callBack != null) {
                    callBack.response(response.body().toString());
                }
            }
        });
    }
}
