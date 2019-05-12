package wjy.weiaichat.protocol.response;


import wjy.weiaichat.protocol.Packet;

import static wjy.weiaichat.protocol.command.Command.LOGIN_RESPONSE;

public class LoginResponsePacket extends Packet {

    private String userName;
    private boolean success;

    private String reason;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
