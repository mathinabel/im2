package com.quyuanjin.im2.netty.pojo;

public class SucceedAddFriendHello {
    private String friendUUID;

    public SucceedAddFriendHello() {
    }

    public SucceedAddFriendHello(String friendUUID) {
        this.friendUUID = friendUUID;
    }

    public String getFriendUUID() {
        return friendUUID;
    }

    public void setFriendUUID(String friendUUID) {
        this.friendUUID = friendUUID;
    }
}
