package com.quyuanjin.im2.netty.helper;

import java.util.UUID;

public class UUIDHelper {

    public static String generateUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        return uuid;
    }
}
