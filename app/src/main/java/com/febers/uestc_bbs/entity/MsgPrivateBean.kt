package com.febers.uestc_bbs.entity

class MsgPrivateBean : MsgBaseBean() {
    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null

    class HeadBean {
        var errCode: String? = null
        var errInfo: String? = null
        var version: String? = null
        var alert: Int = 0
    }

    class BodyBean {
        var externInfo: ExternInfoBean? = null
        var hasNext: Int = 0
        var count: Int = 0
        var list: List<ListBean>? = null

        class ExternInfoBean {
            var padding: String? = null
        }

        class ListBean {
            var plid: Int = 0
            var pmid: Int = 0
            var lastUserId: Int = 0
            var lastUserName: String? = null
            var lastSummary: String? = null
            var lastDateline: String? = null
            var toUserId: Int = 0
            var toUserAvatar: String? = null
            var toUserName: String? = null
            var toUserIsBlack: Int = 0
            var isNew: Int = 0
        }
    }
}
