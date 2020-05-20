package com.hoperun.electronicseals.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.hoperun.electronicseals.bean.DeviceEventResp;
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

    public static BleService getInstance() {
        return instance;
    }

    BluetoothClient mClient;
    ArrayList<BleGattProfile> profiles = new ArrayList<>();

    public void init(Context context) {
        if (mClient == null) {
            mClient = new BluetoothClient(context);
        }
    }

    public BluetoothClient getClient() {
        return mClient;
    }

    List<DeviceEventResp> eventList = new ArrayList<>();

    public interface Listener {
        void onDeviceEvent(DeviceEventResp event);
    }

    private List<Listener> listeners = new ArrayList<>();

    public void registerListener(Listener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListener(DeviceEventResp event) {
        for (Listener listener : listeners) {
            if (listeners.contains(listener)) {
                listener.onDeviceEvent(event);
            }
        }
    }

    SearchResult current;
    BleGattProfile profile;
    boolean syncing = false;

    private UUID getUUID(String number){
        return UUID.fromString(String.format("0000%4s-0000-1000-8000-00805F9B34FB", number));
    }

    Runnable syncRunnable = () -> mClient.read(
            current.getAddress(), getUUID("FFE1"), getUUID("C001"),
//            current.getAddress(), getUUID("1800"), getUUID("2A00"),
            (code, data) -> {
                if (code == REQUEST_SUCCESS) {
                    Log.d(TAG, "read data:" + new String(data));
                    DeviceEventResp event = new DeviceEventResp(); //TODO parse data to event
                    Log.d(TAG, "convert to event:" + event);
                    eventList.add(event);
                    notifyListener(event);
                    new Handler().postDelayed(this::doSycn, 1000);
                }
            });

    public void sycnData(SearchResult result, BleGattProfile profile) {
        Log.d(TAG, "sycnData");
        current = result;
        this.profile = profile;
        syncing = true;
        doSycn();
    }

    private void doSycn() {
        if (syncing) {
            Log.d(TAG, "post sync runnable for read");
            new Handler().post(syncRunnable);
            syncing = false;
        }
//        mClient.write(current.getAddress(), UUID.fromString("0xFFE1"), UUID.fromString(""), "".getBytes(), new BleWriteResponse() {
//            @Override
//            public void onResponse(int code) {
//                if (code == REQUEST_SUCCESS) {
//
//                }
//            }
//        });
    }
}
