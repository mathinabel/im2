package com.quyuanjin.im2.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.quyuanjin.im2.MainActivity;
import com.quyuanjin.im2.R;
import com.quyuanjin.im2.greendao.greendaoacTest.GreenDaoAcTest;
import com.quyuanjin.im2.netty.helper.SPHelper;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.quyuanjin.im2.netty.helper.UUIDHelper;
import com.quyuanjin.im2.test.SqliteTestAc;

public class RegisterAc extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_ac);
        Button rgisterBtn =findViewById(R.id.register);
        rgisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPHelper.getInstance().init(getApplicationContext());
               SPHelper.getInstance().saveParam("myuuid", UUIDHelper.generateUUID());
               String selfuuid=(String) SPHelper.getInstance().getParam("myuuid",null);

               SharedPreferencesUtils.setParam(RegisterAc.this, "myuuid", UUIDHelper.generateUUID());
        //        SharedPreferencesUtils.setParam(RegisterAc.this, "int", 10);
        //        SharedPreferencesUtils.setParam(RegisterAc.this, "boolean", true);
         //       SharedPreferencesUtils.setParam(RegisterAc.this, "long", 100L);
        //        SharedPreferencesUtils.setParam(RegisterAc.this, "float", 1.1f);

                String s= (String) SharedPreferencesUtils.getParam(RegisterAc.this, "myuuid", "");
        //        Boolean s2= (Boolean) SharedPreferencesUtils.getParam(RegisterAc.this, "boolean", false);
        //       Long s3 = (Long) SharedPreferencesUtils.getParam(RegisterAc.this, "long", 0L);
        //        Float s4= (Float) SharedPreferencesUtils.getParam(RegisterAc.this, "float", 0.0f);

         //       ToastUtils.show(RegisterAc.this,s+s2+s3+s4 );
               Intent intent =new Intent(RegisterAc.this, MainActivity.class);
            startActivity(intent);
              finish();
            }
        });
        Button btn =findViewById(R.id.zhuantiao);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RegisterAc.this, SqliteTestAc.class);
                startActivity(intent);
                finish();
            }
        });

        Button btn2 =findViewById(R.id.greendaozhuantiao);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(RegisterAc.this, GreenDaoAcTest.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
