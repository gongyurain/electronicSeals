package com.hoperun.electronicseals.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.adapter.DiscoverListAdapter;
import com.hoperun.electronicseals.adapter.DiscoverListAdapter.Item;
import com.hoperun.electronicseals.service.BleService;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

public class DiscoverActivity extends AppCompatActivity {
    private static final String TAG = DiscoverActivity.class.getName();

    public static void start(Context context) {
        Intent intent = new Intent(context, DiscoverActivity.class);
        context.startActivity(intent);
    }

    Button btScan;

    List<Item> list = new ArrayList<>();
    ListView listView;
    DiscoverListAdapter adapter;

    BluetoothClient mClient;
    boolean scaning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕处于点亮状态
        // window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED); // 竖屏

        buildCustomActionBar("发现蓝牙", true, false);
        setContentView(R.layout.activity_discover);

        btScan = findViewById(R.id.scan);
        btScan.setOnClickListener(v -> switchDiscover());

        listView = findViewById(R.id.list);
        adapter = new DiscoverListAdapter(this.getLayoutInflater(), v -> connect((Item) v.getTag()));
        adapter.setData(list);
        listView.setAdapter(adapter);

        start();
    }

    private void start() {
        BleService.getInstance().init(this);
        mClient = BleService.getInstance().getClient();
        boolean open = mClient.isBluetoothOpened();
        Log.d(TAG, "isBluetoothOpened:" + open);
        if (open) {
        } else {
            Log.d(TAG, "openBluetooth");
            mClient.openBluetooth();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            accessLocationPermission();
        } else {
            startDiscover();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void accessLocationPermission() {
        int accessCoarseLocation = checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFineLocation = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listRequestPermission = new ArrayList<String>();

        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listRequestPermission.isEmpty()) {
            String[] strRequestPermission = listRequestPermission.toArray(new String[listRequestPermission.size()]);
            requestPermissions(strRequestPermission, REQUEST_PERMISSION_LOC);
        } else {
            startDiscover();
        }
    }

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_ENABLE_BT_DISCOVER = 2;
    private static final int REQUEST_PERMISSION_LOC = 3;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOC:
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        Log.d(TAG, "onRequestPermissionsResult " + permissions[i]
                                + ", " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    startDiscover();
                }
                break;
            default:
                return;
        }
    }

    void switchDiscover() {
        if (scaning) {
            stopDiscover();
        } else {
            startDiscover();
        }
    }

    void startDiscover() {
        Log.d(TAG, "startDiscover");
        list.clear();
        adapter.notifyDataSetChanged();

        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(30 * 1000)
                .searchBluetoothClassicDevice(30*1000)
                .build();

        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                Log.d(TAG, "onSearchStarted");
            }

            @Override
            public void onDeviceFounded(SearchResult result) {
                Log.d(TAG, "onDeviceFounded name:" + result.getName() + ", addr:" + result.getAddress());
                Beacon beacon = new Beacon(result.scanRecord);
                Log.d(TAG, String.format("beacon for %s\n%s", result.getAddress(), beacon.toString()));
                updateItem(new Item(result, null));
            }

            @Override
            public void onSearchStopped() {
                Log.d(TAG, "onSearchStopped");
                updateScan(false);
            }

            @Override
            public void onSearchCanceled() {
                Log.d(TAG, "onSearchCanceled");
                updateScan(false);
            }
        });
        updateScan(true);
    }

    private void updateScan(boolean scan) {
        scaning = scan;
        btScan.setText(scaning ? "停止扫描" : "开始扫描");
    }

    private void stopDiscover() {
        Log.d(TAG, "startDiscover");
        mClient.stopSearch();
        updateScan(false);
    }

    ArrayList<BleGattProfile> profiles = new ArrayList<>();

    void connect(Item item) {
        Log.d(TAG, "connect name:" + item.getName() + ", addr:" + item.getAddress());
        stopDiscover();
        mClient.connect(item.getAddress(), new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
                    Log.d(TAG, "connect success, profile:" + profile);
                    profiles.add(profile);
                    item.profile = profile;
                    updateItem(item);
                    Toast.makeText(DiscoverActivity.this, "设备已连接", Toast.LENGTH_SHORT).show();

//                    BleService.getInstance().readNotify(item.searchResult, item.profile);
                    BleNotifyActivity.start(DiscoverActivity.this, item.searchResult, item.profile);
                } else {
                    Toast.makeText(DiscoverActivity.this, "连接失败(" + code + ")", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateItem(Item item) {
        boolean found = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAddress().equalsIgnoreCase(item.getAddress())) {
                list.set(i, item);
                found = true;
                break;
            }
        }
        if (!found) {
            list.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mClient != null) {
            mClient.stopSearch();
        }
    }

    public void buildCustomActionBar(String title, boolean hasBackButton, boolean hasSettingButton) {
        View viewTitleBar = getLayoutInflater().inflate(R.layout.actionbar_discover, null);
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
                    DiscoverActivity.this.finish();
                }
            });
        }
    }

}
