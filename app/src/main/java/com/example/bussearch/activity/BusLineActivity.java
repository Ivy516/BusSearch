package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.search.route.TransitRouteLine;
import com.example.bussearch.R;
import com.example.bussearch.data.BusLineBean;
import com.example.bussearch.http.GetBusLinesHttp;
import com.example.bussearch.view.BusLineView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusLineActivity extends AppCompatActivity {

    private TransitRouteLine.TransitStep step;
    private BusLineView mBusLineView;
    public static final String TAG = "BusLineActivity";
    private String url = "http://39.96.15.89:8080/getBusLines?busName=";
    private List<BusLineBean> mBusLineBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_line);

        step = getIntent().getParcelableExtra("step");
        String busName = step.getVehicleInfo().getTitle();
        Log.d(TAG, "onCreate: " + busName);
        url = url+busName;
        GetBusLinesHttp getBusLinesHttp = GetBusLinesHttp.getInstance(url);
        getBusLinesHttp.sendHttp(new GetBusLinesHttp.callBack() {
            @Override
            public void response(String data) {
                showData(data);
            }
        });

        mBusLineView = findViewById(R.id.bus_line);
        //initData();
        mBusLineView.setBusStops(mBusLineBeans);

    }

    private void showData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                mBusLineBeans.add(new BusLineBean(jsonObject1.getInt("position")
                ,jsonObject1.getString("stop"),
                        jsonObject1.getInt("isArrived") == 1 ? true : false));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initData() {
        mBusLineBeans.add(new BusLineBean(1,"新力村",false));
        mBusLineBeans.add(new BusLineBean(2,"文峰公社",false));
        mBusLineBeans.add(new BusLineBean(3,"吉祥路口",false));
        mBusLineBeans.add(new BusLineBean(4,"南山路",true));
        mBusLineBeans.add(new BusLineBean(5,"中研所",false));
        mBusLineBeans.add(new BusLineBean(6,"邮电大学",false));
        mBusLineBeans.add(new BusLineBean(7,"黄桷垭",false));
        mBusLineBeans.add(new BusLineBean(8,"崇文路口",true));
        mBusLineBeans.add(new BusLineBean(9,"上新街站",false));
        mBusLineBeans.add(new BusLineBean(10,"龙门浩",false));
        mBusLineBeans.add(new BusLineBean(11,"轨道上新街站",true));
        mBusLineBeans.add(new BusLineBean(12,"小什字小商品市场",false));
        mBusLineBeans.add(new BusLineBean(13,"临江门",false));
        mBusLineBeans.add(new BusLineBean(14,"较场口",false));
    }

    public static void actionStart(Context context, TransitRouteLine.TransitStep step) {
        Intent intent = new Intent(context, BusLineActivity.class);
        intent.putExtra("step",step);
        context.startActivity(intent);
    }
}