package wjy.morelove.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import wjy.morelove.Jyson.Jyson;
import wjy.morelove.R;
import wjy.morelove.activity.BrowserActivity;
import wjy.morelove.adapter.ScenicAreaAdpter;
import wjy.morelove.bean.Channel;
import wjy.morelove.bean.ScenicArea;
import wjy.morelove.handler.RecyclerViewLoadDataHandler;
import wjy.morelove.request.HttpUrl;
import wjy.morelove.request.LoadDataContract;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.widget.LoadDataNullLayout;
import wjy.morelove.widget.WSwipeRefreshLayout;

/**
 * 旅游景点
 *
 * @author wjy
 */
public class ScenicAreaFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, WSwipeRefreshLayout.OnLoadListener {

    @BindView(R.id.mRecyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    protected WSwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.errorLayout)
    protected LoadDataNullLayout errorLayout;
    private RecyclerViewLoadDataHandler recyclerViewLoadDataHandler;

    private ScenicAreaAdpter mAdapter;

    private View headView;

    private String keyword;

    private int currentPage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scenicarea;
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
        this.swipeRefreshLayout.setOnLoadListener(this);
        if (this.mAdapter == null) {
            this.mAdapter = new ScenicAreaAdpter(this.getContext(), this.mRecyclerView);
        }
        if (headView == null) {
            headView = LayoutInflater.from(this.getContext()).inflate(R.layout.rv_head_scenicarea_search, null);
            this.mAdapter.setHeadView(headView);
        }
        ((EditText) headView.findViewById(R.id.etKeyword))
                //监听回车
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    /**
                     * imeOptions表示要设置的行为模式，常用的有以下几种：
                     actionUnspecified  未指定，对应常量EditorInfo.IME_ACTION_UNSPECIFIED.
                     actionNone 没有动作,对应常量EditorInfo.IME_ACTION_NONE 
                     actionGo 去往，对应常量EditorInfo.IME_ACTION_GO
                     actionSearch 搜索，对应常量EditorInfo.IME_ACTION_SEARCH    
                     actionSend 发送，对应常量EditorInfo.IME_ACTION_SEND   
                     actionNext 下一个，对应常量EditorInfo.IME_ACTION_NEXT   
                     actionDone 完成，对应常量EditorInfo.IME_ACTION_DONE
                     */
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            //强制隐藏键盘
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                            //刷新数据
                            keyword = textView.getText().toString();
                            onRefresh();
                            return true;
                        }
                        return false;
                    }
                });
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.setOnItemClickListerer(item -> {
            String url="[callToUrl]http://s.tuniu.com/search_complex/whole-nn-0-"+item.getScenicName();
            Channel channel = new Channel("百度糯米",url);
            Intent intent = new Intent(getActivity(), BrowserActivity.class);
            intent.putExtra("Channel",channel);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //减头
        if (this.mAdapter.getItemCount() - 1 == 0) {
            this.onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        this.currentPage = 1;
        this.recyclerViewLoadDataHandler.onRefresh();
        this.loadData();
    }

    @Override
    public void onLoadMore() {
        this.currentPage++;
        this.recyclerViewLoadDataHandler.onLoadMore();
        this.loadData();
    }


    private void loadData() {
        this.loadDataContract.loadData(Request.Method.POST, HttpUrl.SCENICAREA_SERACH_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("page", currentPage + "");
                if (keyword != null && !keyword.equals(""))
                    param.put("keyword", keyword);
                return param;
            }

            @Override
            public void onSuccess(String data) {
                List<ScenicArea> scenicAreaList;
                try {
                    scenicAreaList = (List<ScenicArea>) new Jyson().parseJson(data, ScenicArea.class);
                } catch (Exception e) {
                    this.onError(new ApiException(e, ApiException.ErrorCode.PARSE_ERROR));
                    return;
                }
                if (currentPage == 1) {
                    if (scenicAreaList == null || scenicAreaList.size() == 0) {
                        //没有数据
                        recyclerViewLoadDataHandler.onNotData();
                        return;
                    }
                    mAdapter.setData(scenicAreaList);
                    recyclerViewLoadDataHandler.onRefreshSuccess();
                } else {
                    if (scenicAreaList == null || scenicAreaList.size() == 0) {
                        //没有更多数据
                        recyclerViewLoadDataHandler.onNotMoreData();
                        return;
                    }
                    mAdapter.addData(scenicAreaList);
                    recyclerViewLoadDataHandler.onLoadMoreSuccess();
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
