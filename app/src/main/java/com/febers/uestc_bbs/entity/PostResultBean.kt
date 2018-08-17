/*
 * Created by Febers at 18-8-16 上午12:24.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-16 上午12:24.
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
"newTopicPanel": [
{
"type": "normal",
"action": "",
"title": "发表帖子"
}
],
"classificationTop_list": [],
"classificationType_list": [],
"isOnlyTopicType": 0,
"anno_list": [],
"list": [
{
"board_id": 61,
"board_name": "二手专区",
"topic_id": 1729954,
"type": "normal",
"title": "Leopold 机械键盘，黑色侧刻静音红轴",
"user_id": 179010,
"user_nick_name": "快乐的码农",
"userAvatar": "http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=179010&size=middle",
"last_reply_date": "1534347650000",
"vote": 0,
"hot": 0,
"hits": 11,
"replies": 1,
"essence": 0,
"top": 0,
"status": 0,
"subject": "成色很新，很少使用，键盘比较多，用不到就换钱吧",
"pic_path": "",
"ratio": "1",
"gender": 0,
"userTitle": "逆戟鲸 (Lv.12)",
"recommendAdd": 0,
"special": 0,
"isHasRecommendAdd": 0,
"imageList": [],
"sourceWebUrl": "http://bbs.uestc.edu.cn/forum.php?mod=viewthread&tid=1729954",
"verify": []
}
],
"page": 1,
"has_next": 1,
"total_num": 1574982
}
 */
data class PostResultBean(var rs: String? = null, var errcode: String? = null,
                          var head: ResultHeadBean? = null, var body: Any? = null,
                          var list: List<SimplePostBean>? = null, var page: Int? = null,
                          var has_next: Int? = null, var total_num: Int? = null)

data class SimplePostBean(var boadrd_id: String? = null, var board_name: String? = null,
                          var topic_id: String? = null, var type: String? = null,
                          var title: String? = null, var user_id: String? = null,
                          var user_nick_name: String? = null, var userAvatar: String? = null,
                          var last_reply_date: String? = null, var vote: String? = null,
                          var hot: String? = null, var hits: String? = null,
                          var replies: String? = null, var essence: String? = null,
                          var top: String? = null, var status: String? = null,
                          var subject: String? = null, var pic_path: String? = null,
                          var retio: String? = null, var gender: String? = null,
                          var userTitle: String? = null, var recommendAdd: String? = null,
                          var imageList:Array<String>? = null, var sourceWebUrl: String? = null,
                          var veryfy: String? = null)