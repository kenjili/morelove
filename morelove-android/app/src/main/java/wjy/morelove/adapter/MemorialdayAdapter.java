package wjy.morelove.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wjy.morelove.AppConfig;
import wjy.morelove.R;
import wjy.morelove.bean.MemorialDay;

public class MemorialdayAdapter extends PagerAdapter {

    public interface OnDeleteClickListener{
        void onDeleteClick(MemorialDay memorialDay);
    }
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    private List<MemorialDay> data;
    private Context context;
    private LayoutInflater mInflater;
    private RequestOptions requestOptions;

    private int[] priorityResource = new int[]{
            R.drawable.memorial_priority_01,
            R.drawable.memorial_priority_02,
            R.drawable.memorial_priority_03,
            R.drawable.memorial_priority_04,
            R.drawable.memorial_priority_05,
            R.drawable.memorial_priority_06,
            R.drawable.memorial_priority_07
    };

    public MemorialdayAdapter(Context context, List<MemorialDay> album) {
        this.context = context;
        this.data = album;
        this.mInflater = LayoutInflater.from(context);

        requestOptions = AppConfig.getRequestOptions();
        requestOptions.error(R.drawable.img_default_memorailday)
                .placeholder(R.drawable.img_loading);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    class ItemView {

        @BindView(R.id.ivBg)
        protected ImageView ivBg;
        @BindView(R.id.tvMemorialdayName)
        protected TextView tvMemorialdayName;
        @BindView(R.id.tvMemorialdayDate)
        protected TextView tvMemorialdayDate;
        @BindView(R.id.vPriority)
        protected View vPriority;

        private MemorialDay memorialDay;


        public ItemView(View rootView) {
            ButterKnife.bind(this, rootView);
        }

        public void showImg(MemorialDay memorialDay) {
            this.memorialDay = memorialDay;
            tvMemorialdayName.setText(memorialDay.getMemorialName());
            tvMemorialdayDate.setText(sdf.format(memorialDay.getMemorialDate()));
            vPriority.setBackgroundResource(memorialDay.getPriority()==0?0
                    :priorityResource[(memorialDay.getPriority()-1)%priorityResource.length]);

            //加载原图
            Glide.with(context)
                    .load(memorialDay.getImage())
                    .apply(requestOptions)
                    .into(this.ivBg);
        }

        @OnClick(R.id.btnDelete)
        protected void btnDeleteClick(){
            if(onDeleteClickListener!=null)
                onDeleteClickListener.onDeleteClick(memorialDay);
        }
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = mInflater.inflate(R.layout.view_memorialday_details, null);
        ItemView itemView = new ItemView(rootView);
        itemView.showImg(this.data.get(position));
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     * 设置每个item的宽度
     *
     * @param position
     * @return
     */
    @Override
    public float getPageWidth(int position) {
        return 0.8f;
    }

}
