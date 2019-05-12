package wjy.morelove.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import wjy.morelove.Jyson.Jyson;
import wjy.morelove.R;
import wjy.morelove.activity.ItineraryDeatilsActivity;
import wjy.morelove.activity.LovetimeDetailsActivity;
import wjy.morelove.adapter.ItineraryAdpter;
import wjy.morelove.adapter.LoveTimeAdpter;
import wjy.morelove.bean.Channel;
import wjy.morelove.bean.Itinerary;
import wjy.morelove.bean.Lovetime;
import wjy.morelove.handler.RecyclerViewLoadDataHandler;
import wjy.morelove.request.HttpUrl;
import wjy.morelove.request.LoadDataContract;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.widget.GridSpacingItemDecoration;
import wjy.morelove.widget.LoadDataNullLayout;
import wjy.morelove.widget.WSwipeRefreshLayout;

/**
 * 时光动态页面
 */
public class LoveTimeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, WSwipeRefreshLayout.OnLoadListener {

    @BindView(R.id.refreshLayout)
    protected WSwipeRefreshLayout refreshLayout;
    @BindView(R.id.errorLayout)
    protected LoadDataNullLayout errorLayout;
    private RecyclerViewLoadDataHandler recyclerViewLoadDataHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_lovetime;
    }


    @BindView(R.id.mRecyclerView)
    protected RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;


    private Channel channel;
    private int currentPage;//当前页码
    private final int onePageSize = 10;//每页数据大小

    @Override
    protected void onFragmentInitFinish() {

        this.errorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });
        this.recyclerViewLoadDataHandler = new RecyclerViewLoadDataHandler(errorLayout, refreshLayout);

        Channel channel = (Channel) getArguments().getSerializable("Channel");
        if (channel == null || channel.getTag() == null)
            throw new NullPointerException("页面参数不能为空");
        this.channel = channel;

        //上拉加载下拉刷新监听
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        if (this.mAdapter == null) {
            initAdapter();
            //加载一次数据
            this.onRefresh();
        }
    }

    private void initAdapter() {
        if (channel.getTag().equals("[sg]")) {
            this.mAdapter = new LoveTimeAdpter(this.getContext(), item -> {
                //点击事件
                Intent intent = new Intent(getActivity(), LovetimeDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Lovetime", item);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            //item装饰，就是用于设置边距之类的
            this.mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(this.getContext(), 1, 10, true));
            this.mRecyclerView.setLayoutManager(new GridLayoutManager(
                    getActivity(), 1, GridLayoutManager.VERTICAL, false));
        } else if (channel.getTag().equals("[lx]")) {
            this.mAdapter = new ItineraryAdpter(this.getContext());
            ((ItineraryAdpter) this.mAdapter).setOnItemClickListerer((Itinerary item) -> {
                //点击事件
                Intent intent = new Intent(getActivity(), ItineraryDeatilsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Itinerary", item);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            this.mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(this.getContext(), 2, 10, true));
            this.mRecyclerView.setLayoutManager(new GridLayoutManager(
                    getActivity(), 2, GridLayoutManager.VERTICAL, false));

//            //瀑布流
//            SpacesItemDecoration decoration=new SpacesItemDecoration((int) ScreenHelper.dpToPx(this.getActivity(),10));
//            mRecyclerView.addItemDecoration(decoration);
//            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        } else if (channel.getTag().equals("[kyk]")) {
            this.mAdapter = new LoveTimeAdpter(this.getContext(), item -> {
                //点击事件
                Intent intent = new Intent(getActivity(), LovetimeDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Lovetime", item);
                intent.putExtras(bundle);
                startActivity(intent);
            });
            //item装饰，就是用于设置边距之类的
            this.mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(this.getContext(), 1, 10, true));
            ((LoveTimeAdpter) this.mAdapter).setPublic(true);
            this.mRecyclerView.setLayoutManager(new GridLayoutManager(
                    getActivity(), 1, GridLayoutManager.VERTICAL, false));
        }

        this.mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 刷新
     */
    @Override
    public void onRefresh() {
        this.recyclerViewLoadDataHandler.onRefresh();
        currentPage = 1;
        loadData();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        this.recyclerViewLoadDataHandler.onLoadMore();
        currentPage++;
        loadData();
    }


    private void loadData() {
        if (this.channel.getTag().equals("[sg]")) {
            String url = HttpUrl.LOVETIME_PRIVATE_LIST.replace("{pageSize}", onePageSize + "")
                    .replace("{page}", currentPage + "");
            loadDataContract.loadData(Request.Method.GET,
                    url,
                    new LoadDataContract.ViewDataContract() {
                        @Override
                        public Map<String, String> getParams() {
                            return null;
                        }

                        @Override
                        public void onSuccess(String json) {
                            List<Lovetime> lovetimeList = null;
                            try {
                                lovetimeList = (List<Lovetime>) new Jyson().parseJson(json, Lovetime.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.onError(new ApiException(e, ApiException.ErrorCode.PARSE_ERROR));
                            }
                            if (currentPage == 1) {
                                if (lovetimeList == null || lovetimeList.size() == 0) {
                                    //没有数据
                                    recyclerViewLoadDataHandler.onNotData();
                                    return;
                                }
                                recyclerViewLoadDataHandler.onRefreshSuccess();
                                ((LoveTimeAdpter) mAdapter).setData(lovetimeList);
                            } else {
                                if (lovetimeList == null || lovetimeList.size() == 0) {
                                    //没有更多数据
                                    recyclerViewLoadDataHandler.onNotMoreData();
                                    return;
                                }
                                recyclerViewLoadDataHandler.onLoadMoreSuccess();
                                ((LoveTimeAdpter) mAdapter).addData(lovetimeList);
                            }
                        }

                        @Override
                        public void onError(ApiException ex) {
                            //有一部分异常已经在请求统一异常处理ExceptionHandler处理
                            recyclerViewLoadDataHandler.handleException(ex);
                        }
                    });
        } else if (this.channel.getTag().equals("[lx]")) {
            String url = HttpUrl.ITINERARY_LIST_URL.replace("{pageSize}", onePageSize + "")
                    .replace("{page}", currentPage + "");
            loadDataContract.loadData(Request.Method.GET,
                    url,
                    new LoadDataContract.ViewDataContract() {
                        @Override
                        public Map<String, String> getParams() {
                            return null;
                        }

                        @Override
                        public void onSuccess(String json) {
                            List<Itinerary> itineraryList = null;
                            try {
                                itineraryList = (List<Itinerary>) new Jyson().parseJson(json, Itinerary.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.onError(new ApiException(e, ApiException.ErrorCode.PARSE_ERROR));
                            }
                            if (currentPage == 1) {
                                if (itineraryList == null || itineraryList.size() == 0) {
                                    //没有数据
                                    recyclerViewLoadDataHandler.onNotData();
                                    return;
                                }
                                recyclerViewLoadDataHandler.onRefreshSuccess();
                                ((ItineraryAdpter) mAdapter).setData(itineraryList);
                            } else {
                                if (itineraryList == null || itineraryList.size() == 0) {
                                    //没有更多数据
                                    recyclerViewLoadDataHandler.onNotMoreData();
                                    return;
                                }
                                recyclerViewLoadDataHandler.onLoadMoreSuccess();
                                ((ItineraryAdpter) mAdapter).addData(itineraryList);
                            }
                        }

                        @Override
                        public void onError(ApiException ex) {
                            //有一部分异常已经在请求统一异常处理ExceptionHandler处理
                            recyclerViewLoadDataHandler.handleException(ex);
                        }
                    });
        } else if (this.channel.getTag().equals("[kyk]")) {
            String url = HttpUrl.LOVETIME_PUBLIC_LIST.replace("{pageSize}", onePageSize + "")
                    .replace("{page}", currentPage + "");
            loadDataContract.loadData(Request.Method.GET,
                    url,
                    new LoadDataContract.ViewDataContract() {
                        @Override
                        public Map<String, String> getParams() {
                            return null;
                        }

                        @Override
                        public void onSuccess(String json) {
                            List<Lovetime> lovetimeList = null;
                            try {
                                lovetimeList = (List<Lovetime>) new Jyson().parseJson(json, Lovetime.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.onError(new ApiException(e, ApiException.ErrorCode.PARSE_ERROR));
                            }
                            if (currentPage == 1) {
                                if (lovetimeList == null || lovetimeList.size() == 0) {
                                    //没有数据
                                    recyclerViewLoadDataHandler.onNotData();
                                    return;
                                }
                                recyclerViewLoadDataHandler.onRefreshSuccess();
                                ((LoveTimeAdpter) mAdapter).setData(lovetimeList);
                            } else {
                                if (lovetimeList == null || lovetimeList.size() == 0) {
                                    //没有更多数据
                                    recyclerViewLoadDataHandler.onNotMoreData();
                                    return;
                                }
                                recyclerViewLoadDataHandler.onLoadMoreSuccess();
                                ((LoveTimeAdpter) mAdapter).addData(lovetimeList);
                            }
                        }

                        @Override
                        public void onError(ApiException ex) {
                            //有一部分异常已经在请求统一异常处理ExceptionHandler处理
                            recyclerViewLoadDataHandler.handleException(ex);
                        }
                    });
        }
    }
}
