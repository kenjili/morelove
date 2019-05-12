package wujiuye.morelove.chat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wujiuye.morelove.chat.packet.response.RequestStatePacket;


public class StateResponseHandler extends SimpleChannelInboundHandler<RequestStatePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestStatePacket requestStatePacket) {
        System.out.println(requestStatePacket.getErrorCode()+"==>"+ requestStatePacket.getErrorMessage());
    }

}
