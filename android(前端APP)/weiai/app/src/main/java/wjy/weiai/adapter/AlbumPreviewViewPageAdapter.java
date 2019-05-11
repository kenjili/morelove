package wjy.weiai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.weiai.AppConfig;
import wjy.weiai.R;
import wjy.weiai.bean.Album;
import wjy.weiai.bean.Photos;

public class AlbumPreviewViewPageAdapter extends PagerAdapter{

    private Album data;
    private Context context;
    private LayoutInflater mInflater;
    private RequestOptions requestOptions;

    public AlbumPreviewViewPageAdapter(Context context,Album album){
        this.context = context;
        this.data = album;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.getPhotosList().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    class ItemView{

        @BindView(R.id.ivImgSource)
        protected ImageView ivItem;

        public ItemView(View rootView){
            ButterKnife.bind(this,rootView);
        }

        public void showImg(Photos photo){
            //加载原图
            Glide.with(context)
                    .load(photo.getImg())
                    .apply(AppConfig.getRequestOptions())
                    .into(this.ivItem);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = mInflater.inflate(R.layout.view_image,null);
        ItemView itemView = new ItemView(rootView);
        itemView.showImg(this.data.getPhotosList().get(position));
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     * 设置每个item的宽度
     * @param position
     * @return
     */
    @Override
    public float getPageWidth(int position) {
        return 0.8f;
    }

}
