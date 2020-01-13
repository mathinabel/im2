package com.quyuanjin.im2.netty;

import com.quyuanjin.im2.constant.ProtoConstant;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyLongChannel {

    private static Channel channel;
    private static NioEventLoopGroup group;

    public NettyLongChannel() {
    }

    public static boolean initNetty() {
        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast("handler", new ChatClientHandler());

                        }
                    });
            ChannelFuture future = bootstrap.connect("192.168.1.103", 8089).sync();
            channel = future.channel();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    public static void sendAndReflash(String head, String payload) {

        channel.writeAndFlush(head + "|" + payload + "\r\n");

    }

    /*
     消息协议：str0：协议   str1：selfuuid    str2：touuid     str3：contentType    str4：sendTime

     str5：receivedTime      str6：content    str7：netPath   str8:recorderTime
     */
    public static void sendMsg(String fromuuid, String touuid, String contentType, String sendTime,
                               String receivedTime, String content, String netPath, String recorderTime) {

        channel.writeAndFlush(
                ProtoConstant.SEND_MESSAGE + "|" +
                        fromuuid + "|" +
                        touuid + "|" +
                        contentType + "|" +
                        sendTime +"|" +
                        receivedTime +"|" +
                        content +"|" +
                        netPath +"|" +
                        recorderTime+"|" +"\r\n");

    }

    public static void sendRegisterMsg(String nickname, String pwd, String phone) {

        channel.writeAndFlush(ProtoConstant.REGIST_WITH_PWD + "|" + nickname + "|" + pwd + "|" + phone + "\r\n");

    }

    public static void sendLoginMsg(String phone, String pwd) {

        channel.writeAndFlush(ProtoConstant.SEND_LOGIN_MSG + "|" + phone + "|" + pwd + "\r\n");

    }

    public static void sendAddFriendRequest(String selfuuid, String phone) {

        //直接登录的用户服务端没有回馈selfuuid导致加好友时无法发送
        channel.writeAndFlush(ProtoConstant.ADD_FRIEND + "|" + selfuuid + "|" + phone + "|" + "0" + "\r\n");

    }

    public static void searchMsgAccordingUUID(String searchUUID) {

        channel.writeAndFlush(ProtoConstant.SEARCH_MSG_ACCORDING_UUID + "|" + searchUUID + "\r\n");

    }

    public static void sendReceivedAddFriend(String selfuuid, String friendUUID) {
        channel.writeAndFlush(ProtoConstant.SEND_RECEIVED_ADD_FRIEND + "|" + selfuuid + "|" + friendUUID + "|" + "\r\n");

    }

    public static void sendPullUnreadMsg(String selfuuid) {

        channel.writeAndFlush(ProtoConstant.SEND_PULL_UNREAD_MSG + "|" + selfuuid + "\r\n");

    }

    public static void sendPullUnreadAddFriendMsg(String selfuuid) {
        channel.writeAndFlush(ProtoConstant.PULL_UNREAD_ADDFRIEND + "|" + selfuuid + "\r\n");
    }

    public static void requestFriendList(String selfuuid) {
        channel.writeAndFlush(ProtoConstant.REQUEST_FRIENDS_LIST + "|" + selfuuid + "\r\n");
    }

   /* public static void main(String[] args) {
        NettyLongChannel.initNetty();

        NettyLongChannel.sendAndReflash("84safas6ewf");
    }*/

    public static void nettyshoutdown() {
        group.shutdownGracefully();
    }
}