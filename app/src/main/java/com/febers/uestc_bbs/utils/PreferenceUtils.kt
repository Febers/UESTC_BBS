/*
 * Created by Febers at 18-6-12 下午7:16.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午7:16.
 */

package com.febers.uestc_bbs.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log.d
import org.jetbrains.anko.defaultSharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 自定义的封装SharedPreferences工具类
 * 使用方法：
 * var i by PreferenceUtils.get("name", "default_value")
 * 通过赋值的方式简单改变i的值:
 * i = "new_value"
 */
class PreferenceUtils<T>(val context: Context, val name: String, val default: T): ReadWriteProperty<Any?, T> {

    val prefs: SharedPreferences by lazy { context.defaultSharedPreferences }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun <T> findPreference(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is Int -> getInt(name, default)
            is String -> getString(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }
        return@with res as T
    }

    private fun <T> putPreference(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is Int -> putInt(name, value)
            is String -> putString(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }.apply()
    }
}