package wujiuye.morelove.chat.protocol.serialize.impl;

import com.alibaba.fastjson.JSON;
import wujiuye.morelove.chat.protocol.serialize.Alogrithm;
import wujiuye.morelove.chat.protocol.serialize.Serializer;

public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlogrithm() {
        return Alogrithm.JSON.getValue();
    }

    @Override
    public byte[] serialize(Object object) {

        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {

        return JSON.parseObject(bytes, clazz);
    }
}
