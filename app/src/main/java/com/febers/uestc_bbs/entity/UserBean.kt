/*
 * Created by Febers at 18-6-14 下午10:41.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-14 下午10:41.
 */

package com.febers.uestc_bbs.entity


data class UserBean (
        var valid : Boolean = false,
        var msg: String = "",
        var name: String = "",
        var uid: String = "",
        var title: String = "",
        var gender: String = "",
        var token: String = "",
        var secrete: String = "",
        var score: String = "",
        var avatar: String = "",
        var groupId: String = "",
        var mobile: String = "",
        var credits: String = "",
        var extcredits2: String = ""
)