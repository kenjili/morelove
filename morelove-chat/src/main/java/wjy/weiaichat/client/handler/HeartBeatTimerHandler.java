package wjy.weiaichat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import wjy.weiaichat.protocol.request.HeartBeatRequestPacket;

import java.util.concurrent.TimeUnit;

/**
 * 心跳定时处理器
 */
public class HeartBeatTimerHandler extends ChannelInboundHandlerAdapter {

    private static final int HEARTBEAT_INTERVAL = 5;

    /**'
     * 连接成功后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //5秒响应一次心跳
        ctx.executor().scheduleAtFixedRate(() -> {
            //System.out.println("发送心跳包....");
            //响应心跳包
            ctx.writeAndFlush(new HeartBeatRequestPacket());
        }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
        super.channelActive(ctx);
    }
}