package wjy.weiai.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wjy.weiai.R;
import wjy.weiai.adapter.FoundPageAdapter;
import wjy.weiai.bean.Channel;
import wjy.weiai.widget.expandablebuttonmenu.ScreenHelper;

public class FoundPageFragment extends BaseFragment {

    @BindView(R.id.findTablayout)
    protected TabLayout findTablayout;
    @BindView(R.id.findViewPage)
    protected ViewPager findViewPage;

    private FoundPageAdapter pageAdapter;

    private List<Fragment> fragments;
    private List<Channel> tabIndicators;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_findpage;
    }

    @Override
    protected void onFragmentInitFinish() {
        initPageAdapter();
    }


    private void initPageAdapter() {
        //fragment标题
        this.tabIndicators = new ArrayList<>();
        this.tabIndicators.add(new Channel(getText(R.string.find_title_video).toString(),""));
        this.tabIndicators.add(new Channel(getText(R.string.find_title_jindian).toString(),""));
        //fragment
        this.fragments = new ArrayList<>();
        this.fragments.add(new BoxOfficeFragment());
        this.fragments.add(new ScenicAreaFragment());
        //适配器
        this.pageAdapter = new FoundPageAdapter(getChildFragmentManager(),fragments,tabIndicators);
        this.findViewPage.setAdapter(this.pageAdapter);
        //tablayout与viewpage联动
        this.findTablayout.setupWithViewPager(this.findViewPage);
        this.reflex(this.findTablayout);
        this.findViewPage.setCurrentItem(0);
    }

    /**
     * 设置TabLyout的下滑线宽度
     *
     * @param tabLayout
     */
    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性
                        // tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //因为我想要的效果是
                        // 字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        int dp10 = (int) ScreenHelper.dpToPx(tabLayout.getContext(), 10);
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
