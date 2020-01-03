package com.quyuanjin.im2.netty.eventbustest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.quyuanjin.im2.R;

import org.greenrobot.eventbus.EventBus;

public class SecondActivity extends AppCompatActivity {
    private Button mButton2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventbus_test_two);
        mButton2=findViewById(R.id.btn2);
        jumpActivity();
    }

    private void jumpActivity() {
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     EventBus.getDefault().post(new MessageEvent("欢迎大家浏览我写的博客"));
                finish();
            }
        });
    }
}
