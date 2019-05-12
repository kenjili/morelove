package wujiuye.morelove.chat.packet.response;



import wujiuye.morelove.chat.packet.bean.Message;
import wujiuye.morelove.chat.packet.bean.User;
import wujiuye.morelove.chat.protocol.packet.Packet;
import wujiuye.morelove.chat.protocol.command.Command;

import java.util.List;

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

        return Command.MESSAGE_RESPONSE;
    }
}
