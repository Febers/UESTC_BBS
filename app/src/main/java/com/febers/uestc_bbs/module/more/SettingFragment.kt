package com.febers.uestc_bbs.module.more

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseSwipeFragment
import com.febers.uestc_bbs.base.SHOW_BOTTOM_BAR_ON_DESTROY
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseSwipeFragment() {

    override fun setToolbar(): Toolbar? {
        return toolbar_setting
    }

    override fun setContentView(): Int {
        return R.layout.fragment_setting
    }

    companion object {
        @JvmStatic
        fun newInstance(showBottomBarOnDestroy: Boolean) =
                SettingFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }
}