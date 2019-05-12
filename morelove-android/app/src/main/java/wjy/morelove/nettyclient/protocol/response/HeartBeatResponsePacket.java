package wjy.morelove.nettyclient.protocol.response;

import wjy.morelove.nettyclient.protocol.Packet;

import static wjy.morelove.nettyclient.protocol.command.Command.HEARTBEAT_RESPONSE;

/**
 * 心跳响应包
 */
public class HeartBeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }

}
