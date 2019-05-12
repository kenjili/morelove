package wjy.morelove.request;

import java.io.Serializable;


/**
 * api请求结果
 * @author wjy
 *
 */
public class RequestResult implements Serializable {

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

}
