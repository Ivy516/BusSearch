package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.search.route.TransitRouteLine;
import com.example.bussearch.R;

public class RoutesActivity extends AppCompatActivity {
    static TransitRouteLine mLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
    }

    public static void startRoutesActivity(Context startContext) {
        Intent intent = new Intent(startContext, RoutesActivity.class);
        startContext.startActivity(intent);

    }
}
