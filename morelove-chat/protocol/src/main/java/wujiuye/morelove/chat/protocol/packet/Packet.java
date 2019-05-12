package wujiuye.morelove.chat.protocol.packet;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 数据包抽象
 *
 */
public abstract class Packet {
    //当前版本号
    public static final Byte CURRENT_VERSION = 1;

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = CURRENT_VERSION;

    @JSONField(serialize = false)
    public abstract Byte getCommand();

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }
}
