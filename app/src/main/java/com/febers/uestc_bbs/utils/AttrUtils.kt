package com.febers.uestc_bbs.utils

import android.content.Context
import android.util.Log.i
import android.util.TypedValue

object AttrUtils {
    fun getColor(context: Context, attr: Int) : Int {
        val typeValue = TypedValue()
        context.theme.resolveAttribute(attr, typeValue, true)
        i("Attr", "${typeValue.data}")
        return typeValue.data
}
}