package com.febers.uestc_bbs.io

import android.content.Context
import com.febers.uestc_bbs.base.QUERY_HISTORY
import com.febers.uestc_bbs.utils.PreferenceUtils


object SearchStore {

    private const val searchKeywordSize = 12

    fun save(context: Context, keyword: String, list: MutableList<String>) {
        when {
            list.contains(keyword) -> {
                list.remove(keyword)
                list.add(0, keyword)
            }
            list.size < searchKeywordSize -> {
                list.add(0, keyword)
            }
            else -> {
                list.removeAt(list.size-1)
                list.add(0, keyword)
            }
        }
        var history: String by PreferenceUtils(context, QUERY_HISTORY, "")
        history = list.joinToString("♪")
    }

    fun get(context: Context): List<String> {
        val history: String by PreferenceUtils(context, QUERY_HISTORY, "")
        if (history.isEmpty()) return emptyList()
        return history.split("♪")
    }

    fun clear(context: Context, list: MutableList<String>) {
        list.clear()
        var history: String by PreferenceUtils(context, QUERY_HISTORY, "")
        history = ""
    }
}