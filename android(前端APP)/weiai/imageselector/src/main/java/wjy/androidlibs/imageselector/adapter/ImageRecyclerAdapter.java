package wjy.androidlibs.imageselector.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Toast;

import wjy.androidlibs.imageselector.ImageSelector;
import wjy.androidlibs.imageselector.R;
import wjy.androidlibs.imageselector.bean.ImageItem;
import wjy.androidlibs.imageselector.utils.Utils;
import wjy.androidlibs.imageselector.widget.SuperCheckBox;

import java.util.ArrayList;

/**
 * 显示图片的RecyclerView的适配器，且显示图片item是否选中状态
 *
 * @author wjy
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {


    private static final int ITEM_TYPE_CAMERA = 0;  //相机
    private static final int ITEM_TYPE_NORMAL = 1;  //图片

    private ImageSelector imageSelector;
    private Activity mActivity;
    private ArrayList<ImageItem> images;       //当前需要显示的所有的图片数据
    private boolean isShowCamera;         //是否显示拍照按钮，只有在单选模式的时候才为true，否则false
    private int mImageSize;               //每个条目的大小
    private LayoutInflater mInflater;
    private OnImageItemClickListener listener;   //图片被点击的监听

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnImageItemClickListener {
        void onImageItemClick(View view, ImageItem imageItem, int position);

        void onCameraClick();
    }

    public void setData(ArrayList<ImageItem> images) {
        if (images == null || images.size() == 0) this.images = new ArrayList<>();
        else this.images = new ArrayList<>(images);
        notifyDataSetChanged();
    }

    /**
     * 构造方法
     */
    public ImageRecyclerAdapter(Activity activity) {
        this.mActivity = activity;
        this.images = new ArrayList<>();

        mImageSize = Utils.getImageItemWidth(mActivity);
        imageSelector = ImageSelector.getInstance();
        isShowCamera = !imageSelector.isMultiMode();
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CAMERA) {
            return new CameraViewHolder(mInflater.inflate(R.layout.rv_item_camera, parent, false));
        }
        return new ImageViewHolder(mInflater.inflate(R.layout.rv_item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof CameraViewHolder) {
            ((CameraViewHolder) holder).bindCamera();
        } else if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowCamera) return position == 0 ? ITEM_TYPE_CAMERA : ITEM_TYPE_NORMAL;
        return ITEM_TYPE_NORMAL;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return isShowCamera ? images.size() + 1 : images.size();
    }

    public ImageItem getItem(int position) {
        if (isShowCamera) {
            if (position == 0) return null;
            return images.get(position - 1);
        } else {
            return images.get(position);
        }
    }

    /**
     * image item
     */
    private class ImageViewHolder extends ViewHolder {

        View rootView;
        ImageView ivThumb;
        View mask;
        View checkView;
        SuperCheckBox cbCheck;


        ImageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
            mask = itemView.findViewById(R.id.mask);
            checkView = itemView.findViewById(R.id.checkView);
            cbCheck = (SuperCheckBox) itemView.findViewById(R.id.cb_check);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
        }

        void bind(final int position) {
            final ImageItem imageItem = getItem(position);
            ivThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onImageItemClick(rootView, imageItem, position);
                }
            });
            checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cbCheck.setChecked(!cbCheck.isChecked());
                    int selectLimit = imageSelector.getSelectLimit();
                    if (cbCheck.isChecked() && imageSelector.getSelectedImages().size() >= selectLimit) {
                        Toast.makeText(mActivity.getApplicationContext(), mActivity.getString(R.string.ip_select_limit, selectLimit), Toast.LENGTH_SHORT).show();
                        cbCheck.setChecked(false);
                        mask.setVisibility(View.GONE);
                    } else {
                        imageSelector.addSelectedImageItem(position, imageItem, cbCheck.isChecked());
                        mask.setVisibility(View.VISIBLE);
                    }
                }
            });
            //根据是否多选，显示或隐藏checkbox
            if (imageSelector.isMultiMode()) {
                cbCheck.setVisibility(View.VISIBLE);
                //查询当前图片item是否在选中列表中，在则显示选中状态
                boolean checked = imageSelector.getSelectedImages().contains(imageItem);
                if (checked) {
                    mask.setVisibility(View.VISIBLE);
                    cbCheck.setChecked(true);
                } else {
                    mask.setVisibility(View.GONE);
                    cbCheck.setChecked(false);
                }
            } else {
                cbCheck.setVisibility(View.GONE);
            }
            //让指定的图片加载器加载图片显示到imageview
            imageSelector.getImageLoader().displayImage(mActivity, imageItem.path, ivThumb, mImageSize, mImageSize);
        }

    }

    /**
     * 相机item
     */
    private class CameraViewHolder extends ViewHolder {

        View mItemView;

        CameraViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        void bindCamera() {
            mItemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            mItemView.setTag(null);
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onCameraClick();
                }
            });
        }
    }
}
