package wjy.morelove.nettyclient.protocol.request;

import wjy.morelove.nettyclient.protocol.Packet;

import static wjy.morelove.nettyclient.protocol.command.Command.HEARTBEAT_REQUEST;

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
