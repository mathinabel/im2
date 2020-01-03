package com.quyuanjin.im2.netty;


import android.util.Log;

import com.quyuanjin.im2.app.App;
import com.quyuanjin.im2.constant.ProtoConstant;
import com.quyuanjin.im2.constant.State;
import com.quyuanjin.im2.greendao.pojo.Message;
import com.quyuanjin.im2.greendao.pojo.UnReadAddFriendsMsg;
import com.quyuanjin.im2.greendao.pojo.UnReadMessage;
import com.quyuanjin.im2.netty.helper.SharedPreferencesUtils;
import com.quyuanjin.im2.netty.pojo.AddFriend;
import com.quyuanjin.im2.netty.pojo.RegisterWithUUID;
import com.quyuanjin.im2.netty.pojo.SendLoginMsgBack;
import com.quyuanjin.im2.netty.pojo.SendLongConnectBack;
import com.quyuanjin.im2.netty.pojo.SendMessage;
import com.quyuanjin.im2.netty.pojo.StorePullUnReadMsg;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriendHelloTwo;
import com.quyuanjin.im2.netty.pojo.SucceedAddFriendTwo;


import org.greenrobot.eventbus.EventBus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandler extends SimpleChannelInboundHandler<String> {




    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        if (!msg.contains("HTTP/1.1") && !msg.contains("Host:") && !msg.contains("Proxy-Connection: keep-alive") && !msg.contains("User2Model-Agent:")) {
            System.out.println(msg);
            String[] recemsg = msg.split("\\|");
            switch (recemsg[0]) {
                case ProtoConstant.REGISTER_WITH_PWD_BACK:

                    EventBus.getDefault().post(new RegisterWithUUID(recemsg[1]));
                    break;

                case ProtoConstant.SEND_LOGIN_MSG_BACK:

                    EventBus.getDefault().post(new SendLoginMsgBack(recemsg[1]));
                    break;
                case ProtoConstant.LONG_CONNECT_BACK:
                    EventBus.getDefault().post(new SendLongConnectBack());
                    break;
                case ProtoConstant.SEND_MESSAGE_BACK:

                    EventBus.getDefault().post(new SendMessage(recemsg[2],recemsg[1]));




                    break;
                //在线时发送的加好友
                case ProtoConstant.ADD_FRIEND_BACK:
                    EventBus.getDefault().post(new AddFriend(recemsg[1],recemsg[2]));
                    break;

                    //发好友请求时不在线，上线时拉取的请求
                //插入数据库在ContractFragment主界面读取数据库
                case ProtoConstant.PULL_UNREAD_ADDFRIEND_BACK:
                    //包格式 head|意向加好友id|意向加好友id|意向加好友id|
                   //存进数据库
                    for (int i=1;i<recemsg.length;i++)
                    {
                        UnReadAddFriendsMsg unReadAddFriendsMsg=new UnReadAddFriendsMsg();
                        unReadAddFriendsMsg.setFriendUUId(recemsg[i]);
                        unReadAddFriendsMsg.setState(State.UN_READ);
                        App.getDaoSession().getUnReadAddFriendsMsgDao().insert(unReadAddFriendsMsg);
                    }
                    break;
                case ProtoConstant.SEND_RECEIVED_ADD_FRIEND_BACK:
                    Log.d("199","收到消息");
                    //发送到homeFragment
                    EventBus.getDefault().post(new SucceedAddFriendTwo(recemsg[1]));
                    //发送到notification Fragment
                    EventBus.getDefault().post(new SucceedAddFriendHelloTwo(recemsg[1]));

                    break;
                case ProtoConstant.SEND_PULL_UNREAD_MSG_BACK:
                    //todo 第二次再打开app时会导致再次读取并且存储
                    // ，这可能会导致多次打开之后出现数据重复的问题
                    //解包 senduuid sendime sendmsg 并存进greendao的未读表和消息表
                    EventBus.getDefault().post(new StorePullUnReadMsg(recemsg));
                    //在上面的store里面进行
         /*           for (int i=1;i<recemsg.length;i+=3)
                    {
                        UnReadMessage unReadMessage=new UnReadMessage();
                        unReadMessage.setSenderId(recemsg[i]);
                        unReadMessage.setSendTime(recemsg[i+1]);
                        unReadMessage.setMsg(recemsg[i+2]);
                        App.getDaoSession().getUnReadMessageDao().insert(unReadMessage);

                        //逻辑要清楚 message 发送时，自身是发送者，对方是接收者
                        // 接收时 自身是接收者，对方是发送者
                        //现在是接收数据，所以自身是接收者，将对方的uuid存进发送者里
                        Message message=new Message();
                        message.setType("0");
                        message.setSendID(recemsg[i]);
                        message.setReceiveId();
                        message.setCreateTime(recemsg[i+1]);
                        message.setMsg(recemsg[i+2]);
                        App.getDaoSession().getMessageDao().insert(message);

                    }*/
                    break;
                default:
                    break;

            }

        }
    }
}