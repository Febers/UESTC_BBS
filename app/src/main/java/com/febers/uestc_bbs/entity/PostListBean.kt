package com.febers.uestc_bbs.entity

class PostListBean {

    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":""}}
     * newTopicPanel : [{"type":"normal","action":"","title":"发表帖子"},{"type":"vote","action":"","title":"发起投票"}]
     * classificationTop_list : []
     * classificationType_list : [{"classificationType_id":391,"classificationType_name":"转帖"},{"classificationType_id":392,"classificationType_name":"原创"},{"classificationType_id":393,"classificationType_name":"倾诉"},{"classificationType_id":394,"classificationType_name":"其他"},{"classificationType_id":395,"classificationType_name":"表白"},{"classificationType_id":396,"classificationType_name":"讨论"},{"classificationType_id":397,"classificationType_name":"版务"}]
     * isOnlyTopicType : 1
     * anno_list : []
     * forumInfo : {"id":45,"title":"情感专区","description":"","icon":"http://bbs.uestc.edu.cn/data/attachment/common/star/common_45_icon.png","td_posts_num":"10","topic_total_num":"21314","posts_total_num":"735552","is_focus":0}
     * topTopicList : [{"id": 1456557,"title": "清水河畔论坛新手导航 V2.1"}]
     * list : [{"board_id":45,"board_name":"情感专区","topic_id":1737972,"type":"normal","title":"[讨论]爱情还是面包","user_id":200762,"user_nick_name":"蒲公英的约定","userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=200762&size=middle","last_reply_date":"1539393699000","vote":0,"hot":0,"hits":526,"replies":19,"essence":0,"top":0,"status":32800,"subject":"当爱情跟面包不能同时拥有时，你会选择爱情还是面包。","pic_path":"","ratio":"1","gender":0,"userTitle":"蝌蚪 (Lv.1)","recommendAdd":0,"special":0,"isHasRecommendAdd":0,"imageList":[],"sourceWebUrl":"http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1737972","verify":[]},{"board_id":45,"board_name":"情感专区","topic_id":1737869,"type":"normal","title":"[倾诉]研三钢铁直男，健身房看到有初恋般的学妹，该怎...","user_id":195840,"user_nick_name":"Answer110","userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=195840&size=middle","last_reply_date":"1539407447000","vote":0,"hot":0,"hits":3098,"replies":119,"essence":0,"top":0,"status":288,"subject":"研三自动化钢铁理工男一枚，生活小资。  今晚在健身房骑单","pic_path":"","ratio":"1","gender":1,"userTitle":"虾米 (Lv.2)","recommendAdd":4,"special":0,"isHasRecommendAdd":0,"imageList":[],"sourceWebUrl":"http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1737869","verify":[]},{"board_id":45,"board_name":"情感专区","topic_id":1737771,"type":"normal","title":"[讨论]一个人","user_id":216490,"user_nick_name":"小野人","userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=216490&size=middle","last_reply_date":"1539152075000","vote":0,"hot":0,"hits":179,"replies":5,"essence":0,"top":0,"status":32800,"subject":"以前喜欢一个人，现在是不是就真的喜欢一个人？","pic_path":"","ratio":"1","gender":0,"userTitle":"蝌蚪 (Lv.1)","recommendAdd":0,"special":0,"isHasRecommendAdd":0,"imageList":[],"sourceWebUrl":"http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1737771","verify":[]},{"board_id":45,"board_name":"情感专区","topic_id":1737768,"type":"normal","title":"[原创]废话很多，也很爱说话。","user_id":205987,"user_nick_name":"十八姑娘","userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=205987&size=middle","last_reply_date":"1539403659000","vote":0,"hot":0,"hits":460,"replies":19,"essence":0,"top":0,"status":32800,"subject":"我知道你爱说话  有很多臭毛病  总是想让别人跟着你的脚","pic_path":"","ratio":"1","gender":0,"userTitle":"虾米 (Lv.2)","recommendAdd":0,"special":0,"isHasRecommendAdd":0,"imageList":[],"sourceWebUrl":"http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1737768","verify":[]},{"board_id":45,"board_name":"情感专区","topic_id":1737758,"type":"normal","title":"[倾诉]可能男生就是放不下前任吧","user_id":192934,"user_nick_name":"nidianyaowan","userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=192934&size=middle","last_reply_date":"1539269507000","vote":0,"hot":0,"hits":383,"replies":11,"essence":0,"top":0,"status":32,"subject":"女生似乎要好一些","pic_path":"","ratio":"1","gender":0,"userTitle":"草鱼 (Lv.5)","recommendAdd":0,"special":0,"isHasRecommendAdd":0,"imageList":[],"sourceWebUrl":"http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1737758","verify":[]}]
     * page : 1
     * has_next : 1
     * total_num : 21314
     */

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null
    var isOnlyTopicType: Int = 0
    var forumInfo: ForumInfoBean? = null
    var page: Int = 0
    var has_next: Int = 0
    var total_num: Int = 0
    var newTopicPanel: List<NewTopicPanelBean>? = null
    var classificationTop_list: List<*>? = null
    var classificationType_list: List<ClassificationTypeListBean>? = null
    var anno_list: List<*>? = null
    var topTopicList: List<TopTopicListBean>? = null
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

    class ForumInfoBean {
        /**
         * id : 45
         * title : 情感专区
         * description :
         * icon : http://bbs.uestc.edu.cn/data/attachment/common/star/common_45_icon.png
         * td_posts_num : 10
         * topic_total_num : 21314
         * posts_total_num : 735552
         * is_focus : 0
         */

        var id: Int = 0
        var title: String? = null
        var description: String? = null
        var icon: String? = null
        var td_posts_num: String? = null
        var topic_total_num: String? = null
        var posts_total_num: String? = null
        var is_focus: Int = 0
    }

    class NewTopicPanelBean {
        /**
         * type : normal
         * action :
         * title : 发表帖子
         */

        var type: String? = null
        var action: String? = null
        var title: String? = null
    }

    class ClassificationTypeListBean {
        /**
         * classificationType_id : 391
         * classificationType_name : 转帖
         */

        var classificationType_id: Int = 0
        var classificationType_name: String? = null
    }

    class TopTopicListBean {
        //topTopicList : [{"id": 1456557,"title": "清水河畔论坛新手导航 V2.1"}]
        var id: Int? = 0
        var title: String? = null
    }
    class ListBean {
        /**
         * board_id : 45
         * board_name : 情感专区
         * topic_id : 1737972
         * type : normal
         * title : [讨论]爱情还是面包
         * user_id : 200762
         * user_nick_name : 蒲公英的约定
         * userAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=200762&size=middle
         * last_reply_date : 1539393699000
         * vote : 0
         * hot : 0
         * hits : 526
         * replies : 19
         * essence : 0
         * top : 0
         * status : 32800
         * subject : 当爱情跟面包不能同时拥有时，你会选择爱情还是面包。
         * pic_path :
         * ratio : 1
         * gender : 0
         * userTitle : 蝌蚪 (Lv.1)
         * recommendAdd : 0
         * special : 0
         * isHasRecommendAdd : 0
         * imageList : []
         * sourceWebUrl : http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1737972
         * verify : []
         */

        var board_id: Int = 0
        var board_name: String? = null
        var topic_id: Int? = null
        //热门帖子
        var source_id: Int? = null
        var type: String? = null
        var title: String? = null
        var user_id: Int = 0
        var user_nick_name: String? = null
        var userAvatar: String? = null
        var last_reply_date: String? = null
        var vote: Int = 0
        var hot: Int = 0
        var hits: Int = 0
        var replies: Int = 0
        var essence: Int = 0
        var top: Int = 0
        var status: Int = 0
        var subject: String? = null
        //热门帖子
        var summary: String? = null
        var pic_path: String? = null
        var ratio: String? = null
        var gender: Int = 0
        var userTitle: String? = null
        var recommendAdd: Int = 0
        var special: Int = 0
        var isHasRecommendAdd: Int = 0
        var sourceWebUrl: String? = null
        var imageList: List<*>? = null
        var verify: List<*>? = null

        fun getTopic_id(): Int {
            return topic_id!!
        }

        fun setTopic_id(topic_id: Int) {
            this.topic_id = topic_id
        }

        fun getSource_id(): Int {
            return source_id!!
        }

        fun setSource_id(source_id: Int) {
            this.source_id = source_id
        }
    }
}
