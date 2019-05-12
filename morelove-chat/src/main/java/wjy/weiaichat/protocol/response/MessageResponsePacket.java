package wjy.weiaichat.protocol.response;


import wjy.weiaichat.bean.Message;
import wjy.weiaichat.protocol.Packet;
import wjy.weiaichat.session.User;

import java.util.List;

import static wjy.weiaichat.protocol.command.Command.MESSAGE_RESPONSE;

/**
 * 响应消息
 * @author wjy
 */
public class MessageResponsePacket extends Packet {

    private User fromUser;//谁发来的消息
    private List<Message> messages;//未读的消息

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}
