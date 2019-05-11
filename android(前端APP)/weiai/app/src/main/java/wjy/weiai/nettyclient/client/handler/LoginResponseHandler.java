package wjy.weiai.nettyclient.client.handler;

import android.content.Context;

import io.netty.channel.ChannelHandlerContext;
import wjy.weiai.nettyclient.protocol.response.LoginResponsePacket;


public class LoginResponseHandler extends BaseHandler<LoginResponsePacket> {

    public LoginResponseHandler(Context context) {
        super(context);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.isSuccess()) {
            sendBroadcast(HandlerBroadcastProtocol.ACTION_LOGIN_SUCCESS,loginResponsePacket);
        } else {
            sendBroadcast(HandlerBroadcastProtocol.ACTION_LOGIN_FAIL,loginResponsePacket);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        sendBroadcast(HandlerBroadcastProtocol.ACTION_CONNECT_DEATH,null);
    }
}
