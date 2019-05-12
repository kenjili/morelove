package wjy.morelove.nettyclient.protocol;

import android.annotation.TargetApi;
import android.os.Build;

import io.netty.buffer.ByteBuf;
import wjy.morelove.nettyclient.protocol.request.HeartBeatRequestPacket;
import wjy.morelove.nettyclient.protocol.request.LoginRequestPacket;
import wjy.morelove.nettyclient.protocol.request.MessageRequestPacket;
import wjy.morelove.nettyclient.protocol.response.HeartBeatResponsePacket;
import wjy.morelove.nettyclient.protocol.response.LoginResponsePacket;
import wjy.morelove.nettyclient.protocol.response.MessageResponsePacket;
import wjy.morelove.nettyclient.protocol.response.RequestStatePacket;
import wjy.morelove.nettyclient.serialize.Serializer;
import wjy.morelove.nettyclient.serialize.impl.JSONSerializer;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static wjy.morelove.nettyclient.protocol.command.Command.HEARTBEAT_REQUEST;
import static wjy.morelove.nettyclient.protocol.command.Command.HEARTBEAT_RESPONSE;
import static wjy.morelove.nettyclient.protocol.command.Command.LOGIN_REQUEST;
import static wjy.morelove.nettyclient.protocol.command.Command.LOGIN_RESPONSE;
import static wjy.morelove.nettyclient.protocol.command.Command.MESSAGE_REQUEST;
import static wjy.morelove.nettyclient.protocol.command.Command.MESSAGE_RESPONSE;
import static wjy.morelove.nettyclient.protocol.command.Command.REQUEST_STATE;

/**
 * 数据包编解码实现类
 */
public class PacketCodeManager {

    public static final byte[] MAGIC_NUMBER = new byte[]{'w','e','i','a','i'};//魔数
    public static final PacketCodeManager INSTANCE = new PacketCodeManager();//单例

    //key为命令，value为包类型
    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    //key为序列化算法名称，value为序列化算法实现类
    private final Map<Byte, Serializer> serializerMap;


    private PacketCodeManager() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(REQUEST_STATE, RequestStatePacket.class);//响应请求状态的数据包
        packetTypeMap.put(HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);//心跳请求包
        packetTypeMap.put(HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);//心跳响应包
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);//登录数据包
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);//登录响应数据包
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);//接收的消息数据包
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);//响应的消息数据包

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();//json序列号算法
        serializerMap.put(serializer.getSerializerAlogrithm(), serializer);
    }


    /**
     * 数据包编码
     * @param byteBuf
     * @param packet
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void encode(ByteBuf byteBuf, Packet packet) {
        // 1. 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        //加密
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
     * @param byteBuf
     * @return
     */
    @TargetApi(Build.VERSION_CODES.O)
    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(MAGIC_NUMBER.length);

        // 读取版本号
        byte version = byteBuf.readByte();
        if(version!=Packet.CURRENT_VERSION){
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
        //解密
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
