package com.quyuanjin.im2.ac;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.layout.QMUIButton;
import com.quyuanjin.im2.R;
import com.quyuanjin.im2.adapter.ChatAcAdapter;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.greendao.pojo.Chating;
import com.quyuanjin.im2.greendao.pojo.ChatingDao;
import com.quyuanjin.im2.greendao.pojo.Message;
import com.quyuanjin.im2.greendao.pojo.MessageDao;
import com.quyuanjin.im2.msg.Msg;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.quyuanjin.im2.netty.pojo.SendMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class ChatAc extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private ChatAcAdapter adapter;
    private Context context;
    Handler handler = new Handler();
    private TextView mTextView;
    private QMUIButton qmuiButton;

    //获取自身uuid

    //String[] testuser ={"123","321"};
    private String selfuuid = null;
    private String receiveUUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_ac);

        //todo 收到传入的 uuid
        receiveUUID = getIntent().getStringExtra("receiveUUID");



        mTextView = findViewById(R.id.uuidtextview);
        mTextView.setText(receiveUUID);

        qmuiButton = findViewById(R.id.backbtn);
        qmuiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        inputText = findViewById(R.id.editText);
        send = findViewById(R.id.sendButton);
        adapter = new ChatAcAdapter(context, msgList);
        msgRecyclerView = findViewById(R.id.char_ac_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // LinearLayoutLayout即线性布局，创建对象后把它设置到RecyclerView当中
        msgRecyclerView.setLayoutManager(layoutManager);
        msgRecyclerView.setAdapter(adapter);

        //获取自身uuid
        selfuuid = (String) SharedPreferencesUtils.getParam(this, "uuid", "");
        ToastUtils.show(this, selfuuid);
        //获取该chat表中的聊天条目循环放入list中
        getMsgAndList();



        send.setOnClickListener(new View.OnClickListener() {                 //发送按钮点击事件
            @Override
            public void onClick(View v) {

                //获取EditText中的内容
                String content = inputText.getText().toString();

                //内容不为空则创建一个新的Msg对象，并把它添加到msgList列表中
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    //调用适配器的notifyItemInserted()用于通知列表有新的数据插入，这样新增的一条消息才能在RecyclerView中显示
                    adapter.notifyItemInserted(msgList.size() - 1);
                    //调用scrollToPosition()方法将显示的数据定位到最后一行，以保证可以看到最后发出的一条消息
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);

                    //  new Thread() {};
                    //自身每聊一条数据就往greendao里插入一条
                    insertNewMsg(content, "1");
                    insertChatingIfHasNot(receiveUUID, content);//耗时操作
                    //发送网络
                    NettyLongChannel.sendMsg(selfuuid, receiveUUID, content);


                    //调用EditText的setText()方法将输入的内容清空
                    inputText.setText("");

                    //todo 发完消息就显示圈圈，visiable
                    //todo 收到发送成功时，eventbus取消显示

                }
            }
        });

        msgRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int hightDiff = msgRecyclerView.getRootView().getHeight() - msgRecyclerView.getHeight();

                if (hightDiff > 500) { // 如果高度差超过100像素，就很有可能是有软键盘...
                    scrollToBottom();
                }
            }
        });

        //   hintKeyBoard();
        //设置下滑隐藏软键盘
        msgRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < -1) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
                }

            }
        });


    }

    private void insertChatingIfHasNot(String receiveUUID, String content) {
        int size = App.getDaoSession().getChatingDao().queryBuilder().where(ChatingDao.Properties.FriendUUId.eq(receiveUUID)).list().size();
        if (size == 0) {//chating表里面没有该uuid了，添加
            Chating chating = new Chating();
            chating.setNewestMsg(content);
            App.getDaoSession().getChatingDao().insert(chating);
        }
    }

    private void insertNewMsg(String msg, String type) {

        Message message = new Message();
        message.setMsg(msg);
        message.setReceiveId(receiveUUID);
        message.setSendID(selfuuid);
        message.setType(type);
        App.getDaoSession().getMessageDao().insert(message);
    }

    private void getMsgAndList() {

        MessageDao messageDao = App.getDaoSession().getMessageDao();
        List<Message> messageList = messageDao.queryBuilder().build().list();
        String s = null;
        if (messageList != null) {

            if (messageList.size() != 0) {
                for (int i = 0; i < messageList.size(); i++) {
                    Message message = messageList.get(i);
                    if ((message.getSendID().equals(receiveUUID) && message.
                            getReceiveId().equals(selfuuid)) ||
                            (message.getSendID().equals(selfuuid) && message.
                                    getReceiveId().equals(receiveUUID))) {
                        int anInt = Integer.parseInt(message.getType());
                        Msg msg = new Msg(message.getMsg(), anInt);
                        msgList.add(msg);
                    }

                }
            }

        }
        synchronized (Thread.currentThread()) {
            try {
                adapter.notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        msgRecyclerView.scrollToPosition(msgList.size() - 1);
    }

    private void scrollToBottom() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                msgRecyclerView.scrollToPosition(msgList.size() - 1);

            }
        }, 100);


    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("state","msgFrag执行了start,注册eventbud");
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.d("state","msgFrag执行了stop,解注eventbud");
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecievedMsg(SendMessage sendMessage) {
        String receMsg = sendMessage.getMsg();
        Msg msg = new Msg(receMsg, Msg.TYPE_RECEIVED);
       //这里应该不用插入sql，因为已经在mainac 和messageFragment里面插入过了，
        // 而且关闭本chatAc时候，ac关闭注册也关闭，打开时只要查询数据库
        // insertNewMsg(receMsg, "0");
        msgList.add(msg);
        //调用适配器的notifyItemInserted()用于通知列表有新的数据插入，这样新增的一条消息才能在RecyclerView中显示
        adapter.notifyItemInserted(msgList.size() - 1);
        //调用scrollToPosition()方法将显示的数据定位到最后一行，以保证可以看到最后发出的一条消息
        msgRecyclerView.scrollToPosition(msgList.size() - 1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

