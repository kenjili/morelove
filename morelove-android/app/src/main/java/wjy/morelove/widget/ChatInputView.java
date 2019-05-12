package wjy.morelove.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import wjy.morelove.R;
import wjy.morelove.widget.emoji.EmojiFragment;

/**
 * 聊天输入控件，基类，只是对方法的定义，不做实现
 * 继承自FrameLayout，可以在xml布局文件中使用
 *
 * @author wjy
 */
public class ChatInputView extends FrameLayout {

    /**
     * 监听器，提供给外部监听获取输入的内容
     */
    public interface OnChatInputListener {
        /**
         * 输入内容改变
         *
         * @param message
         */
        void onInputMessageChange(String message);

        /**
         * 点击发送按钮
         *
         * @param message 当前输入框输入的内容
         */
        void onSendClick(String message);

        /**
         * 开始录制
         */
        void onStartRecordVoice();

        /**
         * 取消录制
         */
        void onCancelRecordVoice();

        /**
         * 停止录制
         *
         * @param voicePath 语音保存的路径
         */
        void onSuccessRecordVoice(String voicePath);

        /**
         * 选中图片发送
         * 选中图片之后要求直接发送
         *
         * @param imgPath 图片路径
         */
        void onSelectImage(String imgPath);
    }

    private OnChatInputListener mOnChatInputListener;

    public void setOnChatInputListener(OnChatInputListener onChatInputListener) {
        this.mOnChatInputListener = onChatInputListener;
    }

    public ChatInputView(@NonNull Context context) {
        super(context);
        this.init(context);
    }

    public ChatInputView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public ChatInputView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }


    private ImageView btnVoice;//切换录制语音
    private EditText etMessage;//编辑消息
    private TextView btnRecordVoice;//按住录音
    private ImageView btnExpression;//选择表情
    private ImageView btnMore;//选择更多
    private Button btnSend;//发送按钮

    /**
     * 要使用表情功能先要调用settingEmojiFragment方法初始化容器
     */
    private FrameLayout emojiRootPanle;//表情容器
    private EmojiFragment emojiFragment;

    public void settingEmojiFragment(FragmentActivity activity) {
        emojiFragment = new EmojiFragment(activity);
        emojiFragment.setOnSelectedEmojiListener(new EmojiFragment.OnSelectedEmojiListener() {
            @Override
            public void onSelectedEmoji(String emoji) {
                insertStringToInputView(emoji);
            }
        });
        this.emojiRootPanle.addView(emojiFragment.getRootView(),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    private final int INPUT_STATE_MESSAGE = 1;//输入状态
    private final int INPUT_STATE_MESSAGE_LENGTH = 2;//输入状态且输入有文字了
    private final int INPUT_STATE_VOICE = 4;//已经切换到语音录制状态
    private final int INPUT_STATE_RECORD_VOICEIMG = 8;//录制中
    private final int INPUT_STATE_SELECT_EXPRESSION = 16;//选择表情
    private final int INPUT_STATE_SELECT_MORE = 32;//弹出更多
    private int currentInputState = 0;

    private void init(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_chat_input, null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        this.addView(rootView, 0, layoutParams);
        this.onInitFindView();
        this.onInit();
    }


    private void onInitFindView() {
        this.btnVoice = findViewById(R.id.btnVoice);
        this.etMessage = findViewById(R.id.etMessage);
        this.btnRecordVoice = findViewById(R.id.btnRecordVoice);
        this.btnExpression = findViewById(R.id.btnExpression);
        this.btnMore = findViewById(R.id.btnMore);
        this.btnSend = findViewById(R.id.btnSend);
        this.emojiRootPanle = findViewById(R.id.emojiRootPanle);
    }


    /**
     * 输入状态改变时调用
     */
    private void onInputSteteChange() {
        if ((currentInputState & INPUT_STATE_MESSAGE) == INPUT_STATE_MESSAGE) {
            btnExpression.setVisibility(VISIBLE);
        } else {
            btnExpression.setVisibility(GONE);
            endInput();
        }

        if ((currentInputState & INPUT_STATE_MESSAGE_LENGTH) == INPUT_STATE_MESSAGE_LENGTH) {
            btnSend.setVisibility(VISIBLE);
            btnExpression.setVisibility(VISIBLE);
            btnMore.setVisibility(GONE);
        } else {
            btnSend.setVisibility(GONE);
            btnMore.setVisibility(VISIBLE);
        }

        if ((currentInputState & INPUT_STATE_SELECT_EXPRESSION) == INPUT_STATE_SELECT_EXPRESSION) {
            endInput();
            emojiRootPanle.setVisibility(VISIBLE);
            btnExpression.setImageResource(R.mipmap.icon_chat_input_wenzi);
        } else {
            btnExpression.setImageResource(R.mipmap.icon_chat_input_bq);
            emojiRootPanle.setVisibility(GONE);
        }


        if ((currentInputState & INPUT_STATE_SELECT_MORE) == INPUT_STATE_SELECT_MORE) {

        } else {

        }

        if ((currentInputState & INPUT_STATE_VOICE) == INPUT_STATE_VOICE
                || (currentInputState & INPUT_STATE_RECORD_VOICEIMG) == INPUT_STATE_RECORD_VOICEIMG) {
            endInput();
            etMessage.setVisibility(GONE);
            btnMore.setVisibility(GONE);
            btnVoice.setImageResource(R.mipmap.icon_chat_input_wenzi);
            btnRecordVoice.setVisibility(VISIBLE);
        } else {
            etMessage.setVisibility(VISIBLE);
            btnRecordVoice.setVisibility(GONE);
            btnVoice.setImageResource(R.mipmap.icon_chat_input_video);
        }
    }

    /**
     * 1.发送语音与【输入消息、选择表情、发送按钮、选择更多】互弃，发送语音状态下只能点击【录制、切回文字】
     * 2.输入消息时可选择【表情、发送、选择更多、切换到录音】，与【录制】互弃
     */
    private void onInit() {
        this.btnVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentInputState != INPUT_STATE_VOICE) {
                    //切换到可以录制语音状态
                    currentInputState = INPUT_STATE_VOICE;
                } else {
                    currentInputState = INPUT_STATE_MESSAGE;
                    if (etMessage.getText().length() > 0) {
                        currentInputState |= INPUT_STATE_MESSAGE_LENGTH;
                    }
                }
                onInputSteteChange();
            }
        });

        this.btnRecordVoice.setOnTouchListener(new OnTouchListener() {
            private boolean isDown = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    //按下录制中
                    isDown = true;
                    currentInputState |= INPUT_STATE_RECORD_VOICEIMG;
                    onInputSteteChange();
                } else if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    //松开取消
                    isDown = false;
                    currentInputState &= ~INPUT_STATE_RECORD_VOICEIMG;
                    onInputSteteChange();
                }
                return true;
            }
        });

        this.etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etMessage.getText().length() > 0) {
                    if ((currentInputState & INPUT_STATE_MESSAGE_LENGTH) == INPUT_STATE_MESSAGE_LENGTH)
                        return;
                    currentInputState |= INPUT_STATE_MESSAGE_LENGTH;
                    onInputSteteChange();
                } else {
                    if ((currentInputState & INPUT_STATE_MESSAGE_LENGTH) == INPUT_STATE_MESSAGE_LENGTH) {
                        currentInputState &= ~INPUT_STATE_MESSAGE_LENGTH;
                        onInputSteteChange();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        this.etMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击输入框去掉表情选择
                currentInputState = INPUT_STATE_MESSAGE;
                if (etMessage.getText().length() > 0)
                    currentInputState |= INPUT_STATE_MESSAGE_LENGTH;
                onInputSteteChange();
            }
        });

        this.etMessage.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    //获取到焦点
                    currentInputState = INPUT_STATE_MESSAGE;
                    if (etMessage.getText().length() > 0)
                        currentInputState |= INPUT_STATE_MESSAGE_LENGTH;
                    onInputSteteChange();
                }
            }
        });

        this.btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendMessage = etMessage.getText().toString();
                etMessage.setText("");
                currentInputState = 0;
                if (mOnChatInputListener != null) {
                    mOnChatInputListener.onSendClick(sendMessage);
                }
            }
        });

        this.btnMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((currentInputState & INPUT_STATE_SELECT_MORE) == INPUT_STATE_SELECT_MORE)
                    currentInputState &= ~INPUT_STATE_SELECT_MORE;
                else
                    currentInputState = INPUT_STATE_SELECT_MORE;
                onInputSteteChange();
            }
        });

        this.btnExpression.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((currentInputState & INPUT_STATE_SELECT_EXPRESSION) == INPUT_STATE_SELECT_EXPRESSION) {
                    currentInputState &= ~INPUT_STATE_SELECT_EXPRESSION;
                } else {
                    currentInputState |= INPUT_STATE_SELECT_EXPRESSION;
                }
                onInputSteteChange();
            }
        });
    }


    /**
     * 结束当前输入
     * 强制隐藏软键盘
     */
    public void endInput() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
    }


    /**
     * 给输入框插入文本
     * 在当前光标位置插入
     *
     * @param emojiCode
     */
    public void insertStringToInputView(String emojiCode) {
        if (emojiCode == null)
            return;
        int start = etMessage.getSelectionStart();
        int end = etMessage.getSelectionEnd();
        if (start < 0) {
            etMessage.append(emojiCode);
        } else {
            etMessage.getText().replace(Math.min(start, end), Math.max(start, end),
                    emojiCode, 0, emojiCode.length());
        }
    }


}

