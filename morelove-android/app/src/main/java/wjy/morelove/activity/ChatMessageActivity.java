package wjy.morelove.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import butterknife.BindView;
import wjy.morelove.App;
import wjy.morelove.Jyson.LinkQueue;
import wjy.morelove.R;
import wjy.morelove.adapter.ChatMessageAdapter;
import wjy.morelove.base.BaseActivity;
import wjy.morelove.bean.Lover;
import wjy.morelove.bean.Message;
import wjy.morelove.bean.User;
import wjy.morelove.nettyclient.ChatServerManager;
import wjy.morelove.nettyclient.protocol.request.MessageRequestPacket;
import wjy.morelove.widget.ChatInputView;

/**
 * 聊天消息页面
 * 客户端实现缓存聊天消息
 *
 * @author wjy
 */
public class ChatMessageActivity extends BaseActivity implements ChatServerManager.OnReceiverMessageListener {

    @BindView(R.id.navTitle)
    protected TextView navTitle;
    private Lover lover;
    private User toUser;
    private User meUser;

    @BindView(R.id.ryMsg)
    protected RecyclerView ryMsg;
    private ChatMessageAdapter messageAdapter;
    private final Queue<Message> messageLinkQueue = new LinkedBlockingDeque<>();

    @BindView(R.id.mChatInput)
    protected ChatInputView mChatInput;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_message;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (App.LOVER_RECORD_UPDATE_SUCCESS_ACTION.equals(intent.getAction())) {
                updateUserInfo();
            }
        }
    };

    @Override
    protected void onInit() {
        mChatInput.settingEmojiFragment(this);
        messageAdapter = new ChatMessageAdapter(this.ryMsg);
        if (App.getApp().getLover() == null) {
            registerReceiver(receiver, new IntentFilter(App.LOVER_RECORD_UPDATE_SUCCESS_ACTION));
            return;
        } else {
            updateUserInfo();
        }
        if (!ChatServerManager.getManager().isConnectChatServer()) {
            //未连接重连
            ChatServerManager.getManager().initChatServerConnect();
        }
    }

    /**
     * singleTask模式和后面的singleInstance模式都是只创建一个实例的。
     * 当intent到来，需要创建singleTask模式Activity的时候，系统会检查栈里面是否已经有该Activity的实例。如果有直接将intent发送给它。
     * 当一个Activity被设置为singletask时
     * （1）它并没有创建新的任务栈（TaskId都是一样的）
     * （2）如果它已经存在于栈中，再次请求触发此Activity时，会调用此类实例的onNewIntent方法，不会重新创建新的实例
     * （3）如果此类所在的任务栈上面有其它Activity，那么其它的Activity会被销毁
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onInit();
    }

    private void updateUserInfo() {
        this.lover = App.getApp().getLover();
        if (App.getApp().getUserLoginInfo().getUsername().equals(lover.getMan().getUsername())) {
            toUser = lover.getWomen();
            meUser = lover.getMan();
        } else {
            toUser = lover.getMan();
            meUser = lover.getWomen();
        }
        this.navTitle.setText(toUser.getUsername());
        ChatServerManager.getManager().setOnReceiverMessageListener(this);
        this.mChatInput.setOnChatInputListener(new ChatInputView.OnChatInputListener() {
            @Override
            public void onInputMessageChange(String message) {

            }

            @Override
            public void onSendClick(String message) {
                Message msg = new Message();
                msg.setSendUser(meUser);
                msg.setReadUser(toUser);
                msg.setCreateDate(new Date(System.currentTimeMillis()));
                msg.setMessage(message);
                msg.setMsgType(0);
                msg.setSendState(0);//发送中
                messageAdapter.addToEndMessage(msg);
                messageLinkQueue.add(msg);//存到发送队列
                //发送
                MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                messageRequestPacket.setToUserName(toUser.getUsername());
                messageRequestPacket.setMessage(message);
                ChatServerManager.getManager().getChatClient().getChannel().writeAndFlush(messageRequestPacket);
            }

            @Override
            public void onStartRecordVoice() {

            }

            @Override
            public void onCancelRecordVoice() {

            }

            @Override
            public void onSuccessRecordVoice(String voicePath) {

            }

            @Override
            public void onSelectImage(String imgPath) {

            }
        });
    }


    @Override
    public void onConnectDeath() {
        while (messageLinkQueue.peek() != null) {
            Message message = messageLinkQueue.poll();
            message.setSendState(2);
        }
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLogginSuccess() {

    }

    @Override
    public void onLogginFail(String errorMsg) {

    }

    @Override
    public void onSendMsgError(String errorMsg) {
        if (messageLinkQueue.peek() != null) {
            Message message = messageLinkQueue.poll();
            message.setSendState(2);
            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSendMsgSuccess() {
        if (messageLinkQueue.peek() != null) {
            Message message = messageLinkQueue.poll();
            message.setSendState(1);
            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onReceiver(List<Message> messagesList) {
        if (messagesList == null)
            return;
        messageAdapter.addToEndMessage(messagesList);
    }
}
