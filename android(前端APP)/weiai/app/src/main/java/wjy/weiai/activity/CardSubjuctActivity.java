package wjy.weiai.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import wjy.weiai.R;
import wjy.weiai.adapter.DaCardAdpter;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.bean.SubjectCard;
import wjy.weiai.handler.RecyclerViewLoadDataHandler;
import wjy.weiai.widget.LoadDataNullLayout;
import wjy.weiai.widget.WSwipeRefreshLayout;


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
        SubjectCard subjectCard = new SubjectCard();
        subjectCard.setSubject("每日晚安卡");
        subjectCard.setNote("每日24点前完成打卡");
        data.add(subjectCard);

        SubjectCard subjectCard2 = new SubjectCard();
        subjectCard2.setSubject("每周一起逛一次街");
        subjectCard2.setNote("每周日20点前完成");
        data.add(subjectCard2);
        this.mAdapter.setData(data);

        if (currentPage == 1) {
            recyclerViewLoadDataHandler.onRefreshSuccess();
        } else {
            recyclerViewLoadDataHandler.onLoadMoreSuccess();
        }
    }

}
