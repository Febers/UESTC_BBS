package com.febers.uestc_bbs.module.context

import android.content.Context
import android.content.Intent
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.module.login.view.LoginActivity

/**
 * Login的Context类,用户接口和状态管理类
 * 当当前用户状态不可用时，通常是未登录时
 * 跳转至登录界面
 */
object LoginContext {
    fun userState(ctx: Context): Boolean {
        val userValid = MyApp.getUser().valid
        return if (userValid) true
        else {
            ctx.startActivity(Intent(ctx, LoginActivity::class.java))
            false
        }
    }
}