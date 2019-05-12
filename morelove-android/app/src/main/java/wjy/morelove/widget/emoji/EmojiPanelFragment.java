package wjy.morelove.widget.emoji;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import wjy.morelove.R;

/**
 * Created by wujiuye on 2017/7/25.
 */

public class EmojiPanelFragment extends Fragment {

    /**
     * 选中表情监听器
     */
    public interface OnItemClickListener{
        void onItemClick(int fragment, int position);
    }

    @BindView(R.id.gv_emoji)
    protected GridView mGridView;
    private EmojiAdapter emojiAdapter;
    private int onViewPageIndex = -1;

    private OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if(bundle.containsKey("fragment")){
            this.onViewPageIndex = bundle.getInt("fragment");
        }
        if(bundle.containsKey("data")){
            this.emojiAdapter = new EmojiAdapter(context, (String[]) bundle.getSerializable("data"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emoji,null);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
    }

    private void initUI(){
        if(emojiAdapter!=null)
            mGridView.setAdapter(emojiAdapter);
        mGridView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id)->{
            if(this.mOnItemClickListener!=null)
                this.mOnItemClickListener.onItemClick(this.onViewPageIndex,position);
        });
    }
}