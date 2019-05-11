package wjy.weiai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;

import java.util.Map;

import butterknife.BindView;
import wjy.weiai.AppConfig;
import wjy.weiai.Jyson.Jyson;
import wjy.weiai.R;
import wjy.weiai.adapter.AlbumImageAdapter;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.bean.Itinerary;
import wjy.weiai.request.HttpUrl;
import wjy.weiai.request.LoadDataContract;
import wjy.weiai.request.exception.ApiException;
import wjy.weiai.utils.DateTimeUtils;
import wjy.weiai.widget.GridSpacingItemDecoration;

/**
 * 侣行记详情页面
 *
 * @author wjy
 */
public class ItineraryDeatilsActivity extends BaseActivity {


    private LoadDataContract loadDataContract;
    private Itinerary itinerary;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_itinerary_datails;
    }


    @BindView(R.id.navTitle)
    protected TextView navTitle;

    @BindView(R.id.ivUserImg)
    protected ImageView ivUser;
    @BindView(R.id.tvUsername)
    protected TextView tvUsername;
    @BindView(R.id.tvDetail)
    protected TextView tvDetails;
    @BindView(R.id.tvDate)
    protected TextView tvDate;
    @BindView(R.id.tvAddress)
    protected TextView tvAddress;

    @BindView(R.id.ryAlbum)
    protected RecyclerView ryAlbum;
    private AlbumImageAdapter albumImageAdapter;

    @Override
    protected void onInit() {
        this.ryAlbum.addItemDecoration(new GridSpacingItemDecoration(this, 2, 10, true));
        this.ryAlbum.setLayoutManager(new GridLayoutManager(
                this, 2, GridLayoutManager.VERTICAL, false));
        if (getIntent().hasExtra("Itinerary")) {
            this.itinerary = (Itinerary) getIntent().getSerializableExtra("Itinerary");
        }
        if (itinerary == null) {
            showThreadToast("相册不存在！！！");
            this.finish();
        }
        this.loadDataContract = new LoadDataContract(this);
        updateUI();
        this.loadItineraryDetatils();
    }


    /**
     * 加载相册详情
     */
    private void loadItineraryDetatils() {
        loadDataContract.loadData(Request.Method.GET, HttpUrl.ITINERARY_DETAILS_URL.replace("{id}", itinerary.getId() + ""), new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                return null;
            }

            @Override
            public void onSuccess(String json) {
                try {
                    itinerary = (Itinerary) new Jyson().parseJson(json, Itinerary.class);
                    if (itinerary == null) {
                        showAlertMessage("加载异常！");
                    }
                    updateUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ApiException ex) {

            }
        });
    }


    /**
     * 更新数据显示
     */
    private void updateUI() {
        if (itinerary != null) {
            this.navTitle.setText(itinerary.getTitle());
            this.tvUsername.setText(itinerary.getUser().getUsername());
            this.tvDate.setText(DateTimeUtils.date2String(itinerary.getCreateDatetime()));
            this.tvDetails.setText(itinerary.getDetails());
            if (itinerary.getAddress() == null || itinerary.getAddress().length() == 0)
                this.tvAddress.setText("未设置地址");
            else
                this.tvAddress.setText(itinerary.getAddress());
            Glide.with(this)
                    .load(itinerary.getUser().getHeadThumb())
                    .apply(AppConfig.getRequestOptionsCircleCrop())
                    .into(this.ivUser);
        }
        if (itinerary != null && itinerary.getAlbum() != null && itinerary.getAlbum().getPhotosList() != null) {
            if (albumImageAdapter == null) {
                albumImageAdapter = new AlbumImageAdapter(this, null);
                albumImageAdapter.setOnItemClickListener(new AlbumImageAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //预览相册详情，并跳到指定相片位置，显示原图
                        Intent intent = new Intent(ItineraryDeatilsActivity.this, AlbumPreviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Itinerary", itinerary);
                        bundle.putInt("postion", position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                this.ryAlbum.setAdapter(albumImageAdapter);
            }
            //显示的还是缩略图
            albumImageAdapter.setImages(itinerary.getAlbum().getPhotosList());
        }
    }

}
