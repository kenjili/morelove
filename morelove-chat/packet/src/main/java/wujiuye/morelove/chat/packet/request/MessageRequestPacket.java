package wujiuye.morelove.chat.packet.request;


import wujiuye.morelove.chat.protocol.packet.Packet;

import static wujiuye.morelove.chat.protocol.command.Command.MESSAGE_REQUEST;

/**
 * 请求消息
 * @author wjy
 */
public class MessageRequestPacket extends Packet {

    private String toUserName;//发送给谁
    private int msgType;//消息类型，0为文本与表情、1为图片、2为语音、3为小视频
    private String message;//当msgType=0时，其为消息内容；当msgType=1时，其为图片的链接；...

    public MessageRequestPacket() {

    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
