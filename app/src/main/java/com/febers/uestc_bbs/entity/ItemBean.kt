package com.febers.uestc_bbs.entity

data class MoreItemBean(var itemName: String, var itemIcon: Int, var showSwitch: Boolean = false, var isCheck: Boolean = false)

data class DetailItemBean(var itemParam: String, var itemValue: String?)

data class OptionItemBean(var itemName: String, var itemIcon: Int)

data class SettingItemBean(var title: String, var tip: String, var showCheck: Boolean = false, var checked: Boolean = false)

data class ProjectItemBean(var name: String, var author: String, var des: String)

data class UserItemBean(var name: String, var isNow: Boolean)

data class IconItemBean(var image: Int, var isChoose: Boolean)

data class UpdateCheckBean(var hasNewerVersion: Boolean, var newVersionCode: String = "", var newVersionDes: String = "", var newVersionDownloadUrl: String = "")


