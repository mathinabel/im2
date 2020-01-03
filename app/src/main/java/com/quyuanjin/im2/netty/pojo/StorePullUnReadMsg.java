package com.quyuanjin.im2.netty.pojo;

public class StorePullUnReadMsg {

    private String[] msg;

    public StorePullUnReadMsg(String[] msg) {
        this.msg = msg;
    }

    public String[] getMsg() {
        return msg;
    }

    public void setMsg(String[] msg) {
        this.msg = msg;
    }
}
