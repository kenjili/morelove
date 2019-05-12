package wjy.morelove.nettyclient.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import wjy.morelove.nettyclient.protocol.Packet;
import wjy.morelove.nettyclient.protocol.PacketCodeManager;


/**
 * 消息数据转字节数组编码器
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
        PacketCodeManager.INSTANCE.encode(out, packet);
    }

}
