package com.hoperun.electronicseals.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

public class BleService {
    private static final String TAG = BleService.class.getName();

    private static BleService instance = new BleService();

    public static BleService getInstance(){
        return instance;
    }

    BluetoothClient mClient;
    SearchResult current;
    ArrayList<BleGattProfile> profiles = new ArrayList<>();

    public void init(Context context){
        if(mClient==null) {
            mClient = new BluetoothClient(context);
        }
    }

    public BluetoothClient getClient(){
        return mClient;
    }

    public void sycnData(){
        mClient.read(current.getAddress(), UUID.fromString("0xFFE1"), UUID.fromString(""), new BleReadResponse() {
            @Override
            public void onResponse(int code, byte[] data) {
                if (code == REQUEST_SUCCESS) {
                }
            }
        });
        mClient.write(current.getAddress(), UUID.fromString("0xFFE1"), UUID.fromString(""), "".getBytes(), new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {
                }
            }
        });
    }
}
