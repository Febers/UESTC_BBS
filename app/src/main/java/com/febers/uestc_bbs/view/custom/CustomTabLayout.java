package com.febers.uestc_bbs.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.febers.uestc_bbs.utils.StatusBarUtils;

/**
 * 自定义控件，在其中令其paddingTop为statusBar的高度
 * 实现statusBar的隐藏与视图的完美显示
 */
public class CustomTabLayout extends TabLayout {

    private Context mContext;

    public CustomTabLayout(Context context) {
        this(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setPadding(getPaddingLeft(), getPaddingTop()+StatusBarUtils.INSTANCE.getHeight(mContext), getRight(), getBottom());
    }
}
