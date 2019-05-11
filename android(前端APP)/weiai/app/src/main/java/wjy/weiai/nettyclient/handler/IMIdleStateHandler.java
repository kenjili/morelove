package wjy.weiai.nettyclient.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 空闲检测
 * @author wjy
 */
public class IMIdleStateHandler extends IdleStateHandler {

    //读超时
    private static final int READER_IDLE_TIME = 15;


    public IMIdleStateHandler() {
        //单位秒
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    /**
     * 读超时关闭连接
     * @param ctx
     * @param evt
     */
    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        if(evt.state() == IdleState.READER_IDLE){
            System.out.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
            ctx.channel().close();
        }
    }
}
