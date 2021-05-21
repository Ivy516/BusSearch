package com.example.bussearch.application;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.bussearch.helper.ReflectHelper;

public class MapApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ReflectHelper.unseal(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
