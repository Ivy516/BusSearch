package com.example.bussearch.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.example.bussearch.R;
import com.example.bussearch.overlayutil.OverlayManager;
import com.example.bussearch.overlayutil.TransitRouteOverlay;
import com.example.bussearch.view.RouteView;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends AppCompatActivity {
    static TransitRouteLine mLine;
    static List<TransitRouteLine.TransitStep> mTransitSteps = new ArrayList<>();
    private ArrayList<String> lines = new ArrayList<>();
    private RouteView mRouteView;
    private BaiduMap mBaiduMap;
    private MapView mapView;
    private TransitRouteOverlay overlay;
    private boolean isFirstDraw = true;
    public static final String TAG = "RoutesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_routes);
        mapView = findViewById(R.id.baidu_map);
        mBaiduMap = mapView.getMap();

        mLine = getIntent().getParcelableExtra("line");
        mTransitSteps = mLine.getAllStep();

        mapView.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                overlay = new TransitRouteOverlay(mBaiduMap,RoutesActivity.this);
                overlay.setData(mLine);
                overlay.addToMap();
                Log.d(TAG, "onDraw: ");
            }
        });


//        mRouteView = findViewById(R.id.bus_route);
//        mRouteView.setBusDataList(mTransitSteps);
//
//        mRouteView.setOnItemSelectListener(new RouteView.OnItemSelectListener() {
//            @Override
//            public void onItemSelect(TransitRouteLine.TransitStep step) {
//                if (step.getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.BUSLINE) {
//                    BusLineActivity.actionStart(RoutesActivity.this, step);
//                } else if (step.getStepType() ==
//                TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING) {
//                    BNaviMainActivity.actionStart(RoutesActivity.this, step);
//                }
//            }
//        });
    }

    public static void startRoutesActivity(Context startContext, TransitRouteLine line) {
        Intent intent = new Intent(startContext, RoutesActivity.class);
        intent.putExtra("line", line);
        startContext.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
