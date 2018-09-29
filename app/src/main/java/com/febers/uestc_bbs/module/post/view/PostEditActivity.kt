package com.febers.uestc_bbs.module.post.view

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseSwipeActivity
import com.febers.uestc_bbs.module.more.BlockFragment

class PostEditActivity : BaseSwipeActivity() {

    override fun setView(): Int {
        return R.layout.activity_post_edit
    }

    override fun initView() {
        loadRootFragment(R.id.post_edit_container, BlockFragment.newInstance(true))
    }
}
