package com.febers.uestc_bbs.entity

class PushMessageBean {

    var msgs: List<PushMsg>? = null

    class PushMsg {
        var text: String = ""
        var show: Boolean = false
        var id: Int = 0
    }
}