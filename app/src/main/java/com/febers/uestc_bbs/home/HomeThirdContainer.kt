package com.febers.uestc_bbs.home

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.module.message.view.MsgPagerFragment

class HomeThirdContainer : BaseFragment() {
    override fun setContentView(): Int {
        return R.layout.container_home_third
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (findChildFragment(MsgPagerFragment::class.java) == null) {
            loadRootFragment(R.id.home_third_container, MsgPagerFragment())
        }
    }
}