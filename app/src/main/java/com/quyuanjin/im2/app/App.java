package com.quyuanjin.im2.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.quyuanjin.im2.constant.ProtoConstant;

import com.quyuanjin.im2.greendao.pojo.DaoMaster;
import com.quyuanjin.im2.greendao.pojo.DaoSession;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.NetUtils;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.pojo.SendLoginMsgBack;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

import io.netty.util.NetUtil;

public class App extends Application {


    private static DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils
        x.Ext.init(this);

        String uuid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uuid", "");
        if (NetUtils.isConnected(this) && uuid != null) {
            if (NettyLongChannel.initNetty()) {
                NettyLongChannel.sendAndReflash(ProtoConstant.LONG_CONNECT, uuid + "\r\n");
            } else {
                Toast.makeText(getApplicationContext(), "服务器挂了", Toast.LENGTH_SHORT).show();
            }
        }

        /*
         * 初始化GreenDao,直接在Application中进行初始化操作
         */


        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "im.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Reconnect(SendLoginMsgBack sendLoginMsgBack) {
        String uuid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uuid", "");
        if (NetUtils.isConnected(this) && uuid != null) {
            if (NettyLongChannel.initNetty()) {
                NettyLongChannel.sendAndReflash(ProtoConstant.LONG_CONNECT, uuid + "\r\n");
            } else {
                Toast.makeText(getApplicationContext(), "服务器挂了", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
