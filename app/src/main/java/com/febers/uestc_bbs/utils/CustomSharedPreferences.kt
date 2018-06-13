/*
 * Created by Febers at 18-6-12 下午7:42.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-7 下午5:36.
 */

package com.febers.uestc_bbs.utils

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.febers.uestc_bbs.base.BaseApplication

class CustomSharedPreferences private constructor() {

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.context())
    }

    private object SingletonHolder {
        val INSTANCE = CustomSharedPreferences()
    }

    fun put(key: String, value: String): Boolean {
        return sharedPreferences.edit().putString(key, value).commit()
    }

    fun put(key: String, value: Int): Boolean {
        return sharedPreferences.edit().putInt(key, value).commit()
    }

    fun put(key: String, value: Boolean): Boolean {
        return sharedPreferences.edit().putBoolean(key, value).commit()
    }

    fun put(key: String, value: Float): Boolean {
        return sharedPreferences.edit().putFloat(key, value).commit()
    }

    fun put(key: String, value: Long): Boolean {
        return sharedPreferences.edit().putLong(key, value).commit()
    }

    fun put(key: String, values: Set<String>): Boolean {
        return sharedPreferences.edit().putStringSet(key, values).commit()
    }

    fun put(key: String, value: Any): Boolean {
        return if (value is String) {
            put(key, value)
        } else if (value is Boolean) {
            put(key, value)
        } else if (value is Int) {
            put(key, value)
        } else if (value is Float) {
            put(key, value)
        } else if (value is Long) {
            put(key, value)
        } else if (value is Set<*>) {
            put(key, value)
        } else {
            throw UnsupportedOperationException("This value type " + value.javaClass.toString() + " can't put SharedPreferences ")
        }
    }

    operator fun get(key: String, defValue: String): String? {
        return sharedPreferences.getString(key, defValue)
    }

    operator fun get(key: String, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    operator fun get(key: String, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    operator fun get(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    operator fun get(key: String, defValue: Float): Float {
        return sharedPreferences.getFloat(key, defValue)
    }

    operator fun get(key: String, defValue: Set<String>): Set<*>? {
        return sharedPreferences.getStringSet(key, defValue)
    }

    operator fun get(key: String, defValue: Any): Any? {
        return if (defValue is String) {
            get(key, defValue)
        } else if (defValue is Boolean) {
            get(key, defValue)
        } else if (defValue is Int) {
            get(key, defValue)
        } else if (defValue is Float) {
            get(key, defValue)
        } else if (defValue is Long) {
            get(key, defValue)
        } else if (defValue is Set<*>) {
            get(key, defValue)
        } else {
            throw UnsupportedOperationException("This value type " + defValue.javaClass.toString() + " can't get in SharedPreferences ")
        }
    }

    fun remove(key: String): Boolean {
        return sharedPreferences.edit().remove(key).commit()
    }

    fun clearAll(): Boolean {
        return sharedPreferences.edit().clear().commit()
    }

    companion object {

        val instance: CustomSharedPreferences
            get() = SingletonHolder.INSTANCE
    }
}
