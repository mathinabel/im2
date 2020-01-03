package com.quyuanjin.im2.netty.pojo;

public class SucceedAddFriend {
    private String friendUUID;

    public SucceedAddFriend() {
    }

    public SucceedAddFriend(String friendUUID) {
        this.friendUUID = friendUUID;
    }

    public String getFriendUUID() {
        return friendUUID;
    }

    public void setFriendUUID(String friendUUID) {
        this.friendUUID = friendUUID;
    }
}
