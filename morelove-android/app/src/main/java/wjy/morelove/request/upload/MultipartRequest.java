package wjy.morelove.request.upload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import wjy.androidlibs.imageselector.bean.ImageItem;
import wjy.morelove.App;

/**
 * 多文件上传，volley+httpmime实现，不支持进度监听
 *
 * @author wjy
 */
public class MultipartRequest extends Request<String> {

    private final Response.Listener<String> mListener;

    private Map<String, String> mParams;//上传文件时附带的参数
    private String mFilePartName;//文件对应的字段名
    private ArrayList<ImageItem> mFileParts;//图片文件
    private final OnProcessChangeListener onProcessChangeListener;

    private Context context;

    /**
     * 进度通知的Handler，使用创建这个对象的线程
     */
    private Handler notificationHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0) {
                onProcessChangeListener.onProcessChange((Long) msg.obj);
            }
        }
    };

    public MultipartRequest(Context context, String url, Map<String, String> params, String imgFieldName, ArrayList<ImageItem> imgFiles,
                            Response.Listener<String> stringListener,
                            @Nullable OnProcessChangeListener onProcessChangeListener,
                            @Nullable Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.context = context;
        this.mParams = params;
        this.mFileParts = imgFiles;
        this.mFilePartName = imgFieldName;
        this.mListener = stringListener;
        this.onProcessChangeListener = onProcessChangeListener;
        //设置重试策略，设置链接超时和读取超时为6秒，最大重试次数为1
        setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> header = new HashMap<>();
        //写入cookie
        String jsessionid = new App.SharedPreferencesBuild(context).getValueForKey("SHIROSESSION");
        if (jsessionid != null) {
            header.put("Cookie", "SHIROSESSION=" + jsessionid + ";");
        }
        return header;
    }

    private String LINE_END = "\r\n";//换行符
    private String PREFIX = "--";//固定的分界行的前缀
    private final String BOUNDARY = UUID.randomUUID().toString();//随机的boundary

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }


    @Override
    public boolean isUploadFile() {
        return true;
    }


    /**
     * 处理文件上传
     *
     * @param connection
     * @throws IOException
     */
    @Override
    public void handUploadRequest(HttpURLConnection connection) throws IOException {
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", this.getBodyContentType());

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        StringBuffer sb = new StringBuffer();
        if (mParams != null && mParams.size() > 0) {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                sb.append(PREFIX).append(BOUNDARY).append(LINE_END);//分界符行
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                sb.append(LINE_END);//空一行再输出值
                sb.append(entry.getValue() + LINE_END);
            }
        }

        //使用utf8编码向流中写入字符串
        outputStream.writeUTF(sb.toString());

        long currentUploadSize = 0;//当前上传的字节数
        long notifictionSize = 0;
        for (int i = 0; i < mFileParts.size(); i++) {
            ImageItem uploadFile = mFileParts.get(i);
            outputStream.writeBytes(PREFIX + BOUNDARY + LINE_END);//分界符行
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + mFilePartName + "\"; filename=\"" + uploadFile.path + "\"" + LINE_END);
            String imgType = uploadFile.path.contains("png") ? "png" : (uploadFile.path.contains("gif") ? "gif" : "jpeg");
            outputStream.writeBytes("Content-Type: image/" + imgType + LINE_END);
            outputStream.writeBytes(LINE_END);//空一行再输出图片

            FileInputStream fStream = new FileInputStream(uploadFile.path);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length;
            while ((length = fStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                currentUploadSize+=length;
                //通知进度
                notifictionSize+=length;
                //改变大于100kb才通知一次
                if(notifictionSize>102400){
                    //不要在这里直接调用接口通知进度改变，
                    //因为监听者可能会执行些很耗时的操作，导致上传被中断了或超时了
                    Message message = new Message();
                    message.what=0;
                    message.obj = currentUploadSize;
                    notificationHandler.sendMessage(message);
                    notifictionSize = 0;
                }
            }
            //结束之后要写换行
            outputStream.writeBytes(LINE_END);
            fStream.close();
        }
        outputStream.writeBytes(PREFIX + BOUNDARY + PREFIX + LINE_END);//结束行分界符
        outputStream.flush();
        outputStream.close();
        //结束也通知一次
        Message message = new Message();
        message.what=0;
        message.obj = currentUploadSize;
        notificationHandler.sendMessage(message);
    }

}
