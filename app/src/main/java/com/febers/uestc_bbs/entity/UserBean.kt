/*
 * Created by Febers at 18-6-14 下午10:41.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-14 下午10:41.
 */

package com.febers.uestc_bbs.entity


data class UserBean (
        var valid : Boolean = false,
        var msg: String? = null,
        var name: String? = null,
        var uid: String? = null,
        var title: String? = null,
        var gender: String? = null,
        var token: String? = null,
        var secrete: String? = null,
        var score: String? = null,
        var avatar: String? = null,
        var groupId: String? = null,
        var mobile: String? = null,
        var credits: String? = null,
        var extcredits2: String? = null
)