package wjy.weiai.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import wjy.weiai.bean.Channel;

public class FoundPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<Channel> tabIndicators;

    public FoundPageAdapter(FragmentManager fm, List<Fragment> fragments, List<Channel> tabIndicators) {
        super(fm);
        this.fragments = fragments;
        this.tabIndicators = tabIndicators;
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
