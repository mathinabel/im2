package com.quyuanjin.im2.netty.pojo;

public class SucceedAddFriendHelloTwo {
    private String friendUUID;

    public SucceedAddFriendHelloTwo() {
    }

    public SucceedAddFriendHelloTwo(String friendUUID) {
        this.friendUUID = friendUUID;
    }

    public String getFriendUUID() {
        return friendUUID;
    }

    public void setFriendUUID(String friendUUID) {
        this.friendUUID = friendUUID;
    }
}
