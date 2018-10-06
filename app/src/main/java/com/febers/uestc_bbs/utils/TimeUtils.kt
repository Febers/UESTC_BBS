/*
 * Created by Febers at 18-8-17 上午4:54.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 上午4:54.
 */

package com.febers.uestc_bbs.utils

import android.os.SystemClock
import android.util.Log.i
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    /*
     * 将时间戳转换为时间
     */
    fun stampChange(s: String?): String {
        if (s == null) return ""

        val lt = s.toLong()
        val nt = System.currentTimeMillis()
        var dt = nt - lt
        if (dt/1000 < 60) return (dt/1000).toString() + "s"
        dt /= (1000 * 60)   //转换成分钟
        //23min
        if (dt<60) {
            return dt.toString()+"min"
        }
        //78min
        if (dt<60*24) {
            return (dt/60).toString()+"h"
        }
        //86401min
        if (dt<60*24*30) {
            return (dt/(60*24)).toString()+"day"
        }
        return stampToDate(lt)
    }

    private fun stampToDate(s: Long): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("English"))
            sdf.format(Date(s))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}