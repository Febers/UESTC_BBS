package com.febers.uestc_bbs.entity

class MsgAtBean : MsgBaseBean() {

    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":""},"data":[{"dateline":"1537967101000","type":"at","note":"四条眉毛 在主题 已解决 中提到了您测试@四条眉毛现在去看看。","fromId":1707366,"fromIdType":"at","author":"四条眉毛","authorId":196486,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle","actions":[]}]}
     * page : 1
     * has_next : 0
     * total_num : 1
     * list : [{"board_name":"程序员","board_id":70,"topic_id":1707366,"topic_subject":"已解决","topic_content":" 本帖最后由 四条眉毛 于 2018-3-14 21:36 编辑 \r\n\r\n代码水平问题\r\n","topic_url":"","reply_content":"测试\r\n \r\n","reply_url":"","reply_remind_id":30934341,"reply_nick_name":"四条眉毛","user_id":196486,"icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle","is_read":1,"replied_date":"1537967101000"}]
     */

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
         * data : [{"dateline":"1537967101000","type":"at","note":"四条眉毛 在主题 已解决 中提到了您测试@四条眉毛现在去看看。","fromId":1707366,"fromIdType":"at","author":"四条眉毛","authorId":196486,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle","actions":[]}]
         */

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
             * dateline : 1537967101000
             * type : at
             * note : 四条眉毛 在主题 已解决 中提到了您测试@四条眉毛现在去看看。
             * fromId : 1707366
             * fromIdType : at
             * author : 四条眉毛
             * authorId : 196486
             * authorAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle
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
         * board_name : 程序员
         * board_id : 70
         * topic_id : 1707366
         * topic_subject : 已解决
         * topic_content :  本帖最后由 四条眉毛 于 2018-3-14 21:36 编辑
         *
         * 代码水平问题
         *
         * topic_url :
         * reply_content : 测试
         *
         *
         * reply_url :
         * reply_remind_id : 30934341
         * reply_nick_name : 四条眉毛
         * user_id : 196486
         * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle
         * is_read : 1
         * replied_date : 1537967101000
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
