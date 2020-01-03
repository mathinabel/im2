package com.quyuanjin.im2.greendao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class UnReadAddFriendsMsg {
    @Id(autoincrement = true)
    private    Long id;
    private  String friendUUId;
    private  String createTime;
    private  String name;
    private  String phoneNumber;
    private   String sex;
    private   String portrait;
    private   String description;
    private   String updateTime;
    private  int state;
    @Generated(hash = 1112649063)
    public UnReadAddFriendsMsg(Long id, String friendUUId, String createTime, String name, String phoneNumber, String sex, String portrait, String description, String updateTime, int state) {
        this.id = id;
        this.friendUUId = friendUUId;
        this.createTime = createTime;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.portrait = portrait;
        this.description = description;
        this.updateTime = updateTime;
        this.state = state;
    }
    @Generated(hash = 300344079)
    public UnReadAddFriendsMsg() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFriendUUId() {
        return friendUUId;
    }

    public void setFriendUUId(String friendUUId) {
        this.friendUUId = friendUUId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
