package com.quyuanjin.im2.netty.eventbustest;

public class MessageEvent {

    private String message;
    private String UUID;
    public  MessageEvent(String message,String UUID){
        this.UUID=UUID;
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public String getUUID() {
        return UUID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
