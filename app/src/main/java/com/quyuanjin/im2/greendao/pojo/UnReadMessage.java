package com.quyuanjin.im2.greendao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class UnReadMessage {
    @Id(autoincrement = true)
    private    Long id;
    private   String receiverId;
    private  String senderId;
    private   String msg;
    private   String sendTime;
    private    String receiveTime;
    @Generated(hash = 171615722)
    public UnReadMessage() {
    }
    @Generated(hash = 404367117)
    public UnReadMessage(Long id, String receiverId, String senderId, String msg, String sendTime, String receiveTime) {
        this.id = id;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.msg = msg;
        this.sendTime = sendTime;
        this.receiveTime = receiveTime;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
}
