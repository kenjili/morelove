package wjy.morelove.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * 网络管理者，监听网络状态改变
 * @author wjy
 */
public class NetworkManager {

    /**
     * 网络类型
     *
     * @author wujiuye
     */
    public enum NetworkType {
        //移动数据网络,wifi网络,无网络
        Mobile, WIFI, NOT_NETWORK
    }

    public interface OnNetWorkChangeLister {
        /**
         * 没有网络连接
         */
        void onNot();

        /**
         * wifi网络
         */
        void onWifi();

        /**
         * 移动网络
         */
        void onMobile();
    }

    /**
     * 当前网络的连接类型
     */
    private NetworkType mNetworkType = NetworkType.NOT_NETWORK;
    public NetworkType getCuttNetworkType() {
        return mNetworkType;
    }

    /**
     * 网络状态监听接口
     */
    private OnNetWorkChangeLister onNetWorkChangeLister = null;
    public void setOnNetWorkChangeLister(OnNetWorkChangeLister onNetWorkChangeLister) {
        this.onNetWorkChangeLister = onNetWorkChangeLister;
    }


    /**
     * 过滤器
     */
    private IntentFilter filter = new IntentFilter(
            ConnectivityManager.CONNECTIVITY_ACTION);
    /**
     * 网络状态监听广播
     */
    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                mNetworkType = getNetworkType(context);
                if(onNetWorkChangeLister!=null){
                    if(mNetworkType==NetworkType.WIFI){
                        onNetWorkChangeLister.onWifi();
                    }else if(mNetworkType==NetworkType.Mobile){
                        onNetWorkChangeLister.onMobile();
                    }else{
                        onNetWorkChangeLister.onNot();
                    }
                }
            }
        }
    };


    /**
     * 注册广播接收器
     * 静态注册网络状态监听器在7.0即系统24之后是失效的，接收不到系统广播的，只能通过动态注册。
     * @param application
     * @return
     */
    public void registerReceiver(Application application) {
        //注册网络状态监听广播
        application.registerReceiver(this.networkChangeReceiver, filter);
        this.mNetworkType = getNetworkType(application);
    }


    /**
     * 当前网络连接状态类型
     *
     * @param context
     * @return
     */
    private static NetworkType getNetworkType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                    return NetworkType.WIFI;
                } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                    return NetworkType.Mobile;
                }
            }
            return NetworkType.NOT_NETWORK;
        } else {
            //获取所有网络连接的信息
            Network[] networks = connectivityManager.getAllNetworks();
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networks[i]);
                if (networkInfo.isConnected()) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        return NetworkType.Mobile;
                    } else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                        return NetworkType.WIFI;
                    }
                }
            }
            return NetworkType.NOT_NETWORK;
        }
    }


}
