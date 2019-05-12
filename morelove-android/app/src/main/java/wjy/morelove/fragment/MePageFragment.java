package wjy.morelove.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import wjy.morelove.App;
import wjy.morelove.R;
import wjy.morelove.activity.CardSubjuctActivity;
import wjy.morelove.activity.LoginActivity;
import wjy.morelove.activity.MemorialDayActivity;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.bean.Lover;
import wjy.morelove.bean.LoverDetails;
import wjy.morelove.request.HttpUrl;
import wjy.morelove.request.LoadDataContract;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.utils.WImageCache;

/**
 * 我的页面
 *
 * @author wjy
 */
public class MePageFragment extends BaseFragment {

    // Volley框架网络请求队列
    private RequestQueue mQueue;
    // 图片加载监听器
    private ImageLoader imageLoader;


    @BindView(R.id.ivLoverImage)
    protected NetworkImageView ivLoverImage;
    @BindView(R.id.ivManUserImg)
    protected NetworkImageView ivManUserImg;
    @BindView(R.id.ivWomenUserImg)
    protected NetworkImageView ivWomenUserImg;
    @BindView(R.id.tvManUsername)
    protected TextView tvManUsername;
    @BindView(R.id.tvWomenUsername)
    protected TextView tvWomenUsername;

    @BindView(R.id.tvSignature)
    protected TextView tvSignature;

    @BindView(R.id.tvMemorialdayCount)
    protected TextView tvMemorialdayCount;
    @BindView(R.id.tvMovieRecordCount)
    protected TextView tvMovieRecordCount;
    @BindView(R.id.tvPunchcardCount)
    protected TextView tvPunchcardCount;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mepage;
    }

    @Override
    protected void onFragmentInitFinish() {
        this.mQueue = Volley.newRequestQueue(getContext());
        this.imageLoader = new ImageLoader(this.mQueue, WImageCache.instance());
        this.getActivity().registerReceiver(loverUpdateBroadcastReceiver, new IntentFilter(App.LOVER_RECORD_UPDATE_SUCCESS_ACTION));
        updateUI();
    }


    /**
     * 更新数据显示
     */
    private void updateUI() {
        LoverDetails loverDetails = App.getApp().getLoverDetails();
        if (loverDetails != null) {
            if (loverDetails.getLover() != null) {
                Lover lover = loverDetails.getLover();
                tvSignature.setText(lover.getDetails());
                tvManUsername.setText(lover.getMan().getUsername());
                tvWomenUsername.setText(lover.getWomen().getUsername());
                // 加载过程显示的图片
                ivManUserImg.setDefaultImageResId(R.mipmap.default_user_img1);
                ivWomenUserImg.setDefaultImageResId(R.mipmap.default_user_img2);
                ivLoverImage.setDefaultImageResId(R.drawable.image_default_lover_bg);
                // 加载失败显示的图片
                ivManUserImg.setErrorImageResId(R.mipmap.default_user_img1);
                ivWomenUserImg.setErrorImageResId(R.mipmap.default_user_img2);
                ivLoverImage.setDefaultImageResId(R.drawable.image_default_lover_bg);
                // 网络图片链接，加载缩略图
                ivManUserImg.setImageUrl(lover.getWomen().getHeadThumb(), imageLoader);
                ivWomenUserImg.setImageUrl(lover.getWomen().getHeadThumb(), imageLoader);
                ivLoverImage.setImageUrl(lover.getLoveImgThumb(), imageLoader);
            }
            tvMemorialdayCount.setText(loverDetails.getMemorialdayCount() + " 条");
            tvMovieRecordCount.setText(loverDetails.getMovieCount() + " 部");
            tvPunchcardCount.setText(loverDetails.getPunchcardCount() + " 主题");
        }

    }


    private BroadcastReceiver loverUpdateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(App.LOVER_RECORD_UPDATE_SUCCESS_ACTION)) {
                updateUI();
            }
        }
    };


    @OnClick(R.id.lyMemorial)
    protected void callToMemorialday() {
        ((BaseActivity) getActivity()).startActivity(MemorialDayActivity.class);
    }

    @OnClick(R.id.lySubjectCard)
    protected void callToSubjectCard() {
        ((BaseActivity) getActivity()).startActivity(CardSubjuctActivity.class);
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.ivLogout)
    protected void ivLogoutClick() {
        ((BaseActivity) getActivity()).showWaitDialog("正在退出...");
        this.loadDataContract.loadData(Request.Method.GET, HttpUrl.USER_LOGOUT_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                return null;
            }

            @Override
            public void onSuccess(String json) {
                ((BaseActivity) getActivity()).dismissWaitDialog();
                ((BaseActivity) getActivity()).startActivity(LoginActivity.class);
                getActivity().finish();
            }

            @Override
            public void onError(ApiException ex) {
                ((BaseActivity) getActivity()).dismissWaitDialog();
                ((BaseActivity) getActivity()).showAlertMessage("出错啦！");
            }
        });
    }

}


