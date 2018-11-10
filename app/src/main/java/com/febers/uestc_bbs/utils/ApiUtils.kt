/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.utils

object ApiUtils {

    const val BBS_BASE_URL = "http://bbs.uestc.edu.cn/"
    /**
     * 登录、注销。
     */
    const val BBS_LOGIN_URL = "mobcent/app/web/index.php?r=user/login"

    /**
     * 热门帖子：
        page:1
        pageSize:10
        moduleId:2
        r:portal/newslist
     最新发表/回复
        boardId:0
        page:1
        pageSize:5
        sortby:new/all
        r:forum/topiclist
     */
    const val BBS_HOME_POST_URL = "mobcent/app/web/index.php"

    /**
     * 设置用户位置（与周边用户/帖子功能配合使用）。
        longitude 经度
        latitude 纬度
        location 地址
     */
    const val BBS_LOCATION_URL = "mobcent/app/web/index.php?r=user/location"

    /**
     * 修改用户信息(头像，密码，性别，签名)
        avatar
        sign
        gender
        oldPassword
        newPassword
     */
    const val BBS_UPDATE_USER_INFO_URL = "mobcent/app/web/index.php?r=user/updateuserinfo"
    const val BBS_UPDATE_USER_SIGN_URL = "mobcent/app/web/index.php?r=user/updateusersign"
    const val BBS_UPDATE_USER_GENDER_URL = "mobcent/app/web/index.php?r=user/updateusergender"
    const val BBS_UPDATE_USER_AVATAR_URL = "mobcent/app/web/index.php?r=user/uploadavatarex"

    /**
        获取用户发布的帖子
     */
    const val BBS_USER_START_URL = "mobcent/app/web/index.php?r=user/topiclist&type=topic"

    const val BBS_USER_REPLY_URL = "mobcent/app/web/index.php?r=user/topiclist&type=reply"

    const val BBS_USER_FAV_URL = "mobcent/app/web/index.php?r=user/topiclist&type=favorite"

    /**
     * 获取用户详细信息
     */
    const val BBS_USER_INFO_URL = "mobcent/app/web/index.php?r=user/userinfo"

    /**
     * 获取版块列表。
        fid 可选。获取指定版块的子版块。
        type 待确认，但是我们可能用不到。
     */
    const val BBS_FORUM_LIST = "mobcent/app/web/index.php?r=forum/forumlist"

    /**
     * 获取某一版块的主题列表。
        boardId 相当于 fid。
        page
        pageSize
        sortby 'publish' == 'new', 'essence' == 'marrow', 'top', 'photo', 'all'（默认）
        filterType 'sortid', 'typeid'
        filterId 分类 ID，只返回指定分类的主题。
        isImageList
        topOrder 0（不返回置顶帖，默认）, 1（返回本版置顶帖）, 2（返回分类置顶帖）, 3（返回全局置顶帖）。置顶帖包含在 topTopicList 字段中。
    */
    const val BBS_TOPIC_LIST_URL = "mobcent/app/web/index.php?r=forum/topiclist"

    /**
     * 获取帖子的回复列表。
        topicId
        authorId 只返回指定作者的回复，默认为 0 返回所有回复。
        order 0 或 1（回帖倒序排列）
        page
        pageSize
     */
    const val BBS_POST_LIST_URL = "mobcent/app/web/index.php?r=forum/postlist"

    /**
     * 发帖/回复。
        act 'new'（发帖）、'reply'（回复）、其他字符串（编辑）
        json JSON 格式的发帖内容。
        JSON 格式：
        {
        "body": {
        "json": {
        "fid": 123, // 发帖时指定版块。
        "tid": 123456, // 回复时指定帖子。
        "typeOption": ...,
        "isAnonymous": 1, // 1 表示匿名发帖。
        "isOnlyAuthor": 1, // 1 表示回帖仅作者可见。
        "typeId": 1234, // 分类。
        "isQuote": 1, 是否引用之前回复的内容。
        "replyId": 123456, 回复 ID（pid）。
        "title": "Title", // 标题。
        "aid": "1,2,3", // 附件 ID，逗号隔开。
        "content": "又是一个 JSON 字符串，格式见下面。",
        "location": "格式待确认"
        }
        }
        }
        body.json.content 格式：

        [
        {
        "type": 0, // 0：文本（解析链接）；1：图片；3：音频;4:链接;5：附件
        "infor": "发帖内容|图片 URL|音频 URL"
        },
        ...
        ]
     */
    const val BBS_TOPIC_ADMIN_URL = "mobcent/app/web/index.php?r=forum/topicadmin"

    /**
     * 搜索（参数和 Discuz! search.php 一致）。
        keyword
        page
        pageSize
        searchid
     */
    const val BBS_SEARCH_URL = "mobcent/app/web/index.php?r=forum/search"

    /**
     * 发送附件的高级版本，可以将图片上传到相册。
        type 'image', 'audio'
        module 'forum', 'album', 'pm'
        albumId
        返回值

        body
        attachment 数组。
        id 附件 ID，发帖时在 aid 参数中指定。
        urlName 附件 URL，发帖时在 infor 字段中指定。
     */
    const val BBS_SEND_ATTACHMENTEX_URL = "mobcent/app/web/index.php?r=forum/sendattachmentex"

    /**
     * 收藏帖子
     */
    const val BBS_POST_FAVORITE_URL = "mobcent/app/web/index.php?r=user/userfavorite"

    /**
     * 投票。
        tid
        options 投票选项，逗号隔开。
        返回值

        vote_rs 数组。
        name
        pollItemId
        totalNum
     */
    const val BBS_VOTE_URL = "mobcent/app/web/index.php?r=forum/vote"


    /**
     * 对回复进行支持/反对操作。
        tid
        pid
        type 'topic'（默认）== 'thread', 'post'
        action 'support'（默认）, 'against'
     */
    const val BBS_SUPPORT_URL = "mobcent/app/web/index.php?r=forum/support"

    /**
     * 站点公告。

        id
     */
    const val BBS_ANNOUNCEMENT_URL = "mobcent/app/web/index.php?r=forum/announcement"

    /**
     * 获取可以@的好友。（相当于河畔编辑器里单击@朋友后弹出的列表。）
        page
        pageSize
        返回数据

        page
        total_num
        has_next
        list
        mUid
        name
        role_num
     */
    const val BBS_AT_USER_LIST_URL = "mobcent/app/web/index.php?r=forum/atuserlist"

    /**
     * 获取包含图片的帖子。
        page
        pageSize
     */
    const val BBS_PHOTO_GALLERY_URL = "mobcent/app/web/index.php?r=forum/photogallery"

    /**
     * 评分记录查询。返回的 HTML。
        tid
        pid
     */
    const val BBS_RATE_LIST_VIEW_URL = "mobcent/app/web/index.php?r=forum/ratelistview"

    /**
     * 活动帖参加、取消。
        tid
        act 'apply'（默认）, 'cancel'
        json JSON 字符串。
        JSON 格式：
        {
        "payment": , //支付的积分
        "message": , // 附加留言
        // 其他参数（活动时填写的信息）
        }
     */
    const val BBS_TOPIC_ACTIVITY_URL = "mobcent/app/web/index.php?r=forum/topicactivity"

    /**
     * 参加活动。返回是 HTML。
        tid
        act
     */
    const val BBS_TOPIC_ACTIVITY_VIEW_URL = "mobcent/app/web/index.php?r=forum/topicactivityview"

    /**
     * 管理操作。返回 HTML。
        fid
        tid
        pid
        act
        type 'topic'（默认）
     */
    const val BBS_TOPIC_ADMIN_VIEW_URL = "mobcent/app/web/index.php?r=forum/topicadminview"

    /**
     * 评分。返回 HTML。

        tid
        pid
     */
    const val BBS_TOPIC_RATE_URL = "mobcent/app/web/index.php?r=forum/topicrate"


    /**
     * 查询新提醒数目（每隔一段时间查询）。
     */
    const val BBS_MESSAGE_HEART_URL = "mobcent/app/web/index.php?r=message/heart"


    /**
     * 各种提醒列表。
        type 'post'（帖子）, 'at'（@消息）, 'system'（系统）
        page
        pageSize
     */
    const val BBS_MESSAGE_NOTIFY_LIST = "mobcent/app/web/index.php?r=message/notifylist"

    /**
    获取私信列表。
     * json 必选。
     * JSON 格式：
    {
    "page": 1, // 可选，默认为 1。
    "pageSize": 10 // 可选，默认为 10。
    }
     */
    const val BBS_MESSAGE_PM_SESSION_LIST = "mobcent/app/web/index.php?r=message/pmsessionlist"

    /**
     * 短消息列表。
        pmlist JSON 字符串。
        JSON 格式：
        {
        "body": {
        "externInfo": {
        "onlyFromUid": 0, // 只返回收到的消息（不包括自己发出去的消息）。
        },
        "pmInfos": {
        "startTime": , // 开始时间（以毫秒为单位）。startTime 和 stopTime 均为 0 表示获取最新（未读）消息，如果要获取历史消息指定一个较早的时间。
        "stopTime": , // 结束时间（以毫秒为单位），为零表示获取晚于 startTime 的所有消息。
        "cacheCount": 1,
        "fromUid": 123, // UID，必须指定。
        "pmLimit": 10, // 最多返回几条结果，默认为 15。
        }
        }
        }
     */
    const val BBS_MESSAGE_PM_LIST = "mobcent/app/web/index.php?r=message/pmlist"

    /**
    短消息管理（发送、删除）。
     * json
     * JSON 格式：
        {
        "action": "send", // 'send'（默认）, 'delplid'（删除和某人的所有会话）, 'delpmid'
        "toUid": 12345, // send
        "valid": {
        "type": "text", // 'text', 'image', 'audio'
        "content": "消息内容（经过 URL 编码）/图片 URL/音频 URL"
        }, // send
        "plid": 123, // delplid（并非 mUid）
        "pmid": 123  // delpmid
        }
     */
    const val BBS_MESSAGE_PM_ADMIN = "mobcent/app/web/index.php?r=message/pmadmin"


    /**
     获取周边用户/帖子。
        longitude 经度。
        latitude 纬度。
        poi 'user'（默认）或 'topic'。
        page
        pageSize
        radius 半径，单位为米（默认为 100000）。
     */
    const val BBS_SURROUNDING = "mobcent/app/web/index.php?r=square/surrounding"

    const val BUGLY_APP_ID = "7b30a56c4e"
}