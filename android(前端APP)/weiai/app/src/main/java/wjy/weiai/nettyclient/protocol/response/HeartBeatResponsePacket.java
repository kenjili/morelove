package wjy.weiai.nettyclient.protocol.response;

import wjy.weiai.nettyclient.protocol.Packet;

import static wjy.weiai.nettyclient.protocol.command.Command.HEARTBEAT_RESPONSE;

/**
 * 心跳响应包
 */
public class HeartBeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }

}
