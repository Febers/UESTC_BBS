/*
 * Created by Febers at 18-8-13 下午11:25.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-13 下午11:25.
 */

package com.febers.uestc_bbs.entity

data class MoreItemBean(var itemName: String, var itemIcon: Int, var showSwitch: Boolean = false, var isCheck: Boolean = false)

data class DetailItemBean(var itemParam: String, var itemValue: String?)

data class OptionItemBean(var itemName: String, var itemIcon: Int)

data class ThemeItemBean(var itemColor: Int = 0, var itemName: String = "", var itemUsing: Boolean = false)

data class SettingItemBean(var title: String, var tip: String, var showCheck: Boolean = false, var checked: Boolean = false)



