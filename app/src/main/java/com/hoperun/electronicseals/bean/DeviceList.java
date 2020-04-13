package com.hoperun.electronicseals.bean;

public class DeviceList {

    /**
     * id : 1
     * SN : 864480040662891
     * TY : null
     * EV : 1
     * VER : null
     * DATA : null
     * GMT : 1586662956000
     */

    private int id;
    private String SN;
    private Object TY;
    private String EV;
    private Object VER;
    private Object DATA;
    private long GMT;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public Object getTY() {
        return TY;
    }

    public void setTY(Object TY) {
        this.TY = TY;
    }

    public String getEV() {
        return EV;
    }

    public void setEV(String EV) {
        this.EV = EV;
    }

    public Object getVER() {
        return VER;
    }

    public void setVER(Object VER) {
        this.VER = VER;
    }

    public Object getDATA() {
        return DATA;
    }

    public void setDATA(Object DATA) {
        this.DATA = DATA;
    }

    public long getGMT() {
        return GMT;
    }

    public void setGMT(long GMT) {
        this.GMT = GMT;
    }
}
