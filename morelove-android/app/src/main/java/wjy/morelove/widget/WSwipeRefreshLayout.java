package wjy.morelove.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;

import wjy.morelove.R;

/**
 * 继承SwipeRefreshLayout，实现一些额外的功能
 *
 * @author wjy
 */
public class WSwipeRefreshLayout extends SwipeRefreshLayout {

    private boolean mIsLoading;//是否正在加载中
    private boolean isInit = true;

    /**
     * 滑动到最后一项加载更多监听器
     */
    public interface OnLoadListener {
        void onLoadMore();
    }

    private OnLoadListener mOnLoadListener;

    public void setOnLoadListener(OnLoadListener listener) {
        mOnLoadListener = listener;
    }

    public WSwipeRefreshLayout(@NonNull Context context) {
        super(context);
        setColorSchemeColors(getResources().getColor(R.color.app_theme_color));
    }

    public WSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeColors(getResources().getColor(R.color.app_theme_color));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isInit) {
            setChildViewOnScroll();
            isInit = false;
        }
    }

    /**
     * 为子控件写入滚动监听事件
     */
    private void setChildViewOnScroll() {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //只要找到ListView、RecyclerView、ScrollView其中的一个就停止
            if (childView instanceof ListView) {
                ListView mListView = (ListView) childView;
                mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (canLoadMore())
                            loadMore();
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    }
                });
                break;
            } else if (childView instanceof RecyclerView) {
                RecyclerView mRecyclerView = (RecyclerView) childView;
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (canLoadMore())
                            loadMore();
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });
                break;
            } else if (childView instanceof ScrollView) {
                ScrollView mScrollView = (ScrollView) childView;
                mScrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        if (canLoadMore())
                            loadMore();
                    }
                });
                break;
            }
        }
    }

    /**
     * 判断是否能加载更多
     *
     * @return
     */
    private boolean canLoadMore() {
        boolean isLastItem = false;//是否滚动到了最后一项
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //只要找到ListView、RecyclerView、ScrollView其中的一个就停止
            if (childView instanceof ListView) {
                ListView mListView = (ListView) childView;
                if (mListView != null && mListView.getAdapter() != null)
                    isLastItem = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
                break;
            } else if (childView instanceof RecyclerView) {
                RecyclerView mRecyclerView = (RecyclerView) childView;
                if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
                    RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                    if (manager != null) {
                        if (manager instanceof StaggeredGridLayoutManager) {
                            int[] lastVisiblePositions = ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(new int[((StaggeredGridLayoutManager) manager).getSpanCount()]);
                            int lastVisiblePos = getMaxElem(lastVisiblePositions);
                            // 判断是否滚动到底部
                            isLastItem = (lastVisiblePos == (mRecyclerView.getAdapter().getItemCount() - 1));
                        } else if (manager instanceof LinearLayoutManager) {
                            isLastItem = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition() == manager.getItemCount() - 1;
                        } else if (manager instanceof GridLayoutManager) {
                            isLastItem = ((GridLayoutManager) manager).findLastCompletelyVisibleItemPosition() == manager.getItemCount() - 1;
                        }
                    }
                }
                break;
            } else if (childView instanceof ScrollView) {
                ScrollView mScrollView = (ScrollView) childView;
                if (mScrollView.getChildCount() != 1) return false;
                //当前滚动到的y坐标+ScrollView的高度>=ScrollView的子控制的高度
                if (mScrollView.getScrollY() + mScrollView.getMeasuredHeight()
                        >= mScrollView.getChildAt(0).getMeasuredHeight()) {
                    //底部
                    isLastItem = true;
                }
                break;
            }
        }
        //当前不处于正在刷新或正在加载状态且滑动到了最后一项
        return isLastItem && !mIsLoading && !isRefreshing();
    }

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }

    /**
     * 当数据加载完成时由外部调用设置上拉到最后一项可加载更多状态
     */
    public void setLoadingfinish() {
        mIsLoading = false;
    }


    /**
     * 加载更多
     */
    private void loadMore() {
        if (mOnLoadListener != null) {
            mOnLoadListener.onLoadMore();
        }
    }


}
