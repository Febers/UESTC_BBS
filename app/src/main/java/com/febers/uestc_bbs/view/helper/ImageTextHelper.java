/*
 * Created by Febers at 18-8-18 下午4:04.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 下午4:04.
 */

package com.febers.uestc_bbs.view.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.febers.uestc_bbs.R;
import com.febers.uestc_bbs.utils.AttrUtils;
import com.febers.uestc_bbs.utils.ViewClickUtils;


import static android.util.Log.i;

/**
 * 构建可显示图片的textview
 * 参考：https://github.com/CentMeng/RichTextView
 */
public class ImageTextHelper {

    private static final String TAG = "ImageTextHelper";

    public static Drawable getUrlDrawable(String source, TextView textView) {
        GlideImageGetter imageGetter = new GlideImageGetter(textView.getContext(),textView);
        return imageGetter.getDrawable(source);
    }

    public static void setImageText(TextView tv, String html){
        if (tv == null) {
            return;     //防止跟kotlin的空冲突
        }
        Context context = tv.getContext();
        Spanned htmlStr = Html.fromHtml(html);

        tv.setClickable(true);
        tv.setTextIsSelectable(true);
        tv.setText(htmlStr);

        //tv.setMovementMethod(LinkMovementMethod.getInstance());
        //换成下面的方法，否则超链接设置失效
        tv.setAutoLinkMask(Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES);

        CharSequence text = tv.getText();
        if(text instanceof Spannable){
            int end = text.length();
            Spannable sp = (Spannable)tv.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            ImageSpan[] imgs = sp.getSpans(0,end,ImageSpan.class);
            StyleSpan[] styleSpans = sp.getSpans(0,end,StyleSpan.class);
            ForegroundColorSpan[] colorSpans = sp.getSpans(0,end,ForegroundColorSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for(URLSpan url : urls){
                MyUrlSpan myUrlSpan = new MyUrlSpan(url.getURL(), context);
                style.setSpan(myUrlSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(AttrUtils.INSTANCE.getColor(tv.getContext(), R.attr.app_color_accent));
                style.setSpan(colorSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for(ImageSpan url : imgs){
                ImageSpan span = new ImageSpan(getUrlDrawable(url.getSource(),tv),url.getSource());
                style.setSpan(span, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                MyUrlSpan myUrlSpan = new MyUrlSpan(url.getSource(),context);
                style.setSpan(myUrlSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for(StyleSpan styleSpan : styleSpans){
                style.setSpan(styleSpan, sp.getSpanStart(styleSpan), sp.getSpanEnd(styleSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            for(ForegroundColorSpan colorSpan : colorSpans){
                style.setSpan(colorSpan, sp.getSpanStart(colorSpan), sp.getSpanEnd(colorSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv.setText(style);
        }
    }

    private static class MyUrlSpan extends ClickableSpan {

        private String mUrl;
        private Context mContext;

        MyUrlSpan(String mUrl, Context context) {
            this.mUrl = mUrl;
            this.mContext = context;
        }

        @Override
        public void onClick(View widget) {
            ViewClickUtils.INSTANCE.linkClick(mUrl, mContext);
        }
    }
}
