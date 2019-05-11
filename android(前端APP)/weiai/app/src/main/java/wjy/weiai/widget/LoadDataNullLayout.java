package wjy.weiai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wjy.weiai.R;

/**
 * 加载数据，加载中，加载失败，加载完成，无数据
 *
 * @author wjy
 */
public class LoadDataNullLayout extends LinearLayout {

    public static final int HIDE_LAYOUT = 0;// 隐藏
    public static final int NETWORK_LOADING = 1;// 加载中
    public static final int NETWORK_ERROR = 2;// 网络异常
    public static final int NODATA = 3;// 无数据,不可点击刷新
    public static final int NODATA_ENABLE_CLICK = 4;// 无数据时，可点击刷新
    public static final int HTTP_REQUEST_ERROR = 5;//请求异常，404等
    public static final int JSON_PARSE_ERROR = 6;//数据解析异常

    public static final String NETWORK_ERROR_MESSAGE = "好像网络异常了！";
    public static final String NODATA_MESSAGE = "还没有数据呢！";

    private ProgressBar animProgress;
    private Context context;
    private ImageView img;
    private OnClickListener listener;
    private TextView errorShowMessage;
    private RelativeLayout rlErrorShowView;

    private String onHttpRequestErrorShowMessage = "服务器响应异常！";// 服务器响应的错误信息
    private String onErrorNoDataShowMessage = NODATA_MESSAGE;// 无数据显示的文本消息
    private int onErrorNetworkShowImageRid = R.mipmap.common_error;// 网络异常时显示的图片
    private int onErrorNoDataShowImageRid = R.mipmap.common_empty;// 无数据时显示的图片

    public void setOnHttpRequestErrorShowMessage(String onHttpRequestErrorShowMessage) {
        this.onHttpRequestErrorShowMessage = onHttpRequestErrorShowMessage;
    }

    public void setOnErrorNoDataShowMessage(String onErrorNoDataShowMessage) {
        this.onErrorNoDataShowMessage = onErrorNoDataShowMessage;
    }


    /**
     * 为控件添加点击事件
     *
     * @param listener
     */
    public void setOnLayoutClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public LoadDataNullLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LoadDataNullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    private void init() {
        View view = View.inflate(context, R.layout.view_loaddata_error_layout, null);
        rlErrorShowView = view.findViewById(R.id.rlErrorShowView);
        img = view.findViewById(R.id.img_error_layout);
        animProgress = view.findViewById(R.id.animProgress);
        errorShowMessage = view.findViewById(R.id.errorShowMessage);
        this.setOnClickListener((View v) -> {
            if (listener != null)
                listener.onClick(v);
        });
        addView(view);
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                rlErrorShowView.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);

                img.setImageResource(onErrorNetworkShowImageRid);
                errorShowMessage.setText(NETWORK_ERROR_MESSAGE);
                break;
            case NETWORK_LOADING:
                animProgress.setVisibility(View.VISIBLE);
                rlErrorShowView.setVisibility(View.GONE);
                break;
            case NODATA:
                rlErrorShowView.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);

                img.setBackgroundResource(onErrorNoDataShowImageRid);
                errorShowMessage.setText(onErrorNoDataShowMessage);
                break;
            case HIDE_LAYOUT:
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                rlErrorShowView.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);

                errorShowMessage.setText(onErrorNoDataShowMessage);
                img.setBackgroundResource(onErrorNoDataShowImageRid);
                break;
            case HTTP_REQUEST_ERROR:
                animProgress.setVisibility(GONE);
                rlErrorShowView.setVisibility(VISIBLE);

                errorShowMessage.setText(onHttpRequestErrorShowMessage);
                img.setBackgroundResource(onErrorNetworkShowImageRid);
                break;
            case JSON_PARSE_ERROR:
                rlErrorShowView.setVisibility(VISIBLE);
                animProgress.setVisibility(GONE);

                img.setBackgroundResource(onErrorNoDataShowImageRid);
                errorShowMessage.setText("数据解析异常");
                break;
            default:
                break;
        }
    }


}
