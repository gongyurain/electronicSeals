package com.hoperun.electronicseals.service;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.hoperun.electronicseals.bean.DeviceEventResp;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

public class BleService {
    private static final String TAG = BleService.class.getName();

    private static BleService instance = new BleService();

    public static BleService getInstance() {
        return instance;
    }

    Context context;
    BluetoothClient mClient;
    ArrayList<BleGattProfile> profiles = new ArrayList<>();

    public void init(Context context) {
        if (mClient == null) {
            this.context = context;
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

    private UUID getUUID(String number) {
        return UUID.fromString(String.format("0000%4s-0000-1000-8000-00805F9B34FB", number));
    }

    Runnable readRunnable = () -> mClient.read(
            current.getAddress(), getUUID("FFE1"), getUUID("C001"),
//            current.getAddress(), getUUID("1800"), getUUID("2A00"),
            (code, data) -> {
                if (code == REQUEST_SUCCESS) {
                    Log.d(TAG, "read data:" + new String(data));
                    DeviceEventResp event = new DeviceEventResp(); //TODO parse data to event
                    Log.d(TAG, "convert to event:" + event);
                    eventList.add(event);
                    notifyListener(event);
                }
            });

    public void readNotify(SearchResult result, BleGattProfile profile) {
        Log.d(TAG, "readNotify");
        current = result;
        this.profile = profile;
        mClient.notify(
//                result.getAddress(), getUUID("FFE1"), getUUID("C001"),
                result.getAddress(), getUUID("180D"), getUUID("2A37"),
                new BleNotifyResponse() {
                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {
                        Log.d(TAG, "onNotify value: " + new String(value));
                        Toast.makeText(context, new String(value), Toast.LENGTH_SHORT).show();
                        DeviceEventResp event = new DeviceEventResp(); //TODO parse data to event
                        Log.d(TAG, "convert to event:" + event);
                        eventList.add(event);
                        notifyListener(event);
                    }

                    @Override
                    public void onResponse(int code) {
                    }
                });
    }
}
