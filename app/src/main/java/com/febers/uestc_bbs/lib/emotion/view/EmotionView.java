package com.febers.uestc_bbs.lib.emotion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.febers.uestc_bbs.R;
import com.febers.uestc_bbs.lib.emotion.EmotionManager;
import com.febers.uestc_bbs.lib.emotion.EmotionTranslator;
import com.febers.uestc_bbs.lib.emotion.emoticons.EmoticonManager;
import com.febers.uestc_bbs.lib.emotion.listeners.IEmotionMenuClickListener;
import com.febers.uestc_bbs.lib.emotion.listeners.IStickerSelectedListener;
import com.febers.uestc_bbs.lib.emotion.sticker.StickerManager;
import com.febers.uestc_bbs.lib.emotion.utils.EmoticonUtils;

import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;


/**
 * Created by huxinyu on 2017/10/19 0019.
 * description : 表情输入键盘视图 View
 */

public class EmotionView extends RelativeLayout {

    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private Context mContext;
    private ViewPager mEmoticonPager;
    private LinearLayout mIndicatorLayout;
    private LinearLayout mBottomTabLayout;
    private EmotionTab mAddTab;
    private EmotionTab mSettingTab;
    private int mTabCount;
    private ArrayList<View> mTabs = new ArrayList<>();
    private int mTabPosi = 0;
    private EmotionEditText mAttachedEditText;
    private IStickerSelectedListener mEmoticonSelectedListener;
    private IEmotionMenuClickListener mEmoticonExtClickListener;
    private boolean loadedResource = false;
    private EmotionViewPagerAdapter adapter;

    public EmotionView(Context context) {
        this(context, null);
    }

    public EmotionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.setVisibility(GONE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getHeight() != 0 && !loadedResource) {
            init();
            initListener();
            loadedResource = true;
        }
    }

    /**
     * 在View的绘制过程中该方法会调用至少两次
     * 参考 https://www.jianshu.com/p/733c7e9fb284
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = measureWidth(widthMeasureSpec);
        mMeasuredHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
    }

    //计算控件布局宽度
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) { // 精确模式直接显示真实 Size
            result = specSize;
        } else { //非精确模式时显示默认 Size 如果是限制类型则显示默认值和限制值中较小的一个
            result = EmoticonUtils.dp2px(mContext, 200);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    //计算控件布局高度
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // 父布局直接指定大小精确模式直接显示真实 Size
            result = specSize;
        } else {
            //非精确模式时显示默认 Size 如果是限制类型则显示默认值和限制值中较小的一个
            //result = EmoticonUtils.dp2px(ctx, 200);
            result = EmoticonUtils.dp2px(mContext, 300);
            //log("height, now result:" + result);
            if (specMode == MeasureSpec.AT_MOST) {
                //父布局指定最大
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.layout_emotion, this);
        }
        mEmoticonPager = (ViewPager) findViewById(R.id.vpEmoticon);
        mIndicatorLayout = (LinearLayout) findViewById(R.id.llIndicator);
        mBottomTabLayout = (LinearLayout) findViewById(R.id.llTabContainer);
        mAddTab = (EmotionTab) findViewById(R.id.tabAdd);
        if (EmotionManager.getInstance().isShowAddButton()) {
            mAddTab.setVisibility(VISIBLE);
        } else {
            mAddTab.setVisibility(GONE);
        }
        initTabs();
    }

    /**
     * 初始化底部按钮
     */
    private void initTabs() {
        if (mBottomTabLayout == null) return;
        mTabs.clear();
        mBottomTabLayout.removeAllViews();
        //添加默认表情 Tab

        /*
         @Febers 在此处循环绘制
         添加多个Tab，当不同的Tab被点击的时候，viewPager显示不同的页数，以此实现多套主题方案
         */
//        for (int i = 0; i < 4; i++) {
//            EmotionTab emojiTab = new EmotionTab(ctx, EmotionDirHelper.getTabIcon(i));
//            mBottomTabLayout.addView(emojiTab);
//            mTabs.add(emojiTab);
//        }
        EmotionTab emojiTab = new EmotionTab(mContext, EmotionManager.getInstance().getDefaultIconRes());
        mBottomTabLayout.addView(emojiTab);
        mTabs.add(emojiTab);
        //添加所有的贴图tab
//        if (EmotionManager.getInstance().isShowStickers()) {  // 是否显示
//            List<StickerCategory> stickerCategories = StickerManager.getInstance().getStickerCategories();
//            for (int i = 0; i < stickerCategories.size(); i++) {
//                StickerCategory category = stickerCategories.get(i);
//                EmotionTab tab;
//                if (category.getName().equals(StickerManager.selfSticker)) {
//                    tab = new EmotionTab(ctx, R.drawable.ic_emotion_self);
//                    mBottomTabLayout.addView(tab);
//                    mTabs.add(tab);
//                } else {
//                    tab = new EmotionTab(ctx, category.getCoverPath());
//                    mBottomTabLayout.addView(tab);
//                    mTabs.add(tab);
//                }
//            }
//        }
//        //最后添加一个表情设置Tab
//        if (EmotionManager.getInstance().isShowSetButton()) {
//            mSettingTab = new EmotionTab(ctx, R.drawable.ic_setting_gray);
//            StateListDrawable drawable = new StateListDrawable();
//            Drawable unSelected = ctx.getResources().getDrawable(R.color.white);
//            drawable.addState(new int[]{-android.R.attr.state_pressed}, unSelected);
//            Drawable selected = ctx.getResources().getDrawable(R.color.color_gray);
//            drawable.addState(new int[]{android.R.attr.state_pressed}, selected);
//            mSettingTab.setBackground(drawable);
//            mBottomTabLayout.addView(mSettingTab);
//            mTabs.add(mSettingTab);
//        }
        selectTab(0); //默认底一个被选中
    }

    /**
     * 选择选中的 Item
     */
    private void selectTab(int tabPosi) {
//        log("Tab is Delected and Position is " + tabPosi);
        if (EmotionManager.getInstance().isShowSetButton()) {
            if (tabPosi == mTabs.size() - 1)
                return;
        }
        for (int i = 0; i < mTabCount; i++) {
            View tab = mTabs.get(i);
            tab.setBackgroundResource(R.drawable.xbg_emotion_tab_normal);
        }
        mTabs.get(tabPosi).setBackgroundResource(R.drawable.xbg_emotion_tab_press);
        //显示表情内容
        fillVpEmotion(tabPosi);
    }

    private void fillVpEmotion(int tabPosi) {
        if (adapter == null) {
            adapter = new EmotionViewPagerAdapter(mMeasuredWidth, mMeasuredHeight, tabPosi, mEmoticonSelectedListener);
        } else {
            adapter.setTabPosi(tabPosi);
        }
        mEmoticonPager.setAdapter(adapter);
        mIndicatorLayout.removeAllViews();
        setCurPageCommon(0);
        if (tabPosi == 0) {
            adapter.attachEditText(mAttachedEditText);
        }
    }

    private void initListener() {
        if (mBottomTabLayout == null) return;
        if (EmotionManager.getInstance().isShowSetButton()) {
            mTabCount = mBottomTabLayout.getChildCount() - 1;//不包含最后的设置按钮
        } else {
            mTabCount = mBottomTabLayout.getChildCount();
        }
        for (int position = 0; position < mTabCount; position++) {
            View tab = mBottomTabLayout.getChildAt(position);
            tab.setTag(position);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTabPosi = (int) v.getTag();
                    selectTab(mTabPosi);
                }
            });
        }
        mEmoticonPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setCurPageCommon(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (mAddTab != null) {
            mAddTab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEmoticonExtClickListener != null) {
                        mEmoticonExtClickListener.onTabAddClick(v);
                    }
                }
            });
        }
        if (mSettingTab != null) {
            mSettingTab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mEmoticonExtClickListener != null) {
                        mEmoticonExtClickListener.onTabSettingClick(v);
                    }
                }
            });
        }
    }


    private void setCurPageCommon(int position) {
//        if (mTabPosi == 0) {
            int emojiPerpage = EmotionManager.getInstance().getEmojiPerPage();
            setCurPage(position, (int) Math.ceil(EmoticonManager.getInstance().getDisplayCount() / (float) emojiPerpage));
//        } else {
//            int stickerPerPage = EmotionManager.getInstance().getStickerPerPage();
//            StickerCategory category = StickerManager.getInstance().getStickerCategories().get(mTabPosi - 1);
//            setCurPage(position, (int) Math.ceil(category.getStickers().size() / (float) stickerPerPage));
//        }
    }

    private void setCurPage(int page, int pageCount) {
        int hasCount = mIndicatorLayout.getChildCount();
        int forMax = Math.max(hasCount, pageCount);
        ImageView ivCur;
        for (int i = 0; i < forMax; i++) {
            if (pageCount <= hasCount) {
                if (i >= pageCount) {
                    mIndicatorLayout.getChildAt(i).setVisibility(View.GONE);
                    continue;
                } else {
                    ivCur = (ImageView) mIndicatorLayout.getChildAt(i);
                }
            } else {
                if (i < hasCount) {
                    ivCur = (ImageView) mIndicatorLayout.getChildAt(i);
                } else {
                    ivCur = new ImageView(mContext);
                    ivCur.setBackgroundResource(R.drawable.xbg_emotion_selector_view_pager_indicator);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(EmoticonUtils.dp2px(mContext, 8), EmoticonUtils.dp2px(mContext, 8));
                    ivCur.setLayoutParams(params);
                    params.leftMargin = EmoticonUtils.dp2px(mContext, 3);
                    params.rightMargin = EmoticonUtils.dp2px(mContext, 3);
                    mIndicatorLayout.addView(ivCur);
                }
            }
            ivCur.setId(i);
            ivCur.setSelected(i == page);
            ivCur.setVisibility(View.VISIBLE);
        }
    }

    public void attachEditText(EmotionEditText inputEditText) {
        if (mAttachedEditText != null) { // 绑定下一个焦点输入控件时将上一个控件吊起的输入框隐藏,并将已输入的内容转成表情放入
            mAttachedEditText.setText(EmotionTranslator.getInstance()
                    .makeEmojiSpannable(mAttachedEditText.getText().toString()));
            mAttachedEditText.getKeyBoardManager().hideInputLayout();
            if (mTabPosi == 0 && adapter != null) { // 重新为表情输入栏绑定输入控件
                adapter.attachEditText(inputEditText);
            }
        }
        mAttachedEditText = inputEditText;
    }

    public EmotionEditText getAttachEditText() {
        return mAttachedEditText;
    }

    public void setEmoticonSelectedListener(IStickerSelectedListener emotionSelectedListener) {
        mEmoticonSelectedListener = emotionSelectedListener;
    }

    public void setEmoticonMenuClickListener(IEmotionMenuClickListener emotionExtClickListener) {
        mEmoticonExtClickListener = emotionExtClickListener;
    }

    /**
     * 新增了表情库后调用
     */
    public void reloadEmos(int position) {
        mTabPosi = position;
        StickerManager.getInstance().loadStickerCategory();
        initTabs();
        initListener();
        invalidate();
        if (0 <= position && position < mTabs.size()) {
            selectTab(position);
        }
    }

}
