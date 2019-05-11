package wjy.weiai.nettyclient.client.handler;

import android.content.Context;

import io.netty.channel.ChannelHandlerContext;
import wjy.weiai.nettyclient.protocol.response.MessageResponsePacket;


public class MessageResponseHandler extends BaseHandler<MessageResponsePacket> {


    public MessageResponseHandler(Context context) {
        super(context);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {
        sendBroadcast(HandlerBroadcastProtocol.ACTION_REVICE_MESSAGE, messageResponsePacket);
    }
}
