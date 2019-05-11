package wjy.weiai.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import io.netty.util.internal.StringUtil;
import wjy.androidlibs.imageselector.ImageSelector;
import wjy.androidlibs.imageselector.activity.ImageSelectorActivity;
import wjy.androidlibs.imageselector.bean.ImageItem;
import wjy.weiai.Jyson.Jyson;
import wjy.weiai.R;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.hook.OnActivityResultCallback;
import wjy.weiai.hook.StartActivityFromResult;
import wjy.weiai.request.HttpUrl;
import wjy.weiai.request.RequestResult;
import wjy.weiai.request.exception.ApiException;
import wjy.weiai.request.upload.MultipartRequest;

public class MemorialDayAddActivity extends BaseActivity {

    @BindView(R.id.navTitle)
    protected TextView navTitle;

    @BindView(R.id.tvPriority)
    protected TextView tvPriority;
    @BindViews({R.id.tvPriority01, R.id.tvPriority02, R.id.tvPriority03, R.id.tvPriority04
            , R.id.tvPriority05, R.id.tvPriority06, R.id.tvPriority07})
    protected List<TextView> tvPriorityList;

    @BindView(R.id.tvName)
    protected TextView tvName;
    @BindView(R.id.tvDate)
    protected TextView tvDate;
    private int mYear, mMonth, mDay;


    @BindView(R.id.ryImage)
    protected View ryImage;
    @BindView(R.id.ivSelectImg)
    protected ImageView ivSelectImg;
    private ImageSelector mImageSelector;
    private ImageItem currentSelectImageItem;

    private RequestQueue mRequestQueue;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_memorialday_add;
    }

    @Override
    protected void onInit() {
        navTitle.setText(R.string.activity_title_memorialday_add);
        initPrioritySelect();
        initDateSelect();
        initImageSelect();
        mRequestQueue = Volley.newRequestQueue(this);
    }

    /**
     * 级别选择
     */
    private void initPrioritySelect() {
        for (TextView pr : tvPriorityList) {
            pr.setOnClickListener(view -> {
                tvPriority.setBackground(view.getBackground());
                tvPriority.setTag(view.getTag());
                for (TextView pr2 : tvPriorityList) {
                    if (pr2.getId() == view.getId()) {
                        pr2.setText("√");
                    } else {
                        pr2.setText("");
                    }
                }
            });
        }
    }

    /**
     * 日期选择
     */
    private void initDateSelect() {
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH) + 1;
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(mYear + "年" + (mMonth > 9 ? mMonth : "0" + mMonth) + "月" + (mDay > 9 ? mDay : "0" + mDay) + "日");
        tvDate.setOnClickListener((view) -> {
            new DatePickerDialog(MemorialDayAddActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    String days;
                    if (mMonth < 10) {
                        if (mDay < 10) {
                            days = new StringBuffer().append(mYear).append("年").append("0").
                                    append(mMonth).append("月").append("0").append(mDay).append("日").toString();
                        } else {
                            days = new StringBuffer().append(mYear).append("年").append("0").
                                    append(mMonth).append("月").append(mDay).append("日").toString();
                        }

                    } else {
                        if (mDay < 10) {
                            days = new StringBuffer().append(mYear).append("年").
                                    append(mMonth).append("月").append("0").append(mDay).append("日").toString();
                        } else {
                            days = new StringBuffer().append(mYear).append("年").
                                    append(mMonth).append("月").append(mDay).append("日").toString();
                        }

                    }
                    tvDate.setText(days);
                }
            }, mYear, mMonth, mDay).show();
        });
    }

    /**
     * 背景图片选择
     */
    private void initImageSelect() {
        mImageSelector = ImageSelector.getInstance();
        mImageSelector.clearSelectedImages();//清除当前选中
        mImageSelector.setMultiMode(false);//设置为单选模式
        mImageSelector.setCrop(false);//不需要裁剪
        ryImage.setOnClickListener(view ->
                startActivityFormResult(ImageSelectorActivity.class, 100, new OnActivityResultCallback() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        if (requestCode == 100) {
                            //添加图片返回
                            if (resultCode == ImageSelector.RESULT_CODE_ITEMS) {
                                ArrayList<ImageItem> imageItems = mImageSelector.getSelectedImages();
                                if (imageItems != null && imageItems.size() > 0) {
                                    currentSelectImageItem = imageItems.get(0);
                                    ImageSelector.getInstance()
                                            .getImageLoader()
                                            .displayImage(MemorialDayAddActivity.this, currentSelectImageItem.path, ivSelectImg, 0, 0);
                                }
                            }
                        }
                    }
                }));
    }


    /**
     * 验证参数
     *
     * @return
     */
    private Map<String, String> vaParam() {
        //纪念日名称
        String name = tvName.getText().toString();
        if (StringUtil.isNullOrEmpty(name)) {
            showAlertMessage("请输入纪念日名称");
            return null;
        }
        //日期
        String date = mYear + "-" + (mMonth > 9 ? mMonth : "0" + mMonth) + "-" + (mDay > 9 ? mDay : "0" + mDay);
        //重要性
        String priority = tvPriority.getTag().toString();
        //背景图片
        if (currentSelectImageItem == null) {
            showAlertMessage("请选中一张图片");
            return null;
        }
        Map<String, String> param = new HashMap<>();
        param.put("priority", priority + "");
        param.put("date", date);
        param.put("name", name);
        return param;
    }


    /**
     * 提交添加纪念日请求
     */
    @OnClick(R.id.btnAdd)
    protected void btnAddClick() {
        Map<String, String> param = vaParam();
        if (param == null) return;
        ArrayList<ImageItem> imageItems = new ArrayList();
        imageItems.add(currentSelectImageItem);
        showWaitDialog("正在上传图片...");
        MultipartRequest uploadRequest = new MultipartRequest(getApplicationContext(), HttpUrl.MEMORIALDAY_ADD_URL,
                param, "image", imageItems,
                (String response) -> {
                    dismissWaitDialog();
                    try {
                        RequestResult requestResult = (RequestResult) new Jyson().parseJson(response, RequestResult.class);
                        if (requestResult != null && requestResult.getErrorCode() == ApiException.ApiResponseErrorCode.SUCCESS) {
                            this.finish();
                            return;
                        }
                        showAlertMessage(requestResult == null ? "上传失败了！" : requestResult.getErrorMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlertMessage("上传失败了！");
                    }
                }, (long bytes) -> {
            //进度
        }, (VolleyError error) -> {
            error.printStackTrace();
            dismissWaitDialog();
            showAlertMessage("上传失败了！");
        }
        );
        this.mRequestQueue.add(uploadRequest);
    }
}
