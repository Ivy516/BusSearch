package com.example.bussearch.http;

import android.util.Log;

import com.example.bussearch.data.BusDatas;
import com.example.bussearch.data.BusLineBean;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBusLineHttp {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public interface callBack{
        void response(String data);
    }
    private String mUrl;
    JSONArray jsonArray;
    private List<BusLineBean> mLines;
    public static final String TAG = "AddBusLineHttp";

    public AddBusLineHttp(List<BusLineBean> mLines, String url) {
       this.mLines = mLines;
       this.mUrl = url;
    }

    public void sendHttp(final callBack callBack) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d(TAG, "sendHttp: " + mUrl);

        try{
            jsonArray = new JSONArray();
            for (BusLineBean line: mLines) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("position",line.getPosition());
                jsonObject.put("stop",line.getStop());
                if (!line.getIsArrived()) {
                    jsonObject.put("isArrived",0);
                }
                jsonObject.put("latitude",line.getLatitude());
                jsonObject.put("longitude",line.getLongitude());
                jsonArray.put(jsonObject);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "sendHttp: " + jsonArray.toString());

        RequestBody requestBody = RequestBody.create(JSON,jsonArray.toString());


        Request request = new Request.Builder()
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
                if (callBack!=null) {
                    callBack.response(response.body().string());
                }
            }
        });
    }
}
