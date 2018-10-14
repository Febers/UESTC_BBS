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

class PMSendResultBean {
    /**
     * rs : 1
     * errcode : 操作成功
     * head : {"errCode":"0000000","errInfo":"操作成功 ","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":""},"plid":4022360,"pmid":4318412,"sendTime":"1539501088000"}
     */

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
        var plid: Int = 0
        var pmid: Int = 0
        var sendTime: String? = null

        class ExternInfoBean {
            var padding: String? = null
        }
    }
}