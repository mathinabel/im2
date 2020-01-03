package com.quyuanjin.im2.netty.pojo;

public class AddFriend {

    private String fromuuid;
    private String touuid;

    public AddFriend() {
    }

    public AddFriend(String fromuuid, String touuid) {
        this.fromuuid = fromuuid;
        this.touuid = touuid;
    }

    public String getFromuuid() {
        return fromuuid;
    }

    public void setFromuuid(String fromuuid) {
        this.fromuuid = fromuuid;
    }

    public String getTouuid() {
        return touuid;
    }

    public void setTouuid(String touuid) {
        this.touuid = touuid;
    }
}
