package wjy.weiaichat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import wjy.weiaichat.client.handler.HeartBeatTimerHandler;
import wjy.weiaichat.client.handler.LoginResponseHandler;
import wjy.weiaichat.client.handler.MessageResponseHandler;
import wjy.weiaichat.client.handler.StateResponseHandler;
import wjy.weiaichat.handler.IMIdleStateHandler;
import wjy.weiaichat.handler.PacketDecoder;
import wjy.weiaichat.handler.PacketEncoder;
import wjy.weiaichat.handler.Spliter;
import wjy.weiaichat.protocol.request.LoginRequestPacket;
import wjy.weiaichat.protocol.request.MessageRequestPacket;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * netty客户端
 * 例子，测试用
 *
 * @author wjy
 */
public class NettyClient {
    private static final int MAX_RETRY = 5;//最大重连次数
//    private static final String HOST = "127.0.0.1";//服务端ip

    private static final String HOST = "47.92.255.174";//服务端ip
    private static final int PORT = 1314;//端口


    public volatile static Boolean isLogin = false;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
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

                        ch.pipeline().addLast(new StateResponseHandler());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());

                        ch.pipeline().addLast(new PacketEncoder());
                        //心跳定时器
                        ch.pipeline().addLast(new HeartBeatTimerHandler());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                new Thread(() -> startLogin(channel)).start();
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    private static void startLogin(final Channel channel) {
        System.out.println("正在登录...");
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserName("春玲");
        loginRequestPacket.setPassword("123456");
        // 发送登录数据包
        channel.writeAndFlush(loginRequestPacket);

        while (!isLogin) {
            if (!channel.isActive()) {
                System.err.println("连接已经断开...");
                return;
            }
            System.out.println("等待服务器响应...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Scanner sc = new Scanner(System.in);
        while (!Thread.interrupted()) {
            MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
            messageRequestPacket.setToUserName(sc.next());
            messageRequestPacket.setMessage(sc.next());
            channel.writeAndFlush(messageRequestPacket);
        }
    }
}