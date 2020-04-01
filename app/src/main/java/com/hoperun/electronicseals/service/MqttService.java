package com.hoperun.electronicseals.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MqttService extends Service {
    private static final String TAG = "MqttService";

    private boolean canDoConnect = true;

    private MqttClient client;
    private MqttConnectOptions conOpt;
    private IMqttCallBack starMQTTCallBack;
    private ExecutorService sExecutorService= Executors.newFixedThreadPool(4);

    public class MqttBinder extends Binder implements IMqttRequest{
        @Override
        public void getDeviceList() {
            sExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected()) {
                        return;
                    }
                    String content = "00000000002";
                    System.out.println("Publishing message: " + content);
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(2);
                    try {
                        client.publish(requestDeviceInfoTopic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void getDeviceInfo(int deviceId) {
            sExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected()) {
                        return;
                    }
                    String content = "00000000002";
                    System.out.println("Publishing message: " + content);
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(2);
                    try {
                        client.publish(requestDeviceInfoTopic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void setMqttCallBack(IMqttCallBack iMqttCallBack) {
            starMQTTCallBack = iMqttCallBack;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MqttBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    public void publish(String topic, MqttMessage mqttMessage) {
        try {
            client.publish(topic, mqttMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String reciveDataTopic = "/smartseal/s2c/#";
    String requestDeviceListTopic = "/smartseal/c2s/devlist";
    String requestDeviceInfoTopic = "/smartseal/c2s/devinfo";
    String broker = "tcp://mq.tongxinmao.com:18831";
    String clientId = "Java testSubscribe";

    private void init() {
        MemoryPersistence persistence = new MemoryPersistence();
        // 服务器地址（协议+地址+端口号）
        try {
            client = new MqttClient(broker, clientId, persistence);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(true);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(30*1000);

        try {
            client.connect(conOpt);
            client.subscribe(reciveDataTopic,2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
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
}
