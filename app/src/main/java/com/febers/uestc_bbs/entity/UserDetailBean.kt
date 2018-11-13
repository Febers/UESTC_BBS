package com.febers.uestc_bbs.entity

class UserDetailBean {

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null
    var flag: Int = 0
    var is_black: Int = 0
    var is_follow: Int = 0
    var isFriend: Int = 0
    var icon: String? = null
    var level_url: String? = null
    var name: String? = null
    var email: String? = null
    var status: Int = 0
    var gender: Int = 0
    var score: Int = 0
    var credits: Int = 0
    var gold_num: Int = 0
    var topic_num: Int = 0
    var photo_num: Int = 0
    var reply_posts_num: Int = 0
    var essence_num: Int = 0
    var friend_num: Int = 0
    var follow_num: Int = 0
    var level: Int = 0
    var sign: String? = null
    var userTitle: String? = null
    var mobile: String? = null
    var verify: List<*>? = null
    var info: List<*>? = null

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
         * repeatList : []
         * profileList : [{"type":"gender","title":"性别","data":"女"},{"type":"birthday","title":"生日","data":"1980 年 1 月 4 日"},{"type":"education","title":"学历","data":"本科"},{"type":"site","title":"个人主页","data":"http://"}]
         * creditList : [{"type":"credits","title":"积分","data":53008},{"type":"extcredits1","title":"威望","data":271},{"type":"extcredits2","title":"水滴","data":10088},{"type":"extcredits6","title":"奖励券","data":1}]
         * creditShowList : [{"type":"credits","title":"积分","data":53008},{"type":"extcredits2","title":"水滴","data":10088}]
         */

        var externInfo: ExternInfoBean? = null
        var repeatList: List<*>? = null
        var profileList: List<ProfileListBean>? = null
        var creditList: List<CreditListBean>? = null
        var creditShowList: List<CreditShowListBean>? = null

        class ExternInfoBean {
            /**
             * padding :
             */

            var padding: String? = null
        }

        class ProfileListBean {
            /**
             * type : gender
             * title : 性别
             * data : 女
             */

            var type: String? = null
            var title: String? = null
            var data: String? = null
        }

        class CreditListBean {
            /**
             * type : credits
             * title : 积分
             * data : 53008
             */

            var type: String? = null
            var title: String? = null
            var data: Int = 0
        }

        class CreditShowListBean {
            /**
             * type : credits
             * title : 积分
             * data : 53008
             */

            var type: String? = null
            var title: String? = null
            var data: Int = 0
        }
    }
}
