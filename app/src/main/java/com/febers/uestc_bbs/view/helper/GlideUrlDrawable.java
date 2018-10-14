/*
 * Created by Febers at 18-8-18 下午3:09.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 下午3:09.
 */

package com.febers.uestc_bbs.view.helper;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;

/**
 * 参考：https://github.com/CentMeng/RichTextView/blob/master/app/src/main/java/com/luoteng/
 * richtextview/utils/GlideUrlDrawable.java
 * 注意的是Glide4中移除了GlideDrawable
 * v4添加了对原生Drawable的支持
 * !!!补充：为了兼容另一个工具类，改回v3版本的glide
 * 而且只能手动导入jar包
 */
public class GlideUrlDrawable extends Drawable implements Drawable.Callback {

    private GlideDrawable mDrawable;

    @Override
    public void draw(Canvas canvas) {
        if (mDrawable != null) {
            mDrawable.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        if (mDrawable != null) {
            mDrawable.setAlpha(alpha);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (mDrawable != null) {
            mDrawable.setColorFilter(cf);
        }
    }

    @Override
    public int getOpacity() {
        if (mDrawable != null) {
            return mDrawable.getOpacity();
        }
        return PixelFormat.UNKNOWN;
    }

    public void setDrawable(GlideDrawable drawable) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback(null);
        }
        drawable.setCallback(this);
        this.mDrawable = drawable;
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        if (getCallback() != null) {
            getCallback().invalidateDrawable(who);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (getCallback() != null) {
            getCallback().scheduleDrawable(who, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (getCallback() != null) {
            getCallback().unscheduleDrawable(who, what);
        }
    }
}
