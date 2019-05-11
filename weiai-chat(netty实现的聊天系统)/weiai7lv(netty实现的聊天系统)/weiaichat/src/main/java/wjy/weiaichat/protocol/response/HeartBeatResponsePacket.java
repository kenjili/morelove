package wjy.weiaichat.protocol.response;

import wjy.weiaichat.protocol.Packet;

import static wjy.weiaichat.protocol.command.Command.HEARTBEAT_RESPONSE;

/**
 * 心跳响应包
 */
public class HeartBeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }

}
