package com.quyuanjin.im2.netty.pojo;

public class SendMessage {
    private String msg;
    private String uuid;

    public SendMessage(String msg, String uuid) {
        this.msg = msg;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SendMessage() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
