package wjy.weiai.nettyclient.serialize.impl;


import wjy.weiai.nettyclient.serialize.Alogrithm;
import wjy.weiai.nettyclient.serialize.Serializer;
import com.alibaba.fastjson.JSON;

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
