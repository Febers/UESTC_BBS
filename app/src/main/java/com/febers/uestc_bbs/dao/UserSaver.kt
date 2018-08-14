/*
 * Created by Febers at 18-8-14 下午2:31.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 下午2:31.
 */

package com.febers.uestc_bbs.dao

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.entity.LoginResultBean


object UserSaver {

    fun save(uid: String, result: LoginResultBean) {
        val edit = context().getSharedPreferences(uid, 0).edit()
        edit.putString(getString(R.string.sp_user_name), result.userName)
        edit.putString(getString(R.string.sp_user_uid), result.uid)
        edit.putString(getString(R.string.sp_user_title), result.userTitle)
        edit.putString(getString(R.string.sp_user_gender), result.gender)
        edit.putString(getString(R.string.sp_user_token), result.token)
        edit.putString(getString(R.string.sp_user_score), result.score)
        edit.putString(getString(R.string.sp_user_secret), result.secret)
        edit.putString(getString(R.string.sp_user_avatar), result.avatar)
        edit.putString(getString(R.string.sp_user_credit_data), result.creditShowList[0].data)
        edit.putString(getString(R.string.sp_user_extcredit_data), result.creditShowList[1].data)
        edit.putString(getString(R.string.sp_user_mobile), result.mobile)
        edit.putString(getString(R.string.sp_user_group_id), result.groupid)
        edit.apply()
    }

    fun get(uid: String) {

    }

    private fun getString(id: Int): String {
        return context().getString(id)
    }

    private fun context(): Context = BaseApplication.context()
}