package com.febers.uestc_bbs.module.post.view.edit

import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.FID
import com.febers.uestc_bbs.base.TITLE
import com.febers.uestc_bbs.module.more.BlockFragment
import com.febers.uestc_bbs.lib.emotion.EmotionTranslator

class PostEditActivity : BaseActivity() {

    private var fid = 0
    private var title = ""

    override fun setView(): Int {
        fid = intent.getIntExtra(FID, 0)
        title = intent.getStringExtra(TITLE)
        return R.layout.activity_post_edit
    }

    //如果fid不为0.说明已经选择了板块
    override fun initView() {
        if (fid == 0) {
            loadRootFragment(R.id.post_edit_container, BlockFragment.newInstance(true))
        } else {
            loadRootFragment(R.id.post_edit_container, PostEditFragment.newInstance(fid, title))
        }
    }

    /**
     * 如果表情键盘正在显示，拦截返回键事件
     * 但是还要将emotionViewVisible的状态更新
     * 否则将处理逻辑交给上层
     */
    override fun onBackPressed() {
        if (!MyApp.emotionViewVisible) {
            super.onBackPressed()
        }
        MyApp.emotionViewVisible = false
    }

    override fun onResume() {
        super.onResume()
        EmotionTranslator.getInstance().resumeGif(localClassName)
    }

    override fun onPause() {
        super.onPause()
        EmotionTranslator.getInstance().pauseGif()
    }

    override fun onDestroy() {
        super.onDestroy()
        EmotionTranslator.getInstance().clearGif(localClassName)
    }
}
