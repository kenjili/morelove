package wujiuye.morelove.chat.packet.request;


import wujiuye.morelove.chat.protocol.command.Command;
import wujiuye.morelove.chat.protocol.packet.Packet;

/**
 * 心跳包
 *
 */
public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_REQUEST;
    }
}
