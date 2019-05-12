package wjy.weiaichat.nettyspring;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.beans.factory.FactoryBean;

import java.util.Date;

/**
 * 提供spring整合netty的ServerBootstrap
 *
 * @author wjy
 */
public class SpringServerBootstrapFactoryBean implements FactoryBean<ServerBootstrap> {

    private int port;
    private int soBackLog;
    private boolean soKeepAlive;
    private boolean tcpNoDelay;
    private SpringChannelInitializerFactoryBean.SpringChannelInitializer springChannelInitializer;

    public SpringServerBootstrapFactoryBean(SpringChannelInitializerFactoryBean.SpringChannelInitializer springChannelInitializer
            , int port) {
        this.springChannelInitializer = springChannelInitializer;
        this.port = port;
    }

    public int getSoBackLog() {
        return soBackLog;
    }

    public void setSoBackLog(int soBackLog) {
        this.soBackLog = soBackLog;
    }

    public boolean isSoKeepAlive() {
        return soKeepAlive;
    }

    public void setSoKeepAlive(boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }


    /**
     * 初始化netty
     */
    private ServerBootstrap init() {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //netty服务端引导类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                //指定处理监听连接的线程组，处理数据交互的工作线程组
                .group(boosGroup, workerGroup)
                //指定channel编程模型，即指定io编程模型，这里选用nio编程模型
                .channel(NioServerSocketChannel.class)
                //option是给服务端的channel设置属性，childOption是给客户端连接的channel设置属性
                //系统用于临时存放已完成三次握手的请求的队列最大长度
                .option(ChannelOption.SO_BACKLOG, getSoBackLog())
                //保活，即是否启用底层心跳包机制
                .childOption(ChannelOption.SO_KEEPALIVE, isSoKeepAlive())
                //tcp无延迟，true表示关闭，false表示开启。如果关闭则有数据发送时就会马上发送，否则如果需要减少发送次数减少网络交互就开启
                .childOption(ChannelOption.TCP_NODELAY, isTcpNoDelay())
                //改用注入方式
                .childHandler(springChannelInitializer);

        //绑定端口
        serverBootstrap.bind(this.port)
                .addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
                        } else {
                            throw new RuntimeException("端口[" + port + "]绑定失败!");
                        }
                    }
                });

        return serverBootstrap;
    }

    @Override
    public ServerBootstrap getObject() throws Exception {
        return this.init();
    }

    @Override
    public Class<?> getObjectType() {
        return ServerBootstrap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
