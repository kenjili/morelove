package wjy.weiaichat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wjy.weiaichat.client.NettyClient;
import wjy.weiaichat.protocol.response.LoginResponsePacket;
import wjy.weiaichat.session.User;


public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        String userName = loginResponsePacket.getUserName();
        if (loginResponsePacket.isSuccess()) {
            System.out.println("[" + userName + "]登录成功!");
            User user = new User();
            user.setUsername(userName);
            NettyClient.isLogin = true;
        } else {
            System.out.println("[" + userName + "]登录失败，原因：" + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接被关闭!");
    }
}
