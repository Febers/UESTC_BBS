package com.febers.uestc_bbs.lib.emotion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.febers.uestc_bbs.lib.emotion.KeyBoardManager;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by huxinyu on 2017/10/24 0024.
 * description ：表情输入 EditText 主要是为了监听返回键（软键盘弹出时常规返回键监听是无效的）
 */

public class EmotionEditText extends AppCompatEditText {

    private KeyBoardManager mKeyBoardManager;
    private IBackPressedListener mBackPressedListener;

    public EmotionEditText(Context context) {
        super(context);
    }

    public EmotionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmotionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.KEYCODE_SOFT_LEFT) {
            if (mBackPressedListener != null) {
                mBackPressedListener.backPressed();
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setBackPressedListener(IBackPressedListener backPressedListener) {
        mBackPressedListener = backPressedListener;
    }

    public interface IBackPressedListener {
        void backPressed();
    }

    public KeyBoardManager getKeyBoardManager() {
        return mKeyBoardManager;
    }

    public void setKeyBoardManager(KeyBoardManager keyBoardManager) {
        mKeyBoardManager = keyBoardManager;
    }
}
