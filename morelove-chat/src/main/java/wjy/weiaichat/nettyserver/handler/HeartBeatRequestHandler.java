package wjy.weiaichat.nettyserver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wjy.weiaichat.protocol.request.HeartBeatRequestPacket;
import wjy.weiaichat.protocol.response.HeartBeatResponsePacket;

/**
 * 心跳处理器
 * 响应客户端的心跳包
 */
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket requestPacket) {
        //System.out.println("响应客户端心跳包。。。。。");
        //响应一个心跳包
        ctx.channel().writeAndFlush(new HeartBeatResponsePacket());
    }
}