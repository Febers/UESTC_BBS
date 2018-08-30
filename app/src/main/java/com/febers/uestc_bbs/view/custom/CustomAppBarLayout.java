package com.febers.uestc_bbs.view.custom;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;

import com.febers.uestc_bbs.utils.StatusBarUtils;

/**
 * 自定义控件，在其中令其paddingTop为statusBar的高度
 * 实现statusBar的隐藏与视图的完美显示
 */
public class CustomAppBarLayout extends AppBarLayout {

    private Context mContext;

    public CustomAppBarLayout(Context context) {
        this(context, null);
    }

    public CustomAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setPadding(getPaddingLeft(), getPaddingTop()+StatusBarUtils.INSTANCE.getHeight(mContext), getRight(), getBottom());
    }
}
