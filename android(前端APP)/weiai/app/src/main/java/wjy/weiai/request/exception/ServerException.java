package wjy.weiai.request.exception;

/**
 * 服务器返回的错误
 * Created by wujiuye on 2017/7/14.
 */

public class ServerException extends RuntimeException {
    public int code;
    public String message;

    public ServerException(int code,String message) {
        this.code = code;
        this.message = message;
    }
}
