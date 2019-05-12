package wujiuye.morelove.chat.protocol.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.internal.StringUtil;
import wujiuye.morelove.chat.protocol.packet.PacketCodeManager;

/**
 * LengthFieldBasedFrameDecoder
 * 基于数据包长度固定的数据帧解码器，解决粘包问题
 *
 * @author wjy
 */
public class Spliter extends LengthFieldBasedFrameDecoder {
    //存储数据段长度的四个字节在数据包中的偏移起始位置
    private static final int LENGTH_FIELD_OFFSET = PacketCodeManager.MAGIC_NUMBER.length + 3;//(版本号+序列化算法+协议)
    //存储数据段长度的四个字节
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //验证此数据包是否是本项目所能识别的数据包，即是否是以魔数MAGIC_NUMBER开始的
        byte[] flags = new byte[PacketCodeManager.MAGIC_NUMBER.length];
        in.readBytes(flags);
        in.resetReaderIndex();//重置一下读取指针
        String strFlag = StringUtil.toHexString(flags);
        String raleFlag = StringUtil.toHexString(PacketCodeManager.MAGIC_NUMBER);
        if (!strFlag.equals(raleFlag)) {
            System.err.println("数据包不合法！");
            ctx.channel().close();
            return null;//拦截
        }
        return super.decode(ctx, in);//交给下一个处理器处理
    }
}
