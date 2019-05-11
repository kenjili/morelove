package wjy.weiai.widget.emoji;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wjy.weiai.R;

/**
 * emoji
 * @author wujiuye
 */
public class EmojiFragment implements EmojiPanelFragment.OnItemClickListener{

	public interface OnSelectedEmojiListener{
		void onSelectedEmoji(String emoji);
	}
	private OnSelectedEmojiListener onSelectedEmojiListener;
	public void setOnSelectedEmojiListener(OnSelectedEmojiListener onSelectedEmojiListener){
		this.onSelectedEmojiListener = onSelectedEmojiListener;
	}

	private View rootView;
	private ViewPager mViewPager;

	private PagerAdapter mAdapter;
	private List<Fragment> fragments;
	private List<String[]> emojiGroupAdapterDatas;

	public EmojiFragment(FragmentActivity activity) {
		rootView = LayoutInflater.from(activity).inflate(R.layout.fragment_emoji_panel,null);
		this.mViewPager = rootView.findViewById(R.id.vpEmoji);
		initViewPage(activity);
	}

	public View getRootView(){
		return rootView;
	}

	/**
	 * 初始化viewpage
	 * @param activity
	 */
	private void initViewPage(FragmentActivity activity) {
		fragments = new ArrayList<>();
		initEmojiFragmentData();
		for (int i = 0; i < this.emojiGroupAdapterDatas.size(); i++) {
			EmojiPanelFragment fragment = new EmojiPanelFragment();
			fragment.setOnItemClickListener(this);
			Bundle bundle = new Bundle();
			bundle.putInt("fragment",i);
			bundle.putSerializable("data",emojiGroupAdapterDatas.get(i));
			fragment.setArguments(bundle);
			fragments.add(fragment);
		}
		mAdapter = new EmojiPageAdapter(activity.getSupportFragmentManager(),fragments);
		mViewPager.setAdapter(mAdapter);
	}


	private void initEmojiFragmentData() {
		if (emojiGroupAdapterDatas == null) {
			emojiGroupAdapterDatas = new ArrayList<>();
		}
		for (int row = 0; row < Emoji.EmojiArray.length; row++) {
			emojiGroupAdapterDatas.add( Emoji.EmojiArray[row]);
		}
	}

	@Override
	public void onItemClick(int fragment, int position) {
		String emoji = emojiGroupAdapterDatas.get(fragment)[position];
		if(onSelectedEmojiListener!=null)
			onSelectedEmojiListener.onSelectedEmoji(emoji);
	}
}