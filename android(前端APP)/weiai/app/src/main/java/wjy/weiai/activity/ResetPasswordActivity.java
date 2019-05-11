package wjy.weiai.activity;

import android.widget.TextView;

import butterknife.BindView;
import wjy.weiai.R;
import wjy.weiai.base.BaseActivity;

/**
 * 找回密码，重置密码页面
 * @author wjy
 */
public class ResetPasswordActivity extends BaseActivity{


    @BindView(R.id.navTitle)
    protected TextView tvTitle;

    @Override
    protected void onInit() {
        tvTitle.setText(R.string.login_forget_password);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_resetpassword;
    }

}
