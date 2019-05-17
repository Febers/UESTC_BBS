package com.febers.uestc_bbs.view.emotion.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.febers.uestc_bbs.view.emotion.EmotionManager;
import com.febers.uestc_bbs.view.emotion.EmotionTranslator;
import com.febers.uestc_bbs.view.emotion.emoticons.EmoticonManager;
import com.febers.uestc_bbs.view.emotion.emoticons.gif.EmojiAdapter;
import com.febers.uestc_bbs.view.emotion.listeners.IStickerSelectedListener;
import com.febers.uestc_bbs.view.emotion.sticker.StickerAdapter;
import com.febers.uestc_bbs.view.emotion.sticker.StickerCategory;
import com.febers.uestc_bbs.view.emotion.sticker.StickerItem;
import com.febers.uestc_bbs.view.emotion.sticker.StickerManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import static com.febers.uestc_bbs.utils.LogUtilsKt.log;


/**
 * 表情控件的ViewPager适配器(emoji + 贴图)
 */

public class EmotionViewPagerAdapter extends PagerAdapter {

    private int mPageCount = 0;
    private int mTabPosi = 0;

    private int mEmotionLayoutWidth;
    private int mEmotionLayoutHeight;

    private IStickerSelectedListener listener;
    private EditText mMessageEditText;


    void attachEditText(EditText messageEditText) {
        mMessageEditText = messageEditText;
    }

    EmotionViewPagerAdapter(int emotionLayoutWidth, int emotionLayoutHeight, int tabPosi, IStickerSelectedListener listener) {
        mEmotionLayoutWidth = emotionLayoutWidth;
        mEmotionLayoutHeight = emotionLayoutHeight;
        mTabPosi = tabPosi;
//        if (mTabPosi == 0) { // 默认的 emoji 或者 gif emoji
            mPageCount = (int) Math.ceil(EmoticonManager.getInstance().getDisplayCount() / (float) EmotionManager.getInstance().getEmojiPerPage());
//        } else { //贴图表情
//            System.out.println(StickerManager.getInstance().getStickerCategories().get(mTabPosi - 1).getStickers().size() + "??????????");
//            mPageCount = (int) Math.ceil(StickerManager.getInstance().getStickerCategories().get(mTabPosi - 1).getStickers().size() / (float) EmotionManager.getInstance().getStickerPerPage());
//        }
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mPageCount == 0 ? 1 : mPageCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        RelativeLayout rl = new RelativeLayout(context);
        rl.setGravity(Gravity.CENTER);
        GridView gridView = new GridView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        gridView.setLayoutParams(params);
        gridView.setGravity(Gravity.CENTER);

        gridView.setTag(position);//标记自己是第几页、
        //@Febers 注释以下代码
//        if (mTabPosi == 0) {
            gridView.setOnItemClickListener(emojiListener);
            gridView.setAdapter(new EmojiAdapter(context, mEmotionLayoutWidth, mEmotionLayoutHeight, position * EmotionManager.getInstance().getEmojiPerPage()));
            gridView.setNumColumns(EmotionManager.getInstance().getEmojiColumn());
//        } else {
//            StickerCategory category = StickerManager.getInstance().getCategory(StickerManager.getInstance().getStickerCategories().get(mTabPosi - 1).getName());
//            gridView.setOnItemClickListener(stickerListener);
//            gridView.setAdapter(new StickerAdapter(mContext, category, mEmotionLayoutWidth, mEmotionLayoutHeight, position * EmotionManager.getInstance().getStickerPerPage()));
//            gridView.setNumColumns(EmotionManager.getInstance().getStickerColumn());
//        }

        rl.addView(gridView);
        container.addView(rl);
        return rl;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private AdapterView.OnItemClickListener emojiListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int index = position + (Integer) parent.getTag() * EmotionManager.getInstance().getEmojiPerPage();
            int count = EmoticonManager.getInstance().getDisplayCount();
            if (position == EmotionManager.getInstance().getEmojiPerPage() || index >= count) {
                onEmojiSelected("/DEL");
            } else {
                String text = EmoticonManager.getInstance().getDisplayText((int) id);
                if (!TextUtils.isEmpty(text)) {
                    onEmojiSelected(text);
                }
            }
        }
    };
    private AdapterView.OnItemClickListener stickerListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            StickerCategory category = StickerManager.getInstance().getStickerCategories().get(mTabPosi - 1);
            List<StickerItem> stickers = category.getStickers();
            int index;
            if (mTabPosi == 1) {
                index = position + (Integer) parent.getTag() * EmotionManager.getInstance().getStickerPerPage() - 1;
            } else {
                index = position + (Integer) parent.getTag() * EmotionManager.getInstance().getStickerPerPage();
            }
            if (index >= stickers.size()) {
                Log.i("PandaQ", "index " + index + " larger than size " + stickers.size());
                return;
            }
            if (listener != null) {
                if (index < 0) {
                    listener.onCustomAdd();
                } else {
                    StickerItem sticker = stickers.get(index);
                    if (sticker.getSourcePath() == null) {
                        return;
                    }
                    listener.onStickerSelected(sticker.getTitle(), sticker.getSourcePath());
                }
            }
        }
    };

    private void onEmojiSelected(String key) {
        if (mMessageEditText == null)
            return;
        Editable editable = mMessageEditText.getText();
        if (key.equals("/DEL")) {
            mMessageEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = mMessageEditText.getSelectionStart();
            int end = mMessageEditText.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            editable.replace(start, end, key);
            int editEnd = mMessageEditText.getSelectionEnd();
            EmotionTranslator.getInstance().replaceEmoticons(editable, 0, editable.toString().length());
            mMessageEditText.setSelection(editEnd);
        }
    }

    void setTabPosi(int tabPosi) {
        mTabPosi = tabPosi;
//        if (mTabPosi == 0) { // 默认的 emoji 或者 gif emoji
            mPageCount = (int) Math.ceil(EmoticonManager.getInstance().getDisplayCount() / (float) EmotionManager.getInstance().getEmojiPerPage());
//        } else { //贴图表情
//            mPageCount = (int) Math.ceil(StickerManager.getInstance().getStickerCategories().get(mTabPosi - 1).getStickers().size() / (float) EmotionManager.getInstance().getStickerPerPage());
//        }
    }
}
