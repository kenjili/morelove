package wjy.morelove.nettyclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.List;

import io.netty.channel.Channel;

import wjy.morelove.App;
import wjy.morelove.bean.Message;
import wjy.morelove.nettyclient.client.ChatClient;
import wjy.morelove.nettyclient.client.handler.BaseHandler;
import wjy.morelove.nettyclient.protocol.request.LoginRequestPacket;
import wjy.morelove.nettyclient.protocol.response.LoginResponsePacket;
import wjy.morelove.nettyclient.protocol.response.MessageResponsePacket;
import wjy.morelove.nettyclient.protocol.response.RequestStatePacket;

public class ChatServerManager {

    public interface OnReceiverMessageListener {
        void onConnectDeath();

        void onLogginSuccess();

        void onLogginFail(String errorMsg);

        void onSendMsgError(String errorMsg);

        void onSendMsgSuccess();

        void onReceiver(List<Message> messagesList);
    }

    private OnReceiverMessageListener onReceiverMessageListener;

    public void setOnReceiverMessageListener(OnReceiverMessageListener onReceiverMessageListener) {
        this.onReceiverMessageListener = onReceiverMessageListener;
    }

    private ChatClient chatClient;

    private ChatServerManager() {
        this.chatClient = new ChatClient(App.getApp(), new ChatClient.OnConnectServerListener() {
            @Override
            public void onConnectSuccess(Channel channel) {
                Log.d("初始化聊天服务长连接", "连接成功....");
                //连接成功登录。。。
                doLogin();
            }

            @Override
            public void onReconnect(int value) {
                Log.d("初始化聊天服务长连接", "重新连接中....第" + value + "次重连");
            }

            @Override
            public void onConnectFail() {
                Log.d("初始化聊天服务长连接", "连接失败....");
            }
        });
        this.registReceiver(App.getApp());
    }

    private static class ChatServerManagerHolder {
        private static final ChatServerManager sChatServerManager = new ChatServerManager();
    }

    public static ChatServerManager getManager() {
        return ChatServerManagerHolder.sChatServerManager;
    }

    /**
     * 判断是否连接聊天服务器成功
     *
     * @return
     */
    public boolean isConnectChatServer() {
        return chatClient.getChannel() != null &&
                chatClient.getChannel().isActive();
    }

    /**
     * 初始化聊天服务长连接
     */
    public void initChatServerConnect() {
        this.chatClient.connectServier();
    }


    public ChatClient getChatClient() {
        return chatClient;
    }

    /**
     * 尝试登录
     */
    private void doLogin() {
        if (!isConnectChatServer())
            return;
        if (App.getApp().getUserLoginInfo() == null)
            return;
        //连接成功登录
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserName(App.getApp().getUserLoginInfo().getUsername());
        loginRequestPacket.setPassword("notpwd");//无需密码
        // 发送登录数据包
        this.chatClient.getChannel().writeAndFlush(loginRequestPacket);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BaseHandler.HandlerBroadcastProtocol.ACTION_CONNECT_DEATH.equals(action)) {
                if (onReceiverMessageListener != null) {
                    onReceiverMessageListener.onConnectDeath();
                }
            } else if (BaseHandler.HandlerBroadcastProtocol.ACTION_LOGIN_SUCCESS.equals(action)) {
                if (onReceiverMessageListener != null)
                    onReceiverMessageListener.onLogginSuccess();
            } else if (BaseHandler.HandlerBroadcastProtocol.ACTION_LOGIN_FAIL.equals(action)) {
                LoginResponsePacket responsePacket = (LoginResponsePacket) intent.getSerializableExtra(BaseHandler.BROADCAST_DATA_KEY);
                if (responsePacket != null) {
                    if (onReceiverMessageListener != null)
                        onReceiverMessageListener.onLogginFail(responsePacket.getReason());
                }
            } else if (BaseHandler.HandlerBroadcastProtocol.ACTION_SEND_MESSAGE_ERROR.equals(action)) {
                RequestStatePacket statePacket = (RequestStatePacket) intent.getSerializableExtra(BaseHandler.BROADCAST_DATA_KEY);
                if (statePacket != null) {
                    if (onReceiverMessageListener != null)
                        onReceiverMessageListener.onSendMsgError(statePacket.getErrorMessage());
                }
            } else if (BaseHandler.HandlerBroadcastProtocol.ACTION_SEND_MESSAGE_SUCCESS.equals(action)) {
                if (onReceiverMessageListener != null)
                    onReceiverMessageListener.onSendMsgSuccess();
            } else if (BaseHandler.HandlerBroadcastProtocol.ACTION_REVICE_MESSAGE.equals(action)) {
                MessageResponsePacket messageResponsePacket = (MessageResponsePacket) intent.getSerializableExtra(BaseHandler.BROADCAST_DATA_KEY);
                if (onReceiverMessageListener != null)
                    onReceiverMessageListener.onReceiver(messageResponsePacket.getMessages());
            }
        }
    };

    /**
     * 注册广播接收器
     */
    private void registReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BaseHandler.HandlerBroadcastProtocol.ACTION_CONNECT_DEATH);
        intentFilter.addAction(BaseHandler.HandlerBroadcastProtocol.ACTION_LOGIN_SUCCESS);
        intentFilter.addAction(BaseHandler.HandlerBroadcastProtocol.ACTION_LOGIN_FAIL);
        intentFilter.addAction(BaseHandler.HandlerBroadcastProtocol.ACTION_SEND_MESSAGE_ERROR);
        intentFilter.addAction(BaseHandler.HandlerBroadcastProtocol.ACTION_SEND_MESSAGE_SUCCESS);
        intentFilter.addAction(BaseHandler.HandlerBroadcastProtocol.ACTION_REVICE_MESSAGE);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }
}
