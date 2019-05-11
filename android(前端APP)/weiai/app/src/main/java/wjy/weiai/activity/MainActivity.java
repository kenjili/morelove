package wjy.weiai.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import wjy.weiai.R;
import wjy.weiai.adapter.HomePageAdapter;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.fragment.FoundPageFragment;
import wjy.weiai.fragment.HomePageFragment;
import wjy.weiai.fragment.MePageFragment;
import wjy.weiai.widget.expandablebuttonmenu.ExpandableMenuOverlay;

/**
 * 主页
 *
 * @author wjy
 */
public class MainActivity extends BaseActivity {

    @BindViews({R.id.lyHome, R.id.lyFind, R.id.lyMe})
    protected List<LinearLayout> toolBar;
    private int[] toolBarMenuSelectRid = new int[]{R.mipmap.icon_toolbar_home, R.mipmap.icon_toolbar_find, R.mipmap.icon_toolbar_love};
    private int[] toolBarMenuNotRid = new int[]{R.mipmap.icon_toolbar_home_n, R.mipmap.icon_toolbar_find_n, R.mipmap.icon_toolbar_love_n};
    private int[] colorRid = new int[]{R.color.app_theme_color, R.color.toolbar_font_color};
    private int currentPageIndex = 0;


    @BindView(R.id.btnPublic)
    protected ExpandableMenuOverlay btnPublic;

    @BindView(R.id.mainViewPage)
    protected ViewPager mainViewPage;
    private HomePageAdapter pageAdapter;

    @Override
    protected void onInit() {
        this.pageAdapter = new HomePageAdapter(getSupportFragmentManager());
        Fragment home = new HomePageFragment();
        Fragment find = new FoundPageFragment();
        Fragment me = new MePageFragment();
        this.pageAdapter.setFragments(home, find, me);
        this.mainViewPage.setAdapter(this.pageAdapter);
        initToolbarMenu();
        this.mainViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeToolbarSelectMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        this.mainViewPage.setCurrentItem(0);
    }

    @Override
    protected int getLayoutId() {
        //mainactivity状态栏的处理
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        this.getWindow().setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
        return R.layout.activity_main;
    }


    /**
     * 改变按钮状态
     */
    private void changeToolbarSelectMenu() {
        int menu = this.mainViewPage.getCurrentItem() % toolBar.size();
        if (menu == currentPageIndex) return;

        LinearLayout linearLayout = toolBar.get(currentPageIndex);
        ImageView imageView = (ImageView) linearLayout.getChildAt(0);
        imageView.setImageResource(toolBarMenuNotRid[currentPageIndex]);
        TextView textView = (TextView) linearLayout.getChildAt(1);
        textView.setTextColor(getResources().getColor(colorRid[1]));

        currentPageIndex = menu;
        linearLayout = toolBar.get(currentPageIndex);
        imageView = (ImageView) linearLayout.getChildAt(0);
        imageView.setImageResource(toolBarMenuSelectRid[currentPageIndex]);
        textView = (TextView) linearLayout.getChildAt(1);
        textView.setTextColor(getResources().getColor(colorRid[0]));
    }


    /**
     * 为toolbar的按钮设置点击事件
     */
    private void initToolbarMenu() {
        for (View view : this.toolBar) {
            view.setOnClickListener((v) -> {
                //改变viewpage
                for (int index = 0; index < toolBar.size(); index++) {
                    if (toolBar.get(index).getId() == v.getId()) {
                        mainViewPage.setCurrentItem(index);
                        break;
                    }
                }
                //再改变菜单选项
                changeToolbarSelectMenu();
            });
        }
    }

}