package wjy.morelove.handler;

import wjy.morelove.request.exception.ApiException;

/**
 * 上拉加载下拉刷新处理器抽象定义
 * @author wjy
 */
public interface RefreshLoadHandler {

    /**
     * 开始刷新
     */
    void onRefresh();

    /**
     * 开始加载更多
     */
    void onLoadMore();

    /**
     * 没有数据-刷新时
     */
    void onNotData();

    /**
     * 没有更多数据-加载更多时
     */
    void onNotMoreData();

    /**
     * 刷新成功--有数据的情况
     */
    void onRefreshSuccess();

    /**
     * 加载成功--有数据的情况
     */
    void onLoadMoreSuccess();

    /**
     * 处理异常
     * @param exception
     */
    void handleException(ApiException exception);

}
