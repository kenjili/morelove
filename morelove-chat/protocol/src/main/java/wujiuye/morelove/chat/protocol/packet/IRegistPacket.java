package wujiuye.morelove.chat.protocol.packet;


/**
 * @author wujiuye
 * @version 1.0 on 2019/5/12 {描述：}
 */
public interface IRegistPacket {

    /**
     * 根据命令注册数据包类型
     *
     * @param command
     * @return
     */
    Class<? extends Packet> registPacket(Byte command);

}
