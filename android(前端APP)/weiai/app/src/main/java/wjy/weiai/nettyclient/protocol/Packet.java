package wjy.weiai.nettyclient.protocol;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 数据包抽象
 *
 */
public abstract class Packet implements Serializable{
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
