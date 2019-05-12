package wjy.morelove.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rainbow.library.GalleryViewPager;
import wjy.morelove.AppConfig;
import wjy.morelove.Jyson.Jyson;
import wjy.morelove.R;
import wjy.morelove.adapter.AlbumPreviewViewPageAdapter;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.bean.Album;
import wjy.morelove.bean.Lovetime;
import wjy.morelove.bean.Photos;
import wjy.morelove.request.HttpUrl;
import wjy.morelove.request.LoadDataContract;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.utils.DateTimeUtils;


/**
 * 恋爱时光详情
 *
 * @author wjy
 */
public class LovetimeDetailsActivity extends BaseActivity {


    @BindView(R.id.ivUserImg)
    protected ImageView ivUser;
    @BindView(R.id.tvUsername)
    protected TextView tvUsername;
    @BindView(R.id.tvDetail)
    protected TextView tvDetails;
    @BindView(R.id.tvDate)
    protected TextView tvDate;

    private Lovetime lovetime;
    private LoadDataContract loadDataContract;


    @BindView(R.id.vpImages)
    protected GalleryViewPager vpImages;
    private AlbumPreviewViewPageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        //mainactivity状态栏的处理
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        this.getWindow().setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
        return R.layout.activity_lovetime_details;
    }


    @Override
    protected void onInit() {
        if (getIntent().hasExtra("Lovetime"))
            lovetime = (Lovetime) getIntent().getSerializableExtra("Lovetime");
        if (lovetime == null){
            showThreadToast("时光记录不存在");
            this.finish();
        }
        updateUI();
        loadLovetimeDetails();
    }

    /**
     * 刷新ui
     */
    private void updateUI(){
        if(lovetime!=null){
            this.tvDate.setText(DateTimeUtils.date2String(lovetime.getCreateDatetime()));
            this.tvDetails.setText(lovetime.getDetails());
            this.tvUsername.setText(lovetime.getUser().getUsername());
            Glide.with(this)
                    .load(lovetime.getUser().getHeadThumb())
                    .apply(AppConfig.getRequestOptionsCircleCrop())
                    .into(this.ivUser);

            List<Photos> photosList = new ArrayList<>();
            if(lovetime.getImg1()!=null){
                Photos photos = new Photos();
                photos.setImg(lovetime.getImg1());
                photos.setImgThumb(lovetime.getImgThumb1());
                photosList.add(photos);
            }
            if(lovetime.getImg2()!=null){
                Photos photos = new Photos();
                photos.setImg(lovetime.getImg2());
                photos.setImgThumb(lovetime.getImgThumb2());
                photosList.add(photos);
            }
            if(lovetime.getImg3()!=null){
                Photos photos = new Photos();
                photos.setImg(lovetime.getImg3());
                photos.setImgThumb(lovetime.getImgThumb3());
                photosList.add(photos);
            }
            Album album = new Album();
            album.setPhotosList(photosList);
            mAdapter = new AlbumPreviewViewPageAdapter(LovetimeDetailsActivity.this,album);
            this.vpImages.setAdapter(mAdapter);
        }
    }


    /**
     * 加载时光详情
     */
    private void loadLovetimeDetails() {
        loadDataContract = new LoadDataContract(this);
        loadDataContract.loadData(Request.Method.GET, HttpUrl.LOVETIME_DETAILS_URL.replace("{page}",lovetime.getId()+""),
                new LoadDataContract.ViewDataContract() {
                    @Override
                    public Map<String, String> getParams() {
                        return null;
                    }

                    @Override
                    public void onSuccess(String json){
                        try {
                            Lovetime lovetime = (Lovetime) new Jyson().parseJson(json,Lovetime.class);
                            if(lovetime!=null){
                               LovetimeDetailsActivity.this.lovetime = lovetime;
                               updateUI();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {

                    }
                });
    }
}
