package wjy.weiaichat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wjy.weiaichat.protocol.response.RequestStatePacket;


public class StateResponseHandler extends SimpleChannelInboundHandler<RequestStatePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestStatePacket requestStatePacket) {
        System.out.println(requestStatePacket.getErrorCode()+"==>"+ requestStatePacket.getErrorMessage());
    }

}
