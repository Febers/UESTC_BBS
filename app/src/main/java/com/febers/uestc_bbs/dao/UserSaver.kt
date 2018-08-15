/*
 * Created by Febers at 18-8-14 下午2:31.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 下午2:31.
 */

package com.febers.uestc_bbs.dao

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.entity.UserBean


object UserSaver {

    fun save(uid: String, user: UserBean) {
        val edit = context().getSharedPreferences(uid, 0).edit()
        edit.putBoolean(getString(R.string.sp_user_valid), user.valid)
        edit.putString(getString(R.string.sp_user_name), user.name)
        edit.putString(getString(R.string.sp_user_uid), user.uid)
        edit.putString(getString(R.string.sp_user_title), user.title)
        edit.putString(getString(R.string.sp_user_gender), user.gender)
        edit.putString(getString(R.string.sp_user_token), user.token)
        edit.putString(getString(R.string.sp_user_score), user.score)
        edit.putString(getString(R.string.sp_user_secret), user.secrete)
        edit.putString(getString(R.string.sp_user_avatar), user.avatar)
        edit.putString(getString(R.string.sp_user_credit_data), user.credits)
        edit.putString(getString(R.string.sp_user_extcredit_data), user.extcredits2)
        edit.putString(getString(R.string.sp_user_mobile), user.mobile)
        edit.putString(getString(R.string.sp_user_group_id), user.groupId)
        edit.putBoolean(getString(R.string.sp_user_exist), true)
        edit.apply()
    }

    fun get(uid: String) : UserBean {
        val user = UserBean()
        val sp = context().getSharedPreferences(uid, 0)
        if (!sp.getBoolean(getString(R.string.sp_user_exist), false)) {
            return user
        }
        user.valid = sp.getBoolean(getString(R.string.sp_user_valid), false)
        user.name = sp.getString(getString(R.string.sp_user_name), "")
        user.uid = sp.getString(getString(R.string.sp_user_uid), "")
        user.title = sp.getString(getString(R.string.sp_user_title), "")
        user.gender = sp.getString(getString(R.string.sp_user_gender), "")
        user.token = sp.getString(getString(R.string.sp_user_token), "")
        user.secrete = sp.getString(getString(R.string.sp_user_secret), "")
        user.avatar = sp.getString(getString(R.string.sp_user_avatar), "")
        user.credits = sp.getString(getString(R.string.sp_user_credit_data), "")
        user.extcredits2 = sp.getString(getString(R.string.sp_user_extcredit_data), "")
        user.groupId = sp.getString(getString(R.string.sp_user_group_id), "")
        user.mobile = sp.getString(getString(R.string.sp_user_mobile), "")
        return user
    }

    private fun getString(id: Int): String {
        return context().getString(id)
    }

    private fun context(): Context = BaseApplication.context()
}