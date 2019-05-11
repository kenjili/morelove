package wjy.weiai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;

import wjy.androidlibs.imageselector.ImageSelector;
import wjy.androidlibs.imageselector.widget.CropImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import wjy.weiai.Jyson.Jyson;
import wjy.weiai.base.NetworkManager;
import wjy.weiai.bean.Lover;
import wjy.weiai.bean.LoverDetails;
import wjy.weiai.bean.User;
import wjy.weiai.hook.ActivityHookManager;
import wjy.weiai.request.DaggerLoadDataContractComponent;
import wjy.weiai.request.HttpUrl;
import wjy.weiai.request.LoadDataContract;
import wjy.weiai.request.LoadDataContractModules;
import wjy.weiai.request.exception.ApiException;

/**
 * 应用程序启动类
 *
 * @author wjy
 */
public class App extends android.app.Application implements LoadDataContractModules.IContent {

    private ActivityHookManager activityHookManager;
    //网络管理者
    private NetworkManager mManager;

    @Inject
    LoadDataContract loadDataContract;

    @Override
    public void onCreate() {
        super.onCreate();
        //启用hook
        activityHookManager = new ActivityHookManager(this);
        try {
            activityHookManager.hookStartActivity();
            activityHookManager.hookLaunchActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DaggerLoadDataContractComponent.builder()
                //因为LoadDataContractModules需要一个参数来实例化，
                //而实例化的参数类型的构造方法我们并没有使用@Inject声明，
                //因为组件的创建不是我们所管理的，总不能给一个activity添加一个构造方法然后在构造方法上添加@Inject注解
                .loadDataContractModules(new LoadDataContractModules(this::getContent))
                .build()
                //需要注入（使用@Inject注解）的字段在哪个类就是传递哪个类的实例，不能是父类，因为父类没有这个字段
                //可以通过将该字段写入到父类来达到效果。实际上是这样赋值的：
                //tagger.loadDataContract = new LoadDataContractModules().被@Provides注解的方法()
                //而tagger就是injection方法传入的实例对象。
                //这个injection方法是@Component声明的接口的定义方法，我们自己定义的。
                .injection(this);

        this.mManager = new NetworkManager();
        this.mManager.registerReceiver(this);
        sApp = new WeakReference<>(this);
        this.initImageSelector();
    }

    private static WeakReference<App> sApp;

    public static App getApp() {
        return sApp.get();
    }


    /**
     * 获取网络管理者
     *
     * @return
     */
    public NetworkManager getNetworkManager() {
        return this.mManager;
    }

    @Override
    public Context getContent() {
        return this;
    }


    /**
     * SharedPreferences工具类
     * @author wjy
     */
    public static class SharedPreferencesBuild{

        private SharedPreferences sharedPreferences;
        private Map<String,String> param;
        private List<String> removeKeys;

        public SharedPreferencesBuild(Context context){
            this.sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(context);
            this.param = new HashMap<>();
            this.removeKeys = new ArrayList<>();
        }

        public SharedPreferencesBuild addKeyValue(String key,String value){
            this.param.put(key,value);
            return this;
        }

        public SharedPreferencesBuild removeForKey(String key){
            this.removeKeys.add(key);
            return this;
        }

        public void submit(){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for(String key:param.keySet()){
                editor.putString(key,param.get(key));
            }
            for(String key:removeKeys){
                editor.remove(key);
            }
            editor.commit();
        }

        public String getValueForKey(String key){
            return sharedPreferences.getString(key,"");
        }

    }

    /**
     * 保存用户登录的用户名密码
     *
     * @param username
     * @param password
     */
    public void savaUserLoginInfo(String username, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    /**
     * 获取用户登录用的用户名密码
     *
     * @return
     */
    public User getUserLoginInfo() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        User user = new User();
        user.setUsername(sharedPreferences.getString("username", ""));
        user.setPassword(sharedPreferences.getString("password", ""));
        return user;
    }


    /**
     * 保存情侣记录，在整个应用都能访问到
     */
    private Lover sLover = null;
    private LoverDetails sLoverDetails = null;
    //在需要的activity中注册广播监听，当lover记录更新的适合会发送一条该action的广播
    public final static String LOVER_RECORD_UPDATE_SUCCESS_ACTION = "wjy.weiai.LOVER_RECORD_UPDATE_SUCCESS_ACTION";

    /**
     * 更新用户情侣记录,如果在其它地方调用了接口更新了lover记录，
     * 那么就需要调用这个方法获取最新的记录
     */
    public void updateUserLoverRecord() {
        this.sLover = null;
        this.getLover();
    }

    /**
     * 懒加载，只有需要用到且没加载过的时候才会请求加载
     *
     * @return
     */
    public Lover getLover() {
        if (sLover == null) {
            loadDataContract.loadData(Request.Method.GET, HttpUrl.LOVER_RECORD_URL, new LoadDataContract.ViewDataContract() {
                @Override
                public Map<String, String> getParams() {
                    return null;
                }

                @Override
                public void onSuccess(String json) {
                    try {
                        LoverDetails loverDetails = (LoverDetails) new Jyson().parseJson(json, LoverDetails.class);
                        if (loverDetails != null && loverDetails.getLover()!=null) {
                            synchronized (App.this) {
                                sLover = loverDetails.getLover();
                                sLoverDetails = loverDetails;
                                //发送一条更新广播
                                sendBroadcast(new Intent(LOVER_RECORD_UPDATE_SUCCESS_ACTION));
                            }
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(ApiException ex) {

                }
            });
        }
        return sLover;
    }

    /**
     * 获取情侣信息详情,包含纪念日总数等信息
     * @return
     */
    public LoverDetails getLoverDetails() {
        if(sLoverDetails==null)
            getLover();
        return sLoverDetails;
    }

    /**
     * 初始化ImageSelector图片选择器
     */
    private void initImageSelector() {
        //可在用到的地方再设置，或者在用到的地方再根据需求覆盖当前的配置
        //裁剪配置、只有单选模式下该配置才会有效
        ImageSelector.getInstance().setCrop(true)                            //允许裁剪（单选才有效）
                .setSaveRectangle(true)                      //是否按矩形区域保存,否者跟随裁剪框的形状
                .setStyle(CropImageView.Style.RECTANGLE)     //裁剪框的形状
                .setFocusWidth(800)                          //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                .setFocusHeight(800)                         //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                .setOutPutX(1000)                            //保存文件的宽度。单位像素
                .setOutPutY(1000);                           //保存文件的高度。单位像素
    }


}
