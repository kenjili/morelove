package wujiuye.morelove.chat.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wujiuye.morelove.chat.packet.bean.Message;
import wujiuye.morelove.chat.packet.bean.User;
import wujiuye.morelove.chat.packet.response.MessageResponsePacket;


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
