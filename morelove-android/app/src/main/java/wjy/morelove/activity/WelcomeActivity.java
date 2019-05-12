package wjy.morelove.activity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;


import butterknife.BindView;
import wjy.morelove.App;
import wjy.morelove.R;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.bean.AppVersion;
import wjy.morelove.bean.User;
import wjy.morelove.request.DaggerLoadDataContractComponent;
import wjy.morelove.request.HttpUrl;
import wjy.morelove.request.LoadDataContract;
import wjy.morelove.request.LoadDataContractModules;
import wjy.morelove.request.exception.ApiException;
import wjy.morelove.utils.AppUtils;

/**
 * 启动页，欢迎页
 * 用户已经登录跳转到首页
 * 用户未登录跳转到登录页
 *
 * @author wjy
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.tvAppVersion)
    protected TextView tvAppVersion;

    @BindView(R.id.tvTime)
    protected TextView tvTime;

    private volatile int jishi = 3;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (jishi > 0) {
                tvTime.setText(jishi + " 秒");
                return;
            }
        }
    };

    @Override
    protected void onInit() {
        DaggerLoadDataContractComponent
                .builder()
                .loadDataContractModules(new LoadDataContractModules(this::getContent))
                .build()
                .injection(this);

        AppVersion appVersion = AppUtils.appVersion(this);
        tvAppVersion.setText(tvAppVersion.getText().toString() + appVersion.getName());
        //模拟计时器
        new Thread() {
            @Override
            public void run() {
                while (jishi >= 0) {
                    mHandler.sendEmptyMessage(0x00);
                    try {
                        Thread.sleep(1000);
                        jishi--;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //自动登录
        this.loadDataContract = new LoadDataContract(this);
        User user = ((App) getApplication()).getUserLoginInfo();
        if (user != null && user.getUsername() != null && user.getPassword() != null) {
            this.loadDataContract.loadData(Request.Method.POST, HttpUrl.USER_LOGIN_URL, new LoadDataContract.ViewDataContract() {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("username", user.getUsername());
                    map.put("password", user.getPassword());
                    return map;
                }

                @Override
                public void onSuccess(String data) {
                    while (jishi > 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    startActivity(MainActivity.class);
                    WelcomeActivity.this.finish();
                }

                @Override
                public void onError(ApiException ex) {
                    while (jishi > 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    startActivity(LoginActivity.class);
                    WelcomeActivity.this.finish();
                }
            });
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

}
