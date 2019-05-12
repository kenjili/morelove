package wujiuye.morelove.chat.srv.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import wujiuye.morelove.chat.srv.session.UserManager;


/**
 * 授权处理器
 * 验证用户是否已经登录
 */
@Component
public class AuthHandler implements FactoryBean<AuthHandler.AuthChannelHandler> {

    public class AuthChannelHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //判断用户是否已经登录
            if (!UserManager.hasLogin(ctx.channel())) {
                System.err.println("未登录!");
                ctx.channel().close();
            } else {
                //从责任链中移除
                ctx.pipeline().remove(this);//热插拔，因为每个新的连接都要求登录一次，所以只有通过一次登录验证往后就不需要再验证了
                super.channelRead(ctx, msg);
            }
        }
    }

    @Override
    public AuthChannelHandler getObject() throws Exception {
        return new AuthChannelHandler();
    }

    @Override
    public Class<?> getObjectType() {
        return AuthChannelHandler.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
