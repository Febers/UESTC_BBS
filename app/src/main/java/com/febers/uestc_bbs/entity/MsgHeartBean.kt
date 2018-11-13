package com.febers.uestc_bbs.entity

class MsgHeartBean {

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
        /**
         * externInfo : {"padding":"","heartPeriod":"120000","pmPeriod":"20000"}
         * replyInfo : {"count":1,"time":"1538750473000"}
         * atMeInfo : {"count":1,"time":"1538750473000"}
         * pmInfos : [{"fromUid":214009,"plid":4022360,"pmid":4022360,"time":"1538750459000"}]
         * friendInfo : {"count":1,"time":"1538750422000"}
         */

        var externInfo: ExternInfoBean? = null
        var replyInfo: ReplyInfoBean? = null
        var atMeInfo: AtMeInfoBean? = null
        var friendInfo: FriendInfoBean? = null
        var pmInfos: List<PmInfosBean>? = null

        class ExternInfoBean {
            /**
             * padding :
             * heartPeriod : 120000
             * pmPeriod : 20000
             */

            var padding: String? = null
            var heartPeriod: String? = null
            var pmPeriod: String? = null
        }

        class ReplyInfoBean {
            /**
             * count : 1
             * time : 1538750473000
             */

            var count: Int = 0
            var time: String? = null
        }

        class AtMeInfoBean {
            /**
             * count : 1
             * time : 1538750473000
             */

            var count: Int = 0
            var time: String? = null
        }

        class FriendInfoBean {
            /**
             * count : 1
             * time : 1538750422000
             */

            var count: Int = 0
            var time: String? = null
        }

        class PmInfosBean {
            /**
             * fromUid : 214009
             * plid : 4022360
             * pmid : 4022360
             * time : 1538750459000
             */

            var fromUid: Int = 0
            var plid: Int = 0
            var pmid: Int = 0
            var time: String? = null
        }
    }
}
