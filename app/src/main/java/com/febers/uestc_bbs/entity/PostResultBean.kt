/*
 * Created by Febers at 18-8-17 下午8:58.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:58.
 */

package com.febers.uestc_bbs.entity

/**
 *{
"rs": 1,
"errcode": "",
"head": {
"errCode": "00000000",
"errInfo": "调用成功,没有任何错误",
"version": "2.6.1.7",
"alert": 0
},
"body": {
"externInfo": {
"padding": ""
}
},
"topic": {
"topic_id": 1729987,
"title": "[求助讨论]那些吹嘘公务员的",
"type": "normal",
"special": 0,
"sortId": 0,
"user_id": 217224,
"user_nick_name": "柠檬2",
"replies": 70,
"hits": 2458,
"essence": 0,
"vote": 0,
"hot": 0,
"top": 0,
"is_favor": 0,
"create_date": "1534393711000",
"icon": "http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=217224&size=middle",
"level": 0,
"userTitle": "蝌蚪 (Lv.1)",
"userColor": "",
"isFollow": 0,
"zanList": [
{
"tid": "1729987",
"recommenduid": "184828",
"dateline": "1534496166",
"username": "Ycy-uestc",
"count(distinct recommenduid)": "1"
}
],
"content": [
{
"infor": "总发公务员工资特别高的那些人，一定家里边没人当公务员。不了解基层公务员的工资就别混淆视听了，忽悠你电学子进去了之后，哭都不知道找谁哭",
"type": 0
}
],
"poll_info": null,
"activityInfo": null,
"location": "",
"delThread": false,
"managePanel": [],
"extraPanel": [
{
"action": "http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/topicrate&sdkVersion=2.6.1.7&
accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1729987&pid=30884386&type=view",
"title": "评分",
"extParams": {
"beforeAction": "http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/topicrate&sdkVersion=2.6.1.7&
accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1729987&pid=30884386&type=check"
},
"type": "rate"
},
{
"action": "http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/support&sdkVersion=2.6.1.7&
accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1729987&pid=30884386&type=thread",
"title": "支持",
"extParams": {
"beforeAction": "",
"recommendAdd": 6,
"isHasRecommendAdd": 0
},
"type": "support"
}
],
"mobileSign": "来自安卓客户端",
"status": 1,
"reply_status": 1,
"flag": 0,
"gender": 0,
"reply_posts_id": 30884386,
"rateList": {
"padding": ""
},
"relateItem": []
},
"page": 1,
"has_next": 1,
"total_num": 70,
"list": [
{
"reply_id": 200599,
"reply_content": [
{
"infor": "老哥具体聊聊啊[mobcent_phiz=http://bbs.uestc.edu.cn/static/image/smiley/alu/57.gif]",
"type": 0
}
],
"reply_type": "normal",
"reply_name": "朝暾",
"reply_posts_id": 30884392,
"poststick": 0,
"position": 2,
"posts_date": "1534393979000",
"icon": "http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=200599&size=middle",
"level": 0,
"userTitle": "虾米 (Lv.2)",
"userColor": "",
"location": "",
"mobileSign": "",
"reply_status": 1,
"status": 1,
"role_num": 1,
"title": "",
"gender": 0,
"is_quote": 0,
"quote_pid": 0,
"quote_content": "",
"quote_user_name": "",
"delThread": false,
"managePanel": [],
"extraPanel": [
{
"action": "http://bbs.uestc.edu.cn/mobcent/app/web/index.php?r=forum/support&sdkVersion=2.6.1.7&
accessToken=f265c4405fa903d493da843e28e63&accessSecret=08a733a5054250923e04b4ee534dc&apphash=&tid=1729987&pid=30884392&type=post",
"title": "支持",
"recommendAdd": "",
"extParams": {
"beforeAction": "",
"recommendAdd": 0,
"isHasRecommendAdd": 0
},
"type": "support"
}
]
}
],
"forumName": "就业创业",
"boardId": 174,
"forumTopicUrl": "http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1729987",
"img_url": "",
"icon_url": ""
}
 */



data class PostResultBean(var rs: String? = null, var errcode: String? = null, var head: ResultHeadBean? = null,
                          var body: Any? = null, var topic:PostTopicBaen? = null, var page: String? = null, var have_next: String? = null,
                          var total_num: String? = null, var list: List<PostReplyBean>? = null, var forumName: String? = null,
                          var boardId: String? = null, var forumTopicUrl: String? = null, var img_url: String? = null,
                          var icon_url: String? = null)

data class PostTopicBaen(var topic_id: String? = null, var title: String? = null, var type: String?,
                         var special: String?, var sortId: String?, var user_id: String? = null,
                         var user_nick_name: String?, var replies: String?, var hits: String?,
                         var essence: String?, var vote: String?, var hot: String?,
                         var top: String?, var is_favor: String?, var create_date: String?,
                         var icon: String? = null, var level: String? = null, var userTitle: String? = null,
                         var userColor: String?, var isFollow: String?, var zanList: List<Any>?,
                         var content: List<SimpleContentBean>? = null, var poll_info: Any?, var activityInfo: Any?,
                         var location: String?, var delThread: Any?, var managePanel: List<Any>?,
                         var extraPanel: List<PostExtraPanelBean>? = null, var mobileSign :String?, var status: String?,
                         var reply_status: String?, var flag :String?, var gender :String?,
                         var reply_posts_id: String?, var rateList: Any?, var relateItem: Any?)

data class SimpleContentBean(var infor :String? = null, var type :String? = null, var originalInfo: String? = null,
                             var url: String? = null)

data class PostExtraPanelBean(var action: String?, var title: String?, var extParams: Any?,
                              var beforeAction: String?, var type: String?)

data class PostReplyBean(var reply_id: String?, var reply_content: List<SimpleContentBean>? = null, var reply_type: String?,
                         var reply_name: String?, var reply_post_id: String?, var poststick: String?,
                         var poisition: String?, var posts_data: String?, var icon: String?,
                         var level: String?, var userTitle: String?, var userColor: String?,
                         var location: String?, var mobileSign: String?, var reply_status: String?,
                         var status: String?, var role_num: String?, var title: String?,
                         var gender: String?, var is_quote: String?, var quote_pid: String?,
                         var quote_content: String?, var quote_user_name: String?, var delThread: String?,
                         var managePanel: List<Any>?, var extraPanel: List<PostExtraPanelBean>?)