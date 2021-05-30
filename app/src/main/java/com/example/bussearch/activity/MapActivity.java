package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
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

public class MapActivity extends AppCompatActivity {

    private MapView mBaiduMapView;
    private BaiduMap mBaiduMap;
    private TransitRouteLine mLine;
    private RoutePlanSearch mSearch;
    private String start,end;
    private OnGetRoutePlanResultListener mListener;
    private ArrayList<TransitRouteLine> mRouteList = new ArrayList<>();
    public static final String TAG = "PlansActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mLine = getIntent().getParcelableExtra("line");

        mBaiduMapView = findViewById(R.id.bai_du_map);
        mBaiduMap = mBaiduMapView.getMap();

        //TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
        //overlay.setData(mLine);
        //在地图上绘制TransitRouteOverlay
//        overlay.addToMap();
//        overlay.zoomToSpan();

//        Intent plans = getIntent();
//        start = plans.getStringExtra("start");
//        end = plans.getStringExtra("end");
//        Log.d(TAG, "onCreate: start = " + start + " ,end = " + end);
//        mSearch = RoutePlanSearch.newInstance();
        //createListener();
//        mSearch.setOnGetRoutePlanResultListener(mListener);
        //searchRoutes();
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
                mRouteList.clear();
                if (transitRouteResult != null && transitRouteResult.getRouteLines() != null) {
//                    TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
//                    if (transitRouteResult.getRouteLines().size() > 0) {
//                        overlay.setData(transitRouteResult.getRouteLines().get(1));
//                        //在地图上绘制TransitRouteOverlay
//                        overlay.addToMap();
//                        //overlay.zoomToSpan();
//                    }
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
        //mSearch.destroy();
        mBaiduMapView.onDestroy();
    }

    public static void actionStart(Context context, TransitRouteLine line) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("line", line);
        context.startActivity(intent);
    }
}
