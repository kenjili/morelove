package wjy.androidlibs.imageselector.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import wjy.androidlibs.imageselector.ImageDataSource;
import wjy.androidlibs.imageselector.ImageSelector;
import wjy.androidlibs.imageselector.R;
import wjy.androidlibs.imageselector.adapter.ImageFolderAdapter;
import wjy.androidlibs.imageselector.adapter.ImageRecyclerAdapter;
import wjy.androidlibs.imageselector.adapter.ImageRecyclerAdapter.OnImageItemClickListener;
import wjy.androidlibs.imageselector.bean.ImageFolder;
import wjy.androidlibs.imageselector.bean.ImageItem;
import wjy.androidlibs.imageselector.utils.Utils;
import wjy.androidlibs.imageselector.widget.FolderPopUpWindow;
import wjy.androidlibs.imageselector.widget.GridSpacingItemDecoration;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 选择图片页面
 *
 * @author wjy
 */
public class ImageSelectorActivity extends ImageBaseActivity implements OnImageItemClickListener, ImageSelector.OnImageSelectedListener, View.OnClickListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;
    public static final String EXTRAS_IMAGES = "IMAGES";

    private View mFooterBar;     //底部栏
    private Button mBtnOk;       //确定按钮
    private View mllDir; //文件夹切换按钮
    private TextView mtvDir; //显示当前文件夹
    private TextView mBtnPre;      //预览按钮
    private ImageFolderAdapter mImageFolderAdapter;    //图片文件夹的适配器
    private FolderPopUpWindow mFolderPopupWindow;  //ImageSet的PopupWindow
    private RecyclerView mRecyclerView;
    private ImageRecyclerAdapter mRecyclerAdapter;
    private ImageDataSource imageDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageselector);

        ImageSelector.getInstance().clear();
        ImageSelector.getInstance().setOnImageSelectedListener(this);

        /**
         * 获取传递过来的选中图片，要求当前页面需要在已选图片显示打勾按钮
         */
        Intent data = getIntent();
        if (data != null && data.getExtras() != null) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(EXTRAS_IMAGES);
            //当遇到这些图片时应该显示选中状态
            ImageSelector.getInstance().setSelectedImages(images);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        findViewById(R.id.btn_back).setOnClickListener(this);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);

        mBtnPre = (TextView) findViewById(R.id.btn_preview);
        mBtnPre.setOnClickListener(this);

        mFooterBar = findViewById(R.id.footer_bar);
        mllDir = findViewById(R.id.ll_dir);
        mllDir.setOnClickListener(this);
        mtvDir = (TextView) findViewById(R.id.tv_dir);

        if (ImageSelector.getInstance().isMultiMode()) {
            mBtnOk.setVisibility(View.VISIBLE);
            //取消了预览所有图片功能
//            mBtnPre.setVisibility(View.VISIBLE);
            mBtnPre.setVisibility(View.GONE);
        } else {
            mBtnOk.setVisibility(View.GONE);
            mBtnPre.setVisibility(View.GONE);
        }

        mImageFolderAdapter = new ImageFolderAdapter(this);
        mRecyclerAdapter = new ImageRecyclerAdapter(this);
        onImageSelected(0, null, false);

        /**
         * 动态获取权限
         */
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            //检查是否有读取内存卡权限，没有就要动态获取
            if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
                return;
            }
        }
        /**
         * 开始加载所有图片，按图片文件夹分类
         */
        this.imageDataSource = new ImageDataSource(this,onImagesLoadedListener);
        //加载根目录下的所有图片文件夹
        this.imageDataSource.startLoad(null);
    }

    private ImageDataSource.OnImagesLoadedListener onImagesLoadedListener =  new ImageDataSource.OnImagesLoadedListener() {
        @Override
        public void onImagesLoaded(List<ImageFolder> imageFolders) {
            ImageSelector.getInstance().setImageFolders(imageFolders);
            //没有读取到文件夹的情况下就不要更新ui
            if(imageFolders!=null&&imageFolders.size()>0){
                updateUI();
            }
        }
    };

    /**
     * 更新ui
     * 默认显示第一个图片文件夹下的所有图片
     */
    private void updateUI() {
        mRecyclerAdapter.setData(ImageSelector.getInstance().getImageFolders().get(0).images);
        mRecyclerAdapter.setOnImageItemClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(this, 2), false));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        //为文件夹列表适配器写入数据
        mImageFolderAdapter.setData(ImageSelector.getInstance().getImageFolders());
    }


    /**
     * 请求动态获取权限完成之后
     * 不管成功或失败都会执行到这个方法，相当与onActivityResult方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            //读取存储权限
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(this.imageDataSource==null){
                    this.imageDataSource = new ImageDataSource(this,onImagesLoadedListener);
                }
                this.imageDataSource.startLoad(null);
            } else {
                showToast("权限被禁止，无法选择本地图片");
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            //访问相机权限
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取到相机权限之后去拍照
                onCameraClick();
            } else {
                showToast("权限被禁止，无法打开相机");
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            Intent intent = new Intent();
            intent.putExtra(ImageSelector.EXTRA_RESULT_ITEMS, ImageSelector.getInstance().getSelectedImages());
            setResult(ImageSelector.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
            finish();
        } else if (id == R.id.ll_dir) {
            if (ImageSelector.getInstance().getImageFolders() == null) {
                Log.i("ImageSelectorActivity", "您的手机没有图片");
                return;
            }
            //点击文件夹按钮
            createPopupFolderList();
            //刷新数据
            mImageFolderAdapter.setData(ImageSelector.getInstance().getImageFolders());
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.showAtLocation(mFooterBar, Gravity.NO_GRAVITY, 0, 0);
                //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                int index = mImageFolderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                mFolderPopupWindow.setSelection(index);
            }
        } else if (id == R.id.btn_preview) {
            //预览所有图片功能

        } else if (id == R.id.btn_back) {
            //点击返回按钮
            finish();
        }
    }

    /**
     * 创建底部弹出的ListView
     * 显示图片文件夹供选择
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopUpWindow(this, mImageFolderAdapter);
        mFolderPopupWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listview, View view, int position, long l) {
                mImageFolderAdapter.setSelectIndex(position);
                //改变当前选中的文件夹索引
                ImageSelector.getInstance().setCurrentImageFolderPosition(position);
                mFolderPopupWindow.dismiss();

                //等同于
                //mImageFolderAdapter.getItem(position);
                ImageFolder imageFolder = (ImageFolder) listview.getAdapter().getItem(position);
                if (null != imageFolder) {
                    //刷新图片显示，显示选中的文件夹下面的图片
                    mRecyclerAdapter.setData(imageFolder.images);
                    mtvDir.setText(imageFolder.name);
                }
            }
        });
        mFolderPopupWindow.setMargin(mFooterBar.getHeight());
    }


    /**
     * 图片点击事件
     *
     * @param view
     * @param imageItem
     * @param position
     */
    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
        //根据是否有相机按钮确定位置，单选模式下才有相机选项
        position = ImageSelector.getInstance().isMultiMode() ? position : position - 1;
        if (ImageSelector.getInstance().isMultiMode()) {
            //多选模式
            //预览当前点击的图片的功能
            Intent intent = new Intent();
            intent.setClass(this,ImageDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ImageDetailsActivity.OPEN_PARAM_IMAGE_KEY,imageItem);
            //当前item是否已经选中
            bundle.putSerializable(ImageDetailsActivity.OPEN_PARAM_IMAGE_IS_SELECTED_KEY,ImageSelector.getInstance().isSelect(imageItem));
            intent.putExtras(bundle);
            startActivityForResult(intent,ImageSelector.REQUEST_CODE_DETAILS);
        } else {
            //单选模式下，点击图片就是选中
            ImageSelector.getInstance().clearSelectedImages();
            ImageSelector.getInstance().addSelectedImageItem(position, ImageSelector.getInstance().getCurrentImageFolderItems().get(position), true);
            //是否需要裁剪
            if (ImageSelector.getInstance().isCrop()) {
                Intent intent = new Intent(ImageSelectorActivity.this, ImageCropActivity.class);
                startActivityForResult(intent, ImageSelector.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
            } else {
                Intent intent = new Intent();
                intent.putExtra(ImageSelector.EXTRA_RESULT_ITEMS, ImageSelector.getInstance().getSelectedImages());
                setResult(ImageSelector.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                finish();
            }
        }
    }

    /**
     * 点击拍照的item，调用相机拍照
     */
    @Override
    public void onCameraClick() {
        //检查是否有调用相机的权限，没有则请求获取权限
        if (!this.checkPermission(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ImageSelectorActivity.REQUEST_PERMISSION_CAMERA);
        } else {
            //调用相机拍照
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
                File takeImageFile;
                if (Utils.existSDCard())
                    takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
                else takeImageFile = Environment.getDataDirectory();
                takeImageFile = createFile(takeImageFile, "IMG_", ".jpg");
                if (takeImageFile != null) {
                    //保存这个takeImageFile
                    ImageSelector.getInstance().setTakeImageFile(takeImageFile);

                    // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                    // 可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                    // 如果没有指定uri，则data就返回有数据！
                    Uri uri;
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        uri = Uri.fromFile(takeImageFile);
                    } else {
                        /**
                         * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                         * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                         */
                        uri = FileProvider.getUriForFile(this, "com.lzy.imagepicker.fileprovider", takeImageFile);
                        //加入uri权限 要不三星手机不能拍照
                        List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
            }
            this.startActivityForResult(takePictureIntent, ImageSelector.REQUEST_CODE_TAKE);
        }
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    private File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    /**
     * 图片被选中刷新ui
     *
     * @param position
     * @param item
     * @param isAdd
     */
    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
        if (ImageSelector.getInstance().getSelectImageCount() > 0) {
            mBtnOk.setText(getString(R.string.ip_select_complete, ImageSelector.getInstance().getSelectImageCount(), ImageSelector.getInstance().getSelectLimit()));
            mBtnOk.setEnabled(true);
            mBtnPre.setEnabled(true);
            mBtnPre.setText(getResources().getString(R.string.ip_preview_count, ImageSelector.getInstance().getSelectImageCount()));
            //@by wujiuye
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBtnPre.setTextColor(getColor(R.color.ip_text_primary_inverted));
                mBtnOk.setTextColor(getColor(R.color.ip_text_primary_inverted));
            } else {
                mBtnPre.setTextColor(getResources().getColor(R.color.ip_text_primary_inverted));
            }
            mBtnOk.setTextColor(getResources().getColor(R.color.ip_text_primary_inverted));
        } else {
            mBtnOk.setText(getString(R.string.ip_complete));
            mBtnOk.setEnabled(false);
            mBtnPre.setEnabled(false);
            mBtnPre.setText(getResources().getString(R.string.ip_preview));
            //@by wujiuye
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mBtnPre.setTextColor(getColor(R.color.ip_text_secondary_inverted));
                mBtnOk.setTextColor(getColor(R.color.ip_text_secondary_inverted));
            } else {
                mBtnPre.setTextColor(getResources().getColor(R.color.ip_text_secondary_inverted));
                mBtnOk.setTextColor(getResources().getColor(R.color.ip_text_secondary_inverted));
            }
        }
        //通知RecyclerView指定的item改变了
        for (int i = ImageSelector.getInstance().isMultiMode() ? 0 : 1; i < mRecyclerAdapter.getItemCount(); i++) {
            if (mRecyclerAdapter.getItem(i).path != null && mRecyclerAdapter.getItem(i).path.equals(item.path)) {
                mRecyclerAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果是拍照，因为拍照指定了存储的Uri，所以返回的data一定为null
        if (resultCode == RESULT_OK && requestCode == ImageSelector.REQUEST_CODE_TAKE) {
            // 相机拍照的图片没有显示在手机图库中
            // 想要在手机相册图库中显示刚拍照的图片可以采用发送广播的方式
            // 发送广播通知图片增加了
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            //从imagePicker中取得takeImageFile
            Uri contentUri = Uri.fromFile(ImageSelector.getInstance().getTakeImageFile());
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

            //获取图片的绝对路径
            String path = ImageSelector.getInstance().getTakeImageFile().getAbsolutePath();
            ImageItem imageItem = new ImageItem();
            imageItem.path = path;
            //清空所有选中添加当前选中
            ImageSelector.getInstance().clearSelectedImages();
            ImageSelector.getInstance().addSelectedImageItem(0, imageItem, true);

            //如果是单选模式且需要裁剪，那么就跳转到裁剪页面
            if (!ImageSelector.getInstance().isMultiMode()) {
                if (ImageSelector.getInstance().isCrop()) {
                    Intent intent = new Intent(ImageSelectorActivity.this, ImageCropActivity.class);
                    startActivityForResult(intent, ImageSelector.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(ImageSelector.EXTRA_RESULT_ITEMS, ImageSelector.getInstance().getSelectedImages());
                    setResult(ImageSelector.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                    finish();
                }
            }
            return;
        }

        if (data != null && data.getExtras() != null) {
            if (resultCode == ImageSelector.RESULT_CODE_BACK) {
                if(resultCode == ImageSelector.REQUEST_CODE_DETAILS){
                    //如果是从预览图片页中返回
                    ImageItem imageItem = (ImageItem) data.getSerializableExtra(ImageDetailsActivity.OPEN_PARAM_IMAGE_IS_SELECTED_KEY);
                    Boolean isSelected = data.getBooleanExtra(ImageDetailsActivity.OPEN_PARAM_IMAGE_KEY,false);

                }
            } else {
                //从拍照界面返回
                if (data.getSerializableExtra(ImageSelector.EXTRA_RESULT_ITEMS) == null) {
                    //点击了 X 按钮 , 放弃了拍摄的照片（没有选择拍摄的图片）

                } else {
                    //说明是从裁剪页面过来的数据，直接返回就可以
                    setResult(ImageSelector.RESULT_CODE_ITEMS, data);
                    this.finish();
                }
            }
        }
    }

}