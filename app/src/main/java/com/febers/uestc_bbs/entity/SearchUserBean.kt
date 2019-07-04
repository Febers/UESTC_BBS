package com.febers.uestc_bbs.entity

data class SearchUserBean(
    var body: SearchUserBeanBody?,
    var errcode: String?,
    var has_next: Int?,
    var head: SearchUserBeanHead?,
    var page: Int?,
    var rs: Int?,
    var searchid: Int?,
    var total_num: Int?
)

data class SearchUserBeanBody(
    var externInfo: SearchUserBeanExternInfo?,
    var list: List<SearchUserBeanList>?
)

data class SearchUserBeanList(
    var credits: Int?,
    var dateline: String?,
    var distance: String?,
    var gender: Int?,
    var icon: String?,
    var isFollow: Int?,
    var isFriend: Int?,
    var is_black: Int?,
    var level: Int?,
    var location: String?,
    var name: String?,
    var signture: String?,
    var status: Int?,
    var uid: Int?,
    var userTitle: String?
)

data class SearchUserBeanExternInfo(
    var padding: String?
)

data class SearchUserBeanHead(
    var alert: Int?,
    var errCode: String?,
    var errInfo: String?,
    var version: String?
)