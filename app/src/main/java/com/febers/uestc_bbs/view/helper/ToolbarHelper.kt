package com.febers.uestc_bbs.view.helper

import android.view.View
import androidx.appcompat.widget.Toolbar
import java.lang.reflect.Field

object ToolbarHelper {

    fun getNavView(toolbar: Toolbar): View? {
        return try {
            var mNavButtonViewField: Field? = null
            mNavButtonViewField = try {
                toolbar.javaClass.getDeclaredField("mNavButtonView")
            } catch (e: NoSuchFieldException) {
                toolbar.javaClass.superclass?.getDeclaredField("mNavButtonView")
            }
            mNavButtonViewField?.isAccessible = true
            mNavButtonViewField?.get(toolbar) as? View
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}