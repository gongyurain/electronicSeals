package com.hoperun.electronicseals.service;

public interface IMqttRequest {
    public void getDeviceList();

    public void getDeviceInfo(int deviceId);
}
