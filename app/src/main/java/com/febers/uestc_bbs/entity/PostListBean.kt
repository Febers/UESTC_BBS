package com.febers.uestc_bbs.entity

class PostListBean {
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
    }
}
