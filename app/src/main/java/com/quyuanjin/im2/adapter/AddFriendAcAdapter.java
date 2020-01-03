package com.quyuanjin.im2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quyuanjin.im2.R;
import com.quyuanjin.im2.ac.AddFriendAc;
import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.constant.State;
import com.quyuanjin.im2.greendao.pojo.UnReadAddFriendsMsg;
import com.quyuanjin.im2.greendao.pojo.UnReadAddFriendsMsgDao;
import com.quyuanjin.im2.msg.Msg;
import com.quyuanjin.im2.netty.NettyLongChannel;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.helper.ToastUtils;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriend;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriendHello;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class AddFriendAcAdapter extends BaseAdapter {


    private Context context;
    private List<UnReadAddFriendsMsg> uuidList;
    private String selfuuid;


    public AddFriendAcAdapter(Context context, List<UnReadAddFriendsMsg> uuidList) {

        this.context = context;
        this.uuidList = uuidList;
        selfuuid = (String) SharedPreferencesUtils.getParam(context, "uuid", "");

    }


    @Override
    public int getCount() {
        if (uuidList != null) {
            if (uuidList.size() > 0) {
                return uuidList.size();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return uuidList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View view1 = LayoutInflater
                .from(context)
                .inflate(R.layout.add_friend_ac_list, viewGroup, false);
        TextView nameTextview = view1.findViewById(R.id.nameTextView);
        final Button acceptBtn = view1.findViewById(R.id.acceptButton);

        nameTextview.setText(uuidList.get(i).getFriendUUId());
        if (uuidList.get(i).getState() == State.UN_READ) {
            acceptBtn.setVisibility(View.VISIBLE);
        }
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptBtn.setVisibility(View.INVISIBLE);
                EventBus.getDefault().post(new SucceedAddFriend(uuidList.get(i).getFriendUUId()));
                //发送到notification Fragment
                EventBus.getDefault().post(new SucceedAddFriendHello(uuidList.get(i).getFriendUUId()));
                acceptBtn.setVisibility(View.INVISIBLE);
                //发送到网络通知对方添加
                NettyLongChannel.sendReceivedAddFriend(selfuuid, uuidList.get(i).getFriendUUId());


              /*    //删除未添加表的该未添加用户
              App.getDaoSession().getUnReadAddFriendsMsgDao().queryBuilder()
                        .where(UnReadAddFriendsMsgDao.Properties.FriendUUId.
                                eq(uuidList.get(i).getFriendUUId()))
                        .buildDelete()
                        .executeDeleteWithoutDetachingEntities();*/
                //不要删除信息，改为更改信息状态，并且通知服务器更改状态
                /*
                //方法一：对所有的数据都取出来处理
                UnReadAddFriendsMsgDao unReadAddFriendsMsgDao = App.getDaoSession().getUnReadAddFriendsMsgDao();
                List<UnReadAddFriendsMsg> unReadAddFriendsMsgList = unReadAddFriendsMsgDao.loadAll();
                for (int i = 0; i < unReadAddFriendsMsgList.size(); i++) {
                    unReadAddFriendsMsgList.get(i).setState(State.READ);
                }
                unReadAddFriendsMsgDao.updateInTx(unReadAddFriendsMsgList);
                */
                //方法二:查询组成list时就挑选合适的uuid，遍历合适的uuid ，如果是当前的position
                //上的uuid就执行插入操作
                UnReadAddFriendsMsgDao unReadAddFriendsMsgDao = App.getDaoSession().getUnReadAddFriendsMsgDao();
                List<UnReadAddFriendsMsg> unReadAddFriendsMsgList = unReadAddFriendsMsgDao.queryBuilder().
                        where(UnReadAddFriendsMsgDao.Properties.FriendUUId.
                                eq(uuidList.get(i).getFriendUUId())).list();
                for (int j = 0; j < unReadAddFriendsMsgList.size(); j++) {
                    if (unReadAddFriendsMsgList.get(j).getFriendUUId().
                            equals(uuidList.get(i).getFriendUUId())){
                        unReadAddFriendsMsgList.get(j).setState(State.READ);
                    }
                      unReadAddFriendsMsgDao.updateInTx(unReadAddFriendsMsgList);
                }

                //通知服务器更改状态




                ToastUtils.show(context, uuidList.get(i).getFriendUUId());
            }
        });
        return view1;
    }


}



