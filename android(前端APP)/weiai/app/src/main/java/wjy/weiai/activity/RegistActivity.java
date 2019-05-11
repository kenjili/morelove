package wjy.weiai.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;
import wjy.weiai.R;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.request.HttpUrl;
import wjy.weiai.request.LoadDataContract;
import wjy.weiai.request.exception.ApiException;


/**
 * 注册页面
 *
 * @author wjy
 */
public class RegistActivity extends BaseActivity {


    @BindView(R.id.navTitle)
    protected TextView tvTitle;


    @BindView(R.id.tvUsername)
    protected TextView tvUsername;
    @BindView(R.id.tvPassword)
    protected TextView tvPassword;
    @BindView(R.id.tvRealname)
    protected TextView tvRealname;
    @BindView(R.id.rbSex)
    protected RadioGroup rbSex;
    @BindView(R.id.tvPhoneNumber)
    protected TextView tvPhoneNumber;
    @BindView(R.id.tvValidationCode)
    protected TextView tvValidationCode;
    @BindView(R.id.tvBirthday)
    protected TextView tvBirthday;

    @BindView(R.id.btnSendValidationCode)
    protected Button btnSendValidationCode;

    private int mYear, mMonth, mDay;

    private LoadDataContract loadDataContract;

    private static final int GET_SMS_CODE_TIME = 60;//隔60秒才能重新获取验证码
    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                int currentJiShi = GET_SMS_CODE_TIME - msg.arg1;
                if (currentJiShi == 0) {
                    btnSendValidationCode.setEnabled(true);
                    btnSendValidationCode.setText(R.string.send_validation_code);
                } else {
                    btnSendValidationCode.setText(currentJiShi + "秒后可重试");
                }
            }
        }
    };
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onInit() {
        tvTitle.setText(R.string.regist);
        loadDataContract = new LoadDataContract(this);
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH) + 1;
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        tvBirthday.setOnClickListener((view) -> {
            new DatePickerDialog(RegistActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    String days;
                    if (mMonth < 10) {
                        if (mDay < 10) {
                            days = new StringBuffer().append(mYear).append("年").append("0").
                                    append(mMonth).append("月").append("0").append(mDay).append("日").toString();
                        } else {
                            days = new StringBuffer().append(mYear).append("年").append("0").
                                    append(mMonth).append("月").append(mDay).append("日").toString();
                        }
                    } else {
                        if (mDay < 10) {
                            days = new StringBuffer().append(mYear).append("年").
                                    append(mMonth).append("月").append("0").append(mDay).append("日").toString();
                        } else {
                            days = new StringBuffer().append(mYear).append("年").
                                    append(mMonth).append("月").append(mDay).append("日").toString();
                        }
                    }
                    tvBirthday.setText(days);
                }
            }, mYear, mMonth, mDay).show();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_regist;
    }


    /**
     * 获取短信验证码
     */
    @OnClick(R.id.btnSendValidationCode)
    protected void btnSendValidationCodeClick() {
        if (tvPhoneNumber.getText().length() != 11) {
            showThreadToast("请先输入手机号码！");
            return;
        }
        btnSendValidationCode.setEnabled(false);
        btnSendValidationCode.setText("稍等...");
        this.loadDataContract.loadData(Request.Method.POST, HttpUrl.SMS_CODE_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("phoneNumber", tvPhoneNumber.getText().toString());
                return param;
            }

            @Override
            public void onSuccess(String json) {
                mExecutorService.submit(() -> {
                    int jishi = 0;
                    do {
                        jishi++;
                        Message message = new Message();
                        message.what = 0;
                        message.arg1 = jishi;
                        handler.sendMessage(message);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (jishi < 60);
                });
            }

            @Override
            public void onError(ApiException ex) {
                btnSendValidationCode.setEnabled(true);
                btnSendValidationCode.setText(R.string.send_validation_code);
                showThreadToast("验证码发送失败！");
            }
        });
    }


    @OnClick(R.id.btnRegist)
    protected void onBtnRegistClick() {
        showWaitDialog("正在注册...");
        String username = tvUsername.getText().toString();
        String password = tvPassword.getText().toString();
        String realname = tvRealname.getText().toString();
        String phoneNumber = tvPhoneNumber.getText().toString();
        String validationCode = tvValidationCode.getText().toString();
        int sex = ((RadioButton) rbSex.getChildAt(0)).isChecked() ? 0 : 1;
        //日期
        String date = mYear + "-" + (mMonth > 9 ? mMonth : "0" + mMonth) + "-" + (mDay > 9 ? mDay : "0" + mDay);

        this.loadDataContract.loadData(Request.Method.POST, HttpUrl.USER_REGIST_URL, new LoadDataContract.ViewDataContract() {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("username", username);
                param.put("password", password);
                param.put("realname", realname);
                param.put("phoneNumber", phoneNumber);
                param.put("smsCode", validationCode);
                param.put("sex", sex + "");
                param.put("date", date);
                return param;
            }

            @Override
            public void onSuccess(String data) {
                dismissWaitDialog();
                showAlertMessage("注册成功", "恭喜您注册成功！现在可以去登录啦！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        RegistActivity.this.finish();
                    }
                });
            }

            @Override
            public void onError(ApiException ex) {
                dismissWaitDialog();
                showAlertMessage(ex.message);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mExecutorService != null)
            mExecutorService.shutdown();
        super.onDestroy();
    }
}
