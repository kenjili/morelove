package wjy.morelove.hook;

import android.content.Intent;

/**
 * startActivityFromResult方式启动activity的回调接口
 * StartActivityFromResult工具类的接口
 * @author wjy
 */
public interface OnActivityResultCallback{
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
