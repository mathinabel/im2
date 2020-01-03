package com.quyuanjin.im2.ac;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.Toast;

import com.quyuanjin.im2.MainActivity;
import com.quyuanjin.im2.R;
import com.quyuanjin.im2.helputils.CProgressDialogUtils;
import com.quyuanjin.im2.helputils.OkGoUpdateHttpUtil;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.NetUtils;
import com.quyuanjin.im2.netty.helper.SPHelper;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.quyuanjin.im2.R.drawable.splash;

public class SplashAc extends AppCompatActivity {

    ImageView splash_imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_ac);
        splash_imageView = findViewById(R.id.splash);
        Random random = new Random();
        int num = random.nextInt(5);
        switch (num) {
            case 0:
                splash_imageView.setBackgroundResource(R.drawable.splash3);
                break;
            case 1:
                splash_imageView.setBackgroundResource(R.drawable.splash4);
                break;
            case 2:
                splash_imageView.setBackgroundResource(R.drawable.splash3);
                break;
            case 3:
                splash_imageView.setBackgroundResource(R.drawable.splash4);
                break;
            case 4:
                splash_imageView.setBackgroundResource(R.drawable.splash5);
                break;

            default:
                splash_imageView.setBackgroundResource(R.drawable.splash6);
                break;
        }
        //   ImageView splash_imageView = findViewById(R.id.splash);
        checkNetWork();

        checkIsFirstStart();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(200);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(alphaAnimation);

        splash_imageView.setAnimation(animationSet);
        animationSet.setRepeatCount(1);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //检查是否第一次登陆

                checkIsLogin();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void checkIsLogin() {
        boolean islogin = (Boolean) SharedPreferencesUtils.getParam(SplashAc.this, "isLogin", false);
        String selfuuid = (String) SharedPreferencesUtils.getParam(this, "uuid", "");
        if (islogin) {
            //如果已登录过，从服务器拉取未读信息
            NettyLongChannel.sendPullUnreadMsg(selfuuid);
            //还要拉取未添加好友信息
            NettyLongChannel.sendPullUnreadAddFriendMsg(selfuuid);
            Intent intent = new Intent(SplashAc.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashAc.this, LoginAc.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkIsFirstStart() {


    }


    private void checkNetWork() {


    }

    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d("TAG", "当前版本名称：" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    private void initNet() {

        boolean connected = NetUtils.isConnected(SplashAc.this);
        if (connected) {
            boolean wifi = NetUtils.isWifi(SplashAc.this);
            boolean rd = NetUtils.is3rd(SplashAc.this);
            if (wifi) {
                ToastUtils.show(SplashAc.this, "WIFI已经连接");
            } else if (rd) {
                ToastUtils.show(SplashAc.this, "手机流量已经连接");
            } else {
                ToastUtils.show(SplashAc.this, "网络连接不可用，请检查网络设置");
                NetUtils.openSetting(SplashAc.this);
            }
        } else {
            ToastUtils.show(SplashAc.this, "网络连接不可用，请检查网络设置");
            NetUtils.openSetting(SplashAc.this);
        }
    }

}

