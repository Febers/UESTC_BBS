package com.febers.uestc_bbs.module.login.model

import android.content.Context
import android.content.Intent
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.module.login.view.LoginActivity

/**
 * Login的Context类,用户接口和状态管理类
 */
object LoginContext {
    fun userState(ctx: Context): Boolean {
        val userValid = MyApplication.getUser().valid
        return if (userValid) true
        else {
            ctx.startActivity(Intent(ctx, LoginActivity::class.java))
            false
        }
    }
}