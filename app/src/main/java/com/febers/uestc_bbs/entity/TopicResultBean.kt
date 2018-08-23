/*
 * Created by Febers at 18-8-16 上午12:24.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-16 上午12:24.
 */

package com.febers.uestc_bbs.entity

data class TopicResultBean(var rs: String? = null, var errcode: String? = null,
                           var head: ResultHeadBean? = null, var body: Any? = null,
                           var list: List<SimpleTopicBean>? = null, var page: Int? = null,
                           var has_next: Int? = null, var total_num: Int? = null)

data class SimpleTopicBean(var board_id: String? = null, var board_name: String? = null,
                           //左边普通，右边为热门帖子的id
                           var topic_id: String? = null, var source_id: String? = null,

                           var type: String? = null,
                           var title: String? = null, var user_id: String? = null,
                           var user_nick_name: String? = null, var userAvatar: String? = null,
                           var last_reply_date: String? = null, var vote: String? = null,
                           var hot: String? = null, var hits: String? = null,
                           var replies: String? = null, var essence: String? = null,
                           var top: String? = null, var status: String? = null,
                            //左边为普通，右边为热门帖子的summary
                           var subject: String? = null, var summary: String? = null,
                           var pic_path: String? = null, var retio: String? = null,
                           var gender: String? = null, var userTitle: String? = null,
                           var recommendAdd: String? = null, var imageList:Array<String>? = null,
                           var sourceWebUrl: String? = null, var veryfy: String? = null)