package wjy.weiai.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import wjy.weiai.App;
import wjy.weiai.R;
import wjy.weiai.hook.OnActivityResultCallback;
import wjy.weiai.hook.StartActivityFromResult;
import wjy.weiai.nettyclient.ChatServerManager;
import wjy.weiai.request.LoadDataContract;
import wjy.weiai.request.LoadDataContractModules;
import wjy.weiai.widget.WLoadingDialog;

/**
 * activity基类
 * @author wjy
 */
public abstract class BaseActivity extends FragmentActivity
        implements NetworkManager.OnNetWorkChangeLister, LoadDataContractModules.IContent {

    //页面跳转工具
    private StartActivityFromResult mStartActivityFromResult;
    //加载对话框
    private Dialog loadingDialog;

    @Inject
    public LoadDataContract loadDataContract;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        compat(R.color.app_theme_color);
        //在activity创建时加入到栈中
        ActivityManager.getInstance().pushActivity(this);
        //网络监听
        ((App)getApplication()).getNetworkManager().setOnNetWorkChangeLister(this);
        NetworkManager.NetworkType network = ((App)getApplication()).getNetworkManager().getCuttNetworkType();
        if(network == NetworkManager.NetworkType.NOT_NETWORK){
            this.onNot();
        }
        //页面跳转工具类
        this.mStartActivityFromResult = new StartActivityFromResult(this);
        this.setContentView(getLayoutId());
        ButterKnife.bind(this);
        this.onInit();
    }


    /**
     * 设置状态栏背景为指定颜色
     * @param statusColorRid  状态栏颜色的id
     */
    @SuppressLint("InlinedApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void compat(int statusColorRid){
        //当前手机版本为6.0及以上
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decorView = this.getWindow().getDecorView();
            if(decorView != null){
                //设置状态栏字体颜色
                int vis = decorView.getSystemUiVisibility();
                vis |= View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(vis);
            }
            //添加Flag把状态栏设为可绘制模式
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏背景色
            this.getWindow().setStatusBarColor(this.getResources().getColor(statusColorRid));
        }
    }

    /**
     * 返回当前activity的布局文件的id
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 子类不能再重写onCreate了，只能重写onInit
     */
    protected abstract void onInit();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //activity销毁后从栈中移除
        ActivityManager.getInstance().popActivity();
    }


    /**
     * 导航栏返回按钮点击
     * 在配置文件中注册的点击事件
     */
    public void doBack(View ivBack){
        this.finish();
    }


    /*******************************************************
     *      activity跳转
     *******************************************************/

    public void startActivity(Class<?> activity){
        this.startActivity(activity,null);
    }

    public void startActivity(Class<?> activity,Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(this,activity);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void startActivityFormResult(Class<?> activity, int requestCode, OnActivityResultCallback callback) {
        this.mStartActivityFromResult.startForResult(activity, requestCode, callback);
    }

    public void startActivityFormResult(Intent intent, int requestCode, OnActivityResultCallback callback) {
        this.mStartActivityFromResult.startForResult(intent, requestCode, callback);
    }

    public void startActivityFormResult(Class<?> activity,int requestCode,Bundle bundle, OnActivityResultCallback callback) {
        Intent intent = new Intent();
        intent.setClass(this,activity);
        if(bundle!=null){
            intent.putExtras(bundle);
        }
        this.mStartActivityFromResult.startForResult(intent, requestCode, callback);
    }

    /*******************************************************
     *      end activity跳转
     *******************************************************/



    /*******************************************************
     *      网络状态改变时的处理
     *******************************************************/
    @Override
    public void onMobile() {
        if(!ChatServerManager.getManager().isConnectChatServer()){
            ChatServerManager.getManager().initChatServerConnect();
        }
    }

    @Override
    public void onWifi() {
        if(!ChatServerManager.getManager().isConnectChatServer()){
            ChatServerManager.getManager().initChatServerConnect();
        }
    }

    @Override
    public void onNot() {
        showToast("无网络连接，请检查网络");
    }

    /*******************************************************
     *      end 网络状态改变时的处理
     *******************************************************/




    /*******************************************************
     *      线程安全的Toast
     *******************************************************/

    private Handler toastHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x01){
                showToast((String)msg.obj);
            }
        }
    };

    public void showThreadToast(String message){
        if(!Looper.getMainLooper().isCurrentThread()){
            Message msg = new Message();
            msg.what = 0x01;
            msg.obj = message;
            toastHandler.sendMessage(msg);
        }
        showToast(message);
    }

    private void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    /*******************************************************
     *      end 线程安全的Toast
     *******************************************************/


    /*******************************************************
     *      加载对话框、错误提示对话框
     *******************************************************/

    public void showWaitDialog(String waitMessage){
        if(this.loadingDialog==null) {
            this.loadingDialog = new WLoadingDialog(this)
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setHintText(waitMessage)
                    .create();
        }
        this.loadingDialog.show();
    }


    public void dismissWaitDialog(){
        if(this.loadingDialog!=null)
            this.loadingDialog.dismiss();
    }


    public void showAlertMessage(String message){
        this.showAlertMessage(message,null);
    }
    public void showAlertMessage(String title,String message){
        this.showAlertMessage(title,message,null);
    }
    public void showAlertMessage(String title,String message, DialogInterface.OnClickListener onClickListener){
        AlertDialog dialog = new AlertDialog.Builder(this,R.style.AlertDialogTheme)
                .setIcon(R.mipmap.ic_launcher)//设置标题的图片
                .setTitle(title)//设置对话框的标题
                .setMessage(message)
                .setPositiveButton("确定",onClickListener==null? (DialogInterface dialogInterface, int which) -> {
                    dialogInterface.dismiss();
                }:onClickListener).create();
        dialog.show();
    }


    /*******************************************************
     *      end 加载对话框、错误提示对话框
     *******************************************************/


    @Override
    public Context getContent() {
        return this;
    }
}
