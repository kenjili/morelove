package wjy.weiai.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wjy.weiai.bean.Channel;
import wjy.weiai.fragment.LoveTimeFragment;


/**
 * 首页的viewpage适配器
 * @author wjy
 */
public class NewsPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<Channel> tabIndicators;

    public NewsPageAdapter(FragmentManager fm, List<Channel> tabIndicators) {
        super(fm);
        this.tabIndicators = tabIndicators;
        this.fragments = new ArrayList<>();
        for (Channel channel : tabIndicators) {
            Fragment fragment=new LoveTimeFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Channel",channel);
            //Fragment在横竖屏切换等情况下系统会通过反射重新实例话这个对象，这种情况下构造传参数据就会丢失了
            fragment.setArguments(bundle);
            this.fragments.add(fragment);
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (fragments.size() <= position) {
            //确保下标不会超过fragments的存储的fragment数量
            position = position % fragments.size();
        }
        Object obj = super.instantiateItem(container, position);
        return obj;
    }


    /**
     * 获取fragment的标题，tablayout会调用
     *
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabIndicators.get(position).getTitle();
    }
}
