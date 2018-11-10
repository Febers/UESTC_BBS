package com.febers.uestc_bbs.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.febers.uestc_bbs.R;
import com.febers.uestc_bbs.utils.ImageLoader;

import java.util.List;

import static android.util.Log.i;

public class ImgGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> imgPaths;
    private OnImageClickListener imageClickListener;

    public ImgGridViewAdapter(Context context, List<String> imgPaths) {
        this.imgPaths = imgPaths;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgPaths.size();
    }

    @Override
    public Object getItem(int i) {
        return imgPaths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.i("PE", "getView: " + i);
        ViewHolder holder;
        if (view == null || view.getTag() == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_layout_post_img, null);
            holder = new ViewHolder();
            holder.btnDelete = view.findViewById(R.id.btn_img_item_post_edit);
            holder.image = view.findViewById(R.id.image_view_item_post_edit);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ImageLoader.INSTANCE.loadResource(context, imgPaths.get(i), holder.image, false);
        holder.image.setOnClickListener((v) -> {
            if (imageClickListener != null) {
                imageClickListener.onImageClick(i);
            }
        });

        if (i == imgPaths.size() - 1) {
            holder.btnDelete.setVisibility(View.GONE);
        } else  {
            holder.btnDelete.setVisibility(View.VISIBLE);
        }
        /*
         * 点击图片上的删除按钮之后
         * 回调接口的监听器，通知图片已删除
         */
        holder.btnDelete.setOnClickListener((v) -> {
            if (imageClickListener != null) {
                imageClickListener.onImageDeleteClick(i);
            }
        });
        return view;
    }

    class ViewHolder {
        ImageView btnDelete;
        ImageView image;
    }

    public void setImageClickListener(OnImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
        void onImageDeleteClick(int position);
    }
}


