package wjy.weiai.nettyclient.protocol.request;

import wjy.weiai.nettyclient.protocol.Packet;

import static wjy.weiai.nettyclient.protocol.command.Command.HEARTBEAT_REQUEST;

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
