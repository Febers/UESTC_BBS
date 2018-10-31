/*
 * Created by Febers at 18-8-14 下午2:31.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 下午2:31.
 */

package com.febers.uestc_bbs.dao

import android.content.Context
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.base.SP_NOW_UID
import com.febers.uestc_bbs.base.SP_USERS
import com.febers.uestc_bbs.base.SP_USER_IDS
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.google.gson.Gson

/**
 * 当用户登录的时候，获取sp中user_uid的字符串
 * 其中的规则应该为:字符串由uid和,组成，如@123@132@321
 * 使用的时候将其变成数组
 * 同时sp应该存储当前使用的uid(now_uid)
 */
object UserStore {

    /**
     * 此方法只会在登录成功的时候调用，登录成功之后，保存于User的原始资料
     * 然后添加新的uid到user_ids的字符串中，然后将uid设置为nowUId
     */
    fun add(uid: Int, userSimple: UserSimpleBean) {
        context().getSharedPreferences(SP_USERS, 0).edit().apply {
            val gson = Gson()
            val json: String = gson.toJson(userSimple)
            putString(uid.toString(), json)
            apply()
        }
        var userIds by PreferenceUtils(context(), SP_USER_IDS, "")
        userIds = "$userIds@$uid"
        var nowUid by PreferenceUtils(context(), SP_NOW_UID, 0)
        nowUid = uid
    }

    /**
     * 获取当前正在使用的User，一般由App初始化时调用
     */
    fun  getNowUser(): UserSimpleBean {
        return getUser(getNowUid())
    }

    fun getNowUid(): Int {
        val nowUid by PreferenceUtils(context(), SP_NOW_UID, 0)
        return nowUid
    }

    fun setNowUid(uid: Int) {
        var nowUid by PreferenceUtils(context(), SP_NOW_UID, 0)
        nowUid = uid
    }

    /**
     * 通过uid获取用户，调用的地方有: 设置界面根据获取的uid数组，一个一个获取uid
     * 然后通过uid一个一个获取有效用户列表
     */
    fun getUser(uid: Int) : UserSimpleBean {
        try {
            with(context().getSharedPreferences(SP_USERS, 0)) {
                val gson = Gson()
                val json: String? = this.getString(uid.toString(), "")
                return gson.fromJson(json, UserSimpleBean::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return UserSimpleBean()
        }
    }

    /**
     * 返回本机上登录的所有用户
     * 解析存储的字符串
     */
    fun getAllUser(): List<UserSimpleBean> {
        try {
            val userIds by PreferenceUtils(context(), SP_USER_IDS, "")
            if (userIds.isEmpty()) return emptyList()
            val uidList = userIds.removePrefix("@").split("@")
            val userList: MutableList<UserSimpleBean> = ArrayList(uidList.size)
            uidList.forEach {
                userList.add(getUser(it.toInt()))
            }
            return userList
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    /**
     * 删除用户，首先在user_ids清除uid
     */
    fun delete(uid: Int) {
        var userIds by PreferenceUtils(context(), SP_USER_IDS, "")
        userIds = userIds.replace("@$uid", "")
        context().getSharedPreferences(SP_USERS, 0).edit().apply {
            putString(uid.toString(), "")
            apply()
        }
    }

    private fun context(): Context = MyApp.context()
}