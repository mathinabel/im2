package com.quyuanjin.im2.greendao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Chating {
    @Id(autoincrement = true)
    private  Long id;
    private   String friendUUId;
    private  String createTime;
    private  String name;
    private  String portrait;
    private  String newestMsg;
    private  String updateTime;
    @Generated(hash = 922048049)
    public Chating() {
    }
    @Generated(hash = 1536703978)
    public Chating(Long id, String friendUUId, String createTime, String name, String portrait, String newestMsg, String updateTime) {
        this.id = id;
        this.friendUUId = friendUUId;
        this.createTime = createTime;
        this.name = name;
        this.portrait = portrait;
        this.newestMsg = newestMsg;
        this.updateTime = updateTime;
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

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getNewestMsg() {
        return newestMsg;
    }

    public void setNewestMsg(String newestMsg) {
        this.newestMsg = newestMsg;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
