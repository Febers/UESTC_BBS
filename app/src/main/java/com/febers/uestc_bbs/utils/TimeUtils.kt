/*
 * Created by Febers at 18-8-17 上午4:54.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 上午4:54.
 */

package com.febers.uestc_bbs.utils

import android.os.SystemClock
import android.util.Log.i
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    /*
     * 将时间戳转换为时间
     */
    fun stampToDate(s: String?): String {
        if (s == null) {
            return ""
        }
        val lt = s.toLong()
        val nt = System.currentTimeMillis()
        var dt = nt - lt
        dt = dt/(1000*60)   //转换成分钟
        if (dt<60) {
            return dt.toString()+"min"
        }
        if (dt<3600) {
            return (dt/60).toString()+"h"
        }
        return (dt/(3600*24)).toString()+"d"
    }
}