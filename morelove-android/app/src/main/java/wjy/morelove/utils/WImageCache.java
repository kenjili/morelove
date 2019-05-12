package wjy.morelove.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import java.io.File;

import wjy.morelove.App;
import wjy.morelove.AppConfig;

/**
 * 图片缓存类
 * 实现内存和外存双缓存
 * @author wjy
 * 
 */
public class WImageCache implements ImageCache {

	//内存缓存
	private static LruCache<String, Bitmap> mMemoryCache;
	//存储卡缓存
	private static AppCache appCache;

	private static WImageCache wImageCache;

	private WImageCache() {
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
		appCache = AppCache.get(new File(AppConfig.getHttpResponseCachePath(App.getApp()) + App.getApp().getClass().getSimpleName()));
	}

	public static WImageCache instance() {
		if (wImageCache == null) {
			wImageCache = new WImageCache();
		}
		return wImageCache;
	}

	/**
	 * 从缓存取图片
	 */
	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = mMemoryCache.get(url);
		if(bitmap==null)
			bitmap = appCache.getAsBitmap(url);
		return bitmap;
	}

	/**
	 * 将图片保存到缓存中
	 */
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		if(mMemoryCache.get(url)==null){
			mMemoryCache.put(url, bitmap);
			appCache.put(url,bitmap);
		}
	}

}
