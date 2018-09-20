package com.febers.uestc_bbs.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

import java.lang.ref.WeakReference;

/**
 * 图片垂直居中显示
 */
public class CenterImageSpan extends ImageSpan {

    public CenterImageSpan(Drawable d) {
        super(d);
    }

    public CenterImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public CenterImageSpan(Drawable d, String source) {
        super(d, source);
    }

//    @Override
//    public void draw(@NonNull Canvas canvas, CharSequence text,
//                     int start, int end, float x,
//                     int top, int y, int bottom, @NonNull Paint paint) {
//        // image to draw
//        Drawable b = getDrawable();
//        // font metrics of text to be replaced
//        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
//        int transY = (y + fm.descent + y + fm.ascent) / 2
//                - b.getBounds().bottom / 2;
//
//        canvas.save();
//        canvas.translate(x, transY);
//        b.draw(canvas);
//        canvas.restore();
//    }
}
