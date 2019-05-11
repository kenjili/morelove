package wjy.weiai.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 首页的viewpage适配器
 * @author wjy
 * ViewPager更新数据问题：直接使用notifyDataSetChanged是无法更新，需要同时重写getItemPosition返回常量 POSITION_NONE
 * 页面缓存问题：使用ViewPager容器，需要左右滑动显示多个页面，默认只能缓存临近操作两个页面
 * 需要保存选项卡所有页面，通过属性设置ViewPager.setOffscreenPageLimit( 缓存页数值 ); 来实现。
 */
public class HomePageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private final FragmentManager fm;

    public HomePageAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
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

    public void setFragments(Fragment... fragments) {
        if (this.fragments != null) {
            //从viewpage中移除现有的fragment
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            fm.executePendingTransactions();
        }
        for (Fragment fragment : fragments) {
            this.fragments.add(fragment);
        }
        notifyDataSetChanged();
    }


    /**
     * 实例化item
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (fragments.size() <= position) {
            //确保下标不会超过fragments的存储的fragment数量
            position = position % fragments.size();
        }
        Object obj = super.instantiateItem(container, position);
        return obj;
    }

}
