package com.quyuanjin.im2.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.quyuanjin.im2.R;
import com.quyuanjin.im2.adapter.MsgAdapter;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.greendao.pojo.Chating;
import com.quyuanjin.im2.greendao.pojo.UnReadMessage;
import com.quyuanjin.im2.msg.Msg;
import com.quyuanjin.im2.netty.helper.NotificationUtils;
import com.quyuanjin.im2.netty.pojo.ChatingUUIDAndMSg;
import com.quyuanjin.im2.netty.pojo.NetMessage;
import com.quyuanjin.im2.netty.pojo.SendMessage;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriend;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriendHello;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriendHelloTwo;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;

public class MessageFragment extends Fragment {


    private View rootView;

    private View view;//定义view用来设置fragment的layout
    public RecyclerView mCollectRecyclerView;//定义RecyclerView
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> uuidList = new ArrayList<>();
    //自定义recyclerveiw的适配器
    private MsgAdapter msgAdapter;

    //放uuid的列表
    private List<String> mLsit;

    //   @BindView(R.id.msg_recycler_view)
    RecyclerView msgRecyclerView;

    public MessageFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != view) {

            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.message_fragment, container, false);
            msgRecyclerView = view.findViewById(R.id.msg_recycler_view);

            EventBus.getDefault().register(this);
            Log.d("state","msgFrag执行了onCreateView");

            initData();

            //对recycleview进行配置
            initRecyclerView();
            //获取未读信息
          //  checkUnreadMsg();
            reflashLayout();
        }

        return view;
    }

    private void reflashLayout() {

        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayouttwo);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("123","执行了onStart()");
        //创建时获取chating表的正在聊天条目,并且展示
        getUUidFromChatingTable();
    }

    //创建时获取chating表的正在聊天条目
    private void getUUidFromChatingTable() {
        List<Chating> chatList = App.getDaoSession().getChatingDao().queryBuilder().build().list();
        if (chatList.size() > 0) {
            for (int i = 0; i < chatList.size(); i++) {
                Chating chating = chatList.get(i);
                if (!uuidList.contains(chating.getFriendUUId())) {
                    //如果当前uuidlist列表时没有该list则添加
                    arrayList.add(chating.getNewestMsg());
                    uuidList.add(chating.getFriendUUId());
                    msgAdapter.notifyDataSetChanged();
                }}}
        Log.d("123","执行了getUUidFromChatingTable()");

    }

    private void checkUnreadMsg() {
        //查询数据库unreadmsg表获取未读信息
        //将信息的uuid用于展示，
        List<UnReadMessage> unReadMessageList = App.getDaoSession().getUnReadMessageDao().queryBuilder().list();
        if (unReadMessageList.size() > 0) {
            for (int i = 0; i < unReadMessageList.size(); i++) {
                //如果该arraylist不含重复的uuid 则将uuid加入
                if (!arrayList.contains(unReadMessageList.get(i).getSenderId())) {
                    arrayList.add(unReadMessageList.get(i).getSenderId());
                    //计数条目加一，且重新排序
                    uuidList.add(unReadMessageList.get(i).getMsg());
                }
            }
            msgAdapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView() {
        msgAdapter = new MsgAdapter(this.getContext(), arrayList, uuidList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        msgRecyclerView.setAdapter(msgAdapter);


    }

    private void initData() {

        uuidList.add("hello");
        arrayList.add("测试账号");

    }


    @Subscribe(threadMode = ThreadMode.MAIN)//  chating表好友加一，并通知list显示
    public void AddEvent2(SucceedAddFriendHello succeedAddFriendHello) {
        //  chating表好友加一
        Chating chating = new Chating();
        chating.setFriendUUId(succeedAddFriendHello.getFriendUUID());
        chating.setNewestMsg("你好");
        App.getDaoSession().getChatingDao().insert(chating);
        // 并通知list显示
        arrayList.add("你好啊");
        uuidList.add(succeedAddFriendHello.getFriendUUID());
        msgAdapter.notifyDataSetChanged();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)//  chating表好友加一，并通知list显示
    public void AddEvent02(SucceedAddFriendHelloTwo succeedAddFriendHelloTwo) {
        //  chating表好友加一
        Chating chating = new Chating();
        chating.setFriendUUId(succeedAddFriendHelloTwo.getFriendUUID());
        chating.setNewestMsg("你好");
        App.getDaoSession().getChatingDao().insert(chating);
        // 并通知list显示
        uuidList.add(succeedAddFriendHelloTwo.getFriendUUID());
        arrayList.add("你好啊");
        msgAdapter.notifyDataSetChanged();
    }

 /*   @Subscribe(threadMode = ThreadMode.MAIN)//  chating界面好友个数加一，并通知list显示
    public void AddEvent03(ChatingUUIDAndMSg chatingUUIDAndMSg) {
        if (uuidList != null) {
            String touuid = chatingUUIDAndMSg.getToUUID();
            for (int i = 0; i < uuidList.size(); i++) {
                if ((uuidList.get(i)).equals(touuid)) {//界面列表有该uuid
                    //将list置顶
                    Collections.swap(uuidList, 0, i);
                    Collections.swap(arrayList, 0, i);
                    msgAdapter.notify();
                }
            }
        }
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)// 状态通知栏通知有新消息
    public void insetchating(SendMessage sendMessage) {
        // 应该做查重uuid，防止添加到相同的 uuid
        if (!uuidList.contains(sendMessage.getUuid())) {
            //如果当前uuidlist列表时没有该list则添加
            arrayList.add(sendMessage.getMsg());
            uuidList.add(sendMessage.getUuid());
            for (int i = 0; i < uuidList.size(); i++) {
                if ((uuidList.get(i)).equals(sendMessage.getUuid())) {
                    //将list置顶
                    Collections.swap(uuidList, 0, i);
                    Collections.swap(arrayList, 0, i);
                    msgAdapter.notifyDataSetChanged();
                }
            }
            msgAdapter.notifyDataSetChanged();
            //且存进chating数据库,如果数据库没有该条记录
            insertChatingIfHasNot(sendMessage.getUuid(), sendMessage.getMsg());


        }
        //状态栏通知
        NotificationUtils notificationUtils = new NotificationUtils(view.getContext());
        notificationUtils.sendNotification(sendMessage.getUuid(), sendMessage.getMsg());
    }


 /*  @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
       Log.d("state","msgFrag执行了start");

    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        Log.d("state","msgFrag执行了stop");
    }
*/




    private void searchChatingAndShow() {
        List<Chating> chatList = App.getDaoSession().getChatingDao().queryBuilder().build().list();
        if (chatList.size() != 0) {
            for (int i = 0; i < chatList.size(); i++) {
                Chating chating = chatList.get(i);
                if (chating.getFriendUUId() != null) {
                    //chating表中有uuid，且在arrlist中没有，则添加
                    if (arrayList.size() != 0) {
                        for (int i1 = 0; i1 < arrayList.size(); i1++) {//遍历arrayList
                            if (!(arrayList.get(i1).equals(chating.getFriendUUId()))) {//里面有chating的uuid
                                arrayList.add(chating.getFriendUUId());

                            }

                        }
                    }
                }
            }
            synchronized (Thread.currentThread()) {
                try {
                    msgAdapter.notifyAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //如果数据库正在聊天表没有该条记录，那么插入该条数据
    private void insertChatingIfHasNot(String uuid, String content) {
        List<Chating> chatList = App.getDaoSession().getChatingDao().queryBuilder().build().list();
        if (chatList.size() > 0) {
            for (int i = 0; i < chatList.size(); i++) {
                Chating chating = chatList.get(i);

                if (chating.getFriendUUId() == null || (chating.getFriendUUId()).equals(uuid)) {
                    Chating chating1 = new Chating();
                    chating.setFriendUUId(uuid);
                    chating.setNewestMsg(content);
                    App.getDaoSession().getChatingDao().insert(chating1);
                    //     EventBus.getDefault().post(new ChatingUUIDAndMSg(touuid, content));
                }

            }
        }
    }
}