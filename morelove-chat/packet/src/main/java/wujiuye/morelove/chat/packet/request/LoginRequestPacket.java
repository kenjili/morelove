package wujiuye.morelove.chat.packet.request;


import wujiuye.morelove.chat.protocol.command.Command;
import wujiuye.morelove.chat.protocol.packet.Packet;

public class LoginRequestPacket extends Packet {
    private String userName;
    private String password;

    public LoginRequestPacket() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Byte getCommand() {

        return Command.LOGIN_REQUEST;
    }
}
