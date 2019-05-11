package wjy.weiai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.weiai.AppConfig;
import wjy.weiai.R;
import wjy.weiai.bean.ScenicArea;
import wjy.weiai.widget.GridSpacingItemDecoration;
import wjy.weiai.widget.SpacesItemDecoration;
import wjy.weiai.widget.expandablebuttonmenu.ScreenHelper;

/**
 * 内容适配器
 *
 * @author wjy
 */
public class ScenicAreaAdpter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * item点击事件
     */
    public interface OnItemClickListerer {
        void onItemClick(ScenicArea item);
    }

    private OnItemClickListerer onItemClickListerer = null;

    public void setOnItemClickListerer(
            OnItemClickListerer onItemClickListerer) {
        this.onItemClickListerer = onItemClickListerer;
    }

    // 头部视图
    public final static int HEAD_VIEW = 1;
    // 一行两列
    public final static int TWO_CELL = 2;

    /**
     * 数据源
     */
    private List<ScenicArea> data;
    private Context context;

    /**
     * 头部视图
     */
    private View headView = null;

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    /**
     * 构造函数
     *
     * @param context
     */
    public ScenicAreaAdpter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.data = new ArrayList<>();
        //初始化recyclerView的布局
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(context, 2, 10, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                context, 2, GridLayoutManager.VERTICAL, false);
        int spanCount = gridLayoutManager.getSpanCount();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (getItemViewType(position)) {
                    case HEAD_VIEW:
                        return spanCount / HEAD_VIEW;
                    case TWO_CELL:
                        return spanCount / TWO_CELL;
                    default:
                        return spanCount;
                }

            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    public void setData(List<ScenicArea> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public void addData(List<ScenicArea> data) {
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

        public abstract void showData(ScenicArea bean);
    }

    public class HeadViewHolder extends BaseViewHolder {

        public HeadViewHolder(View rootView) {
            super(rootView);
        }

        @Override
        public void showData(ScenicArea bean) {

        }


    }

    /**
     * 一行显示两列的样式
     *
     * @author wjy
     */
    public class TwoCellTypeViewHolder extends BaseViewHolder {
        @BindView(R.id.tvTitle)
        protected TextView tvTitle;
        @BindView(R.id.tvDetail)
        protected TextView tvDetail;

        @BindView(R.id.ivAlbum)
        protected ImageView ivAlbum;

        public TwoCellTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showData(ScenicArea bean) {
            try {
                Glide.with(context)
                        .load(bean.getImages().get(0).getPicUrlSmall())
                        .apply(AppConfig.getRequestOptionsWithRoundingRadius((int) ScreenHelper.dpToPx(context, 16)))
                        .into(ivAlbum);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            tvTitle.setText(bean.getScenicName());
            tvDetail.setText(bean.getSummary());
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
        switch (viewType) {
            case HEAD_VIEW:
                return new HeadViewHolder(this.headView);
            case TWO_CELL:
                return new TwoCellTypeViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(
                        R.layout.rv_item_scenicarea, parent, false));
        }
        return null;
    }

    /**
     * 绑定item视图数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScenicArea bean = this.getItem(position);
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
        return this.data.size() + (headView == null ? 0 : 1);
    }

    /**
     * 手动计算item
     *
     * @return
     */
    private ScenicArea getItem(int position) {
        if (position == 0 && headView != null)
            return null;
        position = position - (headView != null ? 1 : 0);
        return this.data.get(position);
    }

    /**
     * 根据数据源下标获取该item对应的样式
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && headView != null)
            return HEAD_VIEW;
        return TWO_CELL;
    }


    /**
     * 当前item被回收时调用，可用来释放绑定在view上的数据
     *
     * @param holder
     */
    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
