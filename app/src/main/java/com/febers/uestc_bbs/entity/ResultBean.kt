package com.febers.uestc_bbs.entity

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

class PostSendResultBean {
    /**
     * rs : 1
     * errcode : 回复成功
     * head : {"errCode":"00000000","errInfo":"回复成功","version":"2.6.1.7","alert":1}
     * body : {"externInfo":{"padding":""}}
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

        class ExternInfoBean {
            var padding: String? = null
        }
    }
}


class PostFavResultBean {
    /**
     * rs : 1
     * errcode : 信息收藏成功
     * head : {"errCode":"02000030","errInfo":"信息收藏成功","version":"2.6.1.7","alert":1}
     * body : {"externInfo":{"padding":""}}
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

        class ExternInfoBean {
            var padding: String? = null
        }
    }
}

class PostVoteResultBean {

    /**
     * rs : 1
     * errcode : 投票成功
     * head : {"errCode":"00000000","errInfo":"投票成功","version":"2.6.1.7","alert":1}
     * body : {"externInfo":{"padding":""}}
     * vote_rs : [{"name":"官网显示面试未通过","pollItemId":15729,"totalNum":42},{"name":"官网显示面试完成，
     * 收到offer排序短信","pollItemId":15730,"totalNum":66},{"name":"官网显示面试完成，未收到offer排序短信",
     * "pollItemId":15731,"totalNum":16},{"name":"官网显示录用排序，收到offer排序短信","pollItemId":15732,"totalNum":119},
     * {"name":"官网显示录用排序，未收到offer排序短信","pollItemId":15733,"totalNum":18},{"name":"已经拿到offer","pollItemId":15734,"totalNum":64},
     * {"name":"酱油通道","pollItemId":15735,"totalNum":364}]
     */

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null
    var vote_rs: List<VoteRsBean>? = null

    class HeadBean {
        var errCode: String? = null
        var errInfo: String? = null
        var version: String? = null
        var alert: Int = 0
    }

    class BodyBean {
        var externInfo: ExternInfoBean? = null

        class ExternInfoBean {
            var padding: String? = null
        }
    }

    class VoteRsBean {
        var name: String? = null
        var pollItemId: Int = 0
        var totalNum: Int = 0
    }
}

class UserUpdateResultBean {
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

        class ExternInfoBean {
            var padding: String? = null
        }
    }
}