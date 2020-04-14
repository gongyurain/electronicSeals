package com.hoperun.electronicseals.bean;

import java.io.Serializable;

public class DeviceEventDetailResp implements Serializable {
    private int id;         //event id
    private String sn;      //device sn
    private long time;      //occur time
    private String addr;    // device addr
    private String type;    // event type
    private String creator;
    private String sealReason;
    private long sealTime;
    private String unsealMethod;
    private long unsealTime;
    private String targetInfo;
    private long createTime;
    private long invalidTime;

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSealReason() {
        return sealReason;
    }

    public void setSealReason(String sealReason) {
        this.sealReason = sealReason;
    }

    public long getSealTime() {
        return sealTime;
    }

    public void setSealTime(long sealTime) {
        this.sealTime = sealTime;
    }

    public String getUnsealMethod() {
        return unsealMethod;
    }

    public void setUnsealMethod(String unsealMethod) {
        this.unsealMethod = unsealMethod;
    }

    public long getUnsealTime() {
        return unsealTime;
    }

    public void setUnsealTime(long unsealTime) {
        this.unsealTime = unsealTime;
    }

    public String getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(String targetInfo) {
        this.targetInfo = targetInfo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(long invalidTime) {
        this.invalidTime = invalidTime;
    }

    @Override
    public String toString() {
        return "DeviceEventDetailResp{" +
                "id=" + id +
                ", sn='" + sn + '\'' +
                ", time=" + time +
                ", addr='" + addr + '\'' +
                ", type='" + type + '\'' +
                ", creator='" + creator + '\'' +
                ", sealReason='" + sealReason + '\'' +
                ", sealTime=" + sealTime +
                ", unsealMethod='" + unsealMethod + '\'' +
                ", unsealTime=" + unsealTime +
                ", targetInfo='" + targetInfo + '\'' +
                ", createTime=" + createTime +
                ", invalidTime=" + invalidTime +
                '}';
    }
}
