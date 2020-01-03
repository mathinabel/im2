package com.quyuanjin.im2.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.quyuanjin.im2.R;

public class ContractDetailAc extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_detail);
        Button chatterton =findViewById(R.id.chatButton);
        //todo 通过传过来的ReceiveUUID  并以此从数据库friends表中寻找该用户的其他信息并展示
        //todo 展示界面的xml
        //todo 如果有网络，从网络获取该uuid的信息并（与之比较）更新


        chatterton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ContractDetailAc.this,ChatAc.class);
                //todo 继续传递uuid
                intent.putExtra("receiveUUID",getIntent().getStringExtra("receiveUUID"));
                startActivity(intent);
                finish();
            }
        });

    }
}
