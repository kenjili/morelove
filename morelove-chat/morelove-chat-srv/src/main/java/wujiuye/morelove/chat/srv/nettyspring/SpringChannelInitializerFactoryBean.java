package wujiuye.morelove.chat.srv.nettyspring;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import wujiuye.morelove.chat.srv.handler.HeartBeatRequestHandler;
import wujiuye.morelove.chat.protocol.handler.IMIdleStateHandler;
import wujiuye.morelove.chat.protocol.handler.PacketDecoder;
import wujiuye.morelove.chat.protocol.handler.PacketEncoder;
import wujiuye.morelove.chat.protocol.handler.Spliter;

/**
 * spring整合netty的ChannelInitializer
 * 实现ApplicationContextAware可在setApplicationContext方法获取到ApplicationContext
 *
 * @author wjy
 */
public class SpringChannelInitializerFactoryBean
        implements FactoryBean<SpringChannelInitializerFactoryBean.SpringChannelInitializer>
        , ApplicationContextAware {

    private ApplicationContext mApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.mApplicationContext = applicationContext;
    }

    public class SpringChannelInitializer extends ChannelInitializer<NioSocketChannel> {

        /**
         * 固定的处理器在这里固定注入，且要求不能是单例
         * @param nioSocketChannel
         * @throws Exception
         */
        @Override
        public void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
            //添加空闲检测
            nioSocketChannel.pipeline().addLast(new IMIdleStateHandler());
            //数据包验包器
            nioSocketChannel.pipeline().addLast(new Spliter());
            //数据包解码器
            nioSocketChannel.pipeline().addLast(new PacketDecoder());
            //要确保ChannelHandler的顺序
            //ChanellHandler有autoRelease属性netty会自动释放，连接断开会自动释放,所以确保每个新的连接使用的ChannelHandler都是存在的。
            nioSocketChannel.pipeline().addLast((ChannelHandler) mApplicationContext.getBean("loginRequestHandler"));
            nioSocketChannel.pipeline().addLast((ChannelHandler) mApplicationContext.getBean("authHandler"));
            nioSocketChannel.pipeline().addLast((ChannelHandler) mApplicationContext.getBean("messageRequestHandler"));
            //心跳处理器
            nioSocketChannel.pipeline().addLast(new HeartBeatRequestHandler());
            //数据包编码器
            nioSocketChannel.pipeline().addLast(new PacketEncoder());
        }
    }

    /**
     * 如果isSingleton返回true只会被调用一次
     *
     * @return
     * @throws Exception
     */
    @Override
    public SpringChannelInitializer getObject() throws Exception {
        return new SpringChannelInitializer();
    }

    @Override
    public Class<?> getObjectType() {
        return SpringChannelInitializer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
