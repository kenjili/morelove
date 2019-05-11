package wjy.weiai.nettyclient.client.handler;

import android.content.Context;
import android.content.Intent;

import io.netty.channel.SimpleChannelInboundHandler;
import wjy.weiai.nettyclient.protocol.Packet;

public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {

    public static final String BROADCAST_DATA_KEY = "netty.packet";
    public static class HandlerBroadcastProtocol{
        public final static String ACTION_CONNECT_DEATH = "netty.connect.death";//掉线了
        public final static String ACTION_LOGIN_SUCCESS="netty.login.success";//登录成功
        public final static String ACTION_LOGIN_FAIL="netty.login.fail";//登录失败
        public final static String ACTION_SEND_MESSAGE_SUCCESS="netty.send.message.success";//发送消息成功
        public final static String ACTION_SEND_MESSAGE_ERROR="netty.send.message.error";//发送消息失败
        public final static String ACTION_REVICE_MESSAGE="netty.revice.message";//接收到消息
    }

    private Context mContext;
    public BaseHandler(Context context){
        this.mContext = context;
    }

    /**
     * 发送一条广播
     */
    protected void sendBroadcast(String action, Packet packet){
        Intent intent = new Intent(action);
        if(packet!=null){
            intent.putExtra(BROADCAST_DATA_KEY,packet);
        }
        mContext.sendBroadcast(intent);
    }
}
