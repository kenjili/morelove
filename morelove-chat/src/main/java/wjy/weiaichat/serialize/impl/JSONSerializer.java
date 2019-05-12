package wjy.weiaichat.serialize.impl;

import com.alibaba.fastjson.JSON;
import wjy.weiaichat.serialize.Alogrithm;
import wjy.weiaichat.serialize.Serializer;

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
