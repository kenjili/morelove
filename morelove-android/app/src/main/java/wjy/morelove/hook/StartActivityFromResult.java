package wjy.morelove.hook;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 页面跳转并获取页面返回结果的工具类
 * @author wjy
 */
public class StartActivityFromResult {


    private OnActivityResultFragment onActivityResultFragment;

    public StartActivityFromResult(Activity activity) {
        getOnResultFragment(activity);
    }

    private void getOnResultFragment(Activity activity) {
        this.onActivityResultFragment = findOnResultFragment(activity);
        //如果没找到再创建，避免同一个activity多次调用多次创建OnActivityResultFragment的情况
        if (this.onActivityResultFragment == null) {
            this.onActivityResultFragment = new OnActivityResultFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(this.onActivityResultFragment, this.getClass().getName())
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
    }

    private OnActivityResultFragment findOnResultFragment(Activity activity) {
        return (OnActivityResultFragment) activity.getFragmentManager().findFragmentByTag(this.getClass().getName());
    }


    public void startForResult(Class<?> clazz, int requestCode, OnActivityResultCallback callback) {
        Intent intent = new Intent(onActivityResultFragment.getActivity(), clazz);
        startForResult(intent, requestCode, callback);
    }


    public void startForResult(Intent intent, int requestCode, OnActivityResultCallback callback) {
        onActivityResultFragment.startForResult(intent, requestCode, callback);
    }


    /**
     * 使用该Fragment间接开启一个activity并获取返回值
     * 在onActivityResult中调用回调方法
     * 静态内部类特点：不能够从静态内部类的对象中访问外部类的非静态成员
     * @author wjy
     */
    public static class OnActivityResultFragment extends Fragment {

        private Map<Integer,OnActivityResultCallback> mCallbacks = new HashMap<>();

        public OnActivityResultFragment(){

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }


        public void startForResult(Intent intent, int requestCode,OnActivityResultCallback callback) {
            mCallbacks.put(requestCode, callback);
            startActivityForResult(intent, requestCode);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            OnActivityResultCallback callback = mCallbacks.remove(requestCode);
            if (callback != null) {
                callback.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
