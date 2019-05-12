package wjy.morelove.request.exception;

/**
 * 封装网络请求可能出现的异常，包括数据解析异常
 * Created by wujiuye on 2017/7/14.
 */

public class ApiException extends Exception {

    /**
     *  约定异常
     * Created by wujiuye on 2017/7/14.
     */
    public interface ErrorCode {
        /**
         * 未知错误
         */
        int UNKNOWN = 1000;
        /**
         * json数据解析错误
         */
        int PARSE_ERROR = 1001;
        /**
         * 网络连接错误
         */
        int NETWORD_ERROR = 1002;
        /**
         * HTTP请求（协议）出错
         */
        int HTTP_ERROR = 1003;

        /**
         * 服务器端成功响应后返回的错误信息如errorCode=0,errorMessage="未登录"
         */
        int HTTP_RESULT_ERROR = 1086;
    }

    /**
     * 服务器响应返回的处理异常代码
     * 与服务器的必须一致
     */
    public interface ApiResponseErrorCode{
        int EXCEPTION = -1;
        int SUCCESS = 0;
        int NOTLOGIN = 1;
        int NOTPERM = 2;
        int NOTPERM_BINDLOVER = 3;
        int NOTPERM_ADMIN = 4;
    }

    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}