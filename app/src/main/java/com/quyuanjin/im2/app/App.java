package com.quyuanjin.im2.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;
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

import java.io.File;

import io.netty.util.NetUtil;

public class App extends Application {


    private static DaoSession daoSession;

    private static App mApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化xutils
        x.Ext.init(this);
        mApplication = this;
        final String uuid = (String) SharedPreferencesUtils.getParam(getApplicationContext(), "uuid", "");


        final Thread t = new Thread(new Runnable() {
            public void run() {


                if (NetUtils.isConnected(App.this) && uuid != null) {
                    Log.d("net", "执行了sConnected");

                    if (NettyLongChannel.initNetty()) {
                        try {
                            NettyLongChannel.sendAndReflash(ProtoConstant.LONG_CONNECT, uuid + "\r\n");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                    }
                }

            }
        });
        t.start();



        /*
         * 初始化GreenDao,直接在Application中进行初始化操作
         */


        initGreenDao();

        /*
        初始化okhttp帮助类
         */
        initOkHttp();
    }

    private void initOkHttp() {
        String downloadFileDir = Environment.getExternalStorageDirectory().getPath()+"/okHttp_download/";
        String cacheDir = Environment.getExternalStorageDirectory().getPath()+"/okHttp_cache";
        OkHttpUtil.init(getContext())
                .setConnectTimeout(15)//连接超时时间
                .setWriteTimeout(15)//写超时时间
                .setReadTimeout(15)//读超时时间
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setHttpLogTAG("HttpLog")//设置请求日志标识
                .setIsGzip(false)//Gzip压缩，需要服务端支持
                .setShowHttpLog(true)//显示请求日志
                .setShowLifecycleLog(false)//显示Activity销毁日志
            //    .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setCachedDir(new File(cacheDir))//设置缓存目录
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .setResponseEncoding(Encoding.UTF_8)//设置全局的服务器响应编码
                .setRequestEncoding(Encoding.UTF_8)//设置全局的请求参数编码
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getContext())))//持久化cookie
                .build();

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
    public static App getContext() {
        return mApplication;
    }
}
