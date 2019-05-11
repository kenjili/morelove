package wjy.weiai.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class AssetsUtils {

    /**
     * 从Assets文件夹中读取图片
     *
     * @param context
     * @param fileName 带文件路径的文件名
     * @return
     */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 从Assets文件夹中读取文件流
     *
     * @param context
     * @param fileName 带文件路径的文件名
     * @return
     */
    public static InputStream getFileInputStreamWidthAssets(Context context, String fileName) {
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
