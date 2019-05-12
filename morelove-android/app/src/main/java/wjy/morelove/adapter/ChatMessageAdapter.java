package wjy.morelove.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.morelove.App;
import wjy.morelove.AppConfig;
import wjy.morelove.R;
import wjy.morelove.bean.Message;
import wjy.morelove.bean.User;
import wjy.morelove.utils.DateTimeUtils;

/**
 * 聊天消息列表适配器
 *
 * @author wjy
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.BaseViewHolder> {

    private List<Message> data;
    private User meUser;
    private User toUser;
    private WeakReference<RecyclerView> recyclerViewReference;

    public ChatMessageAdapter(RecyclerView recyclerView) {
        this.data = new ArrayList<>();
        this.meUser = App.getApp().getUserLoginInfo();
        this.recyclerViewReference = new WeakReference<>(recyclerView);
        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),1));
    }

    public void addToEndMessage(Message messages) {
        data.add(messages);
        notifyDataSetChanged();
        recyclerViewReference.get().scrollToPosition(data.size());
    }

    public void addToEndMessage(List<Message> messages) {
        int postion = data.size();
        data.addAll(messages);
        notifyDataSetChanged();
        recyclerViewReference.get().scrollToPosition(postion);
    }


    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public abstract void updateUI(Message message, Message lastMsg);

        protected String showDate(Date date) {
            String strTime = DateTimeUtils.date2String(date);
            if (DateTimeUtils.date2StringYMD(date).equals(DateTimeUtils.getCurrentYMDString())) {
                strTime = strTime.split(" ")[1];
            }
            return strTime;
        }
    }

    class SendMsgView extends BaseViewHolder {

        @BindView(R.id.tvTime)
        TextView tvDate;
        @BindView(R.id.tvMessage)
        TextView tvMsg;
        @BindView(R.id.ivUserImg)
        ImageView ivUser;
        @BindView(R.id.pbSend)
        ProgressBar pbSend;
        @BindView(R.id.ivSendState)
        ImageView ivSendState;

        public SendMsgView(View itemView) {
            super(itemView);
        }

        @Override
        public void updateUI(Message message, Message lastMsg) {
            tvMsg.setText(message.getMessage());
            Glide.with(recyclerViewReference.get().getContext())
                    .load(meUser.getHeadThumb())
                    .apply(AppConfig.getRequestOptionsCircleCrop())
                    .into(ivUser);
            if (lastMsg != null) {
                long lastTime = lastMsg.getCreateDate().getTime();
                if (message.getCreateDate().getTime() - lastTime >= 60 * 1000) {
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setText(showDate(message.getCreateDate()));
                } else
                    tvDate.setVisibility(View.GONE);
            } else {
                tvDate.setText(showDate(message.getCreateDate()));
                tvDate.setVisibility(View.VISIBLE);
            }

            switch (message.getSendState()) {
                case 0:
                    pbSend.setVisibility(View.VISIBLE);
                    ivSendState.setVisibility(View.GONE);
                    break;
                case 1:
                    pbSend.setVisibility(View.GONE);
                    ivSendState.setVisibility(View.GONE);
                    break;
                case 2:
                    pbSend.setVisibility(View.GONE);
                    ivSendState.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    class ReciverMsgView extends BaseViewHolder {

        @BindView(R.id.tvTime)
        TextView tvDate;
        @BindView(R.id.tvMessage)
        TextView tvMsg;
        @BindView(R.id.ivUserImg)
        ImageView ivUser;

        public ReciverMsgView(View itemView) {
            super(itemView);
        }

        @Override
        public void updateUI(Message message, Message lastMsg) {
            tvMsg.setText(message.getMessage());
            Glide.with(recyclerViewReference.get().getContext())
                    .load(toUser.getHeadThumb())
                    .apply(AppConfig.getRequestOptionsCircleCrop())
                    .into(ivUser);
            if (lastMsg != null) {
                long lastTime = lastMsg.getCreateDate().getTime();
                if (message.getCreateDate().getTime() - lastTime >= 60 * 1000) {
                    tvDate.setVisibility(View.VISIBLE);
                    tvDate.setText(showDate(message.getCreateDate()));
                } else
                    tvDate.setVisibility(View.GONE);
            } else {
                tvDate.setText(showDate(message.getCreateDate()));
                tvDate.setVisibility(View.VISIBLE);
            }
        }
    }

    public final static int SEND_VIEW = 1;//发送消息
    public final static int REVICE_VIEW = 2;//接收消息

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SEND_VIEW:
                return new SendMsgView(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_item_chat_message_send, null));
            case REVICE_VIEW:
                return new ReciverMsgView(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_item_chat_message_revice, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Message message = this.data.get(position);
        holder.updateUI(message, position > 0 ? this.data.get(position - 1) : null);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        Message message = data.get(position);
        if (message.getSendUser().getUsername().equals(meUser.getUsername())) {
            if (toUser == null)
                toUser = message.getReadUser();
            return SEND_VIEW;
        } else {
            if (toUser == null)
                toUser = message.getSendUser();
            return REVICE_VIEW;
        }
    }
}
