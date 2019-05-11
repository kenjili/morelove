package wjy.weiai.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;

import wjy.androidlibs.imageselector.bean.ImageItem;
import wjy.weiai.Jyson.Jyson;
import wjy.weiai.R;
import wjy.weiai.request.HttpUrl;
import wjy.weiai.request.RequestResult;
import wjy.weiai.request.exception.ApiException;
import wjy.weiai.request.upload.MultipartRequest;

/**
 * 后台发布相册、时光的服务
 * <p>
 * 知识点：
 * 1、被启动的服务的生命周期：
 * 如果一个Service被某个Activity调用Context.startService方法启动，那么不管是否有Activity使用bindService绑定或unbindService解除绑定到该Service，
 * 该Service都在后台运行。如果一个Service被startService方法多次启动，那么onCreate方法只会调用一次，
 * onStart将会被调用多次（对应调用startService的次数），并且系统只会创建Service的一个实例（因此停止服务只需要一次stopService调用）。
 * 该Service将会一直在后台运行，而不管对应程序的Activity是否在运行，直到被调用stopService，或自身的stopSelf方法。
 * 当然如果系统资源不足，android系统也可能结束服务。
 * <p>
 * 2、被绑定的服务的生命周期：
 * 如果一个Service被某个Activity调用Context.bindService方法绑定启动，不管调用bindService调用几次，onCreate方法都只会调用一次，
 * 同时onStart方法始终不会被调用。当连接建立之后，Service将会一直运行，除非调用Context.unbindService断开连接
 * 或者之前调用bindService的Context不存在了（如Activity被finish的时候），系统将会自动停止Service，对应onDestroy将被调用。
 * <p>
 * 3、被启动又被绑定的服务的生命周期：
 * 如果一个Service又被启动又被绑定，则该Service将会一直在后台运行。并且不管如何调用，onCreate始终只会调用一次，对应startService调用多少次，
 * Service的onStart便会调用多少次。调用unbindService将不会停止Service，而必须调用stopService或Service的stopSelf来停止服务。
 * <p>
 * * 同时使用startService与bindService要注意到，Service的终止，需要unbindService与stopService同时调用，才能终止Service，
 * 不管startService与bindService的调用顺序，如果先调用unbindService此时服务不会自动终止，再调用stopService之后服务才会停止，
 * 如果先调用stopService此时服务也不会终止，而再调用unbindService或者之前调用bindService 的Context不存在了（如Activity 被 finish 的时候）之后服务才会自动停止。
 */
public class UploadPublicService extends Service {

    public final static String UPLOAD_IMAGE_NAME = "images";
    public final static String UPLOAD_PARAM_NAME = "param";
    public final static String UPLOAD_ACTION_NAME = "action";

    public final static String UPLOAD_ACTION_LVXINGJI = "lvxingji";
    public final static String UPLOAD_ACTION_LOVERTIME = "lovetime";

    public final static String UPLOAD_BROADCAST_ACTION_ONERROR = UploadPublicService.class.getName() + ".ON_ERROR";
    public final static String UPLOAD_BROADCAST_ACTION_ONSUCCESS = UploadPublicService.class.getName() + ".ON_SUCCESS";
    public final static String ERROR_MESSAGE = "errorMsg";


    public final static int NOTIFICATION_ID = 1000;

    private RequestQueue mRequestQueue;
    private ArrayList<ImageItem> mUploadImages;
    private HashMap<String, String> mParam;
    private long mUploadFileSize = 0;

    private Notification.Builder mUploadProcessNotificationBuilder;
    private String channelId = "upload";//上传进度通知的频道id
    private String channelName = "文件上传";//上传进度通知的频道名称，用户在设置中可以看到

    private String channelIdStatu = "statu";//消息通知
    private String channelNameStatu = "状态通知";

    /**
     * Oreo不用Priority了，用importance
     * IMPORTANCE_NONE 关闭通知
     * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
     * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
     * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
     * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
     */
    private int importance = NotificationManager.IMPORTANCE_HIGH;

    /**
     * 1):START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
     * <p>
     * 2):START_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务
     * <p>
     * 3):START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
     * <p>
     * 4):START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     *
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!notificationUserOpenChannel()) {
            return super.onStartCommand(intent, flags, startId);
        }
        initNotification();
        //开始前台运行显示通知栏，android8.0 需要使用this.startForegroundService(intent);开始前台服务，
        // 并且需要在服务开启的5s内调用startForeground方法显示通知栏
        startForeground(NOTIFICATION_ID, mUploadProcessNotificationBuilder.build());
        this.mUploadImages = (ArrayList<ImageItem>) intent.getSerializableExtra(UPLOAD_IMAGE_NAME);
        this.mParam = (HashMap<String, String>) intent.getSerializableExtra(UPLOAD_PARAM_NAME);
        String action = intent.getStringExtra(UPLOAD_ACTION_NAME);
        for (ImageItem imageItem : mUploadImages) {
            mUploadFileSize += imageItem.size;
        }
        if (mRequestQueue == null) mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        if (action.equals(UPLOAD_ACTION_LVXINGJI)) {
            doUploadAlbum();
        } else if (action.equals(UPLOAD_ACTION_LOVERTIME)) {
            doUploadLovetime();
        }
        return START_REDELIVER_INTENT;
    }

    /**
     * 发布恋爱时光
     */
    private void doUploadLovetime() {
        MultipartRequest uploadRequest = new MultipartRequest(getApplicationContext(), HttpUrl.PUBLIC_LOVETIME_URL,
                mParam, "images", mUploadImages,
                (String response) -> {
                    try {
                        RequestResult requestResult = (RequestResult) new Jyson().parseJson(response, RequestResult.class);
                        if (requestResult != null && requestResult.getErrorCode() == ApiException.ApiResponseErrorCode.SUCCESS) {
                            Intent intent = new Intent(UPLOAD_BROADCAST_ACTION_ONSUCCESS);
                            intent.putExtra(ERROR_MESSAGE, requestResult.getErrorMessage());
                            sendBroadcast(intent);
                            showClickCloseNotification("发表成功", null);
                        } else {
                            Intent intent = new Intent(UPLOAD_BROADCAST_ACTION_ONERROR);
                            intent.putExtra(ERROR_MESSAGE, requestResult.getErrorMessage());
                            sendBroadcast(intent);
                            showClickCloseNotification("发表失败", requestResult.getErrorMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stopService();
                }, (long bytes) -> {
            Log.d("文件上传监听：", "总大小：" + mUploadFileSize + "，已上传：" + bytes);
            //通知更新进度条
            updateNotification(bytes);
        }, (VolleyError error) -> {
            error.printStackTrace();

            Intent intent = new Intent(UPLOAD_BROADCAST_ACTION_ONERROR);
            intent.putExtra(ERROR_MESSAGE, error.getMessage());
            sendBroadcast(intent);
            showClickCloseNotification("发表失败", "请检查您的网络是否能用！");
            stopService();
        }
        );
        this.mRequestQueue.add(uploadRequest);
    }

    /**
     * 发布侣行记，上传相册
     * 上传完成后除了在通知栏通知还是发送一条广播，
     * 不管有没有页面接收这个广播
     */
    private void doUploadAlbum() {
        MultipartRequest uploadRequest = new MultipartRequest(getApplicationContext(), HttpUrl.PUBLIC_ITINERARY_URL,
                mParam, "photos", mUploadImages,
                (String response) -> {
                    try {
                        RequestResult requestResult = (RequestResult) new Jyson().parseJson(response, RequestResult.class);
                        if (requestResult != null && requestResult.getErrorCode() == ApiException.ApiResponseErrorCode.SUCCESS) {
                            Intent intent = new Intent(UPLOAD_BROADCAST_ACTION_ONSUCCESS);
                            intent.putExtra(ERROR_MESSAGE, requestResult.getErrorMessage());
                            sendBroadcast(intent);
                            showClickCloseNotification("发布侣行记成功", "上传完成！");
                        } else {
                            Intent intent = new Intent(UPLOAD_BROADCAST_ACTION_ONERROR);
                            intent.putExtra(ERROR_MESSAGE, requestResult.getErrorMessage());
                            sendBroadcast(intent);
                            showClickCloseNotification("发布侣行记失败", requestResult.getErrorMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stopService();
                }, (long bytes) -> {
            Log.d("文件上传监听：", "总大小：" + mUploadFileSize + "，已上传：" + bytes);
            //通知更新进度条
            updateNotification(bytes);
        }, (VolleyError error) -> {
            error.printStackTrace();

            Intent intent = new Intent(UPLOAD_BROADCAST_ACTION_ONERROR);
            intent.putExtra(ERROR_MESSAGE, error.getMessage());
            sendBroadcast(intent);
            showClickCloseNotification("发布侣行记失败", error.getMessage());
            stopService();
        }
        );
        this.mRequestQueue.add(uploadRequest);
    }


    /**
     * Android赋予了开发者读取通知频道配置的权限，
     * 如果我们的某个功能是必须按照指定要求来配置通知频道才能使用的，
     * 那么就可以提示用户去手动更改通知频道配置。
     */
    private boolean notificationUserOpenChannel() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //android8.0以上系统需要用于打开接收该通知的频道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(channelId);
            if (channel == null) return true;//没有注册这个频道返回true，因为频道在创建通知的时候会创建
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "请手动将通知打开", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    /**
     * 初始化通知栏显示的通知
     */
    public void initNotification() {
        if (this.mUploadProcessNotificationBuilder == null) {
            //android8.0以上系统适配
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //创建通知频道
                NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
                NotificationManager notificationManager = (NotificationManager) getSystemService(
                        NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
                this.mUploadProcessNotificationBuilder = new Notification.Builder(getApplicationContext(), channelId);
            } else {
                //android8.0以下不需要设置频道
                this.mUploadProcessNotificationBuilder = new Notification.Builder(getApplicationContext());
            }
        }
        //设置参数
        this.mUploadProcessNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)//标记声音或震动一次,android8.0以下有效
                .setProgress(100, 0, false)
                .setContentTitle("文件上传中")
                .setContentText("正在上传图片,请稍等片刻...")
                //仅警报一次
                .setOnlyAlertOnce(true);//设置为true只会提醒一次声音，不会重复提醒。即更新进度的时候就不会提醒了
    }

    /**
     * 更新通知栏进度条
     *
     * @param currentUploadBytes 已上传的字节数
     */
    public void updateNotification(long currentUploadBytes) {
        int progress = (int) ((currentUploadBytes * 1.00f / mUploadFileSize) * 100);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mUploadProcessNotificationBuilder.setProgress(100, progress, false);
        manager.notify(NOTIFICATION_ID, mUploadProcessNotificationBuilder.build());
    }


    /**
     * 停止服务并关闭通知栏的显示
     */
    public void stopService() {
        //停止前台并移除通知
        stopForeground(true);
        //停止自身服务
        stopSelf();
    }


    /**
     * 显示条用户点击即消息的通知
     * android8.0 以上如果用户把这个频道关闭了就会接受不到，但是不是很重要的消息就算了
     */
    private void showClickCloseNotification(String title, String message) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder;
        //适配android8.0 需要一个channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建通知频道
            NotificationChannel channel = new NotificationChannel(channelIdStatu, channelNameStatu, importance);
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            //创建Notification
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        //创建Notification
        Notification notification = builder.setContentTitle(title)
                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)//标记声音或震动一次
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .build();
        manager.notify(1234, notification);
    }


    @Override
    public void onDestroy() {
        this.mRequestQueue = null;
        this.mUploadImages = null;
        this.mParam = null;
        super.onDestroy();
    }


    //####################################################
    //      使用bindService开启服务才会执行到，所以不用
    //####################################################

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBind;
    }

    /**
     * 在建完aidl之后需要先执行一次build->make project，会在build/generated/source目录下生成
     * 一个同名的java文件，这个时候才能使用它的Stub子类
     * bindService方式启动服务才会用到
     */
    private final AIDLUploadPublic.Stub mBind = new AIDLUploadPublic.Stub() {
        @Override
        public float getUploadProcess(){
            return 0.0f;
        }
    };

}
