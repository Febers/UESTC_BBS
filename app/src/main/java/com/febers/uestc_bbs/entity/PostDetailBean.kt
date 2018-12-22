package com.febers.uestc_bbs.entity

import com.google.gson.annotations.SerializedName

class PostDetailBean {

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null
    var topic: TopicBean? = null
    var page: Int = 0
    var has_next: Int = 0
    var total_num: Int = 0
    var forumName: String? = null
    var boardId: Int = 0
    var forumTopicUrl: String? = null
    var img_url: String? = null
    var icon_url: String? = null
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

    class ContentBean {
        /**
         *  infor : 银杏大道上树下那个破灯，校庆亮几天美观充充面子也就是了，现在还天天亮，每次从银杏大道走都要被晃眼睛，无论是骑车还是走路；求问有没有有同样感受的同学？
         *  另外觉得这也很费电，除了美观一点作用也没有
         *  type : 0
         *
         * "infor": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/221826jnm65xdo95zxsb2v.png",
         * "type": 1,
         * "originalInfo": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/221826jnm65xdo95zxsb2v.png",
         * "aid": 1880982
         *
         * "infor": "http://app.febers.tech/",
         * "type": 4,
         * "originalInfo": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/221826jnm65xdo95zxsb2v.png",
         * "aid": 1880982,
         * "url": "http://app.febers.tech/"
         *
         * "infor": "iuestc_beta2.9.apk",
         * "type": 5,
         * "originalInfo": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/222551mkmyz00hkd2bksms.png",
         * "aid": 1880984,
         * "url": "http://bbs.uestc.edu.cn/forum.php?mod=attachment&aid=MTg4MDk4NXxlNWY3OGE5YnwxNTM0NTM2NTg0fDE5NjQ4NnwxNzE4MTM0",
         * "desc": "2.94 MB, 下载次数: 44"                                                                                          *
         *
         */

        var infor: String? = null
        var type: Int = 0
        var originalInfo: String? = null
        var url: String? = null
    }

    class TopicBean {
        /**
         * topic_id : 1640734
         * title : 【投票】银杏大道地上的灯晃不晃眼睛？
         * type : vote
         * special : 1
         * sortId : 0
         * user_id : 101245
         * user_nick_name : 漂泊的胡萝卜
         * replies : 29
         * hits : 2013
         * essence : 0
         * vote : 1
         * hot : 0
         * top : 0
         * is_favor : 0
         * create_date : 1481038265000
         * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=101245&size=middle
         * level : 0
         * userTitle : 白鳍 (Lv.9)
         * userColor :
         * isFollow : 0
         * zanList : [{"tid":"1640734","recommenduid":"203159","dateline":"1496627146","username":"Bryantang","count(distinct recommenduid)":"1"},{"tid":"1640734","recommenduid":"181107","dateline":"1493553534","username":"xiaohuihui","count(distinct recommenduid)":"1"},{"tid":"1640734","recommenduid":"190114","dateline":"1491579443","username":"查无此入","count(distinct recommenduid)":"1"},{"tid":"1640734","recommenduid":"183696","dateline":"1484787353","username":"sparrow520","count(distinct recommenduid)":"1"},{"tid":"1640734","recommenduid":"195351","dateline":"1481189979","username":"爱上爬的yang","count(distinct recommenduid)":"1"},{"tid":"1640734","recommenduid":"114141","dateline":"1481071880","username":"禁土","count(distinct recommenduid)":"1"}]
         * content : [{"infor":"银杏大道上树下那个破灯，校庆亮几天美观充充面子也就是了，现在还天天亮，每次从银杏大道走都要被晃眼睛，无论是骑车还是走路；求问有没有有同样感受的同学？\r\n另外觉得这也很费电，除了美观一点作用也没有","type":0}]
         * poll_info : {"deadline":"1567438321","is_visible":1,"voters":688,"type":1,"poll_status":2,"poll_id":[0],"poll_item_list":[{"name":"狗眼要被亮瞎了，极度反感","poll_item_id":13339,"total_num":226,"percent":"32.85%"},{"name":"反感","poll_item_id":13340,"total_num":88,"percent":"12.79%"},{"name":"喜欢","poll_item_id":13341,"total_num":80,"percent":"11.63%"},{"name":"很喜欢","poll_item_id":13342,"total_num":96,"percent":"13.95%"},{"name":"无所谓，酱油通道","poll_item_id":13343,"total_num":198,"percent":"28.78%"}]}
         * activityInfo : null
         * location :
         * delThread : false
         * managePanel : []
         * extraPanel : [{"action":"http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/topicrate&sdkVersion=2.6.1.7&accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1640734&pid=29407264&type=view","title":"评分","extParams":{"beforeAction":"http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/topicrate&sdkVersion=2.6.1.7&accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1640734&pid=29407264&type=check"},"type":"rate"},{"action":"http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/support&sdkVersion=2.6.1.7&accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1640734&pid=29407264&type=thread","title":"支持","extParams":{"beforeAction":"","recommendAdd":4,"isHasRecommendAdd":0},"type":"support"}]
         * mobileSign :
         * status : 1
         * reply_status : 1
         * flag : 0
         * gender : 2
         * reply_posts_id : 29407264
         * rateList : {"padding":""}
         * relateItem : [{"tid":"1647301","subject":"任正非发飙：屁股对着老板，眼睛才能看着客户","image":"","msg":"内部人事透露：在今年华为的节前座谈会上，任老板发飙了。 ","lastReplyTime":"1485915885000","postTime":"1485880323000","author":"xsjswt"},{"tid":"1652202","subject":"眼睛怀孕系列之【羅志祥】夢幻女神朱碧石","image":"","msg":"","lastReplyTime":"1488979068000","postTime":"1488978883000","author":"动感光波biubiu"},{"tid":"1654793","subject":"关于眼睛镜片的问题","image":"","msg":"我一直有个问题,眼睛的镜片搞了很多复杂的工艺,镀了膜什么","lastReplyTime":"1490286130000","postTime":"1490280154000","author":"学号弟"},{"tid":"1656279","subject":"眼睛怀孕系列之眼瞎了","image":"","msg":"然而","lastReplyTime":"1491041970000","postTime":"1491037617000","author":"动感光波biubiu"},{"tid":"1660664","subject":"银杏大道捡到饭卡一张","image":"","msg":"今天自己的饭卡也掉了 找自己饭卡的时候捡到一张别人的饭卡","lastReplyTime":"1493632402000","postTime":"1493620759000","author":"ZZZYYYY"},{"tid":"1661916","subject":"看着我的眼睛告诉我谁最萌？","image":"","msg":"喵遁 萌杀之术！","lastReplyTime":"1494248829000","postTime":"1494247438000","author":"icaruswing"},{"tid":"1662516","subject":"银杏大道的妹子好漂亮","image":"","msg":"都穿丝袜了，总觉得肉丝比较性感","lastReplyTime":"1494579248000","postTime":"1494572313000","author":"一世倾城"},{"tid":"1662517","subject":"品学楼的妹子好漂亮","image":"","msg":"品学楼上自习遇到一个气质美女，从来没见过这么漂亮的","lastReplyTime":"1494588290000","postTime":"1494572492000","author":"superace923"},{"tid":"1663581","subject":"游个泳把自己眼睛给感染了。","image":"","msg":"所以我是应该哭吧。    学校的游泳池应该消毒还是不错的","lastReplyTime":"1495159380000","postTime":"1495098668000","author":"不良青年"},{"tid":"1672119","subject":"谁有今年的毕业歌","image":"","msg":"貌似名字叫《银杏一梦》吧，校毕业晚会上几个人一起唱的，还","lastReplyTime":"1498616881000","postTime":"1498526827000","author":"zhaoyizhe654321"}]
         */

        var topic_id: Int = 0
        var title: String? = null
        var type: String? = null
        var special: Int = 0
        var sortId: Int = 0
        var user_id: Int = 0
        var user_nick_name: String? = null
        var replies: Int = 0
        var hits: Int = 0
        var essence: Int = 0
        var vote: Int = 0
        var hot: Int = 0
        var top: Int = 0
        var is_favor: Int = 0
        var create_date: String? = null
        var icon: String? = null
        var level: Int = 0
        var userTitle: String? = null
        var userColor: String? = null
        var isFollow: Int = 0
        var poll_info: PollInfoBean? = null
        var activityInfo: Any? = null
        var location: String? = null
        var isDelThread: Boolean = false
        var mobileSign: String? = null
        var status: Int = 0
        var reply_status: Int = 0
        var flag: Int = 0
        var gender: Int = 0
        var reply_posts_id: Int = 0
        var rateList: RateListBean? = null
        var zanList: List<ZanListBean>? = null
        var content: List<ContentBean>? = null
        var managePanel: List<*>? = null
        var extraPanel: List<ExtraPanelBean>? = null
        var relateItem: List<RelateItemBean>? = null

        class PollInfoBean {
            /**
             * deadline : 1567438321
             * is_visible : 1
             * voters : 688
             * type : 1 //1应该是单选
             * poll_status : 2  //1为已经投过票
             * poll_id : [0]
             * poll_item_list : [{"name":"狗眼要被亮瞎了，极度反感","poll_item_id":13339,"total_num":226,"percent":"32.85%"},{"name":"反感","poll_item_id":13340,"total_num":88,"percent":"12.79%"},{"name":"喜欢","poll_item_id":13341,"total_num":80,"percent":"11.63%"},{"name":"很喜欢","poll_item_id":13342,"total_num":96,"percent":"13.95%"},{"name":"无所谓，酱油通道","poll_item_id":13343,"total_num":198,"percent":"28.78%"}]
             */

            var deadline: String? = null
            var is_visible: Int = 0
            var voters: Int = 0
            var type: Int = 0
            var poll_status: Int = 0
            var poll_id: List<Int>? = null
            var poll_item_list: List<PollItemListBean>? = null

            class PollItemListBean {
                /**
                 * name : 狗眼要被亮瞎了，极度反感
                 * poll_item_id : 13339
                 * total_num : 226
                 * percent : 32.85%
                 */

                var name: String? = null
                var poll_item_id: Int = 0
                var total_num: Int = 0
                var percent: String? = null
                var isSelectd: Int = 0
            }
        }

        class RateListBean {
            /**
             * padding :
             */

            var padding: String? = null
        }

        class ZanListBean {
            /**
             * tid : 1640734
             * recommenduid : 203159
             * dateline : 1496627146
             * username : Bryantang
             * count(distinct recommenduid) : 1
             */

            var tid: String? = null
            var recommenduid: String? = null
            var dateline: String? = null
            var username: String? = null
            @SerializedName("count(distinct recommenduid)")
            var `_$CountDistinctRecommenduid194`: String? = null // FIXME check this code
        }

        class ExtraPanelBean {
            /**
             * action : http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/topicrate&sdkVersion=2.6.1.7&accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1640734&pid=29407264&type=view
             * title : 评分
             * extParams : {"beforeAction":"http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/topicrate&sdkVersion=2.6.1.7&accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1640734&pid=29407264&type=check"}
             * type : rate
             */

            var action: String? = null
            var title: String? = null
            var extParams: ExtParamsBean? = null
            var type: String? = null

            class ExtParamsBean {
                /**
                 * beforeAction : http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/topicrate&sdkVersion=2.6.1.7&accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1640734&pid=29407264&type=check
                 */

                var beforeAction: String? = null
            }
        }

        class RelateItemBean {
            /**
             * tid : 1647301
             * subject : 任正非发飙：屁股对着老板，眼睛才能看着客户
             * image :
             * msg : 内部人事透露：在今年华为的节前座谈会上，任老板发飙了。
             * lastReplyTime : 1485915885000
             * postTime : 1485880323000
             * author : xsjswt
             */

            var tid: String? = null
            var subject: String? = null
            var image: String? = null
            var msg: String? = null
            var lastReplyTime: String? = null
            var postTime: String? = null
            var author: String? = null
        }
    }

    class ListBean {
        /**
         * reply_id : 101245
         * reply_content : [{"infor":"面子工程好大喜功s.b.\r\n奖学金都不能按时发放","type":0}]
         * reply_type : normal
         * reply_name : 漂泊的胡萝卜
         * reply_posts_id : 29407267
         * poststick : 0
         * position : 2
         * posts_date : 1481038379000
         * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=101245&size=middle
         * level : 0
         * userTitle : 白鳍 (Lv.9)
         * userColor :
         * location :
         * mobileSign :
         * reply_status : 1
         * status : 1
         * role_num : 1
         * title :
         * gender : 2
         * is_quote : 0
         * quote_pid : 0
         * quote_content :
         * quote_user_name :
         * delThread : false
         * managePanel : []
         * extraPanel : [{"action":"http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/support&sdkVersion=2.6.1.7&accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1640734&pid=29407267&type=post","title":"支持","recommendAdd":"","extParams":{"beforeAction":"","recommendAdd":0,"isHasRecommendAdd":0},"type":"support"}]
         */

        var reply_id: Int = 0
        var reply_type: String? = null
        var reply_name: String? = null
        var reply_posts_id: Int = 0
        var poststick: Int = 0
        var position: Int = 0
        var posts_date: String? = null
        var icon: String? = null
        var level: Int = 0
        var userTitle: String? = null
        var userColor: String? = null
        var location: String? = null
        var mobileSign: String? = null
        var reply_status: Int = 0
        var status: Int = 0
        var role_num: Int = 0
        var title: String? = null
        var gender: Int = 0
        var is_quote: Int = 0
        var quote_pid: Int = 0
        var quote_content: String? = null
        var quote_user_name: String? = null
        var isDelThread: Boolean = false
        var reply_content: List<ContentBean>? = null
        var managePanel: List<*>? = null
        var extraPanel: List<ExtraPanelBeanX>? = null

        class ExtraPanelBeanX {
            /**
             * action : http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/support&sdkVersion=2.6.1.7&accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1640734&pid=29407267&type=post
             * title : 支持
             * recommendAdd :
             * extParams : {"beforeAction":"","recommendAdd":0,"isHasRecommendAdd":0}
             * type : support
             */

            var action: String? = null
            var title: String? = null
            var recommendAdd: String? = null
            var extParams: ExtParamsBeanX? = null
            var type: String? = null

            class ExtParamsBeanX {
                /**
                 * beforeAction :
                 * recommendAdd : 0
                 * isHasRecommendAdd : 0
                 */

                var beforeAction: String? = null
                var recommendAdd: Int = 0
                var isHasRecommendAdd: Int = 0
            }
        }
    }
}
