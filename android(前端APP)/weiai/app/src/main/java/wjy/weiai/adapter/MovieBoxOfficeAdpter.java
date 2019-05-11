package wjy.weiai.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.weiai.AppConfig;
import wjy.weiai.R;
import wjy.weiai.bean.BoxOfficeData;
import wjy.weiai.bean.MovieBoxOffice;
import wjy.weiai.widget.GridSpacingItemDecoration;

/**
 * 电影票房适配器
 *
 * @author wjy
 */
public class MovieBoxOfficeAdpter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * item点击事件
     */
    public interface OnItemClickListerer {
        void onItemClick(MovieBoxOffice item);
    }

    private OnItemClickListerer onItemClickListerer = null;

    public void setOnItemClickListerer(
            OnItemClickListerer onItemClickListerer) {
        this.onItemClickListerer = onItemClickListerer;
    }

    // 分组头部视图
    public final static int HEAD_VIEW = 1;
    // 一行一列
    public final static int ONE_CELL = 2;

    /**
     * 数据源
     */
    private BoxOfficeData data;
    private Context context;

    /**
     * 构造函数
     *
     * @param context
     */
    public MovieBoxOfficeAdpter(Context context, BoxOfficeData data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;
        //初始化recyclerView的布局
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(context, 2, 10, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    public void setData(BoxOfficeData data) {
        this.data = data;
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

        public abstract void showData(Serializable bean);
    }

    /**
     * 分组头样式
     */
    public class HeadViewHolder extends BaseViewHolder {


        @BindView(R.id.tvGroupTitle)
        protected TextView tvGroupTitle;

        public HeadViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }

        @Override
        public void showData(Serializable bean) {
            tvGroupTitle.setText((String) bean);
        }


    }

    /**
     * 一行一列样式
     *
     * @author wjy
     */
    public class MovieViewHolder extends BaseViewHolder {
        @BindView(R.id.tvMovieName)
        protected TextView tvMovieName;
        @BindView(R.id.ivMovieImg)
        protected ImageView ivMovieImg;

        @BindView(R.id.tvBoxOffice)
        protected TextView tvBoxOffice;
        @BindView(R.id.tvPiaoJia)
        protected TextView tvPiaoJia;

        @BindView(R.id.tvDays)
        protected TextView tvDays;

        @BindView(R.id.tvDate)
        protected TextView tvDate;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showData(Serializable bean) {
            MovieBoxOffice movieBoxOffice = (MovieBoxOffice) bean;
            tvDate.setText(movieBoxOffice.getReleaseTime());
            tvDays.setText("" + movieBoxOffice.getMovieDay());
            tvPiaoJia.setText("" + (int) movieBoxOffice.getBoxPer());
            tvBoxOffice.setText("" + (int) movieBoxOffice.getBoxOffice());
            tvMovieName.setText(movieBoxOffice.getMovieName());
            Glide.with(context)
                    .load(movieBoxOffice.getMovieImg())
                    .apply(AppConfig.getRequestOptions())
                    .into(ivMovieImg);
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
                return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.rv_item_movieboxoffice_grouptitle, null
                ));
            case ONE_CELL:
                return new MovieViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(
                        R.layout.rv_item_movieboxoffice, parent, false));
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
        MovieBoxOffice bean = this.getItem(position);
        if (bean == null) {
            if (position - 2 == this.data.getHourBoxOffice().size() + this.data.getDayBoxOffice().size()) {
                ((BaseViewHolder) holder).showData("单月票房");
            } else if (position - 1 == this.data.getHourBoxOffice().size()) {
                ((BaseViewHolder) holder).showData("单日票房");
            } else if (position - 1 < 0) {
                ((BaseViewHolder) holder).showData("实时票房");
            }
            return;
        }
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
        int count = 0;
        if (this.data.getHourBoxOffice() != null) {
            count++;
            count += this.data.getHourBoxOffice().size();
        }
        if (this.data.getDayBoxOffice() != null) {
            count++;
            count += this.data.getDayBoxOffice().size();
        }
        if (this.data.getMonthBoxOffice() != null) {
            count++;
            count += this.data.getMonthBoxOffice().size();
        }
        return count;
    }

    /**
     * 手动计算item
     *
     * @return
     */
    private MovieBoxOffice getItem(int position) {
        if(position==0)
            return null;
        position-=1;
        if(position<this.data.getHourBoxOffice().size())
            return this.data.getHourBoxOffice().get(position);
        position-=this.data.getHourBoxOffice().size();

        if(position==0)
            return null;
        position-=1;
        if(position<this.data.getDayBoxOffice().size())
            return this.data.getDayBoxOffice().get(position);

        position-=this.data.getDayBoxOffice().size();
        if(position==0)
            return null;
        position-=1;
        return this.data.getMonthBoxOffice().get(position);
    }


    @Override
    public int getItemViewType(int position) {
        if (getItem(position) == null) return HEAD_VIEW;
        return ONE_CELL;
    }
}
