package com.example.bussearch.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.example.bussearch.R;

public class WalkActivity extends Activity {

    private WalkNavigateHelper mWalkNavigateHelper;
    public static final String TAG = "WalkActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWalkNavigateHelper = WalkNavigateHelper.getInstance();
        View view = mWalkNavigateHelper.onCreate(WalkActivity.this);
        Log.d(TAG, "onCreate: " + view);
        if (view != null) {
            setContentView(view);
        }
        mWalkNavigateHelper.startWalkNavi(WalkActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: WalkActivity");
    }
}