package wjy.weiai.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * hook管理者，在 application中初始化，使用它activity将不需要在配置文件中声明
 * 不适用于AppCompatActivity，如果项目中使用了继承自AppCompatActivity的Activity，请不要在项目中使用这个hook技术
 * @author wjy
 */
public class ActivityHookManager {

    private Class<? extends Activity> mActivityProxy;
    private Context mContext;

    public ActivityHookManager(Context context){
        this.mContext = context;
        this.mActivityProxy = ActivityProxy.class;
    }


    public void hookStartActivity() throws Exception {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            hookStartActivity26h();
        }else{
            hookStartActivity25l();
        }
    }


    /**
     * androud o以上版本适用
     * @throws Exception
     */
    private void hookStartActivity26h() throws Exception{

    }

    /**
     * android o以下版本适用
     * @throws Exception
     */
    private void hookStartActivity25l() throws Exception{
        //获取ActivityManagerNative类的gDefault字段
        Class<?> amnClass = Class.forName("android.app.ActivityManagerNative");
        Field gDefaultField = amnClass.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);
        Object gDefault = gDefaultField.get(null);//静态方法不需要传递对象

        //获取gDefault的mInstance字段的值，这个值就是IActivityManager的实例
        Class<?> mInstanceClass = Class.forName("android.util.Singleton");
        Field mInstanceField = mInstanceClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        //获取到的是IActivityManager的实例
        Object mInstance = mInstanceField.get(gDefault);

        //获取IActivityManager接口类
        Class<?> iamClass = Class.forName("android.app.IActivityManager");
        //代理这个IActivityManager
        mInstance = Proxy.newProxyInstance(mContext.getClassLoader(),
                new Class[]{iamClass}, new StartActivityInvocationHandler(mInstance));

        //将gDefault的mInstance字段替换为代理对象
        mInstanceField.set(gDefault,mInstance);
    }


    private class StartActivityInvocationHandler implements InvocationHandler{

        //代理的目标对象,即实现IActivityManager接口的类的实例
        private Object mTarger;

        public StartActivityInvocationHandler(Object targer){
            this.mTarger = targer;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            if("startActivity".equals(method.getName())){
                Log.e("使用hook方式启动目标activity",method.getName());
                //拦截startActivity方法
                //目标Intent在args[2];
                Intent realIntent = (Intent) args[2];
                //代理的Intent
                Intent proxyIntent = new Intent();
                proxyIntent.setComponent(new ComponentName(mContext,mActivityProxy));
                proxyIntent.putExtra("REAL_INTENT",realIntent);
                //替换目标Intent，使用代理的Intent，用于绕过检测
                args[2] = proxyIntent;
            }
            return method.invoke(mTarger,args);
        }
    }


    public void hookLaunchActivity() throws Exception{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            hookLaunchActivity26h();
        }else{
            hookLaunchActivity25l();
        }
    }

    private void  hookLaunchActivity26h() throws Exception{

    }

    private void hookLaunchActivity25l() throws Exception{
        //获取ActivityThread
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        currentActivityThreadField.setAccessible(true);
        Object sCurrentActivityThread = currentActivityThreadField.get(null);

        //获取Handler
        Field handlerFiled = activityThreadClass.getDeclaredField("mH");
        handlerFiled.setAccessible(true);
        Handler handler = (Handler) handlerFiled.get(sCurrentActivityThread);

        //设置Handler.Callback
        Field callBackField = Handler.class.getDeclaredField("mCallback");
        callBackField.setAccessible(true);
        callBackField.set(handler,new ActivityThreadHandlerCallback());
    }


    private class ActivityThreadHandlerCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(Message message) {
            if(message.what == 100){
                Log.e("使用hook方式启动目标activity","绕过了检测，替换回目标activity");
                handlerLaunchActivity(message);
            }
            return false;
        }

        private void handlerLaunchActivity(Message message){
            Object object = message.obj;
            try {
                //获取到代理的intent
                Field intentField = object.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                Intent proxyIntent = (Intent) intentField.get(object);
                //获取真实的intent
                Intent realIntent = proxyIntent.getParcelableExtra("REAL_INTENT");
                if(realIntent!=null){
                    //替换回目标intent
                    intentField.set(object,realIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
