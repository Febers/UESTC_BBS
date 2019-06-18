package com.febers.uestc_bbs.entity

data class UserCanAtBean(
    var body: Body,
    var errcode: String,
    var has_next: Int,
    var head: Head,
    var list: List<UserCanAtSimple>,
    var page: Int,
    var rs: Int,
    var total_num: Int
)

data class Head(
    var alert: Int,
    var errCode: String,
    var errInfo: String,
    var version: String
)

data class Body(
    var externInfo: ExternInfo
)

data class ExternInfo(
    var padding: String
)

data class UserCanAtSimple(
    var name: String,
    var role_num: Int,
    var uid: Int
)