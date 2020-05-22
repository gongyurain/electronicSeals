package com.hoperun.electronicseals.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MqttService extends Service {
    private static final String TAG = "MqttService";
    private MqttClient client;
    private MqttConnectOptions conOpt;
    private List<IMqttCallBack> mqttCallBacks = new ArrayList<>();
    private static int TRHREAD_SIZE = 4;
    private ExecutorService sExecutorService= Executors.newFixedThreadPool(TRHREAD_SIZE);

    public class MqttBinder extends Binder implements IMqttRequest{
        @Override
        public void getDeviceList() {
            Log.d("gongyu", "getDeviceList" );
            sExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected()) {
                        Log.d("gongyu", "mqtt disconnected, ignore getDeviceList" );
                        return;
                    }
                    String content = "00000000002";
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(2);
                    try {
                        client.publish(requestDeviceListTopic, message);
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
                    String content = String.valueOf(deviceId);
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
           if (!mqttCallBacks.contains(iMqttCallBack)) {
               mqttCallBacks.add(iMqttCallBack);
           }
           Log.i(TAG, "mqtt callback size");
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
    String requestDeviceListTopic = "/smartseal/c2s/eventlist";
    String requestDeviceInfoTopic = "/smartseal/c2s/eventinfo";
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
        client.setCallback(mqttCallback);
//        client.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectionLost(Throwable cause) {
//                Log.e("gongyu", "Throwable:  " + cause);
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                Log.e("gongyu", "topic:  " + topic + "  message: " + message);
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//                Log.e("gongyu", "IMqttDeliveryToken:  " + token);
//            }
//
//            @Override
//            public void connectComplete(boolean reconnect, String serverURI) {
//                Log.e("gongyu", "reconnect:  " + reconnect + "  serverURI: " + serverURI);
//            }
//        });

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(true);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(5);

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
            Log.i(TAG, "messageArrived: " + msgContent);
            String detailLog = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
            Log.i(TAG, detailLog);
            for (IMqttCallBack mqttCallBack : mqttCallBacks) {
                if (mqttCallBack != null) {
                    mqttCallBack.messageArrived(topic, msgContent, message.getQos());
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            Log.i(TAG, "deliveryComplete");
            for (IMqttCallBack mqttCallBack : mqttCallBacks) {
                mqttCallBack.deliveryComplete(arg0);
            }
        }

        @Override
        public void connectionLost(Throwable arg0) {
            Log.e(TAG, "connectionLost");
            for (IMqttCallBack mqttCallBack : mqttCallBacks) {
                mqttCallBack.connectionLost(arg0);
            }
        }
    };
}
