package com.hoperun.electronicseals.bean;

import java.io.Serializable;

public class DeviceEventResp implements Serializable {
    private int id;
    private String sn;
    private long time;
    private String addr;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DeviceEventResp{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", time=" + time +
                ", addr='" + addr + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
