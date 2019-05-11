package wjy.weiai.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import wjy.weiai.R;

/**
 * 自定义加载等待对话框
 * @author wjy
 */
public class WLoadingDialog {

    private final WeakReference<Context> mContext;
    private String mHintText;//显示提示加载的文本
    private boolean mCancelable = true;//是否能点击对话框消失
    private boolean mCanceledOnTouchOutside = true;//是否能点击对话框之外的地方消失

    private Dialog mWLoadingDialog;

    public WLoadingDialog(Context context) {
        this.mContext=new WeakReference<>(context);
    }


    public WLoadingDialog setHintText(String text) {
        this.mHintText = text;
        return this;
    }


    public WLoadingDialog setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return this;
    }

    public WLoadingDialog setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }


    public Dialog create() {
        if (mWLoadingDialog != null) {
            cancel();
        }
        mWLoadingDialog = new Dialog(this.mContext.get(), R.style.WLoadingDialogDefaultStyle);
        View contentView = View.inflate(this.mContext.get(), R.layout.dialog_wloadingdialog, null);
        LinearLayout wLoadingRootView = contentView.findViewById(R.id.loadingDialog);
        TextView hintView = wLoadingRootView.findViewById(R.id.textView);
        hintView.setText(mHintText);
        mWLoadingDialog.setContentView(contentView);
        mWLoadingDialog.setCancelable(this.mCancelable);
        mWLoadingDialog.setCanceledOnTouchOutside(this.mCanceledOnTouchOutside);
        return mWLoadingDialog;
    }

    /**
     * 显示
     */
    public void show() {
        if(mWLoadingDialog==null)
            mWLoadingDialog = create();
        mWLoadingDialog.show();
    }

    /**
     * 取消
     */
    public void cancel() {
        if (mWLoadingDialog != null) {
            mWLoadingDialog.cancel();
        }
        mWLoadingDialog = null;
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        if (mWLoadingDialog != null) {
            mWLoadingDialog.dismiss();
        }
        mWLoadingDialog = null;
    }

}