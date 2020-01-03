package com.quyuanjin.im2.greendao.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class User {
    @Id(autoincrement = true)
    private   Long id;
  private   String createTime;
    private   String name;
    private  String pwd;
    private  String phoneNumber;
    private  String sex;
    private   String portrait;
    private   String description;
    private   String token;
    private   String updateTime;



    @Generated(hash = 586692638)
    public User() {
    }
    @Generated(hash = 1546664684)
    public User(Long id, String createTime, String name, String pwd, String phoneNumber, String sex, String portrait, String description, String token, String updateTime) {
        this.id = id;
        this.createTime = createTime;
        this.name = name;
        this.pwd = pwd;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.portrait = portrait;
        this.description = description;
        this.token = token;
        this.updateTime = updateTime;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
