package com.example.bussearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected EditText from, to;
    protected SuggestionSearch mSuggestionSearch;
    protected String fromKey,toKey;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //获取输入的起始点
        fromKey = from.toString();
        suggestionSearch(fromKey);
        toKey = to.toString();
        suggestionSearch(toKey);
    }

    //初始化界面
    public void initView(){
        from = findViewById(R.id.et_from);
        to = findViewById(R.id.et_to);
    }

    private void suggestionSearch(String keyWord){
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(fromListener);
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
                ArrayList<SuggestionResult.SuggestionInfo> results = (ArrayList<SuggestionResult.SuggestionInfo>) suggestionResult.getAllSuggestions();
//                for (int i = 0; i < results.size(); i++) {
//                    Log.d(TAG, "onGetSuggestionResult: ");
//                }
                showSearchDialog(results);
            }
        }
    };

    public void showSearchDialog(List<SuggestionResult.SuggestionInfo> results) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuggestionSearch.destroy();
    }
}
