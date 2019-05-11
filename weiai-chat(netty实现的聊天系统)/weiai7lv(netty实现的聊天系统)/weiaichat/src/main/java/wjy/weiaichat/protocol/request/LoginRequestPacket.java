package wjy.weiaichat.protocol.request;

import wjy.weiaichat.protocol.Packet;

import static wjy.weiaichat.protocol.command.Command.LOGIN_REQUEST;

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

        return LOGIN_REQUEST;
    }
}
