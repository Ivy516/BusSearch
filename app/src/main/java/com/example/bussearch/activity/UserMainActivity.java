package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bussearch.R;
import com.example.bussearch.data.BusLineBean;
import com.example.bussearch.http.DeleteLineHttp;
import com.example.bussearch.http.GetBusLinesHttp;
import com.example.bussearch.http.ModifyBusHttp;
import com.example.bussearch.http.ModifyBusLineHttp;
import com.example.bussearch.view.RouteView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity implements View.OnClickListener {

    private SearchView mSearchView;
    private TextView mBusLineView,mShowBus;
    private EditText position,stop,spanners,deleteBus;
    private Spinner mSpinner;
    private Button mDelete,mModifyBusData,mModifyStop;
    private StringBuilder str;
    private String name,value;
    public static final String TAG = "UserMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        initView();
        initSearch();
        initSpanner();
        addListener();
    }

    protected void initSpanner() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] chooses = getResources().getStringArray(R.array.choose);
                name = chooses[position];
                Log.d(TAG, "onItemSelected: " + name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void addListener() {
        mDelete.setOnClickListener(this);
        mModifyBusData.setOnClickListener(this);
        mModifyStop.setOnClickListener(this);
    }

    protected void initView() {
        mSearchView = findViewById(R.id.search);
        mBusLineView = findViewById(R.id.tv_line);
        mShowBus = findViewById(R.id.tv_bus_name);
        position = findViewById(R.id.et_position);
        stop = findViewById(R.id.et_stop);
        spanners = findViewById(R.id.edit);
        deleteBus = findViewById(R.id.et_delete);
        mDelete = findViewById(R.id.bt_delete);
        mModifyBusData = findViewById(R.id.modify);
        mModifyStop = findViewById(R.id.modify_stop);
        mSpinner = findViewById(R.id.choose);
    }

    protected void initSearch() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                if (TextUtils.isEmpty(query)) {
                    return false;
                } else {
                    String url = "http://39.96.15.89:8080/getBusLines?busName=";
                    url = url + query;
                    GetBusLinesHttp getBusLinesHttp = GetBusLinesHttp.getInstance(url);
                    getBusLinesHttp.sendHttp(new GetBusLinesHttp.callBack() {
                        @Override
                        public void response(String data) {
                            String lines = showBusLines(data);
                            mShowBus.setText(query);
                            mBusLineView.setText(lines);
                        }
                    });
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    protected String showBusLines(String data) {
       str = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                str.append(jsonArray.getJSONObject(i).getString("stop"));
                if (i != jsonArray.length()-1) {
                    str.append("->");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    @Override
    public void onClick(View v) {
        String url;
        switch (v.getId()) {
            case R.id.choose:
                url = "http://39.96.15.89:8080/modifyBusData";
                    if (name.equals("发车时间")) {
                        name = "startTime";
                    } else if (name.equals("收车时间")) {
                        name = "endTime";
                    } else if (name.equals("票价")) {
                        name = "price";
                    } else if (name.equals("全程距离")) {
                        name = "distance";
                    } else {
                        name = "name";
                    }
                    value = spanners.getText().toString();
                ModifyBusHttp modifyBusHttp = ModifyBusHttp.getInstance(url, name, value);
                modifyBusHttp.sendHttp(new ModifyBusHttp.callBack() {
                    @Override
                    public void response(String data) {
                        spanners.setText("");
                    }
                });
                break;
            case R.id.modify_stop:
                int index = Integer.valueOf(position.getText().toString());
                String stops = stop.getText().toString();
                url = "http://39.96.15.89:8080/modifyBusLine";
                ModifyBusLineHttp modifyBusLineHttp = ModifyBusLineHttp.getInstance(url, index, stops);
                modifyBusLineHttp.sendHttp(new ModifyBusLineHttp.Callback() {
                    @Override
                    public void response(String data) {
                        position.setText("");
                        stop.setText("");
                    }
                });
                break;
            case R.id.bt_delete:
                final String busName = deleteBus.getText().toString();
                url = "http://39.96.15.89:8080/" + busName;
                DeleteLineHttp deleteLineHttp = DeleteLineHttp.getInstance(url);
                deleteLineHttp.sendHttp(new DeleteLineHttp.Callback() {
                    @Override
                    public void response(String data) {
                        deleteBus.setText("");
                    }
                });
                break;
            default:
                break;
        }
    }
}