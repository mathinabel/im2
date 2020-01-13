package com.quyuanjin.im2.netty.pojo;

public class SendMessage {
    private String msg;
    private String uuid;
    private String sendID;
    private String receiveId;
    private String groupId;
    private String type;
    private String createTime;
    private String localPath;
    private String netPath;
    private String contentType;
    private float recorderTime;

    public SendMessage(float recorderTime, String msg, String uuid, String sendID, String receiveId, String groupId, String type, String createTime, String localPath, String netPath, String contentType) {
        this.recorderTime = recorderTime;
        this.msg = msg;
        this.uuid = uuid;
        this.sendID = sendID;
        this.receiveId = receiveId;
        this.groupId = groupId;
        this.type = type;
        this.createTime = createTime;
        this.localPath = localPath;
        this.netPath = netPath;
        this.contentType = contentType;
    }

    public String getSendID() {
        return sendID;
    }

    public float getRecorderTime() {
        return recorderTime;
    }

    public void setRecorderTime(float recorderTime) {
        this.recorderTime = recorderTime;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SendMessage() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
