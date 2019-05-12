package wjy.morelove.widget.emoji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.morelove.R;

/**
 * 表情适配器
 * Created by wujiuye on 2017/7/21.
 */

public class EmojiAdapter extends BaseAdapter {

    private Context mContext;
    private String[] emojiCodes;

    public EmojiAdapter(Context context, String[] emojiCodes) {
        this.mContext = context;
        this.emojiCodes = emojiCodes;
    }

    @Override
    public int getCount() {
        return emojiCodes.length;
    }

    @Override
    public Object getItem(int position) {
        return emojiCodes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class EmojiItemView {
        @BindView(R.id.tv_emoji)
        TextView tvEmoji;

        public EmojiItemView(View rootView) {
            ButterKnife.bind(this, rootView);
        }

        public void showEmoji(String emoji) {
            tvEmoji.setText(emoji);
            tvEmoji.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmojiItemView emojiItemView;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_item_emoji, null);
            convertView.setTag(new EmojiItemView(convertView));
        }
        emojiItemView = (EmojiItemView) convertView.getTag();
        emojiItemView.showEmoji(emojiCodes[position]);
        return convertView;
    }
}
