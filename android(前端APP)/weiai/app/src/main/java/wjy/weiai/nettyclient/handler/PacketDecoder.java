package wjy.weiai.nettyclient.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import wjy.weiai.nettyclient.protocol.PacketCodeManager;

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
