package wjy.androidlibs.imageselector.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import wjy.androidlibs.imageselector.ImageSelector;
import wjy.androidlibs.imageselector.R;
import wjy.androidlibs.imageselector.utils.Utils;
import wjy.androidlibs.imageselector.bean.ImageFolder;

import java.util.ArrayList;
import java.util.List;


/**
 * 弹出列表适配器，显示可选文件夹
 * @author wjy
 */
public class ImageFolderAdapter extends BaseAdapter {


    private Activity mActivity;
    private LayoutInflater mInflater;
    private int mImageSize;
    private List<ImageFolder> imageFolders;
    private int lastSelected = 0;

    public ImageFolderAdapter(Activity activity) {
        mActivity = activity;
        imageFolders = new ArrayList<>();
        mImageSize = Utils.getImageItemWidth(mActivity);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<ImageFolder> folders) {
        if (folders != null && folders.size() > 0) imageFolders = folders;
        else imageFolders.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageFolders.size();
    }

    @Override
    public ImageFolder getItem(int position) {
        return imageFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lv_item_folder, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageFolder folder = getItem(position);
        holder.folderName.setText(folder.name);
        holder.imageCount.setText(mActivity.getString(R.string.ip_folder_image_count, folder.images.size()));
        ImageSelector.getInstance().getImageLoader().displayImage(mActivity, folder.cover.path, holder.cover, mImageSize, mImageSize);

        if (lastSelected == position) {
            holder.folderCheck.setVisibility(View.VISIBLE);
        } else {
            holder.folderCheck.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) {
            return;
        }
        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private class ViewHolder {
        ImageView cover;
        TextView folderName;
        TextView imageCount;
        ImageView folderCheck;

        public ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.iv_cover);
            folderName = (TextView) view.findViewById(R.id.tv_folder_name);
            imageCount = (TextView) view.findViewById(R.id.tv_image_count);
            folderCheck = (ImageView) view.findViewById(R.id.iv_folder_check);
            view.setTag(this);
        }
    }
}
