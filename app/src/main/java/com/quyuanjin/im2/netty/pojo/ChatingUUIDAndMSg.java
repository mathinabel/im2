package com.quyuanjin.im2.netty.pojo;

public class ChatingUUIDAndMSg {
    private String toUUID;
    private String msg;

    public ChatingUUIDAndMSg() {
    }

    public ChatingUUIDAndMSg(String toUUID, String msg) {
        this.toUUID = toUUID;
        this.msg = msg;
    }

    public String getToUUID() {
        return toUUID;
    }

    public void setToUUID(String toUUID) {
        this.toUUID = toUUID;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
