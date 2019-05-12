package wjy.morelove.nettyclient.client;

import android.content.Context;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import wjy.morelove.nettyclient.client.handler.HeartBeatTimerHandler;
import wjy.morelove.nettyclient.client.handler.LoginResponseHandler;
import wjy.morelove.nettyclient.client.handler.MessageResponseHandler;
import wjy.morelove.nettyclient.client.handler.StateResponseHandler;
import wjy.morelove.nettyclient.handler.IMIdleStateHandler;
import wjy.morelove.nettyclient.handler.PacketDecoder;
import wjy.morelove.nettyclient.handler.PacketEncoder;
import wjy.morelove.nettyclient.handler.Spliter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * netty客户端
 * 例子，测试用
 *
 * @author wjy
 */
public class ChatClient {
    private static final int MAX_RETRY = 5;//最大重连次数
    private static final String HOST = "47.92.255.174";//服务端ip
    private static final int PORT = 1314;//端口

    public interface OnConnectServerListener {
        void onConnectSuccess(Channel channel);

        void onReconnect(int value);//重连，value为第几次重连

        void onConnectFail();
    }

    private OnConnectServerListener onConnectServerListener;
    private Context mContext;

    public ChatClient(Context context, OnConnectServerListener onConnectServerListener) {
        this.mContext = context;
        this.onConnectServerListener = onConnectServerListener;
    }

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap;

    public void connectServier() {
        bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        //空闲检测
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());

                        ch.pipeline().addLast(new StateResponseHandler(mContext));
                        ch.pipeline().addLast(new LoginResponseHandler(mContext));
                        ch.pipeline().addLast(new MessageResponseHandler(mContext));

                        ch.pipeline().addLast(new PacketEncoder());
                        //心跳定时器
                        ch.pipeline().addLast(new HeartBeatTimerHandler());
                    }
                });

        try {
            connect(bootstrap, HOST, PORT, MAX_RETRY);
        }catch (Exception e){
            if(onConnectServerListener!=null) {
                onConnectServerListener.onConnectFail();
            }
        }
    }

    private void connect(Bootstrap bootstrap, String host, int port, int retry) {
        this.channel = null;
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                this.channel = channel;
                if (onConnectServerListener != null)
                    onConnectServerListener.onConnectSuccess(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
                if (onConnectServerListener != null)
                    onConnectServerListener.onConnectFail();
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                if (onConnectServerListener != null)
                    onConnectServerListener.onReconnect(order);
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

}
