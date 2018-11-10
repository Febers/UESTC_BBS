package com.febers.uestc_bbs.module.post.view.edit

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.FID
import com.febers.uestc_bbs.base.TITLE
import com.febers.uestc_bbs.module.more.BlockFragment

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

}
