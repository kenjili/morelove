package wjy.morelove.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import wjy.morelove.bean.AppVersion;

public class AppUtils {

    /**
     * 获取应用版本号和版本名称
     * @param context
     * @return
     */
    public static AppVersion appVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        AppVersion appVersion = new AppVersion();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersion.setCode(info.versionCode);
            appVersion.setName(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

}
