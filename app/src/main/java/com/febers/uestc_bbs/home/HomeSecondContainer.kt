package com.febers.uestc_bbs.home

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.module.more.BlockFragment

class HomeSecondContainer : BaseFragment() {
    override fun setView(): Int {
        return R.layout.container_home_second
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (findChildFragment(BlockFragment::class.java) == null) {
            loadRootFragment(R.id.home_second_container, BlockFragment())
        }
    }
}