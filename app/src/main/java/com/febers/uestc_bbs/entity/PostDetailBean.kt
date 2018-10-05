/*
 * Created by Febers at 18-8-17 下午8:58.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:58.
 */

package com.febers.uestc_bbs.entity

data class PostResultBean(var rs: Int? = null, var errcode: String? = null, var head: ResultHeadBean? = null,
                          var body: Any? = null, var topic:PostTopicBean? = null, var page: String? = null, var has_next: Int? = null,
                          var total_num: String? = null, var list: List<PostReplyBean>? = null, var forumName: String? = null,
                          var boardId: String? = null, var forumTopicUrl: String? = null, var img_url: String? = null,
                          var icon_url: String? = null)

data class PostTopicBean(var topic_id: String? = null, var title: String? = null, var type: String?,
                         var special: String?, var sortId: String?, var user_id: String? = null,
                         var user_nick_name: String?, var replies: String?, var hits: String?,
                         var essence: String?, var vote: String?, var hot: String?,
                         var top: String?, var is_favor: Int?, var create_date: String?,
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
                         var position: Int?, var posts_date: String?, var icon: String?,
                         var level: String?, var userTitle: String?, var userColor: String?,
                         var location: String?, var mobileSign: String?, var reply_status: String?,
                         var status: String?, var role_num: String?, var title: String?,
                         var gender: String?, var is_quote: String?, var quote_pid: String?,
                         var quote_content: String?, var quote_user_name: String?, var delThread: String?,
                         var managePanel: List<Any>?, var extraPanel: List<PostExtraPanelBean>?)

