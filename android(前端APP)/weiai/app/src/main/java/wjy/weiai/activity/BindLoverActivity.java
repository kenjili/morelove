package wjy.weiai.activity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import wjy.weiai.Jyson.Jyson;
import wjy.weiai.R;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.bean.LoverBindState;
import wjy.weiai.request.HttpUrl;
import wjy.weiai.request.LoadDataContract;
import wjy.weiai.request.exception.ApiException;


/**
 * 绑定情侣关系
 *
 * @author wjy
 */
public class BindLoverActivity extends BaseActivity {


    @BindView(R.id.navTitle)
    protected TextView tvTitle;
    @BindView(R.id.leftButton)
    protected View leftButton;


    @BindView(R.id.etOtherUsername)
    protected EditText etOtherUsername;
    @BindView(R.id.btnBindOrEnterBind)
    protected Button btnBindOrEnterBind;

    private LoadDataContract loadDataContract;

    @Override
    protected void onInit() {
        tvTitle.setText(R.string.lover_bind);
        leftButton.setVisibility(View.GONE);
        this.loadDataContract = new LoadDataContract(this);
        queryBindState();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bindlover;
    }


    /**
     * 查询绑定状态
     */
    private void queryBindState() {
        this.loadDataContract.loadData(Request.Method.GET, HttpUrl.LOVER_BINDSTATE_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                return null;
            }

            @Override
            public void onSuccess(String json) {
                try {
                    LoverBindState loverBindState = (LoverBindState) new Jyson().parseJson(json, LoverBindState.class);
                    if (loverBindState != null) {
                        //绑定成功了
                        if (loverBindState.getState() == 0) {
                            showAlertMessage("恭喜您！", "对方 '" + loverBindState.getOtherUser().getUsername()
                                            + "' 已经接受您啦！你们已经是情侣了，赶快去发布你们的密照吧！"
                                    , (DialogInterface dialogInterface, int i) ->
                                            BindLoverActivity.this.finish());
                            return;
                        } else if (loverBindState.getState() == 1) {
                            changeEditState(1);
                        } else if (loverBindState.getState() == 2) {
                            changeEditState(2);
                        }
                        if (loverBindState.getOtherUser() != null) {
                            etOtherUsername.setText(loverBindState.getOtherUser().getUsername());
                        }
                    } else {
                        changeEditState(-1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    changeEditState(-1);
                }
            }

            @Override
            public void onError(ApiException ex) {

            }
        });
    }

    /**
     * 根据情侣关系绑定状态来改变输入框和按钮的状态
     *
     * @param bindState
     */
    private void changeEditState(int bindState) {
        switch (bindState) {
            case 1:
                this.etOtherUsername.setEnabled(false);
                this.btnBindOrEnterBind.setEnabled(true);
                this.btnBindOrEnterBind.setText(R.string.lover_wait_other_enter);
                this.btnBindOrEnterBind.setOnClickListener(null);
                break;
            case 2:
                this.etOtherUsername.setEnabled(false);
                this.btnBindOrEnterBind.setEnabled(true);
                this.btnBindOrEnterBind.setText(R.string.lover_enter_bind);
                this.btnBindOrEnterBind.setOnClickListener((view) -> {
                    //确认绑定
                    enterBind();
                });
                break;
            case -1:
                this.etOtherUsername.setEnabled(true);
                this.btnBindOrEnterBind.setEnabled(true);
                this.btnBindOrEnterBind.setText(R.string.lover_button_bind);
                this.btnBindOrEnterBind.setOnClickListener((view) -> {
                    //申请绑定
                    sendBind(etOtherUsername.getText().toString());
                });
                break;
        }
    }

    /**
     * 确认绑定
     */
    private void enterBind() {
        this.loadDataContract.loadData(Request.Method.GET, HttpUrl.LOVER_ENTERBIND_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                return null;
            }

            @Override
            public void onSuccess(String json) {
                queryBindState();
            }

            @Override
            public void onError(ApiException ex) {
                showAlertMessage(ex.message);
            }
        });
    }


    /**
     * 申请绑定
     *
     * @param otherUsername
     */
    private void sendBind(String otherUsername) {
        this.loadDataContract.loadData(Request.Method.POST, HttpUrl.LOVER_BINDLOVER_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("otherUsername", otherUsername);
                return param;
            }

            @Override
            public void onSuccess(String json) {
                queryBindState();
            }

            @Override
            public void onError(ApiException ex) {
                showAlertMessage(ex.message);
            }
        });
    }

}
