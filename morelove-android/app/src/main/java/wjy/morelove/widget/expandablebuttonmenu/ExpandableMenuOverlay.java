package wjy.morelove.widget.expandablebuttonmenu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import wjy.morelove.R;

public class ExpandableMenuOverlay extends ImageButton implements DialogInterface.OnKeyListener, View.OnClickListener {

    private Dialog mDialog;
    private ExpandableButtonMenu mButtonMenu;

    private boolean mAdjustViewSize = true;

    protected boolean mDismissing;

    public ExpandableMenuOverlay(Context context) {
        this(context, null, 0);
    }

    public ExpandableMenuOverlay(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableMenuOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandableMenuOverlay, 0, 0);
            try {
                mAdjustViewSize = a.getBoolean(R.styleable.ExpandableMenuOverlay_adjustViewSize, true);
            } finally {
                //将参数回收复用，就是给初始化弹出对话框用
                a.recycle();
            }
        }
        init(attrs);
    }

    /**
     * 修复了dialog弹出时状态栏变黑的问题
     * @param attrs
     */
    public void init(AttributeSet attrs) {
        //无标题栏透明主题
        mDialog = new Dialog(getContext(), android.R.style.Theme_Translucent_NoTitleBar);
        //FLAG_TRANSLUCENT_STATUS让对话框状态栏透明
        //FLAG_DIM_BEHIND让窗口变暗
        mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //设置透明度
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        mDialog.getWindow().setAttributes(lp);

        mButtonMenu = new ExpandableButtonMenu(getContext(), attrs);
        mButtonMenu.setButtonMenuParentOverlay(this);

        mDialog.setContentView(mButtonMenu);
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setVisibility(View.INVISIBLE);
                mButtonMenu.toggle();
            }
        });
        mDialog.setOnKeyListener(this);
        setOnClickListener(this);
    }

    /**
     * Show the dialog, dimming the screen and expanding the button menu
     */
    public void show() {
        mDialog.show();
    }

    /**
     * Dismiss the dialog, removing screen dim and hiding the expanded menu
     */
    public void dismiss() {
        mButtonMenu.setAnimating(false);
        mDialog.dismiss();
    }

    /**
     * Show the view that expands the button menu
     */
    public void showInitButton() {
        setVisibility(View.VISIBLE);
    }

    /**
     * Set a callback on expanded menu button clicks
     *
     * @param listener
     */
    public void setOnMenuButtonClickListener(ExpandableButtonMenu.OnMenuButtonClick listener) {
        mButtonMenu.setOnMenuButtonClickListener(listener);
    }

    /**
     * Get underlying expandable buttom menu
     *
     * @return
     */
    public ExpandableButtonMenu getButtonMenu() {
        return mButtonMenu;
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && !mDismissing) {
            if (mButtonMenu.isExpanded()) {
                mDismissing = true;
                mButtonMenu.toggle();
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == getId()) {
            show();
        }
    }


    /**
     * 要让弹出对话框的关闭按钮与该按钮屏幕上的位置重叠，达到遮盖的效果
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //setMeasuredDimension((int)(sWidth * mButtonMenu.getMainButtonSize()),(int)(sWidth * mButtonMenu.getMainButtonSize()));
        if (mAdjustViewSize) {
            if (!(getParent() instanceof RelativeLayout)) {
                throw new IllegalStateException("Only RelativeLayout is supported as parent of this view");
            }
            final int sWidth = ScreenHelper.getScreenWidth(getContext());
            final int sHeight = ScreenHelper.getScreenHeight(getContext());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
            params.width = (int) (sWidth * mButtonMenu.getMainButtonSize());
            params.height = (int) (sWidth * mButtonMenu.getMainButtonSize());
            params.setMargins(0, 0, 0, (int) (sHeight * mButtonMenu.getBottomPadding()));
        }
    }

}
