package wjy.morelove.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rainbow.library.GalleryViewPager;
import wjy.morelove.App;
import wjy.morelove.Jyson.Jyson;
import wjy.morelove.R;
import wjy.morelove.adapter.MemorialdayAdapter;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.hook.OnActivityResultCallback;
import wjy.morelove.bean.MemorialDay;
import wjy.morelove.request.HttpUrl;
import wjy.morelove.request.LoadDataContract;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.widget.LoadDataNullLayout;


/**
 * 纪念日页面
 * @author wjy
 */
public class MemorialDayActivity extends BaseActivity{

    @BindView(R.id.navTitle)
    protected TextView navTitle;
    @BindView(R.id.tvAdd)
    protected View tvAdd;


    private MemorialdayAdapter memorialdayAdapter;
    @BindView(R.id.errorLayout)
    protected LoadDataNullLayout errorLayout;

    @BindView(R.id.vpMemorialday)
    protected GalleryViewPager galleryViewPager;
    @BindView(R.id.tvPostion)
    protected TextView tvPostion;

    private int postion;
    private LoadDataContract loadDataContract;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_memorialday;
    }

    @Override
    protected void onInit() {
        navTitle.setText(R.string.activity_title_memorialday);
        tvAdd.setVisibility(View.VISIBLE);
        this.loadDataContract = new LoadDataContract(this);
        updateUI(new ArrayList<>());
        loadDta();
    }


    private void updateUI(List<MemorialDay> data){
        if(data==null||data.size()==0){
            errorLayout.setErrorType(LoadDataNullLayout.NODATA);
        }
        memorialdayAdapter = new MemorialdayAdapter(this,data);
        memorialdayAdapter.setOnDeleteClickListener(memorialDay -> {
            if(memorialDay!=null){
                showAlertMessage("删除提示", "确定要删除[" + memorialDay.getMemorialName() + "]纪念日吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        doDelete(memorialDay);
                    }
                });
            }
        });
        galleryViewPager.setAdapter(memorialdayAdapter);
        postion = 0;
        tvPostion.setText((postion+1)+"/"+memorialdayAdapter.getCount());
        galleryViewPager.addOnPageChangeListener(new GalleryViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                postion = i;
                tvPostion.setText((postion+1)+"/"+memorialdayAdapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    @OnClick(R.id.tvAdd)
    protected void tvAddOnClick(){
        startActivityFormResult(MemorialDayAddActivity.class, 100, new OnActivityResultCallback() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if(requestCode==100){
                    loadDta();
                }
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadDta(){
        errorLayout.setErrorType(LoadDataNullLayout.NETWORK_LOADING);
        loadDataContract.loadData(Request.Method.GET, HttpUrl.MEMORIALDAY_LIST_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                return null;
            }

            @Override
            public void onSuccess(String json) {
                try {
                    List<MemorialDay> memorialDayList = (List<MemorialDay>) new Jyson().parseJson(json,MemorialDay.class);
                    if(memorialDayList==null||memorialDayList.size()==0){
                        errorLayout.setErrorType(LoadDataNullLayout.NODATA);
                        return;
                    }
                    errorLayout.setErrorType(LoadDataNullLayout.HIDE_LAYOUT);
                    App.getApp().updateUserLoverRecord();
                    updateUI(memorialDayList);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorLayout.setErrorType(LoadDataNullLayout.JSON_PARSE_ERROR);
                }
            }

            @Override
            public void onError(ApiException ex) {
                if(ex.code == ApiException.ErrorCode.HTTP_RESULT_ERROR) {
                    errorLayout.setOnHttpRequestErrorShowMessage(ex.message);
                    errorLayout.setErrorType(LoadDataNullLayout.HTTP_REQUEST_ERROR);
                }else{
                    errorLayout.setErrorType(LoadDataNullLayout.NETWORK_ERROR);
                }
            }
        });
    }


    /**
     * 删除纪念日
     * @param memorialDay
     */
    private void doDelete(MemorialDay memorialDay){
        loadDataContract.loadData(Request.Method.POST,
                HttpUrl.MEMORIALDAY_DELETE_URL, new LoadDataContract.ViewDataContract() {
                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> param = new HashMap<>();
                        param.put("id",String.valueOf(memorialDay.getId()));
                        return param;
                    }

                    @Override
                    public void onSuccess(String json) {
                        App.getApp().updateUserLoverRecord();
                        loadDta();
                    }

                    @Override
                    public void onError(ApiException ex) {
                        showAlertMessage("删除失败",ex.code==ApiException.ErrorCode.HTTP_RESULT_ERROR?ex.getMessage():"网络异常！");
                    }
                });
    }

}
