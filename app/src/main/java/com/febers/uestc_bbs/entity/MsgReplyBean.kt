package com.febers.uestc_bbs.entity

class MsgReplyBean : MsgBaseBean() {
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

        var externInfo: ExternInfoBean? = null
        var data: List<DataBean>? = null

        class ExternInfoBean {
            /**
             * padding :
             */

            var padding: String? = null
        }

        class DataBean {
            /**
             * dateline : 1530780312000
             * type : post
             * note : 趁着天黑撒个野 回复了您的帖子 低价出长城驾校王正东教练学车资格   查看
             * fromId : 1722000
             * fromIdType : post
             * author : 趁着天黑撒个野
             * authorId : 195320
             * authorAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=195320&size=middle
             * actions : []
             */

            var dateline: String? = null
            var type: String? = null
            var note: String? = null
            var fromId: Int = 0
            var fromIdType: String? = null
            var author: String? = null
            var authorId: Int = 0
            var authorAvatar: String? = null
            var actions: List<*>? = null
        }
    }

    class ListBean {
        /**
         * board_name : 二手专区
         * board_id : 61
         * topic_id : 1722000
         * topic_subject : null
         * topic_content :  本帖最后由 四条眉毛 于 2018-7-6 17:48 编辑
         *
         * ...null
         *
         * topic_url :
         * reply_content : 王师傅不在成电驾校了？长城驾校远不远啊
         *
         * reply_url :
         * reply_remind_id : 30837058
         * reply_nick_name : 趁着天黑撒个野
         * user_id : 195320
         * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=195320&size=middle
         * is_read : 1
         * replied_date : 1530780312000
         */

        var board_name: String? = null
        var board_id: Int = 0
        var topic_id: Int = 0
        var topic_subject: String? = null
        var topic_content: String? = null
        var topic_url: String? = null
        var reply_content: String? = null
        var reply_url: String? = null
        var reply_remind_id: Int = 0
        var reply_nick_name: String? = null
        var user_id: Int = 0
        var icon: String? = null
        var is_read: Int = 0
        var replied_date: String? = null
    }
}
