/*
 * Created by Febers at 18-6-13 下午5:15.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午8:27.
 */

package com.febers.uestc_bbs.entity

data class LoginResultBean(var rs: Int, var errcode: String,
                           var head: ResultHeadBean, var body: LoginResultBody,
                           var isValidation: String, var token: String, var secret: String,
                           var score: String, var uid: String, var userName: String, var avatar: String,
                           var gender: String, var userTitle: String, var repeatList: Any?, var verify: Any?,
                           var creditShowList: List<LoginCreditShow>, var mobile: String, var groupid: String)

data class ResultHeadBean(var errCode: String, var errInfo: String, var version: String, var alert: String)

data class LoginResultBody(var externInfo: LoginResultExternInfo)

data class LoginResultExternInfo(var padding: String)

data class LoginCreditShow(var type: String, var title: String, var data: String)
