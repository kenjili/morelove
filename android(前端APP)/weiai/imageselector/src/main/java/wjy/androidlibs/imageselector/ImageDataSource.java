package wjy.androidlibs.imageselector;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import wjy.androidlibs.imageselector.bean.ImageFolder;
import wjy.androidlibs.imageselector.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：加载手机图片实现类
 * 修订历史：
 *
 * @author wjy
 * 图片数据源
 * ================================================
 */
public class ImageDataSource {
    /**
     * 所有图片加载完成的回调接口
     */
    public interface OnImagesLoadedListener {
        void onImagesLoaded(List<ImageFolder> imageFolders);
    }

    public static final int LOADER_ALL = 0;         //加载所有图片
    public static final int LOADER_CATEGORY = 1;    //分类加载图片

    private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608

    private FragmentActivity activity;
    private OnImagesLoadedListener loadedListener;                     //图片加载完成的回调接口

    /**
     * @param activity       用于初始化LoaderManager，需要兼容到2.3
     * @param loadedListener 图片加载完成的监听
     */
    public ImageDataSource(FragmentActivity activity, OnImagesLoadedListener loadedListener) {
        this.activity = activity;
        this.loadedListener = loadedListener;
    }


    /**
     * 使用activity.getSupportLoaderManager()获取LoaderManager的实现类对象LoaderManagerImpl
     *
     * @param path 指定扫描的文件夹目录，可以为 null，表示扫描所有图片
     */
    public void startLoad(String path) {
        //获取加载器管理者LoaderManagerImpl的实例
        LoaderManager loaderManager = activity.getSupportLoaderManager();
        int id;
        Bundle bundle = null;
        if (path == null) {
            //加载所有的图片
            id = LOADER_ALL;
        } else {
            //加载指定目录的图片
            id = LOADER_CATEGORY;
            bundle = new Bundle();
            bundle.putString("path", path);
        }
        loaderManager.initLoader(id, bundle, new LoaderManager.LoaderCallbacks<Cursor>() {

            /**
             * 创建加载器
             * @param id
             * @param args
             * @return
             */
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader cursorLoader = null;
                //扫描所有图片
                if (id == LOADER_ALL)
                    cursorLoader = new CursorLoader(ImageDataSource.this.activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
                    //扫描某个图片文件夹
                else if (id == LOADER_CATEGORY)
                    cursorLoader = new CursorLoader(ImageDataSource.this.activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[6] + " DESC");
                return cursorLoader;
            }


            /**
             * 数据加载完成时调用
             * @param loader
             * @param data
             */
            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data != null) {
                    //所有图片文件夹
                    ArrayList<ImageFolder> imageFolders = new ArrayList<>();
                    //所有图片的集合,不分文件夹
                    ArrayList<ImageItem> allImages = new ArrayList<>();
                    while (data.moveToNext()) {
                        //读取每条数据的对应列数据
                        String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        File file = new File(imagePath);
                        if (!file.exists() || file.length() <= 0) continue;
                        long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                        long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));

                        //将数据封装为java bean
                        ImageItem imageItem = new ImageItem();
                        imageItem.name = imageName;
                        imageItem.path = imagePath;
                        imageItem.size = imageSize;
                        imageItem.width = imageWidth;
                        imageItem.height = imageHeight;
                        imageItem.mimeType = imageMimeType;
                        imageItem.addTime = imageAddTime;
                        allImages.add(imageItem);

                        //根据父路径分类存放图片
                        File imageFile = new File(imagePath);
                        File imageParentFile = imageFile.getParentFile();
                        ImageFolder imageFolder = new ImageFolder();
                        imageFolder.name = imageParentFile.getName();
                        imageFolder.path = imageParentFile.getAbsolutePath();

                        /**
                         * 如果文件夹不存在列表中则将文件夹添加到列表中
                         */
                        if (!imageFolders.contains(imageFolder)) {
                            imageFolder.cover = imageItem;//图片文件夹的图标
                            imageFolder.images = new ArrayList<>();
                            imageFolders.add(imageFolder);
                        }
                        //添加图片到文件夹中
                        imageFolders.get(imageFolders.indexOf(imageFolder)).images.add(imageItem);
                    }


                    if (allImages.size() > 0) {
                        //构造所有图片的集合
                        ImageFolder allImagesFolder = new ImageFolder();
                        allImagesFolder.name = ImageDataSource.this.activity.getResources().getString(R.string.ip_all_images);
                        allImagesFolder.path = "/";
                        allImagesFolder.cover = allImages.get(0);
                        allImagesFolder.images = allImages;
                        imageFolders.add(0, allImagesFolder);  //确保第一条是所有图片
                    }

                    //回调接口，通知图片数据准备完成
                    ImageSelector.getInstance().setImageFolders(imageFolders);
                    ImageDataSource.this.loadedListener.onImagesLoaded(imageFolders);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });
    }

}
