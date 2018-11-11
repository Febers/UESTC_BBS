package com.febers.uestc_bbs.entity

class UserPostBean {

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null
    var page: Int = 0
    var has_next: Int = 0
    var total_num: Int = 0
    var list: List<ListBean>? = null

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
         * externInfo : {"padding":""}
         */

        var externInfo: ExternInfoBean? = null

        class ExternInfoBean {
            /**
             * padding :
             */

            var padding: String? = null
        }
    }

    class ListBean {
        /**
         * pic_path :
         * board_id : 70
         * board_name : 程序员
         * topic_id : 1718134
         * type_id : 127
         * sort_id : 0
         * title : [Java]Android端校园服务App“i成电”开始公测啦
         * subject : 利用课余时间，开发了一个Android端应用“i成电”，
         * user_id : 196486
         * last_reply_date : 1527518249000
         * user_nick_name : 四条眉毛
         * hits : 295
         * replies : 23
         * top : 0
         * status : 32
         * essence : 0
         * hot : 0
         * userAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle
         * special : 0
         */

        var pic_path: String? = null
        var board_id: Int = 0
        var board_name: String? = null
        var topic_id: Int = 0
        var type_id: Int = 0
        var sort_id: Int = 0
        var title: String? = null
        var subject: String? = null
        var user_id: Int = 0
        var last_reply_date: String? = null
        var user_nick_name: String? = null
        var hits: Int = 0
        var replies: Int = 0
        var top: Int = 0
        var status: Int = 0
        var essence: Int = 0
        var hot: Int = 0
        var userAvatar: String? = null
        var special: Int = 0
    }
}
