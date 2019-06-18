package com.febers.uestc_bbs.io

import android.content.Context
import com.febers.uestc_bbs.base.QUERY_HISTORY
import com.febers.uestc_bbs.utils.PreferenceUtils

object QueryStore {

    /**
     * 保存搜索历史记录
     * 形如 his,hiss,history,
     */
    fun save(context: Context, keyword: String) {
        var history: String by PreferenceUtils(context, QUERY_HISTORY, "")
        if (history.count { it == ',' } > 3) {
            history = history.substring(history.indexOf(',')+1)
        }
        history = "$keyword,$history"
    }

    fun getQueryHistory(context: Context): List<String> {
        var history: String by PreferenceUtils(context, QUERY_HISTORY, "")
        val stringBuilder = StringBuilder(history)
        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
        return stringBuilder.split(',')
    }
}