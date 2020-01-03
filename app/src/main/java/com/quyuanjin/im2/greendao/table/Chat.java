package com.quyuanjin.im2.greendao.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Chat {
    @Id(autoincrement = true)
    Long id;
    String fromuuid;
    String touuid;
    String msg;
    @Generated(hash = 519536279)
    public Chat() {
    }
    @Generated(hash = 148174519)
    public Chat(Long id, String fromuuid, String touuid, String msg) {
        this.id = id;
        this.fromuuid = fromuuid;
        this.touuid = touuid;
        this.msg = msg;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromuuid() {
        return fromuuid;
    }

    public void setFromuuid(String fromuuid) {
        this.fromuuid = fromuuid;
    }

    public String getTouuid() {
        return touuid;
    }

    public void setTouuid(String touuid) {
        this.touuid = touuid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
