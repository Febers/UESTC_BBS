package com.febers.uestc_bbs.module.theme

import android.util.Log.i
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_theme.*

class ThemeActivity : BaseActivity() {

    private var colorValue: Int = ThemeHelper.getColor(AppColor.COLOR_PRIMARY)

    override fun setView(): Int {
        return R.layout.activity_theme
    }

    override fun setToolbar(): Toolbar? {
        return toolbar_theme
    }

    override fun initView() {
        color_picker.apply {
            addSVBar(color_picker_sv)
            oldCenterColor = ThemeHelper.getColor(AppColor.COLOR_PRIMARY)
            setNewCenterColor(ThemeHelper.getColor(AppColor.COLOR_PRIMARY))
            setOnColorChangedListener {
                btn_choose_theme.setTextColor(it)
                i("theme:", it.toString())
                colorValue = it
            }
        }
        btn_choose_theme.setOnClickListener {
            reChooseTheme(colorValue)
        }
    }

    private fun reChooseTheme(colorPrimary: Int) {
        ThemeHelper.setTheme(colorPrimary, colorPrimary)
        color_picker.oldCenterColor = colorPrimary
    }
}
