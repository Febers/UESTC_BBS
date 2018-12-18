package com.febers.uestc_bbs.module.setting.refresh_style

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.view.helper.initAttrAndBehavior
import kotlinx.android.synthetic.main.item_layout_refresh_style.*

class RefreshStyleItemView: Fragment() {

    private var position = 0
    private var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(HEAD_POSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.item_layout_refresh_style, null)
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isInit && isVisibleToUser) {
            refresh_layout_style.autoRefresh()
        }
    }

    override fun onResume() {
        super.onResume()
        val helper = RefreshViewHelper(context!!)
        refresh_layout_style.apply {
            setRefreshHeader(helper.getHeader(position))
            setPrimaryColors(ThemeHelper.getColorPrimary(), ThemeHelper.getRefreshTextColor())
            setEnableLoadMore(false)
        }
        isInit = true
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