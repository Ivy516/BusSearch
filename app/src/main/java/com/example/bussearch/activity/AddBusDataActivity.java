package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.bussearch.R;
import com.example.bussearch.data.BusDatas;
import com.example.bussearch.data.BusLineBean;
import com.example.bussearch.http.AddBusDataHttp;
import com.example.bussearch.http.AddBusLineHttp;

import java.util.ArrayList;
import java.util.List;

public class AddBusDataActivity extends AppCompatActivity {

    private EditText mEtBusName,mEtStartTime,mEtEndTime,mEtPrice,mEtDistance,mEtName;
    private Button mSend,mBtLine,mBtOk;
    private String busDataUrl = "http://39.96.15.89:8080/addBusData";
    private String busLineUrl = "http://39.96.15.89:8080/addLine";
    private TextView mTvLines;
    private double latitude,longitude;
    private String address;
    private int position = 0;
    public static final String TAG = "AddBusDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus_data);
        initView();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusDatas data = new BusDatas();
                data.setName(mEtBusName.getText().toString());
                data.setStartTime(mEtStartTime.getText().toString());
                data.setEndTime(mEtEndTime.getText().toString());
                data.setPrice(Float.valueOf(mEtPrice.getText().toString()));
                data.setDistance(Float.valueOf(mEtDistance.getText().toString()));
                AddBusDataHttp addBusDataHttp = new AddBusDataHttp(data,busDataUrl);
                addBusDataHttp.sendHttp(new AddBusDataHttp.callBack() {
                    @Override
                    public void response(String data) {
                        Log.d(TAG, "response: " + data);
                        AddBusDataActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mEtBusName.setText("");
                                mEtStartTime.setText("");
                                mEtEndTime.setText("");
                                mEtDistance.setText("");
                                mEtPrice.setText("");
                                Toast.makeText(AddBusDataActivity.this, "增加数据成功",
                                Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

        initLocationOption();
        //路线增加
        mBtLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (address != null) {
                    mTvLines.append(position + "," + address + ","
                            + latitude + "," + longitude + "\n");
                }
            }
        });

        //路线上传
        mBtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] ll = mTvLines.getText().toString().split("\n");
                List<BusLineBean> lines = new ArrayList<>();
                for (int i = 0; i < ll.length; i++) {
                    Log.d(TAG, "onClick: " + ll[i]);
                    String[] a = ll[i].split(",");
                    BusLineBean line = new BusLineBean(Integer.valueOf(a[0]),a[1],false,
                            Double.valueOf(a[2]),Double.valueOf(a[3]));
                    lines.add(line);
                }
                String busName = mEtName.getText().toString();

                AddBusLineHttp addBusLineHttp = new AddBusLineHttp(lines,
                        busLineUrl+"?busName="+busName);
                addBusLineHttp.sendHttp(new AddBusLineHttp.callBack() {
                    @Override
                    public void response(String data) {
                        Log.d(TAG, "response: " + data);
                        AddBusDataActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                position = 0;
                                mTvLines.setText("");
                                mEtName.setText("");
                                Toast.makeText(AddBusDataActivity.this, "增加路线成功",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

    }

    //初始化定位参数
    protected void initLocationOption() {
        LocationClient locationClient = new LocationClient(getApplicationContext());
        LocationClientOption locationClientOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();

        locationClient.registerLocationListener(myLocationListener);
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setScanSpan(1000);
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setIsNeedLocationDescribe(true);
        locationClientOption.setOpenGps(true);

        locationClient.setLocOption(locationClientOption);
        locationClient.start();
    }

    protected class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            latitude = bdLocation.getLatitude();
            longitude = bdLocation.getLongitude();

            String ad = bdLocation.getAddrStr();
            address = ad.substring(ad.indexOf("(")+1,ad.indexOf(")"));

            Log.d(TAG, "onReceiveLocation: latitude = " + latitude + " "
            + "longitude = " + longitude + " " + "address = " + address);
        }
    }

    protected void initView() {
        mEtBusName = findViewById(R.id.name);
        mEtStartTime = findViewById(R.id.et_start_time);
        mEtEndTime = findViewById(R.id.et_end_time);
        mEtPrice = findViewById(R.id.et_price);
        mEtDistance = findViewById(R.id.et_distance);
        mSend = findViewById(R.id.bt_add);

        mEtName = findViewById(R.id.et_add_line);
        mBtLine = findViewById(R.id.bt_add_line);
        mTvLines = findViewById(R.id.tv_lines);
        mBtOk = findViewById(R.id.ok);
    }
}