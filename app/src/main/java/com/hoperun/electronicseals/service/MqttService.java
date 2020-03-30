package com.hoperun.electronicseals.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttService extends Service {
    private static final String TAG = "MqttService";

    private boolean canDoConnect = true;

    private MqttClient client;

    private MqttConnectOptions conOpt;

    private Context context;
    private String serverUrl = "";
    private String userName = "admin";
    private String passWord = "admin";
    private String clientId = "";
    private int timeOut = 10;
    private int keepAliveInterval = 20;
    private boolean retained = false;
    private boolean cleanSession = false;
    private boolean autoReconnect = true;
    private IMqttCallBack starMQTTCallBack;


    public MqttService(){

    }

    /**
     * builder设计模式
     *
     * @param builder
     */
    private MqttService(Builder builder) {
        this.context = builder.context;
        this.serverUrl = builder.serverUrl;
        this.userName = builder.userName;
        this.passWord = builder.passWord;
        this.clientId = builder.clientId;
        this.timeOut = builder.timeOut;
        this.keepAliveInterval = builder.keepAliveInterval;
        this.retained = builder.retained;
        this.cleanSession = builder.cleanSession;
        this.autoReconnect = builder.autoReconnect;
        init();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * Builder 构造类
     */
    public static final class Builder {

        private Context context;
        private String serverUrl;
        private String userName = "admin";
        private String passWord = "password";
        private String clientId;
        private int timeOut = 10;
        private int keepAliveInterval = 20;
        private boolean retained = false;
        private boolean cleanSession = false;
        private boolean autoReconnect = false;

        public Builder serverUrl(String serverUrl) {
            this.serverUrl = serverUrl;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder passWord(String passWord) {
            this.passWord = passWord;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder timeOut(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public Builder keepAliveInterval(int keepAliveInterval) {
            this.keepAliveInterval = keepAliveInterval;
            return this;
        }

        public Builder retained(boolean retained) {
            this.retained = retained;
            return this;
        }

        public Builder autoReconnect(boolean autoReconnect) {
            this.autoReconnect = autoReconnect;
            return this;
        }

        public Builder cleanSession(boolean cleanSession) {
            this.cleanSession = cleanSession;
            return this;
        }

        public MqttService bulid(Context context) {
            this.context = context;
            return new MqttService(this);
        }
    }


    public void publish(String topic, MqttMessage mqttMessage) {
        try {
            client.publish(topic, mqttMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        MemoryPersistence persistence = new MemoryPersistence();
        // 服务器地址（协议+地址+端口号）
        try {
            client = new MqttClient(serverUrl, clientId, persistence);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(cleanSession);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(keepAliveInterval);
    }

    /**
     * 关闭客户端
     */
    public void close() {
        try {
            client.close();
        } catch (Exception e) {
    }
    }

    /**
     * 连接MQTT服务器
     */
    public void connect(IMqttCallBack starMQTTCallBack) {
        this.starMQTTCallBack = starMQTTCallBack;
        if (canDoConnect && !client.isConnected()) {
            try {
                client.connect(conOpt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 订阅主题
     *
     * @param topics 主题
     * @param qos    策略
     */
    public void subscribe(String[] topics, int[] qos) {
        try {
            // 订阅topic话题
            Log.i(TAG, "execute subscribe -- qos = " + qos.toString());
            client.subscribe(topics, qos);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    /**
     * 断开连接
     */
    public void disconnect(){
        try {
            client.disconnect();
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
    }

    /**
     * 判断连接是否断开
     */
    public boolean isConnected(){
        try {
            return client.isConnected();
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        return false;
    }

    /**
     *  MQTT是否连接成功
     */
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "mqtt connect success ");
            if (starMQTTCallBack != null) {
                starMQTTCallBack.connectSuccess(arg0);
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            Log.i(TAG, "mqtt connect failed ");
            if (starMQTTCallBack != null) {
                starMQTTCallBack.connectFailed(arg0, arg1);
            }
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

            String msgContent = new String(message.getPayload());
            String detailLog = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
            Log.e(TAG, "messageArrived:" + msgContent);
            Log.e(TAG, detailLog);
            if (starMQTTCallBack != null) {
                starMQTTCallBack.messageArrived(topic, msgContent, message.getQos());
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            if (starMQTTCallBack != null) {
                starMQTTCallBack.deliveryComplete(arg0);
            }
            Log.i(TAG, "deliveryComplete");
        }

        @Override
        public void connectionLost(Throwable arg0) {
            if (starMQTTCallBack != null) {
                starMQTTCallBack.connectionLost(arg0);
            }
            Log.e(TAG, "connectionLost");
            // 失去连接，重连
        }
    };

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT current network name：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT no network");
            return false;
        }
    }
}
