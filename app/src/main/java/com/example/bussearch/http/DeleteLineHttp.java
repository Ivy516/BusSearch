package com.example.bussearch.http;


import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteLineHttp {
    private static DeleteLineHttp mDeleteLine;
    public static final String TAG = "DeleteLine";
    private String url;

    private DeleteLineHttp(String url) {
        this.url = url;
    }

    public static DeleteLineHttp getInstance(String url) {
        if (mDeleteLine == null) {
            mDeleteLine = new DeleteLineHttp(url);
        }
        return mDeleteLine;
    }

    public interface Callback{
        void response(String data);
    }

    public void sendHttp(final Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
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
