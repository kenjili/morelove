package wjy.morelove.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import wjy.androidlibs.imageselector.ImageSelector;
import wjy.androidlibs.imageselector.activity.ImageSelectorActivity;
import wjy.androidlibs.imageselector.bean.ImageItem;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import wjy.morelove.AppConfig;
import wjy.morelove.R;
import wjy.morelove.adapter.ImagePickerAdapter;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.service.UploadPublicService;

public class PublicItineraryActivity extends BaseActivity {

    @BindView(R.id.navTitle)
    protected TextView navTitle;

    @BindView(R.id.publicButton)
    protected ImageView publicButton;

    @BindView(R.id.rySelectImages)
    protected RecyclerView rySelectImages;
    @BindView(R.id.tvImageSizeCount)
    protected TextView tvImageSizeCount;

    private ImagePickerAdapter mAdapter;
    private ImageSelector mImageSelector;

    private ArrayList<ImageItem> uploadImages;


    @BindView(R.id.etTitle)
    protected EditText etTitle;
    @BindView(R.id.etContext)
    protected EditText etContext;
    @BindView(R.id.tvAddress)
    protected TextView tvAddress;

    @Override
    protected void onInit() {
        this.navTitle.setText(R.string.public_pop_dialog_menu_itinerary);
        this.publicButton.setVisibility(View.VISIBLE);

        mImageSelector = ImageSelector.getInstance();
        mImageSelector.clearSelectedImages();
        //不能直接使用ImagePicker.getInstance().getSelectedImages()
        //否则调用ImagePicker.getInstance().clearSelectedImages();uploadImages也会被清空
        this.uploadImages = new ArrayList<>();

        mAdapter = new ImagePickerAdapter(this, uploadImages, AppConfig.albumUploadMaxNumber());
        mAdapter.setOnItemClickListener((View view, int position) -> {
            int imgCount = uploadImages.size();
            if (position == imgCount) {
                //添加图片
                openImagePicker();
            } else {
                //删除选中图片
                uploadImages.remove(position);
                mAdapter.setImages(uploadImages);
                mAdapter.notifyDataSetChanged();
                updateImagesSize();
            }
        });
        this.rySelectImages.setLayoutManager(new GridLayoutManager(
                this, 3
        ));
        this.rySelectImages.setAdapter(mAdapter);
        updateImagesSize();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_public_itinerary;
    }


    private void updateImagesSize() {
        //计算图片总大小
        long size = 0;
        for (ImageItem imageItem : uploadImages) {
            size += imageItem.size;
            Log.i("imageitem", "[路径：" + imageItem.path + " 名字：" + imageItem.name + " 大小：" + imageItem.size);
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 保留两位小数
        nf.setMaximumFractionDigits(2);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);
        String strSize = nf.format(size / 1048576.0d);
        tvImageSizeCount.setText(strSize + " mb");
    }

    /**
     * 发布
     *
     * @param view
     */
    @OnClick(R.id.publicButton)
    public void onPublicButtonClick(View view) {
        if (uploadImages.size() == 0) {
            showAlertMessage("请先选择图片！");
            return;
        }
        if (etTitle.getText().toString().trim().length() == 0) {
            showAlertMessage("请输入标题！");
            return;
        }
        if (etContext.getText().toString().trim().length() == 0) {
            showAlertMessage("请输入内容！");
            return;
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("title", etTitle.getText().toString());
        param.put("details", etContext.getText().toString());
        //位置信息应该是可以为空的
        param.put("address", tvAddress.getText().toString());

        //开启上传服务
        Intent intent = new Intent();
        intent.setClass(this, UploadPublicService.class);
        intent.putExtra(UploadPublicService.UPLOAD_PARAM_NAME, param);
        intent.putExtra(UploadPublicService.UPLOAD_IMAGE_NAME, uploadImages);
        intent.putExtra(UploadPublicService.UPLOAD_ACTION_NAME, UploadPublicService.UPLOAD_ACTION_LVXINGJI);
        if (Build.VERSION.SDK_INT >= 26) {
            //开启前台服务
            this.startForegroundService(intent);
        } else {
            this.startService(intent);
        }
        this.finish();
    }


    /**
     * 打开图片选择器
     */
    private void openImagePicker() {
        //打开选择,本次允许选择的数量
        mImageSelector.setSelectLimit(AppConfig.albumUploadMaxNumber());
        ImageSelector.getInstance().setMultiMode(true);
        Intent selectImgsIntent = new Intent();
//        mImageSelector.setSelectedImages(this.uploadImages);
        //将当前已选图片传递给图片选择器
        selectImgsIntent.putExtra(ImageSelectorActivity.EXTRAS_IMAGES, this.uploadImages);
        selectImgsIntent.setClass(this, ImageSelectorActivity.class);
        startActivityFormResult(selectImgsIntent, 100, (int requestCode, int resultCode, Intent data) -> {
            if (requestCode == 100) {
                //添加图片返回
                if (resultCode == ImageSelector.RESULT_CODE_ITEMS) {
                    ArrayList<ImageItem> imageItems = mImageSelector.getSelectedImages();
                    if (imageItems != null && imageItems.size() > 0) {
                        for (ImageItem imageItem : imageItems) {
                            if (uploadImages.contains(imageItem)) continue;
                            uploadImages.add(imageItem);
                        }
                        this.mAdapter.setImages(uploadImages);
                        updateImagesSize();
                    }
                }
            }
        });
    }
}
