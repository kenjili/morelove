package wujiuye.morelove.chat.srv;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import wujiuye.morelove.chat.packet.request.HeartBeatRequestPacket;
import wujiuye.morelove.chat.packet.request.LoginRequestPacket;
import wujiuye.morelove.chat.packet.request.MessageRequestPacket;
import wujiuye.morelove.chat.packet.response.HeartBeatResponsePacket;
import wujiuye.morelove.chat.packet.response.LoginResponsePacket;
import wujiuye.morelove.chat.packet.response.MessageResponsePacket;
import wujiuye.morelove.chat.packet.response.RequestStatePacket;
import wujiuye.morelove.chat.protocol.command.Command;
import wujiuye.morelove.chat.protocol.packet.IRegistPacket;
import wujiuye.morelove.chat.protocol.packet.Packet;
import wujiuye.morelove.chat.protocol.packet.PacketCodeManager;


/**
 * 服务端入口
 *
 * @author wjy
 */
public class NettyServer {

    public static void main(String[] args) {
        PacketCodeManager.INSTANCE.init(new IRegistPacket() {
            @Override
            public Class<? extends Packet> registPacket(Byte command) {
                if (command.equals(Command.HEARTBEAT_REQUEST)) {
                    return HeartBeatRequestPacket.class;
                } else if (command.equals(Command.HEARTBEAT_RESPONSE)) {
                    return HeartBeatResponsePacket.class;
                } else if (command.equals(Command.LOGIN_REQUEST)) {
                    return LoginRequestPacket.class;
                } else if (command.equals(Command.LOGIN_RESPONSE)) {
                    return LoginResponsePacket.class;
                } else if (command.equals(Command.MESSAGE_REQUEST)) {
                    return MessageRequestPacket.class;
                } else if (command.equals(Command.MESSAGE_RESPONSE)) {
                    return MessageResponsePacket.class;
                } else if (command.equals(Command.REQUEST_STATE)) {
                    return RequestStatePacket.class;
                }else{
                    return null;
                }
            }
        });
        //初始化spring
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        applicationContext.getBean("serverBootstrap");
    }
}
