package wjy.androidlibs.imageselector;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import wjy.androidlibs.imageselector.bean.ImageFolder;
import wjy.androidlibs.imageselector.bean.ImageItem;
import wjy.androidlibs.imageselector.loader.ImageLoader;
import wjy.androidlibs.imageselector.widget.CropImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择器配置类
 * 1.提供配置功能
 * 2.保存图片文件夹
 * 3.保存图片选中状态
 *
 * @author wjy
 */
public class ImageSelector {

    public static final int REQUEST_CODE_TAKE = 1001;
    public static final int REQUEST_CODE_CROP = 1002;
    public static final int RESULT_CODE_ITEMS = 1003;
    public static final int RESULT_CODE_BACK = 1004;
    public static final int REQUEST_CODE_DETAILS = 1005;

    public static final String EXTRA_RESULT_ITEMS = "extra_result_items";

    private boolean multiMode = true;    //图片选择模式
    private int selectLimit = 9;         //最大选择图片数量
    private boolean crop = true;         //裁剪
    private boolean isSaveRectangle = false;  //裁剪后的图片是否是矩形，否者跟随裁剪框的形状
    private int outPutX = 800;           //裁剪保存宽度
    private int outPutY = 800;           //裁剪保存高度
    private int focusWidth = 280;         //焦点框的宽度
    private int focusHeight = 280;        //焦点框的高度
    private CropImageView.Style style = CropImageView.Style.RECTANGLE; //裁剪框的形状
    private File cropCacheFolder;       //存放裁剪后的图片文件的目录
    private File takeImageFile;         //指定拍照的文件，相机拍照的bitmap保存到这个文件，即图片文件

    private ArrayList<ImageItem> mSelectedImages = new ArrayList<>();   //选中的图片集合
    private List<ImageFolder> mImageFolders;      //所有的图片文件夹
    private int mCurrentImageFolderPosition = 0;  //当前选中的文件夹位置 0表示所有图片

    // 图片选中的监听回调
    private WeakReference<OnImageSelectedListener> mImageSelectedListener;

    /***********************************************************
     *  单例
     ***********************************************************/

    private ImageSelector() {
    }

    private static class ImageSelectorHolder {
        private static final ImageSelector mInstance = new ImageSelector();
    }

    public static ImageSelector getInstance() {
        return ImageSelectorHolder.mInstance;
    }


    /***********************************************************
     *  图片加载器
     ***********************************************************/

    private ImageLoader mImageLoader;

    /**
     * 如果没有设置图片加载器则会使用默认的图片加载器
     *
     * @return
     */
    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            //提供默认的图片加载器
            mImageLoader = new ImageLoader() {
                @Override
                public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                    //使用Glide加载本地图片
                    Glide.with(activity)
                            .load(Uri.fromFile(new File(path)))//设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.image_loading)//加载中显示的图片
                                    .error(R.drawable.image_load_error)//加载错误显示的图片
                                    .fallback(R.drawable.image_load_null)//加载的图片是null时显示的图片
                                    .skipMemoryCache(false)//跳过内存缓存
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)//硬盘缓存-仅仅缓存最终的图像，即降低分辨率后的
                            ).into(imageView);
                }
            };
        }
        return mImageLoader;
    }

    /**
     * 提供给调用者自己设置图片加载器
     *
     * @param imageLoader
     */
    public void setImageLoader(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }

    /***********************************************************
     *  end 图片加载器
     ***********************************************************/

    public boolean isMultiMode() {
        return multiMode;
    }

    public void setMultiMode(boolean multiMode) {
        this.multiMode = multiMode;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public boolean isCrop() {
        return crop;
    }


    /***********************************************************
     *  裁剪配置
     ***********************************************************/

    public ImageSelector setCrop(boolean crop) {
        this.crop = crop;
        return this;
    }

    public ImageSelector setStyle(CropImageView.Style style) {
        this.style = style;
        return this;
    }

    public ImageSelector setSaveRectangle(boolean isSaveRectangle) {
        this.isSaveRectangle = isSaveRectangle;
        return this;
    }

    public ImageSelector setOutPutX(int outPutX) {
        this.outPutX = outPutX;
        return this;
    }

    public ImageSelector setOutPutY(int outPutY) {
        this.outPutY = outPutY;
        return this;
    }

    public ImageSelector setFocusWidth(int focusWidth) {
        this.focusWidth = focusWidth;
        return this;
    }

    public ImageSelector setFocusHeight(int focusHeight) {
        this.focusHeight = focusHeight;
        return this;
    }

    public ImageSelector setCropCacheFolder(File cropCacheFolder) {
        this.cropCacheFolder = cropCacheFolder;
        return this;
    }

    public CropImageView.Style getStyle() {
        return style;
    }

    public boolean isSaveRectangle() {
        return isSaveRectangle;
    }

    public int getOutPutX() {
        return outPutX;
    }


    public int getOutPutY() {
        return outPutY;
    }


    public int getFocusWidth() {
        return focusWidth;
    }


    public int getFocusHeight() {
        return focusHeight;
    }

    public File getCropCacheFolder(Context context) {
        if (cropCacheFolder == null) {
            cropCacheFolder = new File(context.getCacheDir() + "/ImageSelector/cropTemp/");
        }
        return cropCacheFolder;
    }


    /***********************************************************
     *  end 裁剪配置
     ***********************************************************/


    /**
     * 获取拍照时要存放结果的图片文件
     * 确保该方法调用前调用了setTakeImageFile设置takeImageFile
     *
     * @return
     */
    public File getTakeImageFile() {
        return takeImageFile;
    }

    public void setTakeImageFile(File takeImageFile) {
        this.takeImageFile = takeImageFile;
    }


    /**
     * 获取所有的图片文件夹
     *
     * @return
     */
    public List<ImageFolder> getImageFolders() {
        if(mImageFolders==null)mImageFolders = new ArrayList<>();
        return mImageFolders;
    }


    /**
     * 写入图片文件夹列表
     *
     * @param imageFolders
     */
    public void setImageFolders(List<ImageFolder> imageFolders) {
        if(imageFolders==null||imageFolders.size()==0)
            return;
        //不使用源数据的引用，因为当源数据imageFolders.remove(obj)时，这里的mImageFolders中的数据项也会被移除
        //因为mImageFolders与调用者参数imageFolders指向的时同一个对象
        if(mImageFolders==null)mImageFolders = new ArrayList<>();
        mImageFolders.addAll(imageFolders);
    }


    /**
     * 改变当前选中的文件夹的索引
     *
     * @param mCurrentSelectedImageSetPosition
     */
    public void setCurrentImageFolderPosition(int mCurrentSelectedImageSetPosition) {
        mCurrentImageFolderPosition = mCurrentSelectedImageSetPosition;
    }

    /**
     * 获取当前选中的图片文件夹下的所有图片
     *
     * @return
     */
    public ArrayList<ImageItem> getCurrentImageFolderItems() {
        return mImageFolders.get(mCurrentImageFolderPosition).images;
    }


    /**
     * 判断某张图片是否选中状态
     *
     * @param item
     * @return
     */
    public boolean isSelect(ImageItem item) {
        return mSelectedImages.contains(item);
    }

    /**
     * 获取选中的图片总数
     *
     * @return
     */
    public int getSelectImageCount() {
        if (mSelectedImages == null) {
            return 0;
        }
        return mSelectedImages.size();
    }

    /**
     * 获取当前选中的图片
     *
     * @return
     */
    public ArrayList<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }

    /**
     * 清空选中的图片
     */
    public void clearSelectedImages() {
        if (mSelectedImages != null) mSelectedImages.clear();
    }

    /**
     * 清空监听器，清空图片文件夹、清空选中的图片
     */
    public void clear() {
        if (mImageFolders != null) {
            mImageFolders.clear();
            mImageFolders = null;
        }
        if (mSelectedImages != null) {
            mSelectedImages.clear();
        }
        mCurrentImageFolderPosition = 0;
        mImageSelectedListener = null;
    }


    /**
     * 图片选中的监听
     */
    public interface OnImageSelectedListener {
        void onImageSelected(int position, ImageItem item, boolean isAdd);
    }

    /**
     * 写入图片选中监听器
     *
     * @param l
     */
    public void setOnImageSelectedListener(OnImageSelectedListener l) {
        this.mImageSelectedListener = new WeakReference<>(l);
    }


    /**
     * 通知图片选中改变
     *
     * @param position
     * @param item
     * @param isAdd
     */
    private void notifyImageSelectedChanged(int position, ImageItem item, boolean isAdd) {
        if (mImageSelectedListener == null) return;
        if (mImageSelectedListener.get() != null)
            mImageSelectedListener.get().onImageSelected(position, item, isAdd);
    }

    /**
     * 添加选中的图片，如果图片当前已经是选中状态，那么该操作就是移除
     *
     * @param position
     * @param item
     * @param isAdd    是添加还是移除
     */
    public void addSelectedImageItem(int position, ImageItem item, boolean isAdd) {
        if (isAdd) mSelectedImages.add(item);
        else mSelectedImages.remove(item);
        notifyImageSelectedChanged(position, item, isAdd);
    }

    /**
     * 重置选中的图片
     *
     * @param selectedImages
     */
    public void setSelectedImages(ArrayList<ImageItem> selectedImages) {
        if (selectedImages == null) {
            return;
        }
        this.mSelectedImages.clear();
        this.mSelectedImages.addAll(selectedImages);
    }

}