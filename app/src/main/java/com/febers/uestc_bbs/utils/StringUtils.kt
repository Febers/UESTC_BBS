package com.febers.uestc_bbs.utils

fun String.encodeSpaces(): String {
    return this.replace("\n", "<br>").replace("\r", "&nbsp;")
}
