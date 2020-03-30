package com.hoperun.electronicseals.api;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ApiManager {
    private static ApiManager apiManager = null;

    MemoryPersistence persistence = new MemoryPersistence();

    int qos = 2;

    private ApiManager() {};

    public static ApiManager getInstance() {
        if (apiManager == null) {
            synchronized (ApiManager.class) {
                if (apiManager == null) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;
    }

    public void getDeviceInfo() {
        try {
            MqttClient client = new MqttClient(ServiceContract.BROKER, ServiceContract.CLIENT_ID, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + ServiceContract.BROKER);
            client.connect(connOpts);
            System.out.println("Connected");
            for (int i = 0; i < 1; i++) {
                String content = "00000000002";
                System.out.println("Publishing message: " + content);
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(qos);
                client.publish(ServiceContract.DEV_INFO, message);
            }
            System.out.println("Message published");
            client.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

}
