package wujiuye.morelove.chat.packet.response;

import wujiuye.morelove.chat.protocol.command.Command;
import wujiuye.morelove.chat.protocol.packet.Packet;

public class RequestStatePacket extends Packet {

    private int errorCode;
    private String errorMessage;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Byte getCommand() {
        return Command.REQUEST_STATE;
    }
}
