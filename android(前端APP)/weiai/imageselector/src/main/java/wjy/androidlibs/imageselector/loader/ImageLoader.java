package wjy.androidlibs.imageselector.loader;

import android.app.Activity;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * 图片加载器，可扩充
 * 提供调用者配置更优秀的图片加载器加载会显得更加流畅
 * @author wjy
 */
public interface ImageLoader extends Serializable {

    void displayImage(Activity activity, String path, ImageView imageView, int width, int height);

}
