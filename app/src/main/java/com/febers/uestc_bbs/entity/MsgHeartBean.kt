package com.febers.uestc_bbs.entity

class MsgHeartBean {

    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":"","heartPeriod":"120000","pmPeriod":"20000"},"replyInfo":{"count":0,"time":"0"},"atMeInfo":{"count":0,"time":"0"},"pmInfos":[],"friendInfo":{"count":0,"time":"0"}}
     */

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
         * replyInfo : {"count":0,"time":"0"}
         * atMeInfo : {"count":0,"time":"0"}
         * pmInfos : []
         * friendInfo : {"count":0,"time":"0"}
         */

        var externInfo: ExternInfoBean? = null
        var replyInfo: ReplyInfoBean? = null
        var atMeInfo: AtMeInfoBean? = null
        var friendInfo: FriendInfoBean? = null
        var pmInfos: List<*>? = null

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
             * count : 0
             * time : 0
             */

            var count: Int = 0
            var time: String? = null
        }

        class AtMeInfoBean {
            /**
             * count : 0
             * time : 0
             */

            var count: Int = 0
            var time: String? = null
        }

        class FriendInfoBean {
            /**
             * count : 0
             * time : 0
             */

            var count: Int = 0
            var time: String? = null
        }
    }
}
