package com.febers.uestc_bbs.module.setting.refresh_style

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.view.adapter.RefreshStyleAdapter
import com.febers.uestc_bbs.view.custom.IndicatorView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RefreshStyleFragment: BottomSheetDialogFragment() {

    private lateinit var styleAdapter: RefreshStyleAdapter
    private lateinit var tvTitle: TextView
    private lateinit var  btnChooseStyle: Button
    private lateinit var viewPager: ViewPager
    private lateinit var indicator: IndicatorView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_refresh_style, container)

        tvTitle = view.findViewById(R.id.text_view_choose_refresh)
        btnChooseStyle = view.findViewById(R.id.btn_choose_refresh_style)
        viewPager = view.findViewById(R.id.view_pager_refresh_style)
        indicator = view.findViewById(R.id.indicator_refresh_style)
        return view
    }

    /**
     * 设置初始状态为展开
     * 参考：Android开发之BottomsheetDialogFragment的使用
     * https://blog.csdn.net/klxh2009/article/details/80393245
     */
    override fun onStart() {
        super.onStart()
        val bottomSheet = (dialog as BottomSheetDialog).delegate.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        initView()
    }

    private fun initView() {
//        tvTitle.setBackgroundColor(ThemeHelper.getColorPrimary())
//        tvTitle.setTextColor(ThemeHelper.getRefreshTextColor())

        var styleCode by PreferenceUtils(context!!, REFRESH_HEADER_CODE, 0)
        btnChooseStyle.text = "已选择"

        /*------------------- 初始化监听事件 ----------------------*/
        btnChooseStyle.setOnClickListener {
            if (styleCode != viewPager.currentItem) {
                styleCode = viewPager.currentItem
                btnChooseStyle.text = "已选择"
            }
        }

        fun onViewPagerChange(position: Int) {
            indicator.setCurIndex(position)
            if (position != styleCode) {
                btnChooseStyle.text = "选择"
            } else {
                btnChooseStyle.text = "已选择"
            }
        }
        /*-----------------监听事件初始化结束----------------------*/


        styleAdapter = RefreshStyleAdapter(childFragmentManager)
        viewPager.apply {
            offscreenPageLimit = HEAD_COUNT
            adapter = styleAdapter
            currentItem = styleCode
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    onViewPagerChange(position)
                }
            })
        }

        indicator.apply {
            setMaxSize(HEAD_COUNT)
            setCurIndex(styleCode)
        }
    }
}