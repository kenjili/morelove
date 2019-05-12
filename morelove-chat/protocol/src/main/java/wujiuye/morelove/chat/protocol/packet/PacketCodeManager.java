package wujiuye.morelove.chat.protocol.packet;


import io.netty.buffer.ByteBuf;
import wujiuye.morelove.chat.protocol.command.Command;
import wujiuye.morelove.chat.protocol.serialize.Serializer;
import wujiuye.morelove.chat.protocol.serialize.impl.JSONSerializer;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据包编解码实现类
 */
public class PacketCodeManager {

    public static final byte[] MAGIC_NUMBER = new byte[]{'j', 'i', 'u', 'y', 'e'};//魔数
    public static final PacketCodeManager INSTANCE = new PacketCodeManager();//单例

    //key为命令，value为包类型
    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    //key为序列化算法名称，value为序列化算法实现类
    private final Map<Byte, Serializer> serializerMap;


    private PacketCodeManager() {
        packetTypeMap = new HashMap<>();
        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();//json序列号算法
        serializerMap.put(serializer.getSerializerAlogrithm(), serializer);
    }

    /**
     * 必须要被调用一次
     *
     * @param registPacket
     */
    public void init(IRegistPacket registPacket) {
        if (registPacket != null) {
            packetTypeMap.put(Command.REQUEST_STATE, registPacket.registPacket(Command.REQUEST_STATE));//响应请求状态的数据包
            packetTypeMap.put(Command.HEARTBEAT_REQUEST, registPacket.registPacket(Command.REQUEST_STATE));//心跳请求包
            packetTypeMap.put(Command.HEARTBEAT_RESPONSE, registPacket.registPacket(Command.REQUEST_STATE));//心跳响应包
            packetTypeMap.put(Command.LOGIN_REQUEST, registPacket.registPacket(Command.REQUEST_STATE));//登录数据包
            packetTypeMap.put(Command.LOGIN_RESPONSE, registPacket.registPacket(Command.REQUEST_STATE));//登录响应数据包
            packetTypeMap.put(Command.MESSAGE_REQUEST, registPacket.registPacket(Command.REQUEST_STATE));//接收的消息数据包
            packetTypeMap.put(Command.MESSAGE_RESPONSE, registPacket.registPacket(Command.REQUEST_STATE));//响应的消息数据包
        }
    }


    /**
     * 数据包编码
     *
     * @param byteBuf
     * @param packet
     */
    public void encode(ByteBuf byteBuf, Packet packet) {
        // 1. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        //对消息内容加密
        bytes = Base64.getEncoder().encode(bytes);
        // 2. 实际编码过程
        //魔术+一个字节的版本号+一个字节的序列化算法+一个字节的指令+四个字节的数据长度+实际数据
        byteBuf.writeBytes(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());//写入版本号
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlogrithm());//写入序列化算法
        byteBuf.writeByte(packet.getCommand());//写入命令、协议
        byteBuf.writeInt(bytes.length);//写入序列化数据的长度
        byteBuf.writeBytes(bytes);//写入序列化数据
    }


    /**
     * 解码为数据包
     *
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(MAGIC_NUMBER.length);

        // 读取版本号
        byte version = byteBuf.readByte();
        if (version != Packet.CURRENT_VERSION) {
            //版本号不一致解析不了
            return null;
        }

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        //对消息内容解密
        bytes = Base64.getDecoder().decode(bytes);
        //解码数据
        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
