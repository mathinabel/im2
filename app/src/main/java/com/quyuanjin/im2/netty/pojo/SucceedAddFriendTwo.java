package com.quyuanjin.im2.netty.pojo;

public class SucceedAddFriendTwo {
    private String friendUUID;



    public SucceedAddFriendTwo() {
    }

    public SucceedAddFriendTwo(String friendUUID) {
        this.friendUUID = friendUUID;
    }
    public String getFriendUUID() {
        return friendUUID;
    }

    public void setFriendUUID(String friendUUID) {
        this.friendUUID = friendUUID;
    }
}
