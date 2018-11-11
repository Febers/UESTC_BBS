package com.febers.uestc_bbs.module.setting.refresh_style

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.view.helper.initAttrAndBehavior
import kotlinx.android.synthetic.main.item_layout_refresh_style.*

class RefreshStyleItemView: BaseFragment() {

    private var position = 0

    override fun setContentView(): Int {
        arguments?.let {
            position = it.getInt(HEAD_POSITION)
        }
        return R.layout.item_layout_refresh_style
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        val helper = RefreshViewHelper(context!!)
        refresh_layout_style.apply {
            setRefreshHeader(helper.getHeader(position))
            initAttrAndBehavior()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int): RefreshStyleItemView = RefreshStyleItemView().apply {
            arguments = Bundle().apply {
                putInt(HEAD_POSITION, position)
            }
        }
    }
}