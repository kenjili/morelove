package wjy.weiai.hook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Activity的代理类，用于绕过权限检测，需要在配置文件中声明
 * 什么也不需要做
 *
 * @author wjy
 */
public class ActivityProxy extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
