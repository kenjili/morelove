package wjy.weiaichat.nettyserver.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wjy.weiaichat.bean.Message;
import wjy.weiaichat.protocol.response.RequestStatePacket;
import wjy.weiaichat.protocol.request.MessageRequestPacket;
import wjy.weiaichat.protocol.response.MessageResponsePacket;
import wjy.weiaichat.server.MessageService;
import wjy.weiaichat.server.UserService;
import wjy.weiaichat.session.User;
import wjy.weiaichat.session.UserManager;

import java.util.Date;
import java.util.List;

/**
 * SimpleChannelInboundHandler实现了channelRead方法，实现了判断数据包是否是指定的MessageRequestPacket类型的数据包，
 * 如果是则调用channelRead0方法交由自身处理，否则调用ctx的fireChannelRead方法交给下一个处理器处理
 */
@Component
public class MessageRequestHandler implements FactoryBean<MessageRequestHandler.MessageChannelRequestHandler> {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    public class MessageChannelRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {
            // 拿到消息发送方的会话信息
            User user = UserManager.getUser(ctx.channel());
            user = userService.queryUser(user.getUsername());
            // 拿到消息接收方的 channel
            Channel toUserChannel = UserManager.getChannel(messageRequestPacket.getToUserName());

            if (toUserChannel != null && UserManager.hasLogin(toUserChannel)) {
                readUserOnline(ctx, user, toUserChannel, messageRequestPacket);
            } else {
                readUserNotline(ctx, user, messageRequestPacket);
            }
        }

        /**
         * 接收方在线
         */
        private void readUserOnline(ChannelHandlerContext ctx, User user, Channel toUserChannel, MessageRequestPacket messageRequestPacket) {
            boolean flag = savaMessage(user, messageRequestPacket);
            if (flag) {
                List<Message> allNotReadMsg = messageService.readAllNotReadMsg(messageRequestPacket.getToUserName(), user.getUsername());
                MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
                messageResponsePacket.setFromUser(user);
                messageResponsePacket.setMessages(allNotReadMsg);
                //发送给接收方
                toUserChannel.writeAndFlush(messageResponsePacket);
                //响应发送方
                RequestStatePacket requestStatePacket = new RequestStatePacket();
                requestStatePacket.setErrorCode(0);
                requestStatePacket.setErrorMessage("消息发送成功！");
                ctx.channel().writeAndFlush(requestStatePacket);
            } else {
                responseError(ctx);
            }
        }

        private boolean savaMessage(User user, MessageRequestPacket messageRequestPacket) {
            String toUserName = messageRequestPacket.getToUserName();
            User toUser = userService.queryUser(toUserName);
            if (toUser == null) return false;
            Message message = new Message();
            message.setSendUser(user);
            message.setReadUser(toUser);
            message.setMsgType(messageRequestPacket.getMsgType());
            message.setCreateDate(new Date(System.currentTimeMillis()));
            //7天过期
            message.setOverdueDate(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)));
            message.setMessage(messageRequestPacket.getMessage());
            return messageService.savaMessage(message);
        }

        /**
         * 接收方不在线
         */
        private void readUserNotline(ChannelHandlerContext ctx, User user, MessageRequestPacket messageRequestPacket) {
            if (savaMessage(user, messageRequestPacket)) {
                //响应发送方
                RequestStatePacket requestStatePacket = new RequestStatePacket();
                requestStatePacket.setErrorCode(0);
                requestStatePacket.setErrorMessage("user.getUsername() 不在线，已转为离线发送!");
                ctx.channel().writeAndFlush(requestStatePacket);
            } else
                responseError(ctx);
        }

        private void responseError(ChannelHandlerContext ctx) {
            RequestStatePacket requestStatePacket = new RequestStatePacket();
            requestStatePacket.setErrorCode(-1);
            requestStatePacket.setErrorMessage("发送失败！");
            ctx.channel().writeAndFlush(requestStatePacket);
        }
    }


    @Override
    public MessageChannelRequestHandler getObject() throws Exception {
        return new MessageChannelRequestHandler();
    }

    @Override
    public Class<?> getObjectType() {
        return MessageChannelRequestHandler.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
