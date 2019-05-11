package wjy.weiaichat.protocol.request;

import wjy.weiaichat.protocol.Packet;

import static wjy.weiaichat.protocol.command.Command.HEARTBEAT_REQUEST;

/**
 * 心跳包
 *
 */
public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}
