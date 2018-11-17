package com.febers.uestc_bbs.entity

class BoardListBean_ {

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null
    var online_user_num: Int = 0
    var td_visitors: Int = 0
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
         * board_category_id : 174
         * board_category_name : 就业创业
         * board_category_type : 1
         * description :
         * icon : http://bbs.uestc.edu.cn/data/attachment/common/star/common_174_icon.png
         * td_posts_num : 44
         * topic_total_num : 51974
         * posts_total_num : 701644
         * is_focus : 0
         * board_list : [{"board_id":214,"board_name":"招聘信息发布栏","description":"","board_child":0...
         */

        var board_category_id: Int = 0
        var board_category_name: String? = null
        var board_category_type: Int = 0
        var description: String? = null
        var icon: String? = null
        var td_posts_num: String? = null
        var topic_total_num: String? = null
        var posts_total_num: String? = null
        var is_focus: Int = 0
        var board_list: List<BoardListBean>? = null

        class BoardListBean {
            /**
             * board_id : 214
             * board_name : 招聘信息发布栏
             * description :
             * board_child : 0
             * board_img :
             * last_posts_date : 1542344442000
             * board_content : 1
             * forumRedirect :
             * favNum : 288
             * td_posts_num : 9
             * topic_total_num : 21013
             * posts_total_num : 136865
             * is_focus : 0
             */

            var board_id: Int = 0
            var board_name: String? = null
            var description: String? = null
            var board_child: Int = 0
            var board_img: String? = null
            var last_posts_date: String? = null
            var board_content: Int = 0
            var forumRedirect: String? = null
            var favNum: Int = 0
            var td_posts_num: Int = 0
            var topic_total_num: Int = 0
            var posts_total_num: Int = 0
            var is_focus: Int = 0
        }
    }
}
