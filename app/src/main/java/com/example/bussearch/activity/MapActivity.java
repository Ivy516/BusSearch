package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.example.bussearch.R;
import com.example.bussearch.overlayutil.TransitRouteOverlay;

public class MapActivity extends AppCompatActivity {

    private MapView mBaiduMapView;
    private BaiduMap mBaiduMap;
    private TransitRouteLine mLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mLine = getIntent().getParcelableExtra("line");

        mBaiduMapView = findViewById(R.id.bai_du_map);
        mBaiduMap = mBaiduMapView.getMap();
        TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
        overlay.setData(mLine);
        overlay.addToMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBaiduMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBaiduMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMapView.onDestroy();
    }

    public static void actionStart(Context context, TransitRouteLine line) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("line", line);
        context.startActivity(intent);
    }
}
