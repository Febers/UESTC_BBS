package com.febers.uestc_bbs.view.panda_emotion.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.febers.uestc_bbs.R;
import com.febers.uestc_bbs.view.panda_emotion.PandaEmoManager;


/**
 * 表情底部 tab 对象
 */
public class EmotionTab extends RelativeLayout {

    // 贴图表情封面图路径
    private String mStickerCoverImgPath;
    private int mIconSrc = R.drawable.ic_add_gray_small;

    public EmotionTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public EmotionTab(Context context, int iconSrc) {
        super(context);
        mIconSrc = iconSrc;
        init(context, null);
    }

    public EmotionTab(Context context, String stickerCoverImgPath) {
        super(context);
        mStickerCoverImgPath = stickerCoverImgPath;
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.layout_emotion_tab, this);
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmotionTab);
        ImageView ivIcon = (ImageView) findViewById(R.id.ivIcon);
        if (attrs != null) {
            mIconSrc = typedArray.getResourceId(R.styleable.EmotionTab_iconSrc, R.drawable.ic_add_gray_small);
            ivIcon.setImageResource(mIconSrc);
            typedArray.recycle();
        } else {
            if (TextUtils.isEmpty(mStickerCoverImgPath)) {
                ivIcon.setImageResource(mIconSrc);
            } else {
                PandaEmoManager.getInstance().getIImageLoader().displayImage(mStickerCoverImgPath, ivIcon);
            }
        }
    }

}
