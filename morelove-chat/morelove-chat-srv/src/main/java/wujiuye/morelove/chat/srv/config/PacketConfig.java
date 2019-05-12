package wujiuye.morelove.chat.srv.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import wujiuye.morelove.chat.packet.request.HeartBeatRequestPacket;
import wujiuye.morelove.chat.packet.request.LoginRequestPacket;
import wujiuye.morelove.chat.packet.response.HeartBeatResponsePacket;
import wujiuye.morelove.chat.packet.response.LoginResponsePacket;
import wujiuye.morelove.chat.packet.response.RequestStatePacket;
import wujiuye.morelove.chat.protocol.command.Command;
import wujiuye.morelove.chat.protocol.packet.IRegistPacket;
import wujiuye.morelove.chat.protocol.packet.Packet;
import wujiuye.morelove.chat.protocol.packet.PacketCodeManager;


/**
 * @author wujiuye
 * @version 1.0 on 2019/5/12 {描述：
 * 注册数据包
 * }
 */
@Component
public class PacketConfig implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        PacketCodeManager.INSTANCE.init(new IRegistPacket() {
            @Override
            public Class<? extends Packet> registPacket(Byte command) {
                if (command == Command.HEARTBEAT_REQUEST) {
                    return HeartBeatRequestPacket.class;
                } else if (command == Command.HEARTBEAT_RESPONSE) {
                    return HeartBeatResponsePacket.class;
                } else if (command == Command.LOGIN_REQUEST) {
                    return LoginRequestPacket.class;
                } else if (command == Command.LOGIN_RESPONSE) {
                    return LoginResponsePacket.class;
                } else if (command == Command.MESSAGE_REQUEST) {
                    return HeartBeatRequestPacket.class;
                } else if (command == Command.MESSAGE_RESPONSE) {
                    return HeartBeatResponsePacket.class;
                } else if (command == Command.REQUEST_STATE) {
                    return RequestStatePacket.class;
                }
                return null;
            }
        });
    }
}
