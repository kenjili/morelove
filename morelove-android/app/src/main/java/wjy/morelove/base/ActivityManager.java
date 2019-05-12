package wjy.morelove.base;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * activity栈
 * @author wjy
 */
public class ActivityManager {

    //使用弱引用
    private Stack<WeakReference<Activity>> referenceStack;

    private ActivityManager(){
        this.referenceStack = new Stack<>();
        this.referenceStack.clear();
    }

    private static class ActivityManagerHolder{
        private static ActivityManager instance = new ActivityManager();
    }

    public static ActivityManager getInstance(){
        return ActivityManagerHolder.instance;
    }


    /**
     * 将activity压入栈顶
     * @param activity
     */
    public void pushActivity(Activity activity){
        //弱引用
        this.referenceStack.push(new WeakReference<>(activity));
    }

    /**
     * 从栈顶开始会弹出所有已经销毁的activity
     */
    public void popActivity(){
        boolean isPop = false;
        for(int i=referenceStack.size()-1;i>=0;i--){
            if(referenceStack.get(i).get()==null||referenceStack.get(i).get().isDestroyed()){
                referenceStack.remove(i);
                isPop=true;
                break;
            }
        }
        //递归
        if(isPop)popActivity();
    }

}
