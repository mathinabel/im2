package com.quyuanjin.im2.ac;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quyuanjin.im2.MainActivity;
import com.quyuanjin.im2.R;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.greendao.pojo.User;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.quyuanjin.im2.netty.helper.UUIDHelper;
import com.quyuanjin.im2.netty.pojo.RegisterWithUUID;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterTwo extends AppCompatActivity {
    private EditText phonenumber;
    private EditText editpassword;
    private EditText nickname;
    private TextView gologin;
    private Button registerbtn;

    private String phone;
    private String pwd;
    private String name;
    private int i = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register_two);
        phonenumber = this.findViewById(R.id.edit_phone);
        editpassword = this.findViewById(R.id.edit_password);
        nickname = this.findViewById(R.id.edit_name);

        gologin = findViewById(R.id.txt_go_login);
        registerbtn = findViewById(R.id.btn_submit);
        EventBus.getDefault().register(this);
        gotoLogin();
        gotoRegister();
    }

    private void gotoLogin() {
        gologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterTwo.this, LoginAc.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void gotoRegister() {
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phone = phonenumber.getText().toString();
                pwd = editpassword.getText().toString();
                name = nickname.getText().toString();
                if (phone.equals("") || pwd.equals("") || name.equals("")) {
                    ToastUtils.show(getApplicationContext(), "请正确填写内容");
                } else {
                    NettyLongChannel.sendRegisterMsg(name, pwd, phone);
                    //点击登录按钮之后显示转圈圈
                    showSweetDialog();
                }
            }


        });
    }  private void showSweetDialog() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event2(RegisterWithUUID registerWithUUID) {
        String uuid = registerWithUUID.getUuid();
        //grenndao存入user账户中

        User user = new User();
        user.setName(name);
        user.setPwd(pwd);
        user.setToken(uuid);
        user.setPhoneNumber(phone);

        App.getDaoSession().getUserDao().insert(user);

        SharedPreferencesUtils.setParam(RegisterTwo.this, "uuid", uuid);
        SharedPreferencesUtils.setParam(RegisterTwo.this, "isLogin",true);
        //保存完后转跳
        Log.d("123", "卡萨布兰卡3");
        Intent intent = new Intent(RegisterTwo.this, MainActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);

        this.startActivity(intent);
        finish();
        Log.d("123", "卡萨布兰卡4");
    }
}
