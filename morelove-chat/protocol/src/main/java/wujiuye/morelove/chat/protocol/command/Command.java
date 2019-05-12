package wujiuye.morelove.chat.protocol.command;

/**
 * 协议、命令、操作、指令
 * 一个字节
 *
 * @author wjy
 */
public interface Command {

    Byte REQUEST_STATE = 0;//请求状态，如给对方发送消息时，将发送结果告知发送方法

    Byte HEARTBEAT_REQUEST = 1;//心跳包请求包

    Byte HEARTBEAT_RESPONSE = 2;//心跳包响应包

    Byte LOGIN_REQUEST = 2;//标志此数据包为登录请求

    Byte LOGIN_RESPONSE = 3;//标志此数据包为响应登录请求

    Byte MESSAGE_REQUEST = 4;//标志此数据包为消息请求

    Byte MESSAGE_RESPONSE = 5;//标志此数据包为响应消息请求的

}
