package com.febers.uestc_bbs.module.theme

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.ThemeChangedEvent
import com.febers.uestc_bbs.entity.ThemeItemBean
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.utils.postEvent
import com.febers.uestc_bbs.view.adapter.ThemeGridViewAdapter
import kotlinx.android.synthetic.main.activity_theme.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*

class ThemeActivity : BaseActivity() {

    private val themeItemList: MutableList<ThemeItemBean> = ArrayList()
    private lateinit var themeGridViewAdapter: ThemeGridViewAdapter

    private var colorValue: Int = ThemeManager.colorAccent()
    private var tempColorValue = colorValue

    override fun setView(): Int = R.layout.activity_theme

    override fun setToolbar(): Toolbar? = toolbar_common

    override fun setTitle(): String? = getString(R.string.choose_theme)

    override fun initView() {
    }

    override fun afterCreated() {
        initGridView()
        color_picker.apply {
            addSVBar(color_picker_sv)
            color = colorValue
            oldCenterColor = colorValue
            setOnColorChangedListener {
                btn_choose_theme.setTextColor(it)
                tempColorValue = it
            }
        }
        var colorDark by PreferenceUtils(this, COLOR_PRIMARY_DARK, true)
        var tempColorDark = colorDark
//        check_box_theme_color_dark.isChecked = colorDark
//        check_box_theme_color_dark.setOnCheckedChangeListener { compoundButton, b ->
//            tempColorDark = b
//        }
        btn_choose_theme.setTextColor(ThemeManager.colorAccent())
        btn_choose_theme.setOnClickListener {
            if (colorValue != tempColorValue || colorDark != tempColorDark) {
                colorValue = tempColorValue
                colorDark = tempColorDark
                reChooseTheme(colorValue)
            }
        }
    }

    private fun initGridView() {
        initData()
        themeGridViewAdapter = ThemeGridViewAdapter(mContext, themeItemList, object : ThemeGridViewAdapter.OnItemClickListener {
            override fun onClick(position: Int) {
                color_picker.color = themeItemList[position].color
            }
        })
        grid_view_theme.adapter = themeGridViewAdapter
    }

    /**
     * 颜色来自 https://materialuicolors.co
     */
    private fun initData() {
        themeItemList.add(ThemeItemBean(Color.parseColor("#F44336"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#E91E63"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#9C27B0"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#673AB7"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#3F51B5"), false))

        themeItemList.add(ThemeItemBean(Color.parseColor("#2196F3"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#03A9F4"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#00BCD4"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#009688"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#4CAF50"), false))

        themeItemList.add(ThemeItemBean(Color.parseColor("#8BC34A"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#CDDC39"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#FFEB3B"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#FFC107"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#FF9800"), false))

        themeItemList.add(ThemeItemBean(Color.parseColor("#FF5722"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#795548"), false))
        //themeItemList.add(ThemeItemBean(Color.parseColor("#9E9E9E"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#607D8B"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#000000"), false))
        themeItemList.add(ThemeItemBean(Color.parseColor("#FFFFFF"), false))
    }

    private fun reChooseTheme(colorAccent: Int) {
        ThemeManager.setTheme(this, colorAccent)
        color_picker.oldCenterColor = colorAccent
        postEvent(ThemeChangedEvent(dayNightChanged = false))
        changeDrawableHint()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeDrawableHint() {
        resources.getDrawable(R.drawable.xic_list_gray, null).setTint(ThemeManager.colorAccent())
        resources.getDrawable(R.drawable.xic_user_small, null).setTint(ThemeManager.colorAccent())
    }
}
