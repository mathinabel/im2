package com.quyuanjin.im2.msg;

public class Msg {

    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;

    public static final int TYPE_TEXT=2;
    public static final int TYPE_RECORDER=3;


    public static final int TYPE_SEND_SUCCESS=4;
    public static final int TYPE_SEND_FAILED=5;
    private String content;
    private int type;
    private int contentType;
    public Msg(String content,int send_type,int content_type){
        this.content=content;
        this.type=send_type;
        this.contentType=content_type;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }

}
