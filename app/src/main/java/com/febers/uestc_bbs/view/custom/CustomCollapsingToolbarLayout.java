package com.febers.uestc_bbs.view.custom;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;

import com.febers.uestc_bbs.utils.StatusBarUtils;

public class CustomCollapsingToolbarLayout extends CollapsingToolbarLayout {

    private Context mContext;

    public CustomCollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setPadding(getPaddingLeft(), getPaddingTop()+ StatusBarUtils.INSTANCE.getHeight(mContext), getRight(), getBottom());
    }
}
