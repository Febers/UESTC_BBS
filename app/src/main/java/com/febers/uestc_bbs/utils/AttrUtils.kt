package com.febers.uestc_bbs.utils

import android.content.Context
import android.util.Log.i
import android.util.TypedValue

object AttrUtils {

    /**
     * 获取attrs中定义的颜色属性的int值
     *
     * @param context
     * @param attr
     * @return 不同主题下的attr值一般不一样
     */
    fun getColor(context: Context, attr: Int) : Int {
        val typeValue = TypedValue()
        context.theme.resolveAttribute(attr, typeValue, true)
//        i("Attr", "${typeValue.data}")
        return typeValue.data
    }

}