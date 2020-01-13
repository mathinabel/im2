package com.quyuanjin.im2.greendao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Message {
    @Id(autoincrement = true)
    private Long id;
    private String sendID;
    private String receiveId;
    private String groupId;
    private String type;
    private String msg;
    private String createTime;
    private String localPath;
    private String netPath;
    private String contentType;
    private float recorderTime;
private String sendSucceedType;
@Generated(hash = 1991741511)
public Message(Long id, String sendID, String receiveId, String groupId, String type, String msg, String createTime, String localPath, String netPath, String contentType, float recorderTime, String sendSucceedType) {
    this.id = id;
    this.sendID = sendID;
    this.receiveId = receiveId;
    this.groupId = groupId;
    this.type = type;
    this.msg = msg;
    this.createTime = createTime;
    this.localPath = localPath;
    this.netPath = netPath;
    this.contentType = contentType;
    this.recorderTime = recorderTime;
    this.sendSucceedType = sendSucceedType;
}

    @Generated(hash = 637306882)
    public Message() {
    }

    public String getSendSucceedType() {
        return sendSucceedType;
    }

    public void setSendSucceedType(String sendSucceedType) {
        this.sendSucceedType = sendSucceedType;
    }

    public float getRecorderTime() {
        return recorderTime;
    }

    public void setRecorderTime(float recorderTime) {
        this.recorderTime = recorderTime;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
