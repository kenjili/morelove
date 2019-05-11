package wjy.weiai.nettyclient.protocol.response;

import wjy.weiai.nettyclient.protocol.Packet;
import wjy.weiai.nettyclient.protocol.command.Command;

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
