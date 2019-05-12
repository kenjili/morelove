package wujiuye.morelove.chat.packet.response;

import wujiuye.morelove.chat.protocol.packet.Packet;
import wujiuye.morelove.chat.protocol.command.Command;

/**
 * 心跳响应包
 */
public class HeartBeatResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }

}
