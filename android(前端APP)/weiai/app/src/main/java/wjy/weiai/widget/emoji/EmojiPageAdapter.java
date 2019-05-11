package wjy.weiai.widget.emoji;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 首页的viewpage适配器
 * @author wjy
 */
public class EmojiPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public EmojiPageAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
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
}
