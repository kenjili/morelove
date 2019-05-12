package wjy.morelove.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.morelove.AppConfig;
import wjy.morelove.R;
import wjy.morelove.bean.Itinerary;
import wjy.morelove.utils.DateTimeUtils;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册适配器
 * @author wjy
 */
public class ItineraryAdpter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * item点击事件
     */
    public interface OnItemClickListerer {
        void onItemClick(Itinerary item);
    }

    private OnItemClickListerer onItemClickListerer = null;

    public void setOnItemClickListerer(
            OnItemClickListerer onItemClickListerer) {
        this.onItemClickListerer = onItemClickListerer;
    }

    /**
     * 数据源
     */
    private List<Itinerary> data;
    private Context context;

    /**
     * 构造函数
     *
     * @param context
     */
    public ItineraryAdpter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setData(List<Itinerary> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void addData(List<Itinerary> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    /**
     * 基样式
     *
     * @author wjy
     */
    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View arg0) {
            super(arg0);
        }

        public abstract void showData(Itinerary bean);
    }


    /**
     * 一行显示两列的样式
     *
     * @author wjy
     */
    public class TwoCellTypeViewHolder extends BaseViewHolder {

        @BindView(R.id.ivUser)
        protected ImageView ivUser;
        @BindView(R.id.tvUsername)
        protected TextView tvUsername;

        @BindView(R.id.tvItinerartTitle)
        protected TextView tvItinerartTitle;
        @BindView(R.id.tvItinerartContext)
        protected TextView tvItinerartContext;

        @BindView(R.id.ivAlbum)
        protected ImageView ivAlbum;

        @BindView(R.id.tvDate)
        protected TextView tvDate;

        private View rootView;

        public TwoCellTypeViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void showData(Itinerary bean) {
            if(bean.getUser()!=null){
                tvUsername.setText(bean.getUser().getUsername());
                Glide.with(rootView)
                        .load(bean.getUser().getHeadThumb())
                        .apply(AppConfig.getRequestOptionsCircleCrop())
                        .into(ivUser);
            }

            tvItinerartTitle.setText(bean.getTitle());
            tvItinerartContext.setText(bean.getDetails());
            tvDate.setText(DateTimeUtils.date2StringYMD(bean.getCreateDatetime()));

            if (bean.getAlbum().getPhotosList() != null && bean.getAlbum().getPhotosList().size() > 0) {
                String url;
                if (bean.getAlbum().getPhotosList().get(0).getImgThumb() != null) {
                    url = bean.getAlbum().getPhotosList().get(0).getImgThumb();
                } else {
                    url = bean.getAlbum().getPhotosList().get(0).getImg();
                }
                Glide.with(rootView)
                        .load(url)
                        .apply(AppConfig.getRequestOptions())
                        .into(ivAlbum);
            }

        }
    }


    /**
     * 创建item视图
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        return new TwoCellTypeViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(
                R.layout.rv_item_itinerary, parent, false));

    }

    /**
     * 绑定item视图数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Itinerary bean = this.getItem(position);
        if (bean == null)
            return;
        ((BaseViewHolder) holder).showData(bean);
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (onItemClickListerer != null) {
                    onItemClickListerer.onItemClick(bean);
                }
            }
        });
    }

    /**
     * 返回数据源总共多少项
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return this.data.size();
    }

    /**
     * 手动计算item
     *
     * @return
     */
    private Itinerary getItem(int position) {
        return this.data.get(position);
    }


}
