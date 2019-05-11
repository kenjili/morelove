package wjy.weiai.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wjy.weiai.R;
import wjy.weiai.bean.SubjectCard;
import wjy.weiai.widget.GridSpacingItemDecoration;

/**
 * 打卡列表配器
 *
 * @author wjy
 */
public class DaCardAdpter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * item点击事件
     */
    public interface OnItemClickListerer {
        void onItemClick(SubjectCard item);
    }

    private OnItemClickListerer onItemClickListerer = null;

    public void setOnItemClickListerer(
            OnItemClickListerer onItemClickListerer) {
        this.onItemClickListerer = onItemClickListerer;
    }

    // 一行一列
    public final static int ONE_CELL = 1;

    /**
     * 数据源
     */
    private List<SubjectCard> data;
    private Context context;

    /**
     * 构造函数
     *
     * @param context
     */
    public DaCardAdpter(Context context, List<SubjectCard> data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;
        //初始化recyclerView的布局
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(context, 2, 10, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    public void setData(List<SubjectCard> data) {
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
     * 一行一列样式
     *
     * @author wjy
     */
    public class SubjectCardViewHolder extends BaseViewHolder {

        @BindView(R.id.tvSubject)
        protected TextView tvSubject;
        @BindView(R.id.tvNote)
        protected TextView tvNote;

        @BindView(R.id.btnCard)
        protected Button btnCard;

        SubjectCard subjectCard;

        public SubjectCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void showData(Serializable bean) {
            this.subjectCard = (SubjectCard) bean;
            tvSubject.setText(subjectCard.getSubject());
            tvNote.setText(subjectCard.getNote());
            if (subjectCard.getFinish()) {
                btnCard.setText("已完成打卡");
            } else {
                btnCard.setText("打  卡");
            }
        }

        @OnClick(R.id.btnCard)
        protected void onDaCradClick() {
            if (!this.subjectCard.getFinish()) {
                btnCard.setText("已完成打卡");
                btnCard.setClickable(false);
            } else {
                //调整到打卡历史
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ONE_CELL:
            default:
                return new SubjectCardViewHolder(LayoutInflater.from(
                        parent.getContext()).inflate(
                        R.layout.rv_item_cardlist, parent, false));
        }
    }

    /**
     * 绑定item视图数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SubjectCard bean = data.get(position);
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
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        return ONE_CELL;
    }
}
