package wjy.morelove.request.exception;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import wjy.morelove.activity.BindLoverActivity;
import wjy.morelove.activity.LoginActivity;

/**
 * 统一异常处理
 */
public class ExceptionHandler {

    /**
     * 处理异常 包括网络异常，数据解析异常，以及服务器返回的错误码异常
     *
     * @param context
     * @param exception
     * @return false表示不处理，true表示统一处理
     */
    public static synchronized boolean handlerException(Context context, ApiException exception) {
        //处理服务器端成功响应后返回的错误
        if (exception.code == ApiException.ErrorCode.HTTP_RESULT_ERROR) {
            ServerException serverException = (ServerException) exception.getCause();
            if (serverException.code == ApiException.ApiResponseErrorCode.NOTLOGIN) {
                //用户未登录
                if (context instanceof Activity) {
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                //跳转登录页面
                intent.setClass(context, LoginActivity.class);
                context.startActivity(intent);
                return true;
            } else if (serverException.code == ApiException.ApiResponseErrorCode.NOTPERM_BINDLOVER) {
                //用户未绑定情侣关系处理，跳转到绑定情侣关系页面
                Intent intent = new Intent();
                intent.setClass(context, BindLoverActivity.class);
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }

}
