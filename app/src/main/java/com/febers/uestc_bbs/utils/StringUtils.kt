package com.febers.uestc_bbs.utils

fun String.encodeSpaces(): String {
    return this.replace("\n", "<br>").replace("\r", "&nbsp;")
}

fun String.getStringSimplified(): String =
        if (this.length <= 100) this
        else this.substring(0, 99)+"..."

