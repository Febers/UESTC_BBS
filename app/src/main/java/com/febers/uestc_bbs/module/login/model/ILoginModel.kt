/*
 * Created by Febers at 18-6-10 下午12:36.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午12:36.
 */

package com.febers.uestc_bbs.module.login.model

interface ILoginModel {
    fun loginService(userName: String, userPw: String)
}