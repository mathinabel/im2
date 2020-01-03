package com.quyuanjin.im2.netty.pojo;

public class RegisterWithUUID {
private String uuid;

    public RegisterWithUUID(String uuid) {
        this.uuid = uuid;
    }

    public RegisterWithUUID() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
