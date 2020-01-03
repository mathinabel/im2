package com.quyuanjin.im2.greendao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Message {
    @Id(autoincrement = true)
    private   Long id;
    private  String sendID;
    private   String receiveId;
    private    String groupId;
    private   String type;
    private   String msg;
    private   String createTime;
    @Generated(hash = 746480599)
    public Message(Long id, String sendID, String receiveId, String groupId, String type, String msg, String createTime) {
        this.id = id;
        this.sendID = sendID;
        this.receiveId = receiveId;
        this.groupId = groupId;
        this.type = type;
        this.msg = msg;
        this.createTime = createTime;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSendID() {
        return sendID;
    }

    public void setSendID(String sendID) {
        this.sendID = sendID;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
