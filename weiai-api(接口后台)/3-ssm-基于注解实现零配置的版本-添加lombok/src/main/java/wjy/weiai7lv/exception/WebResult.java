package wjy.weiai7lv.exception;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义接口返回类型，用于统一返回json数据的格式
 */
public class WebResult {

    private Integer errorCode;
    private String errorMessage;
    private Object data;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
