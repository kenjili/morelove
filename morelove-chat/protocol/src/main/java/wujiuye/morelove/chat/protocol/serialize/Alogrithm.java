package wujiuye.morelove.chat.protocol.serialize;

/**
 * 序列化算法枚举类型
 * @author wjy
 */
public enum Alogrithm {

    JSON((byte)0x01);

    //算法只能占用数据包的一个字节
    byte value;

    Alogrithm(byte value){
        this.value = value;
    }

    public byte getValue(){
        return this.value;
    }
}
