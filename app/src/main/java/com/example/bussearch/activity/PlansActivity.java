package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.bussearch.R;
import com.example.bussearch.adapter.PlansAdapter;
import com.example.bussearch.overlayutil.TransitRouteOverlay;

import java.util.ArrayList;

public class PlansActivity extends AppCompatActivity {

    private RoutePlanSearch mSearch;
    private String start,end;
    private OnGetRoutePlanResultListener mListener;
    private ArrayList<TransitRouteLine> mRouteList = new ArrayList<>();
    private PlansAdapter mPlansAdapter;
    private RecyclerView mRecyclerView;
    public static final String TAG = "PlansActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        mRecyclerView = findViewById(R.id.plans_recycler_view);
        Intent plans = getIntent();
        start = plans.getStringExtra("start");
        end = plans.getStringExtra("end");
        Log.d(TAG, "onCreate: start = " + start + ",end = " + end);
        mSearch = RoutePlanSearch.newInstance();
        createListener();
        mSearch.setOnGetRoutePlanResultListener(mListener);
        searchRoutes();
    }
    private void searchRoutes() {
        PlanNode stardNode = PlanNode.withCityNameAndPlaceName("重庆", start);
        PlanNode endNode = PlanNode.withCityNameAndPlaceName("重庆", end);

        mSearch.transitSearch((new TransitRoutePlanOption())
        .from(stardNode)
        .to(endNode)
        .city("重庆"));
    }

    private void createListener(){
        mListener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                Log.d(TAG, "onGetTransitRouteResult: ");
                mRouteList.clear();
                if (transitRouteResult != null && transitRouteResult.getRouteLines() != null)
                mRouteList.addAll(transitRouteResult.getRouteLines());
                if (mPlansAdapter == null) {
                    Log.d(TAG, "onGetTransitRouteResult: adapter");
                    LinearLayoutManager manager = new LinearLayoutManager(PlansActivity.this);
                    mRecyclerView.setLayoutManager(manager);
                    mPlansAdapter = new PlansAdapter(PlansActivity.this, mRouteList);
                    mRecyclerView.setAdapter(mPlansAdapter);
                } else {
                    Log.d(TAG, "onGetTransitRouteResult: notification");
                    mPlansAdapter.notificationDataChanged(mRouteList);
                }
                Log.d(TAG, "onGetTransitRouteResult: RouteSize = " +
                        transitRouteResult.getRouteLines().size());
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearch.destroy();
    }
}
