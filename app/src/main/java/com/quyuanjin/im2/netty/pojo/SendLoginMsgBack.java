package com.quyuanjin.im2.netty.pojo;

public class SendLoginMsgBack {
  private  String selfuuid;

    public SendLoginMsgBack() {
    }

    public SendLoginMsgBack(String selfuuid) {
        this.selfuuid = selfuuid;
    }

    public String getSelfuuid() {
        return selfuuid;
    }

    public void setSelfuuid(String selfuuid) {
        this.selfuuid = selfuuid;
    }
}
