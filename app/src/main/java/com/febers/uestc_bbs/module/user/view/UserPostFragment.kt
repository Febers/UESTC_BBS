package com.febers.uestc_bbs.module.user.view

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BasePopFragment
import kotlinx.android.synthetic.main.fragment_user_post.*

class UserPostFragment: BasePopFragment() {

    override fun setToolbar(): Toolbar? {
        return toolbar_user_post
    }

    override fun setContentView(): Int {
        return R.layout.fragment_user_post
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

    }
}