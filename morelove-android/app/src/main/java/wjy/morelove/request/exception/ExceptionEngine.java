package wjy.morelove.request.exception;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;

/**
 * 异常统一处理，将异常统一转换为ApiException
 * Created by wujiuye on 2017/7/14.
 */
public class ExceptionEngine {

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof VolleyError) {
            if (e instanceof NetworkError) {
                //网络异常
                ex = new ApiException(e, ApiException.ErrorCode.NETWORD_ERROR);
                ex.message = "网络错误,请检查网络连接!";
                return ex;
            }else if(e instanceof TimeoutError){
                //网络超时异常
                ex = new ApiException(e, ApiException.ErrorCode.NETWORD_ERROR);
                ex.message = "网络请求超时，请重试!";
                return ex;
            }else{
                //HTTP请求错误(服务器异常)
                VolleyError volleyError = (VolleyError) e;
                ex = new ApiException(e, ApiException.ErrorCode.HTTP_ERROR);
                ex.message = "请求目标服务器出错" + (volleyError.networkResponse==null?"":"响应码："+volleyError.networkResponse.statusCode);
                return ex;
            }
        } else if (e instanceof ServerException) {
            //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, ApiException.ErrorCode.HTTP_RESULT_ERROR);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JSONException) {
            //json解析异常
            ex = new ApiException(e, ApiException.ErrorCode.PARSE_ERROR);
            ex.message = "数据解析错误!";
            return ex;
        } else {
            ex = new ApiException(e, ApiException.ErrorCode.UNKNOWN);
            ex.message = "未知错误!";
            return ex;
        }
    }
}

