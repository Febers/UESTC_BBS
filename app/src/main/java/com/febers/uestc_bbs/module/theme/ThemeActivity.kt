package com.febers.uestc_bbs.module.theme

import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.ThemeChangedEvent
import com.febers.uestc_bbs.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_theme.*
import org.greenrobot.eventbus.EventBus

class ThemeActivity : BaseActivity() {

    private var colorValue: Int = ThemeHelper.getColorPrimary()

    private var tempColorValue = colorValue

    override fun setView(): Int = R.layout.activity_theme

    override fun setToolbar(): Toolbar? = toolbar_theme

    override fun setTitle(): String? = getString(R.string.choose_theme)

    override fun initView() {
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
        check_box_theme_color_dark.isChecked = colorDark
        check_box_theme_color_dark.setOnCheckedChangeListener { compoundButton, b ->
            tempColorDark = b
        }
        btn_choose_theme.setOnClickListener {
            if (colorValue != tempColorValue || colorDark !=tempColorDark) {
                colorValue = tempColorValue
                colorDark = tempColorDark
                reChooseTheme(colorValue)
            }
        }
    }

    private fun reChooseTheme(colorPrimary: Int) {
        ThemeHelper.setTheme(this, colorPrimary, colorPrimary)
        color_picker.oldCenterColor = colorPrimary
        EventBus.getDefault().post(BaseEvent(BaseCode.SUCCESS, ThemeChangedEvent(dayNightChanged = false)))
    }
}
