package wujiuye.morelove.chat.protocol.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import wujiuye.morelove.chat.protocol.packet.PacketCodeManager;

import java.util.List;

/**
 * 字节转消息数据解码器
 */
public class PacketDecoder extends ByteToMessageDecoder{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        out.add(PacketCodeManager.INSTANCE.decode(in));
    }

}
