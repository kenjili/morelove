package wjy.morelove;

import java.io.File;

import android.content.Context;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 */
public class AppConfig {

    /**
     * APP缓存根目录
     *
     * @param context
     * @return
     */
    private static String getAppCacheRootPath(Context context) {
        return context.getApplicationContext().getExternalCacheDir().getAbsolutePath() + File.separator;
    }

    /**
     * 获取缓存响应http请求的数据的根目录
     *
     * @param context
     * @return
     */
    public static String getHttpResponseCachePath(Context context) {
        return getAppCacheRootPath(context) + "response" + File.separator;
    }


    /**
     * 获取Glide加载图片的配置
     *
     * @return
     */
    public static RequestOptions getRequestOptions() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.image_loading)//加载中显示的图片
                .error(R.drawable.image_load_error)//加载错误显示的图片
                .fallback(R.drawable.image_load_null)//加载的图片是null时显示的图片
                .skipMemoryCache(false)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL);//硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的
        return requestOptions;
    }


    /**
     * 获取Glide加载图片后显示圆角图片的配置
     *
     * @param roundingRadius 圆角的半径
     * @return
     */
    public static RequestOptions getRequestOptionsWithRoundingRadius(int roundingRadius) {
        return getRequestOptions()
                //添加转换规则，实现Transformation接口的类对象
                .transform(new RoundedCorners(roundingRadius));//设置圆角图片
    }

    /**
     * 获取Glide加载图片后显示圆形头像的配置
     *
     * @return
     */
    public static RequestOptions getRequestOptionsCircleCrop() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.default_user_img1)//加载中显示的图片
                .error(R.mipmap.default_user_img1)//加载错误显示的图片
                .fallback(R.mipmap.default_user_img1)//加载的图片是null时显示的图片
                .skipMemoryCache(false)//跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL)//硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的
                //添加转换规则，圆形裁剪
                .transform(new CircleCrop());
        return requestOptions;
    }


    /**
     * 相册上传的图片最大数量
     * @return
     */
    public static int albumUploadMaxNumber(){
        return 9;
    }

    /**
     * 时光所能上传的最大图片大小
     * @return
     */
    public static int lovetimeUploadMaxNumber(){
        return 3;
    }
}
