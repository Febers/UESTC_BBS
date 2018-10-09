package com.febers.uestc_bbs.entity

class MsgPrivateBean : MsgBaseBean() {

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null

    class HeadBean {
        /**
         * errCode : 00000000
         * errInfo : 调用成功,没有任何错误
         * version : 2.6.1.7
         * alert : 0
         */

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
            /**
             * padding :
             */

            var padding: String? = null
        }

        class ListBean {
            /**
             * plid : 4021478
             * pmid : 4021478
             * lastUserId : 216786
             * lastUserName : xylly123
             * lastSummary : 发图给你看
             * lastDateline : 1537273815000
             * toUserId : 216786
             * toUserAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=216786&size=middle
             * toUserName : xylly123
             * toUserIsBlack : 0
             * isNew : 0
             */

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
