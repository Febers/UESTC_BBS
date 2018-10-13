/*
 * Created by Febers at 18-8-14 下午2:31.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 下午2:31.
 */

package com.febers.uestc_bbs.dao

import android.content.Context
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.base.SP_USERS
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.google.gson.Gson

object UserStore {

    fun save(uid: Int, userSimple: UserSimpleBean) {
        context().getSharedPreferences(SP_USERS, 0).edit().apply {
            val gson = Gson()
            val json: String = gson.toJson(userSimple)
            putString(uid.toString(), json)
            apply()
        }
    }

    fun get(uid: Int) : UserSimpleBean {
        try {
            with(context().getSharedPreferences(SP_USERS, 0)) {
                val gson = Gson()
                val json: String = this.getString(uid.toString(), "")
                return gson.fromJson(json, UserSimpleBean::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return UserSimpleBean()
        }
    }

    fun delete(uid: String) {
        context().getSharedPreferences(SP_USERS, 0).edit().apply {
            putString(uid, "")
            apply()
        }
    }

    private fun context(): Context = MyApplication.context()
}