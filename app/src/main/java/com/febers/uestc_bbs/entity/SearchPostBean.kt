package com.febers.uestc_bbs.entity

class SearchPostBean {

    var rs: Int = 0
    var errcode: String? = null
    var head: SearchPostBeanHead? = null
    var body: SearchPostBeanBody? = null
    var page: Int = 0
    var has_next: Int = 0
    var total_num: Int = 0
    var searchid: Int = 0
    var list: List<SearchPostBeanList>? = null

    class SearchPostBeanHead {
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

    class SearchPostBeanBody {
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

    class SearchPostBeanList {
        /**
         * board_id : 61
         * topic_id : 1734637
         * type_id : 287
         * sort_id : 0
         * vote : 0
         * title : 【全新】威刚 4G 内存条 DDR3L 1600 Mhz 1.35v
         * subject : 需要的可以联系WX  ：一一三六八九九八八二   可以走
         * user_id : 216786
         * last_reply_date : 1537275251000
         * user_nick_name : xylly123
         * hits : 115
         * replies : 8
         * top : 0
         * status : 0
         * essence : 0
         * hot : 0
         * pic_path :
         */

        var board_id: Int = 0
        var topic_id: Int = 0
        var type_id: Int = 0
        var sort_id: Int = 0
        var vote: Int = 0
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
        var pic_path: String? = null
    }
}
