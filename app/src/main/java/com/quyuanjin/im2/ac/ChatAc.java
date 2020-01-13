package com.quyuanjin.im2.ac;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.bean.UploadFileInfo;
import com.okhttplib.callback.ProgressCallback;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.quyuanjin.im2.R;
import com.quyuanjin.im2.adapter.chatac.RecorderAdapter;
import com.quyuanjin.im2.adapter.chatac.inputpan.InputPanAdater;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.camera.CameraView;
import com.quyuanjin.im2.greendao.pojo.Chating;
import com.quyuanjin.im2.greendao.pojo.ChatingDao;
import com.quyuanjin.im2.greendao.pojo.Message;
import com.quyuanjin.im2.greendao.pojo.MessageDao;
import com.quyuanjin.im2.helputils.PermissionsChecker;
import com.quyuanjin.im2.map.MapMainAc;
import com.quyuanjin.im2.msg.Msg;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.NetUtils;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.quyuanjin.im2.netty.pojo.SendMessage;
import com.quyuanjin.im2.voicerecorder.view.AudioRecorderButton;
import com.quyuanjin.im2.voicerecorder.view.MediaManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;


public class ChatAc extends AppCompatActivity {

    //  private List msgList = new ArrayList<>();
    private List<Recorder> mDatas = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private ImageView addmsg;

    private GridView gridView;
    private ListView msgRecyclerView;
    // private ChatAcAdapter adapter;
    private ArrayAdapter<Recorder> mAdapter;
    private Context context;
    Handler handler = new Handler();
    private TextView mTextView;
    private QMUIButton qmuiButton;

    private String selfuuid = null;
    private String receiveUUID;
    private ImageView voiceRecord;
    private AudioRecorderButton mAudioRecorderButton;

    private View mAnimView;

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_ac);

        initView();

        //获取该chat表中的聊天条目循环放入list中
        getMsgAndList();
        initSendBtn();
        //初始化gridView
        initGridView();
        initAddMsg();


        hideKeyBoard();


    }


    private void hideKeyBoard() {
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
     /*   msgRecyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
            }
        });*/
    }

    private void initView() {
        context = this.getBaseContext();
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
        addmsg = findViewById(R.id.extImageView);
        gridView = findViewById(R.id.gridView);

        voiceRecord = findViewById(R.id.audioImageView);
        mAudioRecorderButton = findViewById(R.id.id_recorder_button);

        voiceRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAudioRecorderButton.getVisibility() == GONE) {
                    mAudioRecorderButton.setVisibility(VISIBLE);
                    mPermissionsChecker = new PermissionsChecker(ChatAc.this);
                    // 缺少权限时, 进入权限配置页面
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        startPermissionsActivity();
                    }


                    mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
                        @Override
                        public void onFinish(final float seconds, final String filePath) {
                            Msg msg = new Msg("", Msg.TYPE_SENT, Msg.TYPE_RECORDER);
                            //每完成一次录音
                            Recorder recorder = new Recorder(seconds, filePath, msg);
                            mDatas.add(recorder);
                            //更新adapter
                            mAdapter.notifyDataSetChanged();
                            msgRecyclerView.setAdapter(mAdapter);
                            //设置listview 位置
                            msgRecyclerView.setSelection(mDatas.size() - 1);

                            Thread t = new Thread(new Runnable() {
                                public void run() {

                                    uploadFile(filePath);

                                    //发送网络
                                    if (NetUtils.isConnected(App.getContext()) && NettyLongChannel.initNetty()) {
                                        NettyLongChannel.sendMsg(selfuuid, receiveUUID,
                                                String.valueOf(Msg.TYPE_RECORDER), "", "",
                                                "", "", String.valueOf(seconds));
                                        insertNewMsg("", "1", "3", filePath, seconds, String.valueOf(Msg.TYPE_SEND_SUCCESS));
                                    } else {
                                        insertNewMsg("", "1", "3", filePath, seconds, String.valueOf(Msg.TYPE_SEND_FAILED));

                                    }
                                }
                            });
                            t.start();


                        }
                    });
                } else {
                    mAudioRecorderButton.setVisibility(GONE);
                }
            }
        });

        // adapter = new ChatAcAdapter(context, msgList);

        mAdapter = new RecorderAdapter(context, mDatas);
        msgRecyclerView = findViewById(R.id.char_ac_recycler_view);


        msgRecyclerView.setAdapter(mAdapter);
        msgRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Msg msg = mDatas.get(position).getMsg();

                if (msg != null) {


                    if (msg.getContentType() == Msg.TYPE_RECORDER) {
                        //如果第一个动画正在运行， 停止第一个播放其他的
                        if (mAnimView != null) {
                            mAnimView.setBackgroundResource(R.drawable.adj);
                            mAnimView = null;
                        }
                        //播放动画
                        mAnimView = view.findViewById(R.id.id_recorder_anim);
                        mAnimView.setBackgroundResource(R.drawable.play_anim);
                        AnimationDrawable animation = (AnimationDrawable) mAnimView.getBackground();
                        animation.start();

                        //播放音频  完成后改回原来的background
                        MediaManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mAnimView.setBackgroundResource(R.drawable.adj);
                            }
                        });
                    }
                }
            }
        });
        //获取自身uuid
        selfuuid = (String) SharedPreferencesUtils.getParam(this, "uuid", "");
        ToastUtils.show(this, selfuuid);

    }


    private void initAddMsg() {
        addmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gridView.getVisibility() == View.GONE) {
                    gridView.setVisibility(View.VISIBLE);
                } else {
                    gridView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initSendBtn() {
        send.setOnClickListener(new View.OnClickListener() {                 //发送按钮点击事件
            @Override
            public void onClick(View v) {

                //获取EditText中的内容
                final String content = inputText.getText().toString();

                //内容不为空则创建一个新的Msg对象，并把它添加到msgList列表中
                if (!"".equals(content)) {


                    Msg msg = new Msg(content, Msg.TYPE_SENT, Msg.TYPE_TEXT);
                    Recorder recorder = new Recorder(0, "", msg);
                    mDatas.add(recorder);
                    Log.d("msg", "::::::::::::" + recorder.getMsg().getContent());
                    mAdapter.notifyDataSetChanged();
                    msgRecyclerView.setAdapter(mAdapter);
                    Log.d("msg", content);
                    msgRecyclerView.setSelection(mDatas.size() - 1);

                    insertChatingIfHasNot(receiveUUID, content);//耗时操作
                    Thread t = new Thread(new Runnable() {
                        public void run() {
                            //发送网络
                            if (NetUtils.isConnected(App.getContext()) && NettyLongChannel.initNetty()) {
                                NettyLongChannel.sendMsg(selfuuid, receiveUUID, String.valueOf(Msg.TYPE_TEXT),
                                        "", "", content, "", "0");

                                //自身每聊一条数据就往greendao里插入一条
                                insertNewMsg(content, String.valueOf(Msg.TYPE_SENT), String.valueOf(Msg.TYPE_TEXT), "", 0, String.valueOf(Msg.TYPE_SEND_SUCCESS));

                            } else {
                                //自身每聊一条数据就往greendao里插入一条
                                insertNewMsg(content, String.valueOf(Msg.TYPE_SENT), String.valueOf(Msg.TYPE_TEXT), "", 0, String.valueOf(Msg.TYPE_SEND_FAILED));

                            }

                        }
                    });
                    t.start();

                    //调用EditText的setText()方法将输入的内容清空
                    inputText.setText("");

                    //todo 发完消息就显示圈圈，visiable
                    //todo 收到发送成功时，eventbus取消显示

                }
            }
        });
    }


    private void initGridView() {
        InputPanAdater inputPanAdater = new InputPanAdater(context);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(ChatAc.this, CameraView.class);
                        startActivityForResult(intent, 7);
                        Log.d("zhixingle", "startActivityForResult");

                        break;
                    case 1:
                        Intent intent2 = new Intent(ChatAc.this, MapMainAc.class);
                        startActivityForResult(intent2, 3);
                    case 2:


                    case 3:

                    default:
                        break;


                }
            }
        });
        gridView.setAdapter(inputPanAdater);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Double dou = data.getDoubleExtra("name", 0);
        //  ToastUtils.show(ChatAc.this, dou.toString());

        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }

        // String[] strings =data.getStringArrayExtra("imgPath");
        // Log.d("zhixingle", "onActivityResult"+strings.length);

        if (resultCode == 7 && requestCode == 7) {
            ArrayList<String> infoList = data.getStringArrayListExtra("imgPath");
            Log.d("zhixingle", "onActivityResult" + infoList.size());


            uploadFile(infoList.get(0));


        }

    }

    private void uploadFile(String path) {
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "请选择上传文件！", Toast.LENGTH_LONG).show();
            return;
        }
        HttpInfo info = HttpInfo.Builder()
                .setUrl("http://192.168.43.75:8080/upload")
                .addUploadFile(new UploadFileInfo()
                        .setInterfaceParamName("test")
                        .setFilePathWithName(path)
                        .setProgressCallback(new ProgressCallback() {
                            @Override
                            public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {

                            }

                            @Override
                            public void onResponseMain(String filePath, HttpInfo info) {




                            }
                        }))
                .build();
        OkHttpUtil.getDefault(this).doUploadFileAsync(info);
    }

    private void insertChatingIfHasNot(String receiveUUID, String content) {
        int size = App.getDaoSession().getChatingDao().queryBuilder().where(ChatingDao.Properties.FriendUUId.eq(receiveUUID)).list().size();
        if (size == 0) {//chating表里面没有该uuid了，添加
            Chating chating = new Chating();
            chating.setNewestMsg(content);
            chating.setFriendUUId(receiveUUID);
            App.getDaoSession().getChatingDao().insert(chating);
        }
    }

    private void insertNewMsg(String msg, String type, String contentType, String localPath, float recorderTime, String sendSucceedType) {

        Message message = new Message();
        message.setMsg(msg);
        message.setReceiveId(receiveUUID);
        message.setSendID(selfuuid);
        message.setType(type);
        message.setContentType(contentType);
        message.setLocalPath(localPath);
        message.setSendSucceedType(sendSucceedType);
        message.setRecorderTime(recorderTime);
        App.getDaoSession().getMessageDao().insert(message);
    }

    private void getMsgAndList() {

        MessageDao messageDao = App.getDaoSession().getMessageDao();
        List<Message> messageList = messageDao.queryBuilder().build().list();

        if (messageList != null) {

            if (messageList.size() > 0) {
                for (int i = 0; i < messageList.size(); i++) {
                    Message message = messageList.get(i);
                    //判断获取我与特定对方的通信message类


                    if (message.getSendID() != null && message.getReceiveId() != null) {
                        if ((receiveUUID.equals(message.getSendID()) && selfuuid.equals(message.getReceiveId())) || (selfuuid.equals(message.getSendID()) && receiveUUID.equals(message.getReceiveId()))) {

                            int anInt = Integer.parseInt(message.getType());
                            int anIntwo = Integer.parseInt(message.getContentType());
                            if (anInt == 0 || anIntwo == 3) {
                                Msg msg = new Msg(message.getMsg(), anInt, anIntwo);

                                Recorder recorder = new Recorder(message.getRecorderTime(), message.getLocalPath(), msg);
                                mDatas.add(recorder);
                            }
                        }

                    }
                }
            }

        }
        synchronized (Thread.currentThread()) {
            try {
                mAdapter.notifyAll();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        msgRecyclerView.setSelection(mDatas.size() - 1);
    }

    private void scrollToBottom() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                msgRecyclerView.setSelection(mDatas.size() - 1);

            }
        }, 100);


    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("zhixingle", "msgFrag执行了start,注册eventbud");
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.d("zhixingle", "msgFrag执行了stop,解注eventbud");
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecievedMsg(SendMessage sendMessage) {
        String receMsg = sendMessage.getMsg();
        Msg msg = new Msg(receMsg, Msg.TYPE_RECEIVED, Msg.TYPE_TEXT);
        //这里应该不用插入sql，因为已经在mainac 和messageFragment里面插入过了，
        // 而且关闭本chatAc时候，ac关闭注册也关闭，打开时只要查询数据库
        // insertNewMsg(receMsg, "0");
        //todo 此处或者别处要做一个转换将语音转换成本地local保存
        Recorder recorder = new Recorder(sendMessage.getRecorderTime(), sendMessage.getLocalPath(), msg);
        mDatas.add(recorder);
        //调用适配器的notifyItemInserted()用于通知列表有新的数据插入，这样新增的一条消息才能在RecyclerView中显示
        mAdapter.notifyDataSetChanged();
        //调用scrollToPosition()方法将显示的数据定位到最后一行，以保证可以看到最后发出的一条消息
        msgRecyclerView.setSelection(mDatas.size() - 1);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // MediaManager.resume();

    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    /**
     * 异步上传图片：显示上传进度
     */
    private void doUploadImg(String url, String filePathOne) {
        HttpInfo info = HttpInfo.Builder()
                .setUrl(url)
                .addUploadFile("file", filePathOne, new ProgressCallback() {
                    //onProgressMain为UI线程回调，可以直接操作UI
                    @Override
                    public void onProgressMain(int percent, long bytesWritten, long contentLength, boolean done) {


                    }
                })
                .build();
        OkHttpUtil.getDefault(this).doUploadFileAsync(info);
    }

    //数据类
    public class Recorder {

        float time;
        String filePath;
        Msg msg;

        public Msg getMsg() {
            return msg;
        }

        public void setMsg(Msg msg) {
            this.msg = msg;
        }

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Recorder(float time, String filePath, Msg msg) {
            super();
            this.time = time;
            this.filePath = filePath;
            this.msg = msg;
        }


    }
}

