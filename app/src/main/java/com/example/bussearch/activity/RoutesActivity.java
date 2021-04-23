package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.search.route.TransitRouteLine;
import com.example.bussearch.R;
import com.example.bussearch.view.RouteView;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity {
    static TransitRouteLine mLine;
    private ArrayList<String> lines = new ArrayList<>();
    private RouteView mBusLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        mLine = getIntent().getParcelableExtra("line");

        initData();
        mBusLineView = findViewById(R.id.bus_line);
    }

    private void initData() {
        for (int i = 0; i < 6; i++) {
            lines.add("站点" + i);
        }
    }

    public static void startRoutesActivity(Context startContext, TransitRouteLine line) {
        Intent intent = new Intent(startContext, RoutesActivity.class);
        intent.putExtra("line", line);
        startContext.startActivity(intent);

    }
}
