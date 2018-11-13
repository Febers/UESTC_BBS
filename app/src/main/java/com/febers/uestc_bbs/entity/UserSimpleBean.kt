package com.febers.uestc_bbs.entity

data class UserSimpleBean (
        var valid : Boolean = false,
        var msg: String = "",
        var name: String = "",
        var uid: Int = 0,
        var title: String = "",
        var gender: String = "",
        var token: String = "",
        var secrete: String = "",
        var score: String = "",
        var avatar: String = "",
        var groupId: String = "",
        var mobile: String = "",
        var credits: String = "",
        var extcredits2: String = "",
        var sign: String = ""
)