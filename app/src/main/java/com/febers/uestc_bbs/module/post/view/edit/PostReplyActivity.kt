package com.febers.uestc_bbs.module.post.view.edit

import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_post_reply.*

class PostReplyActivity: BaseActivity() {

    override fun setView(): Int {
        return R.layout.activity_post_reply
    }

    override fun setToolbar(): Toolbar? = toolbar_post_reply

    override fun initView() {

    }
}