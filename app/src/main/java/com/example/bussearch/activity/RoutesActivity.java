package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.example.bussearch.R;
import com.example.bussearch.view.RouteView;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends AppCompatActivity {
    static TransitRouteLine mLine;
    static List<TransitRouteLine.TransitStep> mTransitSteps = new ArrayList<>();
    private ArrayList<String> lines = new ArrayList<>();
    private RouteView mRouteView;
    public static final String TAG = "RoutesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Log.d(TAG, "onCreate: ");
        mLine = getIntent().getParcelableExtra("line");
        mTransitSteps = mLine.getAllStep();

        mRouteView = findViewById(R.id.bus_route);
        mRouteView.setBusDataList(mTransitSteps);

        mRouteView.setOnItemSelectListener(new RouteView.OnItemSelectListener() {
            @Override
            public void onItemSelect(TransitRouteLine.TransitStep step) {
                if (step.getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.BUSLINE) {
                    BusLineActivity.actionStart(RoutesActivity.this, step);
                } else if (step.getStepType() ==
                TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING) {
                    BNaviMainActivity.actionStart(RoutesActivity.this, step);
                }
            }
        });
    }

    public static void startRoutesActivity(Context startContext, TransitRouteLine line) {
        Intent intent = new Intent(startContext, RoutesActivity.class);
        intent.putExtra("line", line);
        startContext.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
