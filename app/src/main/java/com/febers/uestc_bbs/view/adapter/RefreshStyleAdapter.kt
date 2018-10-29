package com.febers.uestc_bbs.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.febers.uestc_bbs.module.setting.HEAD_COUNT
import com.febers.uestc_bbs.module.setting.RefreshStyleItemView

class RefreshStyleAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return RefreshStyleItemView.newInstance(position)
    }

    override fun getCount(): Int {
        return HEAD_COUNT
    }
}
