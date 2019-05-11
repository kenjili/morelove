package wjy.weiai.adapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.weiai.AppConfig;
import wjy.weiai.R;
import wjy.weiai.bean.Lovetime;
import wjy.weiai.utils.DateTimeUtils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


/**
 * 时光的适配器
 * @author wjy
 */
public class LoveTimeAdpter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * item点击事件
     */
    public interface OnItemClickListerer {
        void onItemClick(Lovetime lovetime);
    }

    private OnItemClickListerer onItemClickListerer;

    public void setOnItemClickListerer(OnItemClickListerer onItemClickListerer) {
        this.onItemClickListerer = onItemClickListerer;
    }

    /**
     * 时光item
     *
     * @author wjy
     */
    public class LovetimeViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ivUser)
        protected ImageView ivUser;
        @BindView(R.id.tvUsername)
        protected TextView tvUsername;

        @BindView(R.id.ivAlbum1)
        protected ImageView ivAlbum1;
        @BindView(R.id.ivAlbum2)
        protected ImageView ivAlbum2;
        @BindView(R.id.ivAlbum3)
        protected ImageView ivAlbum3;

        //内容
        @BindView(R.id.tvDetails)
        protected TextView tvDetails;

        //查看次数
        @BindView(R.id.tvSeeCount)
        protected TextView tvSeeCount;
        //查看次数的父容器
        @BindView(R.id.rySee)
        protected View rySeePanle;

        //日期
        @BindView(R.id.tvDate)
        protected TextView tvDate;

        private View rootView;


        public LovetimeViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            ButterKnife.bind(this, this.rootView);
        }

        public void showData(Lovetime bean) {
            this.rootView.setOnClickListener((view -> {
                if (onItemClickListerer != null) {
                    onItemClickListerer.onItemClick(bean);
                }
            }));

            if(bean.getUser()!=null){
                tvUsername.setText(bean.getUser().getUsername());
                Glide.with(rootView)
                        .load(bean.getUser().getHeadThumb())
                        .apply(AppConfig.getRequestOptionsCircleCrop())
                        .into(ivUser);
            }

            tvDetails.setText(bean.getDetails());
            tvDate.setText(DateTimeUtils.date2StringYMD(bean.getCreateDatetime()));
            tvSeeCount.setText(""+bean.getAccessCount());

            int imgCount = 0;
            if (bean.getImgThumb1() != null) {
                Glide.with(rootView)
                        .load(bean.getImgThumb1())
                        .apply(AppConfig.getRequestOptions())
                        .into(ivAlbum1);
                imgCount++;
            }

            if (bean.getImgThumb2() != null) {
                Glide.with(rootView)
                        .load(bean.getImgThumb2())
                        .apply(AppConfig.getRequestOptions())
                        .into(ivAlbum2);
                imgCount++;
            }

            if (bean.getImgThumb3() != null) {
                Glide.with(rootView)
                        .load(bean.getImgThumb3())
                        .apply(AppConfig.getRequestOptions())
                        .into(ivAlbum3);
                imgCount++;
            }

            switch (imgCount) {
                case 0:
                    ivAlbum1.setVisibility(View.GONE);
                    ivAlbum2.setVisibility(View.GONE);
                    ivAlbum3.setVisibility(View.GONE);
                    break;
                case 1:
                    ivAlbum1.setMinimumHeight(ongImageViewHeight);
                    ivAlbum1.setVisibility(View.VISIBLE);
                    ivAlbum2.setVisibility(View.GONE);
                    ivAlbum3.setVisibility(View.GONE);
                    break;
                case 2:
                    ivAlbum1.setMinimumHeight(twoImageViewHeight);
                    ivAlbum2.setMinimumHeight(twoImageViewHeight);
                    ivAlbum1.setVisibility(View.VISIBLE);
                    ivAlbum2.setVisibility(View.VISIBLE);
                    ivAlbum3.setVisibility(View.GONE);
                    break;
                case 3:
                    ivAlbum1.setMinimumHeight(threeImageViewHeight);
                    ivAlbum2.setMinimumHeight(threeImageViewHeight);
                    ivAlbum3.setMinimumHeight(threeImageViewHeight);
                    ivAlbum1.setVisibility(View.VISIBLE);
                    ivAlbum2.setVisibility(View.VISIBLE);
                    ivAlbum3.setVisibility(View.VISIBLE);
                    break;

            }

            //是否是公开的
            if (isPublic) {
                rySeePanle.setVisibility(View.VISIBLE);
            } else {
                rySeePanle.setVisibility(View.GONE);
            }
        }
    }


    private List<Lovetime> data;
    //是否公开的，公开的是晒一晒
    private boolean isPublic;


    private int ongImageViewHeight, twoImageViewHeight, threeImageViewHeight;

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public LoveTimeAdpter(Context context, OnItemClickListerer onItemClickListerer, List<Lovetime> data) {
        this.data = data;
        this.onItemClickListerer = onItemClickListerer;
        this.ongImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.ry_item_lovetime_one_img_height);
        this.twoImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.ry_item_lovetime_two_img_height);
        this.threeImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.ry_item_lovetime_three_img_height);
    }

    public LoveTimeAdpter(Context context, OnItemClickListerer onItemClickListerer) {
        this(context, onItemClickListerer, new ArrayList<>());
    }

    public void setData(List<Lovetime> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }


    public void addData(List<Lovetime> data) {
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    public void clearAllData() {
        this.data.clear();
        this.notifyDataSetChanged();
    }

    /**
     * 获取item的大小
     */
    @Override
    public int getItemCount() {
        return this.data.size();
    }


    /**
     * 创建item视图
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup vgroup,
                                                      int postion) {
        return new LovetimeViewHolder(LayoutInflater.from(
                vgroup.getContext()).inflate(
                R.layout.rv_item_lovetime, null));
    }

    /**
     * 为item绑定数据
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Lovetime bean = this.data.get(position);
        if (holder instanceof LovetimeViewHolder)
            if (bean != null)
                ((LovetimeViewHolder) holder).showData(bean);
    }


}
