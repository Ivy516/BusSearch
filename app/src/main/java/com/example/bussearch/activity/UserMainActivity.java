package com.example.bussearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bussearch.R;
import com.example.bussearch.http.DeleteLineHttp;
import com.example.bussearch.http.GetBusLinesHttp;
import com.example.bussearch.http.ModifyBusHttp;
import com.example.bussearch.http.ModifyBusLineHttp;

import org.json.JSONArray;
import org.json.JSONObject;


public class UserMainActivity extends AppCompatActivity implements View.OnClickListener {

    private SearchView mSearchView;
    private TextView mBusLineView,mShowBus;
    private EditText position,stop,spanners,deleteBus,mBus;
    private Spinner mSpinner;
    private Button mDelete,mModifyBusData,mModifyStop,mAdd;
    private StringBuilder str;
    private String name,value,bus;
    private mHandler handler = new mHandler();
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
        mAdd.setOnClickListener(this);
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
        mBus = findViewById(R.id.et_bus);
        mAdd = findViewById(R.id.add);
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
                           final String lines = showBusLines(data);
                           UserMainActivity.this.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   mShowBus.setText(query);
                                   mBusLineView.setText(lines);
                               }
                           });
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
        Log.d(TAG, "showBusLines: " + str.toString());
        return str.toString();
    }

    @Override
    public void onClick(View v) {
        String url;
        switch (v.getId()) {
            case R.id.modify:
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
                    bus = mBus.getText().toString();
                Log.d(TAG, "onClick: name = " + name + " value = " + value + " busName = " + bus);
                ModifyBusHttp modifyBusHttp = ModifyBusHttp.getInstance(url, name, value,bus);
                modifyBusHttp.sendHttp(new ModifyBusHttp.callBack() {
                    @Override
                    public void response(String data) {
                        UserMainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spanners.setText("");
                            }
                        });
                    }
                });
                break;
            case R.id.modify_stop:
                int index = Integer.valueOf(position.getText().toString());
                String stops = stop.getText().toString();
                bus = mBus.getText().toString();
                url = "http://39.96.15.89:8080/modifyBusLine";
                ModifyBusLineHttp modifyBusLineHttp = ModifyBusLineHttp.getInstance(url, index, stops,bus);
                modifyBusLineHttp.sendHttp(new ModifyBusLineHttp.Callback() {
                    @Override
                    public void response(String data) {
                        Message message = handler.obtainMessage();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                });
                break;
            case R.id.bt_delete:
                final String busName = deleteBus.getText().toString();
                url = "http://39.96.15.89:8080/deleteLine?busName=" + busName;
                DeleteLineHttp deleteLineHttp = DeleteLineHttp.getInstance(url);
                deleteLineHttp.sendHttp(new DeleteLineHttp.Callback() {
                    @Override
                    public void response(String data) {
                        Message message = handler.obtainMessage();
                        message.what = 3;
                        handler.sendMessage(message);
                    }
                });
                break;
            case R.id.add:
                Intent intent = new Intent(UserMainActivity.this, AddBusDataActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    spanners.setText("");
                    mBus.setText("");
                    break;
                case 2:
                    position.setText("");
                    stop.setText("");
                    break;
                case 3:
                    deleteBus.setText("");
                    break;
                default:
                    break;
            }
        }
    }
}