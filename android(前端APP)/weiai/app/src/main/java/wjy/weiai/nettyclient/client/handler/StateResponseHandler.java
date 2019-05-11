package wjy.weiai.nettyclient.client.handler;

import android.content.Context;

import io.netty.channel.ChannelHandlerContext;
import wjy.weiai.nettyclient.protocol.response.RequestStatePacket;

/**
 * 消息发送状态
 */
public class StateResponseHandler extends BaseHandler<RequestStatePacket> {

    public StateResponseHandler(Context context) {
        super(context);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestStatePacket requestStatePacket) {
        if(requestStatePacket.getErrorCode()==0){
            sendBroadcast(HandlerBroadcastProtocol.ACTION_SEND_MESSAGE_SUCCESS,requestStatePacket);
        }else {
            sendBroadcast(HandlerBroadcastProtocol.ACTION_SEND_MESSAGE_ERROR,requestStatePacket);
        }
    }

}
