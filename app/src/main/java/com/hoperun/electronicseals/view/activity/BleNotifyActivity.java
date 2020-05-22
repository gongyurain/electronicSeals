package com.hoperun.electronicseals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.bean.DeviceEventResp;
import com.hoperun.electronicseals.service.BleService;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class BleNotifyActivity extends AppCompatActivity {
    private static final String TAG = BleNotifyActivity.class.getName();

    private static final String EXTRA_SEARCH_RESULT = "EXTRA_SEARCH_RESULT";
    private static final String EXTRA_BLE_GATT_PROFILE = "EXTRA_BLE_GATT_PROFILE";

    public static void start(Context context, SearchResult result, BleGattProfile profile) {
        Intent intent = new Intent(context, BleNotifyActivity.class);
        intent.putExtra(EXTRA_SEARCH_RESULT, result);
        intent.putExtra(EXTRA_BLE_GATT_PROFILE, profile);
        context.startActivity(intent);
    }

    String text = "";
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕处于点亮状态
        // window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); // 竖屏

        buildCustomActionBar("蓝牙", true, false);

        setContentView(R.layout.activity_ble_notify);

        textView = findViewById(R.id.text);
        loadData();
    }

    private UUID getUUID(String number) {
        return UUID.fromString(String.format("0000%4s-0000-1000-8000-00805F9B34FB", number));
    }

    private void loadData() {
        SearchResult result = getIntent().getParcelableExtra(EXTRA_SEARCH_RESULT);
        BleGattProfile profile = getIntent().getParcelableExtra(EXTRA_BLE_GATT_PROFILE);
        BleService.getInstance().getClient().notify(
                result.getAddress(), getUUID("FFE1"), getUUID("C001"), //me
//                result.getAddress(), getUUID("180D"), getUUID("2A37"), //heart rate
//                result.getAddress(), getUUID("181C"), getUUID("2A8D"), //test helloworld
                new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        String time = new SimpleDateFormat("yy/MM/dd,HH:mm:ss").format(new Date());
                        StringBuilder sb = new StringBuilder();
                        for(int val : value){
                            sb.append(String.format("%02x ", val));
                        }
                        String hex = sb.toString();
                        hex = hex.replace(", ", " ");
                        String str = new String(value);
                        Log.d(TAG, "onNotify value(hex): " + hex);
                        Log.d(TAG, "onNotify value(string): " + str);

                        DeviceEventResp event = new DeviceEventResp(); //TODO parse data to event
                        Log.d(TAG, "convert to event:" + event);
                        text += String.format("时间：%s\n十六进制：%s\n字符串：%s\n\n", time, hex, str);
                        Log.d(TAG, "=====\n" + text);
                        runOnUiThread(BleNotifyActivity.this::updateText);
                    }

                    @Override
                    public void onResponse(int code) {
                        Log.d(TAG, "onResponse code: " + code);

                    }
                });
    }

    private void updateText() {
        textView.setText(text);
    }

    public void buildCustomActionBar(String title, boolean hasBackButton, boolean hasSettingButton) {
        View viewTitleBar = getLayoutInflater().inflate(R.layout.actionbar_view, null);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(viewTitleBar, lp);
        actionBar.setDisplayShowHomeEnabled(false); //去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        TextView tvTitle = (TextView) actionBar.getCustomView().findViewById(R.id.action_bar_title);
        tvTitle.setText(title);
        LinearLayout leftImageButton = (LinearLayout) actionBar.getCustomView().findViewById(R.id.action_bar_left_btn);
        if (!hasBackButton) {
            leftImageButton.setVisibility(View.INVISIBLE);
        } else {
            leftImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BleNotifyActivity.this.finish();
                }
            });
        }
    }

}
