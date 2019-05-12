package wjy.morelove.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import wjy.morelove.Jyson.Jyson;
import wjy.morelove.R;
import wjy.morelove.adapter.DaCardAdpter;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.bean.SubjectCard;
import wjy.morelove.bean.SubjectCardList;
import wjy.morelove.handler.RecyclerViewLoadDataHandler;
import wjy.morelove.request.HttpUrl;
import wjy.morelove.request.LoadDataContract;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.widget.LoadDataNullLayout;
import wjy.morelove.widget.WSwipeRefreshLayout;


/**
 * 打卡列表页面
 */
public class CardSubjuctActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, WSwipeRefreshLayout.OnLoadListener {

    @BindView(R.id.navTitle)
    protected TextView navTitle;
    @BindView(R.id.tvAdd)
    protected View tvAdd;

    @BindView(R.id.mRecyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    protected WSwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.errorLayout)
    protected LoadDataNullLayout errorLayout;
    private RecyclerViewLoadDataHandler recyclerViewLoadDataHandler;

    private int currentPage;
    private DaCardAdpter mAdapter;

    private LoadDataContract loadDataContract;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cardsubjuct;
    }

    @Override
    protected void onInit() {
        navTitle.setText(R.string.activity_title_memorialday);
        tvAdd.setVisibility(View.VISIBLE);
        navTitle.setText("打卡主题");
        ((TextView) tvAdd).setText("创建");
        this.loadDataContract = new LoadDataContract(this);
        updateUI();
        loadData();
    }

    private void updateUI() {
        this.errorLayout.setOnLayoutClickListener(view -> onRefresh());
        this.recyclerViewLoadDataHandler = new RecyclerViewLoadDataHandler(errorLayout, swipeRefreshLayout);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.setOnLoadListener(this);
        if (this.mAdapter == null) {
            this.mAdapter = new DaCardAdpter(this, new ArrayList<>(), this.mRecyclerView);
        }
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.setOnItemClickListerer(item -> {
            if (item.isFinish()) {
                //已经完成打卡，调整到打卡历史

                return;
            }
            //未完成打卡，先打卡
            this.loadDataContract.loadData(Request.Method.POST, HttpUrl.CLOCK_IN_URL, new LoadDataContract.ViewDataContract() {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> param = new HashMap<>();
                    param.put("subject", item.getSubject());
                    return param;
                }

                @Override
                public void onSuccess(String json) {
                    item.setFinish(true);
                    mAdapter.notifyDataSetChanged();
                    showAlertMessage(item.getSubject(), "打卡成功！");
                }

                @Override
                public void onError(ApiException ex) {
                    showThreadToast(ex.message);
                }
            });
        });
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
        List<SubjectCard> data = new ArrayList<>();
        this.loadDataContract.loadData(Request.Method.GET, HttpUrl.CAR_SUBJECT_LIST_URL.replace("{pageSize}", 20 + "")
                .replace("{page}", this.currentPage + ""), new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                return null;
            }

            @Override
            public void onSuccess(String json) {
                try {
                    SubjectCardList subjectCardList = (SubjectCardList) new Jyson().parseJson(json, SubjectCardList.class);
                    if (subjectCardList != null && subjectCardList.getData() != null && subjectCardList.getData().size() > 0) {
                        CardSubjuctActivity.this.mAdapter.setData(subjectCardList.getData());
                        if (currentPage == 1) {
                            recyclerViewLoadDataHandler.onRefreshSuccess();
                        } else {
                            recyclerViewLoadDataHandler.onLoadMoreSuccess();
                        }
                    } else {
                        recyclerViewLoadDataHandler.onNotData();
                    }
                } catch (Exception e) {
                    recyclerViewLoadDataHandler.onNotData();
                }

            }

            @Override
            public void onError(ApiException ex) {
                recyclerViewLoadDataHandler.handleException(ex);
            }
        });
    }

}
