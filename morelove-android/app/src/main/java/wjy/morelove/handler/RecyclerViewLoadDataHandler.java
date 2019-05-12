package wjy.morelove.handler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import wjy.morelove.request.exception.ApiException;
import wjy.morelove.widget.LoadDataNullLayout;
import wjy.morelove.widget.WSwipeRefreshLayout;

/**
 * 适用于 RecyclerView的上拉加载下拉刷新处理器实现
 *
 * @author wjy
 */
public class RecyclerViewLoadDataHandler implements RefreshLoadHandler {


    private WeakReference<LoadDataNullLayout> loadDataNullLayout;
    private WeakReference<WSwipeRefreshLayout> swipeRefreshLayout;

    private boolean isRefresh = false;
    private boolean isLoadMore = false;

    public RecyclerViewLoadDataHandler(LoadDataNullLayout loadDataNullLayout, WSwipeRefreshLayout refreshLayout) {
        this.loadDataNullLayout = new WeakReference<>(loadDataNullLayout);
        this.swipeRefreshLayout = new WeakReference<>(refreshLayout);
    }

    @Override
    public void handleException(ApiException exception) {
        if (isRefresh) {
            swipeRefreshLayout.get().setRefreshing(false);
            loadDataNullLayout.get().setErrorType(LoadDataNullLayout.HIDE_LAYOUT);
        }
        if (loadDataNullLayout.get() != null && swipeRefreshLayout.get() != null) {
            switch (exception.code) {
                case ApiException.ErrorCode.NETWORD_ERROR:
                    //网络异常
                    if (isRefresh)
                        loadDataNullLayout.get().setErrorType(LoadDataNullLayout.NETWORK_ERROR);
                    else if (isLoadMore)
                        Toast.makeText(swipeRefreshLayout.get().getContext(), "网络异常!", Toast.LENGTH_SHORT).show();
                    break;
                case ApiException.ErrorCode.HTTP_ERROR:
                    //http请求出错如404
                    if (isRefresh)
                        loadDataNullLayout.get().setErrorType(LoadDataNullLayout.HTTP_REQUEST_ERROR);
                    else if (isLoadMore)
                        Toast.makeText(swipeRefreshLayout.get().getContext(), "请求出错!", Toast.LENGTH_SHORT).show();
                    break;
                case ApiException.ErrorCode.PARSE_ERROR:
                    //json数据解析异常
                    if (isRefresh)
                        loadDataNullLayout.get().setErrorType(LoadDataNullLayout.JSON_PARSE_ERROR);
                    else if (isLoadMore)
                        Toast.makeText(swipeRefreshLayout.get().getContext(), "数据解析异常!", Toast.LENGTH_SHORT).show();
                    break;
                case ApiException.ErrorCode.HTTP_RESULT_ERROR:
                    //服务处理请求错误，如errorCode = 1未登录
                    Toast.makeText(swipeRefreshLayout.get().getContext(), exception.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        isRefresh = false;
        isLoadMore = false;
    }

    @Override
    public void onRefresh() {
        this.isRefresh = true;
        View chileView = swipeRefreshLayout.get().getChildAt(0);
        if (chileView instanceof RecyclerView) {
            //如果当前适配器并没有任何数据,可能含有头部和底部<=2
            if (((RecyclerView) chileView).getAdapter() == null || ((RecyclerView) chileView).getAdapter().getItemCount() <= 2) {
                swipeRefreshLayout.get().setRefreshing(false);
                loadDataNullLayout.get().setErrorType(LoadDataNullLayout.NETWORK_LOADING);
            } else {
                loadDataNullLayout.get().setErrorType(LoadDataNullLayout.HIDE_LAYOUT);
            }
        }
    }

    @Override
    public void onLoadMore() {
        this.isLoadMore = true;
    }

    @Override
    public void onNotData() {
        this.isRefresh = false;
        loadDataNullLayout.get().setErrorType(LoadDataNullLayout.NODATA);
    }

    @Override
    public void onNotMoreData() {
        this.isLoadMore = false;
        //没有更多数据
        Toast.makeText(swipeRefreshLayout.get().getContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.get().setLoadingfinish();
    }

    @Override
    public void onRefreshSuccess() {
        this.isRefresh = false;
        swipeRefreshLayout.get().setRefreshing(false);
        loadDataNullLayout.get().setErrorType(LoadDataNullLayout.HIDE_LAYOUT);
    }

    @Override
    public void onLoadMoreSuccess() {
        this.isLoadMore = false;
        swipeRefreshLayout.get().setLoadingfinish();
    }
}
