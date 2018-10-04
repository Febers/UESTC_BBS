package com.febers.uestc_bbs.view.utils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.febers.uestc_bbs.R;
import com.febers.uestc_bbs.utils.ImageLoader;

import java.util.HashSet;
import java.util.Set;

public class  GlideImageGetter implements Html.ImageGetter, Drawable.Callback {

    private static final String TAG = "GlideImageGetter";

    private final Context mContext;

    private final TextView mTextView;

    private final Set<ImageGetterViewTarget> mTargets;

    public static GlideImageGetter get(View view) {
        return (GlideImageGetter) view.getTag(R.id.drawable_callback_tag);
    }

    public void clear() {
        GlideImageGetter prev = get(mTextView);
        if (prev == null) return;

        for (ImageGetterViewTarget target : prev.mTargets) {
            Glide.clear(target);
        }
    }

    public GlideImageGetter(Context context, TextView textView) {
        this.mContext = context;
        this.mTextView = textView;

//        clear(); 屏蔽掉这句在TextView中可以加载多张图片
        mTargets = new HashSet<>();
        mTextView.setTag(R.id.drawable_callback_tag, this);
    }

    @Override
    public Drawable getDrawable(String url) {
        final GlideUrlDrawable urlDrawable = new GlideUrlDrawable();
        ImageLoader.INSTANCE.loadViewTarget(mContext, url, new ImageGetterViewTarget(mTextView, urlDrawable));
        return urlDrawable;
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        mTextView.invalidate();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {

    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {

    }

    public class ImageGetterViewTarget extends ViewTarget<TextView, GlideDrawable> {

        private final GlideUrlDrawable mDrawable;

        private Request request;

        @Override
        public Request getRequest() {
            return request;
        }

        @Override
        public void setRequest(Request request) {
            this.request = request;
        }

        private ImageGetterViewTarget(TextView view, GlideUrlDrawable drawable) {
            super(view);
            mTargets.add(this);
            this.mDrawable = drawable;
        }

        @Override
        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
            Rect rect;
            if (resource.getIntrinsicWidth() > 100) {
                float width;
                float height;

                if (resource.getIntrinsicWidth() >= getView().getWidth()) {
                    float downScale = (float) resource.getIntrinsicWidth() / getView().getWidth();
                    width = (float) resource.getIntrinsicWidth() / (float) downScale;
                    height = (float) resource.getIntrinsicHeight() / (float) downScale;
                } else {
                    float multiplier = (float) getView().getWidth() / resource.getIntrinsicWidth();
                    width = (float) resource.getIntrinsicWidth() * (float) multiplier;
                    height = (float) resource.getIntrinsicHeight() * (float) multiplier;
                }

                rect = new Rect(0, 0, Math.round(width), Math.round(height));
            } else {
                rect = new Rect(0, 0, resource.getIntrinsicWidth() * 2, resource.getIntrinsicHeight() * 2);
            }
            resource.setBounds(rect);

            mDrawable.setBounds(rect);
            mDrawable.setDrawable(resource);

            if (resource.isAnimated()) {
                mDrawable.setCallback(get(getView()));
                resource.setLoopCount(GlideDrawable.LOOP_FOREVER);
                resource.start();
            }

            getView().setText(getView().getText());
            getView().invalidate();
        }
    }
}
