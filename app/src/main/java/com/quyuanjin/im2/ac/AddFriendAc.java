package com.quyuanjin.im2.ac;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quyuanjin.im2.R;
import com.quyuanjin.im2.adapter.AddFriendAcAdapter;
import com.quyuanjin.im2.adapter.MsgAdapter;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.greendao.pojo.UnReadAddFriendsMsg;
import com.quyuanjin.im2.msg.Msg;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.pojo.AddFriend;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriend;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriendHello;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class AddFriendAc extends AppCompatActivity {

    EditText editText;
    LinearLayout linearLayout;
    TextView textView;
    String text;

    AddFriendAcAdapter addFriendAcAdapter;
    ListView addFriendAcListView;
    List<UnReadAddFriendsMsg> uuidList = new ArrayList<>();
    Context context;
    String selfuuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_ac);
        //  EventBus.getDefault().register(this);
        editText = this.findViewById(R.id.search_text);
        linearLayout = this.findViewById(R.id.search_result);
        textView = this.findViewById(R.id.search_text_view);

        addFriendAcListView = this.findViewById(R.id.addFriendAcLV);

        selfuuid = (String) SharedPreferencesUtils.getParam(AddFriendAc.this, "uuid", "");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                linearLayout.setVisibility(View.VISIBLE);
                textView.setText(charSequence);
                text = textView.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddFriendAc.this, AddFriendDetailAc.class);
                intent.putExtra("searchmsg", text);
                startActivity(intent);
            }
        });
        initDate();
        initAFListView();
    }

    private void initDate() {


        //查找数据库 未加好友状态
        List<UnReadAddFriendsMsg> unReadAddFriendsMsgList = App.getDaoSession().getUnReadAddFriendsMsgDao().queryBuilder().build().list();

        if (unReadAddFriendsMsgList != null) {
            if (unReadAddFriendsMsgList.size() != 0) {
                uuidList.addAll(unReadAddFriendsMsgList);

            }
        }
    }

    private void initAFListView() {

        addFriendAcAdapter = new AddFriendAcAdapter(AddFriendAc.this, uuidList);

        addFriendAcListView.setAdapter(addFriendAcAdapter);


    }
}
