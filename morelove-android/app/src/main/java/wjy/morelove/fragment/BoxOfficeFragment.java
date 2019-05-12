package wjy.morelove.fragment;


import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;

import java.util.Map;

import butterknife.BindView;
import wjy.morelove.Jyson.Jyson;
import wjy.morelove.R;
import wjy.morelove.activity.BrowserActivity;
import wjy.morelove.adapter.MovieBoxOfficeAdpter;
import wjy.morelove.bean.BoxOfficeData;
import wjy.morelove.bean.Channel;
import wjy.morelove.handler.RecyclerViewLoadDataHandler;
import wjy.morelove.request.HttpUrl;
import wjy.morelove.request.LoadDataContract;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.widget.LoadDataNullLayout;
import wjy.morelove.widget.WSwipeRefreshLayout;

/**
 * 电影票房
 *
 * @author wjy
 */
public class BoxOfficeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.mRecyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    protected WSwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.errorLayout)
    protected LoadDataNullLayout errorLayout;
    private RecyclerViewLoadDataHandler recyclerViewLoadDataHandler;

    private MovieBoxOfficeAdpter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_boxoffice;
    }

    @Override
    protected void onFragmentInitFinish() {
        this.errorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });
        this.recyclerViewLoadDataHandler = new RecyclerViewLoadDataHandler(errorLayout, swipeRefreshLayout);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        if (this.mAdapter == null) this.onRefresh();
    }

    @Override
    public void onRefresh() {
        this.recyclerViewLoadDataHandler.onRefresh();
        this.loadDataContract.loadData(Request.Method.GET, HttpUrl.MOVIE_BOXOFFICE_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                return null;
            }

            @Override
            public void onSuccess(String data) {
                BoxOfficeData boxOfficeData;
                try {
                    boxOfficeData = (BoxOfficeData) new Jyson().parseJson(data, BoxOfficeData.class);
                } catch (Exception e) {
                    this.onError(new ApiException(e, ApiException.ErrorCode.PARSE_ERROR));
                    return;
                }
                if (boxOfficeData == null || boxOfficeData.getDayBoxOffice() == null || boxOfficeData.getMonthBoxOffice() == null || boxOfficeData.getHourBoxOffice() == null) {
                    recyclerViewLoadDataHandler.onNotData();
                } else {
                    if (mAdapter == null) {
                        mAdapter = new MovieBoxOfficeAdpter(getContext(), boxOfficeData, mRecyclerView);
                        mAdapter.setOnItemClickListerer(item->{
                            String url = "[callToUrl]https://dianying.nuomi.com/movie/movielist";
                            Channel channel = new Channel("百度糯米",url);
                            Intent intent = new Intent(getActivity(), BrowserActivity.class);
                            intent.putExtra("Channel",channel);
                            startActivity(intent);
                        });
                    } else {
                        mAdapter.setData(boxOfficeData);
                    }
                    mRecyclerView.setAdapter(mAdapter);
                    recyclerViewLoadDataHandler.onRefreshSuccess();
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
