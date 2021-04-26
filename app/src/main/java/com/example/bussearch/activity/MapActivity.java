package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
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

import java.util.List;

public class MapActivity extends AppCompatActivity {

    private MapView mBaiduMapView;
    private BaiduMap mBaiduMap;
    private TransitRouteLine mLine;
    private OnGetRoutePlanResultListener mListener;
    private RoutePlanSearch mSearch;
    private String start,end;
    public static final String TAG = "MapActivity";
    private GeoCoder mCoder;
    private OnGetGeoCoderResultListener mGeoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mLine = getIntent().getParcelableExtra("line");
        Intent plans = getIntent();
        start = plans.getStringExtra("start");
        end = plans.getStringExtra("end");

        mBaiduMapView = findViewById(R.id.bai_du_map);
        mBaiduMap = mBaiduMapView.getMap();
        mSearch = RoutePlanSearch.newInstance();
        mCoder = GeoCoder.newInstance();
        createListener();
        mSearch.setOnGetRoutePlanResultListener(mListener);
        searchRoutes();
//        TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
//        overlay.setData(mLine);
//        overlay.addToMap();
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
        mSearch.destroy();
    }

    public static void actionStart(Context context, TransitRouteLine line) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("line", line);
        context.startActivity(intent);
    }

    private void latlngToAddress(LatLng latLng) {
        mCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
        mCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MapActivity.this, "获取不到地址", Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "onGetReverseGeoCodeResult: " + reverseGeoCodeResult.getAddress());
                }
            }
        });
    }


    private void searchRoutes() {
        PlanNode stardNode = PlanNode.withCityNameAndPlaceName("北京", start);
        PlanNode endNode = PlanNode.withCityNameAndPlaceName("北京", end);

        mSearch.transitSearch((new TransitRoutePlanOption())
                .from(stardNode)
                .to(endNode)
                .city("北京"));
    }

    private void createListener() {
        mListener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                Log.d(TAG, "onGetTransitRouteResult: ");
                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);

                if (transitRouteResult.getRouteLines() != null && transitRouteResult.getRouteLines().size()>0) {
                    TransitRouteLine line = transitRouteResult.getRouteLines().get(0);
                    overlay.setData(line);
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    Log.d(TAG, "onGetTransitRouteResult: title = " + line.getTitle()
                            + ", distance = " + line.getDistance()
                    + ", duration = " + line.getDuration() + ", allStepSize = " + line.getAllStep().size());
                    Log.d(TAG, "onGetTransitRouteResult: " +
                            line.getAllStep().get(3).getVehicleInfo().getPassStationNum());
                    List<LatLng> latLngList = line.getAllStep().get(1).getWayPoints();

                    Log.d(TAG, "onGetTransitRouteResult: size = " + latLngList.size());
                    for (int i = 0; i < latLngList.size(); i++) {
                        latlngToAddress(latLngList.get(i));
                    }
                }
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
}
