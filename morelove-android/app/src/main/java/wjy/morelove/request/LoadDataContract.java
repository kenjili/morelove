package wjy.morelove.request;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import wjy.morelove.App;
import wjy.morelove.AppConfig;
import wjy.morelove.Jyson.Jyson;
import wjy.morelove.base.NetworkManager;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.request.exception.ExceptionEngine;
import wjy.morelove.request.exception.ExceptionHandler;
import wjy.morelove.request.exception.ServerException;
import wjy.morelove.utils.AppCache;

/**
 * 数据加载器
 * 建议每个Activity或Fragment对象只持有一个实例
 * 每一个实例都会持有一个vollery请求队列
 *
 * @author wjy
 */
public class LoadDataContract {

    //生命周期为整个应用的生命周期，ConcurrentHashMap线程安全
    private final static Map<String, String> sCookies = new ConcurrentHashMap<>();

    /**
     * 数据加载器加载数据需要提供一个实现该接口的对象
     *
     * @author wjy
     */
    public interface ViewDataContract {

        /**
         * 请求开始获取请求参数
         *
         * @return
         */
        Map<String, String> getParams();

        /**
         * 数据请求完成
         *
         * @param json 网络获取的数据
         */
        void onSuccess(String json);

        /**
         * 出现异常调用
         *
         * @param ex
         */
        void onError(ApiException ex);
    }


    //Activity或Fragment
    private Context context;
    //vollery请求队列
    private RequestQueue mQueue;
    //持有一个缓存对象
    private AppCache mACache;


    /**
     * 声明为使用这个构造器创建实例
     * @param iContent
     */
    @Inject
    public LoadDataContract(LoadDataContractModules.IContent iContent) {
        this.context = iContent.getContent();
        mQueue = Volley.newRequestQueue(context);
        Log.d("cache_root_path:", AppConfig.getHttpResponseCachePath(context));
        this.mACache = AppCache.get(new File(AppConfig.getHttpResponseCachePath(context) + context.getClass().getSimpleName()));
    }

    /**
     * 加载数据与刷新数据
     * 自实现了数据缓存，注意，只支持get请求的方法，post不支持（为什么不支持，比如登录请求）
     *
     * @param method post请求还是get请求
     */
    public void loadData(int method, String url, LoadDataContract.ViewDataContract resultContract) {
        //无网络时从缓存中读取
        if (method == Request.Method.GET && ((App) context.getApplicationContext()).getNetworkManager().getCuttNetworkType() == NetworkManager.NetworkType.NOT_NETWORK) {
            //将请求的url+参数拼接为缓存数据使用的key
            StringBuffer cacheKey = new StringBuffer(url);
            Map<String, String> param = resultContract.getParams();
            if (param != null) {
                cacheKey.append("_param:://");
                for (String key : param.keySet()) {
                    cacheKey.append(key);
                    cacheKey.append("/");
                    cacheKey.append(param.get(key));
                    cacheKey.append("/");
                }
            }
            String result = mACache.getAsString(cacheKey.toString());
            resultContract.onSuccess(result);
            return;
        }
        //创建一个新的请求
        StringRequest request = new MyStringRequest(method,
                url, new MyListener(method, url, resultContract), new MyErrorListener(resultContract), resultContract);
        request.setShouldCache(false);//不做任何缓存
        //将请求添加到队列,volley会自动异步执行请求
        this.mQueue.add(request);
    }


    /**
     * 响应的结果为字符串类型的请求
     * 继承StringRequest，实现自己的逻辑
     */
    private class MyStringRequest extends StringRequest {

        private LoadDataContract.ViewDataContract resultContract;


        /**
         * 构造器
         *
         * @param method         post、get请求
         * @param url            请求的url
         * @param listener       服务器响应请求成功监听器
         * @param errorListener  请求异常监听器
         * @param resultContract
         */
        private MyStringRequest(int method, String url, Response.Listener<String> listener,
                                @Nullable Response.ErrorListener errorListener, @NonNull LoadDataContract.ViewDataContract resultContract) {
            super(method, url, listener, errorListener);
            this.resultContract = resultContract;
        }

        /**
         * 重写获取请求参数方法
         * 当该请求被volley执行到时，会调用getParams方法来获取该请求所需要携带的参数
         *
         * @return
         */
        @Override
        protected Map<String, String> getParams() {
            return resultContract.getParams();
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            //拦截获取响应头
            Map<String, String> headers = response.headers;
            if (headers.containsKey("Set-Cookie")) {
                parseVooleyCookie(headers.get("Set-Cookie"));
            }
            return super.parseNetworkResponse(response);
        }

        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> header = new HashMap<>();
            //写入cookie
            header.put("Cookie", "SHIROSESSION=" + sCookies.get("SHIROSESSION") + ";");
            return header;
        }

        /**
         * 方法的作用:解析volley返回cookie
         *
         * @param cookie
         */
        public void parseVooleyCookie(String cookie) {
            String[] cookievalues = cookie.split(";");
            for (int j = 0; j < cookievalues.length; j++) {
                String[] keyValue = cookievalues[j].split("=");
                if (keyValue.length == 2) {
                    if (keyValue[0].contains("SHIROSESSION")) {
                        sCookies.put(keyValue[0], keyValue[1]);
                        //持久化cookie
                        new App.SharedPreferencesBuild(context)
                                .addKeyValue(keyValue[0], keyValue[1])
                                .submit();
                    }
                }
            }
        }
    }


    /**
     * 请求响应成功监听
     */
    private class MyListener extends BaseExceptionHandler implements Response.Listener<java.lang.String> {

        private int method;
        private String url;

        private MyListener(int method, String url, @NonNull LoadDataContract.ViewDataContract resultContract) {
            super(resultContract);
            this.method = method;
            this.url = url;
        }

        @Override
        public void onResponse(java.lang.String response) {
            RequestResult requestResult;
            try {
                requestResult = (RequestResult) new Jyson().parseJson(response, RequestResult.class);
                if (requestResult.getErrorCode() == ApiException.ApiResponseErrorCode.SUCCESS) {
                    //缓存数据
                    //将请求的url+method+参数拼接为缓存数据使用的key
                    StringBuffer cacheKey = new StringBuffer(url);
                    Map<String, String> param = resultContract.getParams();
                    if (param != null) {
                        cacheKey.append("_param:://");
                        for (String key : param.keySet()) {
                            cacheKey.append(key);
                            cacheKey.append("/");
                            cacheKey.append(param.get(key));
                            cacheKey.append("/");
                        }
                    }
                    //只有在请求成功的情况下才会缓存
                    mACache.put(cacheKey.toString(), response);
                    this.resultContract.onSuccess(response);
                } else {
                    handleException(requestResult);
                }
            } catch (Exception e) {
                //所有异常统一处理
                handleException(e);
            }
        }
    }


    /**
     * 请求异常监听
     */
    private class MyErrorListener extends BaseExceptionHandler implements Response.ErrorListener {

        private MyErrorListener(@NonNull LoadDataContract.ViewDataContract resultContract) {
            super(resultContract);
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            handleException(error);
        }
    }


    /**
     * 响应成功监听类和请求异常监听类都需要继承这个类来实现异常处理
     */
    private abstract class BaseExceptionHandler {

        LoadDataContract.ViewDataContract resultContract;

        BaseExceptionHandler(@NonNull LoadDataContract.ViewDataContract resultContract) {
            this.resultContract = resultContract;
        }

        /**
         * 统一异常处理，如果不处理，则抛给ViewDataContract接口onError处理
         *
         * @param ex
         */
        void handleException(Exception ex) {
            ApiException exception = ExceptionEngine.handleException(ex);
            //如果ExceptionHandler不处理，则交还给调用者处理
            if (!ExceptionHandler.handlerException(context, exception)) {
                this.resultContract.onError(exception);
            }
        }

        void handleException(RequestResult requestResult) {
            this.handleException(new ServerException(requestResult.getErrorCode(), requestResult.getErrorMessage()));
        }
    }
}
