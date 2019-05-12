package wjy.morelove.nettyclient.protocol.response;

import wjy.morelove.nettyclient.protocol.Packet;
import wjy.morelove.nettyclient.protocol.command.Command;

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
