package com.hoperun.electronicseals.view.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.api.ServiceContract;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.service.IMqttCallBack;
import com.hoperun.electronicseals.service.MqttService;
import com.hoperun.electronicseals.view.fragment.SearchFragment;
import com.hoperun.electronicseals.view.fragment.UserFragment;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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

    @Override
    public void initData() {
        buildEasyMqttService();
        connect();
        if (isConnected()) {
            Log.e(TAG, "isconnmection");
            for (int i = 0; i < 1; i++) {
                String content = "00000000002";
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(2);
                publish(ServiceContract.DEV_INFO, message);
            }
        }
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

    /**
     * 连接Mqtt服务器
     */
    private void connect() {
        mqttService.connect(new IMqttCallBack() {
            @Override
            public void messageArrived(String topic, String message, int qos) {
                //推送消息到达
                Log.e(TAG, "message= " + message);
            }
            @Override
            public void connectionLost(Throwable arg0) {
                //连接断开
                try {
                    Log.e(TAG + "connectionLost", arg0.toString());
                } catch (Exception e) {

                } finally {

                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken arg0) {
                Log.e(TAG + "@deliveryComplete", "发送完毕" + arg0.toString());
            }
            @Override
            public void connectSuccess(IMqttToken arg0) {
                Toast.makeText(MainActivity.this, "连接成功！", Toast.LENGTH_LONG).show();
                Log.e(TAG + "@connectSuccess", "success");
                subscribe();
            }
            @Override
            public void connectFailed(IMqttToken arg0, Throwable arg1) {
                //连接失败
                Log.e(TAG + "@connectFailed", "fail" + arg1.getMessage());
            }
        });
    }

    /**
     * 订阅主题
     */
    private void subscribe() {
        String[] topics = new String[]{ServiceContract.DEV_INFO};
        //主题对应的推送策略 分别是0, 1, 2 建议服务端和客户端配置的主题一致
        // 0 表示只会发送一次推送消息 收到不收到都不关心
        // 1 保证能收到消息，但不一定只收到一条
        // 2 保证收到切只能收到一条消息
        int[] qoss = new int[]{2};
        mqttService.subscribe(topics, qoss);
    }

    private void buildEasyMqttService() {
        mqttService = new MqttService.Builder()
                //设置自动重连
                .autoReconnect(true)
                //设置清除回话session  true(false) 不收(收)到服务器之前发出的推送消息
                .cleanSession(true)
                .clientId(ServiceContract.CLIENT_ID)
                //mqtt服务器地址 格式例如：
                .serverUrl(ServiceContract.BROKER)
                //心跳包默认的发送间隔
                .keepAliveInterval(20)
                //构建MqttService
                .bulid(this.getApplicationContext());
    }

    /**
     * 判断服务是否连接
     */
    private boolean isConnected() {
        return mqttService.isConnected();
    }

    /**
     * 发布消息
     */
    private void publish(String topic, MqttMessage mqttMessage) {
        mqttService.publish(topic, mqttMessage);
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
}
