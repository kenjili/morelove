package wjy.morelove.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.morelove.AppConfig;
import wjy.morelove.R;
import wjy.morelove.bean.Photos;


/**
 * 旅行记相册适配器
 *
 * @author wjy
 */
public class AlbumImageAdapter extends RecyclerView.Adapter<AlbumImageAdapter.SelectedPicViewHolder> {

    private Context mContext;
    private List<Photos> mData;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<Photos> data) {
        mData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public AlbumImageAdapter(Context mContext, List<Photos> data) {
        if(data==null)data = new ArrayList<>();
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(mInflater.inflate(R.layout.rv_item_album_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_img)
        protected ImageView iv_img;
        private int clickPosition;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            clickPosition = position;
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            Photos item = mData.get(position);
            iv_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext)
                    .load(item.getImgThumb())
                    .apply(AppConfig.getRequestOptions())
                    .into(iv_img);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, clickPosition);
        }
    }
}