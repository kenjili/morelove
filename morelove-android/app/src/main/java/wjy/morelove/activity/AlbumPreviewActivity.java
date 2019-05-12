package wjy.morelove.activity;

import android.widget.TextView;

import butterknife.BindView;
import rainbow.library.GalleryViewPager;
import wjy.morelove.R;
import wjy.morelove.adapter.AlbumPreviewViewPageAdapter;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.bean.Itinerary;

/**
 * 网络相册预览页面
 * Coverflow实现
 *
 * @author wjy
 */
public class AlbumPreviewActivity extends BaseActivity {

    @BindView(R.id.navTitle)
    protected TextView navTitle;

    private Itinerary itinerary;

    @BindView(R.id.vpImages)
    protected GalleryViewPager vpImages;
    private AlbumPreviewViewPageAdapter mAdapter;

    @BindView(R.id.tvPostion)
    protected TextView tvPostion;

    private int currentPostion = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album_preview;
    }

    @Override
    protected void onInit() {
        this.navTitle.setText(R.string.activity_title_ablum_preview);
        if (getIntent().hasExtra("Itinerary")) {
            this.itinerary = (Itinerary) getIntent().getSerializableExtra("Itinerary");
        }
        if (itinerary == null) {
            showThreadToast("相册不存在！！！");
            this.finish();
        }
        this.navTitle.setText(itinerary.getTitle());
        this.onInitViewPage();
        currentPostion = getIntent().getIntExtra("postion", 0);
        this.vpImages.setCurrentItem(currentPostion);
        onSelectedShowImagesChange(currentPostion);
    }

    /**
     * 改变当前显示的位置
     *
     * @param postion
     */
    private void onSelectedShowImagesChange(int postion) {
        currentPostion = postion;
        if (itinerary.getAlbum() != null && itinerary.getAlbum().getPhotosList()!=null)
            this.tvPostion.setText((postion + 1) + "/" + itinerary.getAlbum().getPhotosList().size());
    }

    private void onInitViewPage() {
        mAdapter = new AlbumPreviewViewPageAdapter(this, itinerary.getAlbum());
        this.vpImages.setAdapter(mAdapter);
        this.vpImages.addOnPageChangeListener(new GalleryViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                //改变图片显示
                onSelectedShowImagesChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
