package com.quyuanjin.im2.ac;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.quyuanjin.im2.MainActivity;
import com.quyuanjin.im2.R;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddFriendDetailAc extends AppCompatActivity {

    TextView textView;
    Button addFriend;
    String selfuuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_frined_detail_ac);
        final String searchmsg = getIntent().getStringExtra("searchmsg");
        textView = findViewById(R.id.nameTextView);
        addFriend = findViewById(R.id.addfriend);
        textView.setText(searchmsg);
        selfuuid = (String) SharedPreferencesUtils.getParam(this, "uuid", "");

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 网络发送加好友消息
                NettyLongChannel.sendAddFriendRequest(selfuuid,searchmsg);

                //提示
                new SweetAlertDialog(AddFriendDetailAc.this).setTitleText("发送成功")
                        .setContentText("等待对方回应:内容为"+searchmsg).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finish();
                    }
                })
                        .show();


                //  Intent intent =new Intent(AddFriendDetailAc.this, MainActivity.class);
                //   startActivity(intent);
                //  finish();
            }
        });
    }
}
