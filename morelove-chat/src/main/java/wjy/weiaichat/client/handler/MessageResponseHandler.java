package wjy.weiaichat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wjy.weiaichat.bean.Message;
import wjy.weiaichat.protocol.response.MessageResponsePacket;
import wjy.weiaichat.session.User;


public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {
        User fromUserName = messageResponsePacket.getFromUser();
        System.out.println(fromUserName.getUsername() + " ==> ");
        if (messageResponsePacket.getMessages() != null) {
            for (Message message : messageResponsePacket.getMessages()) {
                System.out.println(message.getMsgType() + "," + message.getMessage() + "," + message.getCreateDate());
            }
        }
    }
}
