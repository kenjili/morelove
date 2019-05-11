package wjy.weiai.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import wjy.weiai.App;
import wjy.weiai.R;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.bean.User;
import wjy.weiai.request.HttpUrl;
import wjy.weiai.request.LoadDataContract;
import wjy.weiai.request.exception.ApiException;
import wjy.weiai.utils.AssetsUtils;
import wjy.weiai.widget.GifView;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.navTitle)
    protected TextView navTitle;
    @BindView(R.id.tvForgetPassword)
    protected TextView tvForgetPassword;
    @BindView(R.id.tvGotoRegist)
    protected TextView tvGotoRegist;

    @BindView(R.id.gifImage)
    protected GifView gifView;

    @BindView(R.id.leftButton)
    protected View leftButton;

    @BindView(R.id.etUsername)
    protected EditText etUsername;
    @BindView(R.id.etPassword)
    protected EditText etPassword;

    //数据加载器
    private LoadDataContract loadDataContract;

    @Override
    protected void onInit() {
//        navTitle.setVisibility(View.GONE);
        navTitle.setText(R.string.login);
        leftButton.setVisibility(View.GONE);
        tvGotoRegist.setVisibility(View.VISIBLE);
//        tvForgetPassword.setVisibility(View.VISIBLE);//忘记密码暂不实现
        this.loadDataContract = new LoadDataContract(this);
        User user = ((App) getApplication()).getUserLoginInfo();
        if(user!=null){
            etUsername.setText(user.getUsername());
            etPassword.setText(user.getPassword());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gifView.play(AssetsUtils.getFileInputStreamWidthAssets(this, "bg_login.gif"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        gifView.stop();
    }


    @OnClick(R.id.tvGotoRegist)
    protected void onRegistClick() {
        startActivity(RegistActivity.class);
    }

    @OnClick(R.id.tvForgetPassword)
    protected void onForgetPasswordClick() {
        startActivity(ResetPasswordActivity.class);
    }

    @OnClick(R.id.btnLogin)
    protected void onLoginClick() {
        showWaitDialog("正在登录...");
        //记住用户名和密码
        ((App)getApplication()).savaUserLoginInfo(this.etUsername.getText().toString(),this.etPassword.getText().toString());
        this.loadDataContract.loadData(Request.Method.POST, HttpUrl.USER_LOGIN_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username", LoginActivity.this.etUsername.getText().toString());
                map.put("password", LoginActivity.this.etPassword.getText().toString());
                return map;
            }

            @Override
            public void onSuccess(String data) {
                dismissWaitDialog();
                startActivity(MainActivity.class);
                LoginActivity.this.finish();
            }

            @Override
            public void onError(ApiException ex) {
                dismissWaitDialog();
                showAlertMessage(ex.message);
            }
        });
    }

}
