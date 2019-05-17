package com.febers.uestc_bbs.module.setting

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.IconItemBean
import com.febers.uestc_bbs.utils.HintUtils
import com.febers.uestc_bbs.utils.IconUtils
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.view.adapter.IconGridViewAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val ICON_CODE = "icon_code"
class IconFragment: BottomSheetDialogFragment() {

    private val iconList: MutableList<IconItemBean> = ArrayList()

    private var nowCode: Int = 0
    private var tempCode: Int = 0

    private lateinit var btnEnter: Button
    private lateinit var iconGridView: GridView
    private lateinit var iconAdapter: IconGridViewAdapter
    private var behavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_icon, container)
        iconGridView = view.findViewById(R.id.grid_view_choose_icon)
        btnEnter = view.findViewById(R.id.btn_choose_icon_enter)
        return view
    }

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        var iconCode: Int by PreferenceUtils(MyApp.context(), ICON_CODE, 0)
        nowCode = iconCode
        tempCode = nowCode
        if (iconCode > 14) {
            iconCode = 0
            tempCode = 0
        }
        btnEnter.post {   //耗时操作
            val bottomSheet = (dialog as BottomSheetDialog).delegate.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (bottomSheet != null) {
                behavior = BottomSheetBehavior.from(bottomSheet)
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }

            iconList.addAll(getIconData())
            iconList[nowCode].isChoose = true
            iconAdapter = IconGridViewAdapter(context!!, iconList,
                    clickListener = object : IconGridViewAdapter.OnItemClickListener {
                        override fun onClick(position: Int) {
                            onIconItemClick(position)
                        }
                    })
            iconGridView.adapter = iconAdapter

            btnEnter.setOnClickListener {
                if (tempCode != iconCode) {
                    IconUtils.changeIcon(iconCode = tempCode, newComponentName = getActivityPath(tempCode), allComponentName = getAllActivityPath())
                    HintUtils.show(activity, "桌面图标更换成功，稍等片刻后生效")
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    private fun onIconItemClick(position: Int) {
        val lastCode = tempCode
        tempCode = position
        iconList[lastCode].isChoose = false
        iconList[position].isChoose = true
        iconAdapter.notifyDataSetChanged()
    }

    private fun getIconData(): List<IconItemBean> = arrayListOf(
            IconItemBean(R.drawable.lic_blue_square, false), IconItemBean(R.drawable.lic_blue_circle, false),
            IconItemBean(R.drawable.lic_red_square, false), IconItemBean(R.drawable.lic_red_circle, false),
            IconItemBean(R.drawable.lic_pink_square, false), IconItemBean(R.drawable.lic_pink_circle, false),
            IconItemBean(R.drawable.lic_green_square, false), IconItemBean(R.drawable.lic_green_circle, false),
            IconItemBean(R.drawable.lic_cyan_square, false), IconItemBean(R.drawable.lic_cyan_circle, false),
            IconItemBean(R.drawable.lic_teal_square, false), IconItemBean(R.drawable.lic_teal_circle, false),
            IconItemBean(R.drawable.lic_gray_square, false), IconItemBean(R.drawable.lic_gray_circle, false))

    private fun getActivityPath(code: Int): String {
        return when(code) {
            0 -> "com.febers.uestc_bbs.home.SplashActivity"
            1 -> "com.febers.uestc_bbs.icon_blue_circle"
            2 -> "com.febers.uestc_bbs.icon_red_square"
            3 -> "com.febers.uestc_bbs.icon_red_circle"
            4 -> "com.febers.uestc_bbs.icon_pink_square"
            5 -> "com.febers.uestc_bbs.icon_pink_circle"
            6 -> "com.febers.uestc_bbs.icon_green_square"
            7 -> "com.febers.uestc_bbs.icon_green_circle"
            8 -> "com.febers.uestc_bbs.icon_cyan_square"
            9 -> "com.febers.uestc_bbs.icon_cyan_circle"
            10 -> "com.febers.uestc_bbs.icon_teal_square"
            11 -> "com.febers.uestc_bbs.icon_teal_circle"
            12 -> "com.febers.uestc_bbs.icon_gray_square"
            13 -> "com.febers.uestc_bbs.icon_gray_circle"
            else -> "com.febers.uestc_bbs.home.SplashActivity"
        }
    }

    private fun getAllActivityPath(): Array<String> = arrayOf(
            "com.febers.uestc_bbs.home.SplashActivity",
            "com.febers.uestc_bbs.icon_blue_circle",
            "com.febers.uestc_bbs.icon_red_square",
            "com.febers.uestc_bbs.icon_red_circle",
            "com.febers.uestc_bbs.icon_pink_square",
            "com.febers.uestc_bbs.icon_pink_circle",
            "com.febers.uestc_bbs.icon_green_square",
            "com.febers.uestc_bbs.icon_green_circle",
            "com.febers.uestc_bbs.icon_cyan_square",
            "com.febers.uestc_bbs.icon_cyan_circle",
            "com.febers.uestc_bbs.icon_teal_square",
            "com.febers.uestc_bbs.icon_teal_circle",
            "com.febers.uestc_bbs.icon_gray_square",
            "com.febers.uestc_bbs.icon_gray_circle"
    )

    override fun onStop() {
        super.onStop()
        iconList.clear()
    }
}