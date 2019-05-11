package wjy.androidlibs.imageselector.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import wjy.androidlibs.imageselector.ImageSelector;
import wjy.androidlibs.imageselector.R;
import wjy.androidlibs.imageselector.bean.ImageItem;

/**
 * 显示大图
 *
 * @author wjy
 */
public class ImageDetailsActivity extends ImageBaseActivity {

    public static final String OPEN_PARAM_IMAGE_KEY = "Image";
    public static final String OPEN_PARAM_IMAGE_IS_SELECTED_KEY = "IsSelected";

    private boolean isSelected;
    private ImageSelector mImageSelector;
    private ImageItem mImageItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedetails);
        mImageSelector = ImageSelector.getInstance();
        mImageItem = (ImageItem) getIntent().getSerializableExtra(OPEN_PARAM_IMAGE_KEY);
        isSelected = getIntent().getBooleanExtra(OPEN_PARAM_IMAGE_IS_SELECTED_KEY, false);
        if (mImageItem == null && savedInstanceState != null) {
            if (savedInstanceState.containsKey(OPEN_PARAM_IMAGE_KEY)) {
                mImageItem = (ImageItem) savedInstanceState.getSerializable(OPEN_PARAM_IMAGE_KEY);
                isSelected = savedInstanceState.getBoolean(OPEN_PARAM_IMAGE_IS_SELECTED_KEY);
            }
        }
        onInitFindView();
    }

    private void onInitFindView() {
        this.findViewById(R.id.btn_ok).setVisibility(View.GONE);
        ImageView imageView = (ImageView) this.findViewById(R.id.ivImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailsActivity.this.finish();
            }
        });
        mImageSelector.getImageLoader().displayImage(this,
                mImageItem.path, imageView, 0, 0);
        this.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDetailsActivity.this.finish();
            }
        });
    }

    /**
     * 结果当前页面
     */
    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(OPEN_PARAM_IMAGE_KEY, mImageItem);
        bundle.putSerializable(OPEN_PARAM_IMAGE_IS_SELECTED_KEY, isSelected);
        intent.putExtras(bundle);
        setResult(ImageSelector.RESULT_CODE_BACK, intent);
        super.finish();
    }
}
