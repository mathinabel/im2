package com.quyuanjin.im2.netty.pojo;

import android.util.Log;

import java.util.Observable;

public class MessageTest extends Observable {
    private String head;
    private String payload;

    public String getHead() {
        return head;
    }

    public void setHead(String mhead) {
        this.head = mhead;

    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {

    }
}
