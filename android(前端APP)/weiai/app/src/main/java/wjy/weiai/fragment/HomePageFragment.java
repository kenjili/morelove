package wjy.weiai.fragment;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import wjy.weiai.App;
import wjy.weiai.R;
import wjy.weiai.activity.ChatMessageActivity;
import wjy.weiai.activity.PublicItineraryActivity;
import wjy.weiai.activity.PublicLovetimeActivity;
import wjy.weiai.adapter.NewsPageAdapter;
import wjy.weiai.base.BaseActivity;
import wjy.weiai.bean.Channel;
import wjy.weiai.bean.Message;
import wjy.weiai.nettyclient.ChatServerManager;
import wjy.weiai.widget.expandablebuttonmenu.ScreenHelper;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 首页
 *
 * @author wjy
 */
public class HomePageFragment extends BaseFragment implements ChatServerManager.OnReceiverMessageListener {

    @BindView(R.id.newsTablayout)
    protected TabLayout newsTablayout;
    @BindView(R.id.newsViewPage)
    protected ViewPager newsViewPage;

    @BindView(R.id.tvChatMessageCount)
    protected TextView tvChatMessageCount;

    private List<Channel> tabTypes;
    private NewsPageAdapter pageAdapter;

    private Dialog mMenuDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void onFragmentInitFinish() {
        initPageAdapter();
    }


    private void initPageAdapter() {
        this.tabTypes = new ArrayList<>();
        this.tabTypes.add(new Channel(getText(R.string.ebm_left_buttom_title).toString(), "[sg]"));
        this.tabTypes.add(new Channel(getText(R.string.ebm_mid_buttom_title).toString(), "[lx]"));
        this.tabTypes.add(new Channel(getText(R.string.ebm_right_buttom_title).toString(), "[kyk]"));
        this.pageAdapter = new NewsPageAdapter(getChildFragmentManager(), this.tabTypes);
        this.newsViewPage.setAdapter(this.pageAdapter);
        this.newsTablayout.setupWithViewPager(this.newsViewPage);
        this.reflex(this.newsTablayout);
        this.newsViewPage.setCurrentItem(0);
    }

    /**
     * 设置TabLyout的下滑线宽度
     *
     * @param tabLayout
     */
    public void reflex(final TabLayout tabLayout) {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        //拿到tabView的mTextView属性
                        // tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //因为我想要的效果是
                        // 字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        int dp10 = (int) ScreenHelper.dpToPx(tabLayout.getContext(), 10);
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick(R.id.ivPublic)
    protected void onPublicClick() {
        if (this.mMenuDialog == null) {
            this.mMenuDialog = new Dialog(this.getContext(), R.style.NormalDialogStyle);
            View view = View.inflate(this.getContext(), R.layout.dialog_public_menu, null);
            view.findViewById(R.id.btnCancel)
                    .setOnClickListener(view1 -> this.mMenuDialog.dismiss());
            view.findViewById(R.id.btnPublicLovetime)
                    .setOnClickListener(view1 -> {
                        this.mMenuDialog.dismiss();
                        //发布恋爱时光
                        Intent intent = new Intent();
                        intent.setClass(getContext(), PublicLovetimeActivity.class);
                        startActivity(intent);
                    });
            view.findViewById(R.id.btnPublicLvxingji)
                    .setOnClickListener(view1 -> {
                        this.mMenuDialog.dismiss();
                        //发布旅行日记
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), PublicItineraryActivity.class);
                        startActivity(intent);
                    });
            this.mMenuDialog.setContentView(view);
            this.mMenuDialog.setCanceledOnTouchOutside(true);
            view.setMinimumHeight((int) (ScreenHelper.getScreenHeight(this.getContext()) * 0.23f));
            Window dialogWindow = this.mMenuDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (ScreenHelper.getScreenWidth(this.getContext()) * 0.9f);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            dialogWindow.setAttributes(lp);
        }
        this.mMenuDialog.show();
    }


    @OnClick(R.id.ivChatMessage)
    protected void onChatMessageClick() {
        //清空未读
        tvChatMessageCount.setText("0");
        tvChatMessageCount.setVisibility(View.GONE);
        new App.SharedPreferencesBuild(this.getContext()).removeForKey("not_read_chat_message")
                .submit();
        ((BaseActivity) getActivity()).startActivity(ChatMessageActivity.class);
        canelNotification();
    }


    @Override
    public void onResume() {
        super.onResume();
        this.initChatMessageServer();
        //未读消息数量
        App.SharedPreferencesBuild build = new App.SharedPreferencesBuild(this.getContext());
        String count = build.getValueForKey("not_read_chat_message");
        int value = 0;
        try {
            value = Integer.valueOf(count);
        }catch (NumberFormatException e){
        }
        if(value>0)
            tvChatMessageCount.setVisibility(View.VISIBLE);
        tvChatMessageCount.setText(count);
    }

    /**
     * 初始化聊天服务，监听接收到消息显示未读消息数量
     */
    private void initChatMessageServer() {
        if (!ChatServerManager.getManager().isConnectChatServer()) {
            ChatServerManager.getManager().initChatServerConnect();
        }
        ChatServerManager.getManager().setOnReceiverMessageListener(this);
    }

    @Override
    public void onConnectDeath() {

    }

    @Override
    public void onLogginSuccess() {

    }

    @Override
    public void onLogginFail(String errorMsg) {

    }

    @Override
    public void onSendMsgError(String errorMsg) {

    }

    @Override
    public void onSendMsgSuccess() {

    }

    /**
     * 显示未读消息数量
     *
     * @param messagesList
     */
    @Override
    public void onReceiver(List<Message> messagesList) {
        int msgCount = 0;
        try {
            msgCount = Integer.valueOf(tvChatMessageCount.getText().toString());
        } catch (NumberFormatException e) {

        }
        if (messagesList != null && messagesList.size() > 0) {
            showNotification(messagesList.get(messagesList.size() - 1));
            msgCount += messagesList.size();
            //持久存储
            new App.SharedPreferencesBuild(this.getContext()).addKeyValue("not_read_chat_message", String.valueOf(msgCount))
                    .submit();
        }
        if (msgCount > 0) {
            tvChatMessageCount.setVisibility(View.VISIBLE);
            if (msgCount < 9) {
                tvChatMessageCount.setText(msgCount + "");
            } else
                tvChatMessageCount.setText("9+");
        }
    }

    private Notification.Builder mNotificationBuilder;
    private int currentNotificationId;

    /**
     * 显示通知
     */
    private void showNotification(Message message) {
        if (mNotificationBuilder == null) {
            //android8.0以上系统适配
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //创建通知频道
                NotificationChannel channel = new NotificationChannel("chatMessageChannel", "聊天通知", NotificationManager.IMPORTANCE_HIGH);
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(
                        NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
                this.mNotificationBuilder = new Notification.Builder(getContext(), "chatMessageChannel");
            } else {
                //android8.0以下不需要设置频道
                this.mNotificationBuilder = new Notification.Builder(getContext());
            }
        }
        //设置参数
        this.mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(Notification.FLAG_ONLY_ALERT_ONCE)//标记声音或震动一次,android8.0以下有效
                .setContentTitle(message.getSendUser().getUsername())
                .setContentText(message.getMessage())
                .setAutoCancel(true)//设置点击之后notification消失
                .setOnlyAlertOnce(false);//设置为true只会提醒一次声音，不会重复提醒。

        //点击跳转到聊天页面
        Intent intent = new Intent(getContext(), ChatMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent,
                //FLAG_CANCEL_CURRENT  ：如果被描述的PendingIntent已经存在,在即将生成一个新的PendingIntent前，当前的一个要被取消。
                //FLAG_UPDATE_CURRENT  ：如果被描述的PendingIntent已经存在,那么继续保持它，但它其中的数据会因为新Intent而更新。
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationBuilder.setContentIntent(pi);

        //显示
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        currentNotificationId = message.getSendUser().getId();
        manager.notify(currentNotificationId, this.mNotificationBuilder.build());
    }

    /**
     * 取消通知显示
     *
     * @param
     */
    private void canelNotification() {
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(currentNotificationId);
    }
}
