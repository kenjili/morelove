package wjy.morelove.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import wjy.morelove.R;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.hook.OnActivityResultCallback;
import wjy.morelove.bean.Channel;
import wjy.morelove.widget.LoadDataNullLayout;


/**
 * 浏览器页面
 * @author wjy
 */
public class BrowserActivity extends BaseActivity {

    @BindView(R.id.progressBar)
    protected ProgressBar progressBar;

    @BindView(R.id.mWebview)
    protected WebView mWebView;

    private ValueCallback<Uri> mUploadMessage;
    private Channel channel;

    @BindView(R.id.navTitle)
    protected TextView navTitle;

    @Override
    protected void onInit() {
        initWebview();
        Channel channel = (Channel) getIntent().getExtras().getSerializable("Channel");
        this.channel = channel;
        if (channel == null || channel.getTag() == null)
            throw new NullPointerException("channel 不能为空！");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    public void onResume() {
        super.onResume();
        String url = channel.getTag().toString().replace("[callToUrl]", "");
        Log.e("url", url);
        this.mWebView.loadUrl(url);
    }


    private void initWebview() {
        // 自适应屏幕
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        //滚动过程中去除滚动条右边出现白边
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//设置缓存
        webSettings.setJavaScriptEnabled(true);//设置能够解析Javascript
        webSettings.setDomStorageEnabled(true);//设置适应Html5
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //允许混合加载http和https页面
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //禁用横向滚动条
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 绑定webviewclient
        WebViewClient webviewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                LoadDataNullLayout loadDataNullLayout = BrowserActivity.this.findViewById(R.id.errorLayout);
                if(loadDataNullLayout!=null) {
                    loadDataNullLayout.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            /**
             * 拦截即将要跳转的url，自己处理跳转
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Android8.0开始WebView的shouldOverrideUrlLoading返回值是false才会自动重定向，并且无需调用loadUrl，与8.0之前的效果刚好相反。
                //Android8.0以下的需要返回true 并且需要loadUrl；8.0之后效果相反
                if (Build.VERSION.SDK_INT < 26) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }


            //加载错误的时候会回调
            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                if (webResourceRequest.isForMainFrame()) {
                    LoadDataNullLayout loadDataNullLayout = BrowserActivity.this.findViewById(R.id.errorLayout);
                    if(loadDataNullLayout!=null) {
                        loadDataNullLayout.setVisibility(View.VISIBLE);
                        mWebView.setVisibility(View.INVISIBLE);
                        loadDataNullLayout.setErrorType(LoadDataNullLayout.NETWORK_ERROR);
                        loadDataNullLayout.setOnLayoutClickListener(view->{
                            mWebView.reload();
                        });
                    }
                }
            }

        };

        mWebView.setWebViewClient(webviewClient);

        // 绑定chromeClient
        WebChromeClient webviewChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String t) {
                super.onReceivedTitle(view, t);
                //t为页面的标题
                navTitle.setText(t.length()<=6?t:(t.substring(0,6)+"..."));
            }

            //加载进度改变
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                progressBar.setProgress(progress);
                if (progressBar.getProgress() == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            /**
             * 重写openFileChooser 支持<input type="file"/>文件选择上传
             *
             * @param uploadMsg
             * @param acceptType
             * @param capture
             */
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                mUploadMessage = uploadMsg;// 保存选择文件后的回调接口
                if (acceptType.indexOf("video") != -1) {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    BrowserActivity.this.startActivityFormResult(intent, 100, new OnActivityResultCallback() {
                        @Override
                        public void onActivityResult(int requestCode, int resultCode, Intent data) {

                        }
                    });
                } else {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");
                    BrowserActivity.this.startActivityFormResult(i, 200, new OnActivityResultCallback() {
                        @Override
                        public void onActivityResult(int requestCode, int resultCode, Intent data) {

                        }
                    });
                }
            }

        };
        mWebView.setWebChromeClient(webviewChromeClient);
    }


    @Override
    public void doBack(View ivBack) {
        if(mWebView.canGoBack()){
            mWebView.goBack();
            return;
        }
        super.doBack(ivBack);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
