package com.hoperun.electronicseals.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.service.IMqttCallBack;
import com.hoperun.electronicseals.service.MqttService;
import com.hoperun.electronicseals.view.fragment.SearchFragment;
import com.hoperun.electronicseals.view.fragment.UserFragment;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.radiogroup)
    RadioGroup radioGroup;

    List<Fragment> fragments;
    private Fragment preFragment;

    private boolean isExit = false;
    private MqttService mqttService;
    private MqttService.MqttBinder mqttBinder;
    @Override
    public void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.search_tab:
                        changefragment(0);
                        break;
                    case R.id.user_tab:
                        changefragment(1);
                        break;
                }
            }
        });
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("gongyu", "sevice" + service.toString());
            mqttBinder = (MqttService.MqttBinder) service;
            mqttBinder.setMqttCallBack(new IMqttCallBack() {
                @Override
                public void messageArrived(String topic, String message, int qos) {
                    Log.e("gongyu","messageArrived [" + topic + ", " + message + "]");
                }

                @Override
                public void connectionLost(Throwable arg0) {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken arg0) {

                }
            });
            mqttBinder.getDeviceList();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void initData() {
        bindService(new Intent(MainActivity.this, MqttService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void initView() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new SearchFragment());
        fragments.add(new UserFragment());
        changefragment(0);
        radioGroup.check(R.id.search_tab);
    }

    protected void changefragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = fragments.get(index);
        if (preFragment != null) {
            transaction.hide(preFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.framelayout, fragment);
        }
        transaction.commit();
        preFragment = fragment;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public BaseContract.BasePresenter initPresenter() {
        return null;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isExit = false;
                    break;
            }
        }
    };

    public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(1, 2000);
        } else {
            finish();
        }
    }



    public void getDeviceInfo(IMqttCallBack mqttCallBack) {
        if (mqttBinder != null) {
            mqttBinder.setMqttCallBack(mqttCallBack);
            mqttBinder.getDeviceList();
        }
    }

    /**
     * 断开连接
     */
    private void disconnect() {
        mqttService.disconnect();
    }

    /**
     * 关闭连接
     */
    private void close() {
        mqttService.close();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
