package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.bussearch.R;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    protected AutoCompleteTextView from, to;
    protected SuggestionSearch mSuggestionSearch;
    protected String fromKey,toKey;
    protected Button mSearchBt;
    private static String TAG = "MainActivity";
    private ArrayList<String> mDatas = new ArrayList<>();
    private LocationClient mLocationClient = null;
    private MyLocationListener mMyLocationListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mSearchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent plans = new Intent(MainActivity.this,PlansActivity.class);
                plans.putExtra("start", fromKey);
                plans.putExtra("end", toKey);
                startActivity(plans);
            }
        });

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(mMyLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setNeedNewVersionRgc(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    //初始化界面
    public void initView(){
        from = findViewById(R.id.et_from);
        to = findViewById(R.id.et_to);
        mSearchBt = findViewById(R.id.bt_search);

        from.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        from.setDropDownWidth(1500);
        to.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        to.setDropDownWidth(1500);

        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    fromSuggestionSearch(s.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, mDatas);
                    from.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                fromKey = s.toString();
            }
        });

        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    toSuggestionSearch(s.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_dropdown_item_1line, mDatas);
                    to.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                toKey = s.toString();
            }
        });
    }

    private void fromSuggestionSearch(String keyWord){
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(fromListener);
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
        .keyword(keyWord)
        .city("重庆"));
    }

    private void toSuggestionSearch(String keyWord) {
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(toListener);
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(keyWord)
                .city("重庆"));
    }


    OnGetSuggestionResultListener fromListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                return;
            } else {
                ArrayList<SuggestionResult.SuggestionInfo> results =
                        (ArrayList<SuggestionResult.SuggestionInfo>) suggestionResult.getAllSuggestions();
                ArrayList<String> data = new ArrayList<>();
                for (int i = 0; i < results.size(); i++) {
                    String address = results.get(i).getKey()+ "     " +results.get(i).getCity()
                            +results.get(i).getDistrict();
                    Log.d(TAG, "onGetSuggestionResult: " +address);
                    if (address!=null && !address.equals(""))
                    data.add(address);
                }
                if (mDatas.size() != 0) {
                    mDatas.clear();
                } else {
                    mDatas.addAll(data);
                }
            }
        }
    };

    OnGetSuggestionResultListener toListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                return;
            } else {
                ArrayList<SuggestionResult.SuggestionInfo> results =
                        (ArrayList<SuggestionResult.SuggestionInfo>) suggestionResult.getAllSuggestions();
                ArrayList<String> data = new ArrayList<>();
                for (int i = 0; i < results.size(); i++) {
                    String address = results.get(i).getKey();
                    Log.d(TAG, "onGetSuggestionResult: " +address);
                    if (address!=null && !address.equals(""))
                        data.add(address);
                }
                if (mDatas.size() != 0) {
                    mDatas.clear();
                } else {
                    mDatas.addAll(data);
                }
            }
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuggestionSearch.destroy();
    }

    class MyLocationListener extends BDAbstractLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            fromKey = bdLocation.getAddrStr();
            Log.d(TAG, "onReceiveLocation: " +fromKey);
        }
    }
}
