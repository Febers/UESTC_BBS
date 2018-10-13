package com.febers.uestc_bbs.entity

class PMDetailBean {

    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":""},"userInfo":{"uid":196486,"name":"四条眉毛","avatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=small"},"pmList":[{"fromUid":199767,"name":"acne","avatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=199767&size=small","msgList":[{"sender":196486,"mid":4248631,"content":" 关于您在\u201c550出国行16g金色iPhone5s\u201d的帖子 http://bbs.uestc.edu.cn/forum.php?mod=redirect&goto=findpost&pid=30280027&ptid=1682710 \n你好，5s还出吗？给个联系方式","type":"text","time":"1505299828000"},{"sender":199767,"mid":4248637,"content":"http://bbs.uestc.edu.cn/data/appbyme/thumb/0/2/0/mobcentSmallPreview_ce5e23382aeb0613a194ac401ccc9c01.jpg","type":"image","time":"1505302915000"},{"sender":199767,"mid":4248638,"content":"您好  暂时还没出 我的微信","type":"text","time":"1505302944000"}],"plid":4002518,"hasPrev":0}]}
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
         * externInfo : {"padding":""}
         * userInfo : {"uid":196486,"name":"四条眉毛","avatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=small"}
         * pmList : [{"fromUid":199767,"name":"acne","avatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=199767&size=small","msgList":[{"sender":196486,"mid":4248631,"content":" 关于您在\u201c550出国行16g金色iPhone5s\u201d的帖子 http://bbs.uestc.edu.cn/forum.php?mod=redirect&goto=findpost&pid=30280027&ptid=1682710 \n你好，5s还出吗？给个联系方式","type":"text","time":"1505299828000"},{"sender":199767,"mid":4248637,"content":"http://bbs.uestc.edu.cn/data/appbyme/thumb/0/2/0/mobcentSmallPreview_ce5e23382aeb0613a194ac401ccc9c01.jpg","type":"image","time":"1505302915000"},{"sender":199767,"mid":4248638,"content":"您好  暂时还没出 我的微信","type":"text","time":"1505302944000"}],"plid":4002518,"hasPrev":0}]
         */

        var externInfo: ExternInfoBean? = null
        var userInfo: UserInfoBean? = null
        var pmList: List<PmListBean>? = null

        class ExternInfoBean {
            /**
             * padding :
             */

            var padding: String? = null
        }

        class UserInfoBean {
            /**
             * uid : 196486
             * name : 四条眉毛
             * avatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=small
             */

            var uid: Int = 0
            var name: String? = null
            var avatar: String? = null
        }

        class PmListBean {
            /**
             * fromUid : 199767
             * name : acne
             * avatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=199767&size=small
             * msgList : [{"sender":196486,"mid":4248631,"content":" 关于您在\u201c550出国行16g金色iPhone5s\u201d的帖子 http://bbs.uestc.edu.cn/forum.php?mod=redirect&goto=findpost&pid=30280027&ptid=1682710 \n你好，5s还出吗？给个联系方式","type":"text","time":"1505299828000"},{"sender":199767,"mid":4248637,"content":"http://bbs.uestc.edu.cn/data/appbyme/thumb/0/2/0/mobcentSmallPreview_ce5e23382aeb0613a194ac401ccc9c01.jpg","type":"image","time":"1505302915000"},{"sender":199767,"mid":4248638,"content":"您好  暂时还没出 我的微信","type":"text","time":"1505302944000"}]
             * plid : 4002518
             * hasPrev : 0
             */

            var fromUid: Int = 0
            var name: String? = null
            var avatar: String? = null
            var plid: Int = 0
            var hasPrev: Int = 0
            var msgList: List<MsgListBean>? = null

            class MsgListBean {
                /**
                 * sender : 196486
                 * mid : 4248631
                 * content :  关于您在“550出国行16g金色iPhone5s”的帖子 http://bbs.uestc.edu.cn/forum.php?mod=redirect&goto=findpost&pid=30280027&ptid=1682710
                 * 你好，5s还出吗？给个联系方式
                 * type : text
                 * time : 1505299828000
                 */

                var sender: Int = 0
                var mid: Int = 0
                var content: String? = null
                var type: String? = null
                var time: String? = null
            }
        }
    }
}
