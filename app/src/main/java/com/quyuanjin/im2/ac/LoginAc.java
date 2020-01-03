package com.quyuanjin.im2.ac;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quyuanjin.im2.MainActivity;
import com.quyuanjin.im2.R;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.quyuanjin.im2.netty.helper.UUIDHelper;
import com.quyuanjin.im2.netty.pojo.SendLoginMsgBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginAc extends AppCompatActivity {
    private EditText phonenumber;
    private EditText editpassword;
    private TextView gotoregister;
    private Button login;

    private String phone;
    private String pwd;

    private int i = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);


        phonenumber = findViewById(R.id.edit_phone);
        editpassword = findViewById(R.id.edit_password);
        gotoregister = findViewById(R.id.txt_go_register);
        login = findViewById(R.id.btn_submit);

        EventBus.getDefault().register(this);
        gotoRegister();
        gotoLogin();
    }

    private void gotoLogin() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = phonenumber.getText().toString();
                pwd = editpassword.getText().toString();
                NettyLongChannel.sendLoginMsg(phone, pwd);
               //点击登录按钮之后显示转圈圈
                showSweetDialog();

            }
        });
    }

    private void showSweetDialog() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);
        new CountDownTimer(800 * 7, 800) {
            public void onTick(long millisUntilFinished) {
                // you can change the progress bar color by ProgressHelper every 800 millis
                i++;
                switch (i) {
                    case 0:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                i = -1;
                pDialog.setTitleText("哪里出了问题!请检查后重新登录。")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
            }
        }.start();
    }

    private void gotoRegister() {
        gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginAc.this, RegisterTwo.class);
                startActivity(intent);

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event1(SendLoginMsgBack sendLoginMsgBack) {

        SharedPreferencesUtils.setParam(LoginAc.this, "uuid", sendLoginMsgBack.getSelfuuid());
        SharedPreferencesUtils.setParam(LoginAc.this, "isLogin",true);
        Intent intent = new Intent(LoginAc.this, MainActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
        finish();
    }

}
