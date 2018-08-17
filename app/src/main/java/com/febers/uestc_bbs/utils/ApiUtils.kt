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
        type 'login'（默认值）或 'logout'。
        username
        password
        mobile （用于手机验证登录，我们用不到，下同）
        code
        isValidation
        返回值
        secret
        token
        avatar 头像 URL。
        uid
        mUserName
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

    /**
     * 获取版块列表。
        fid 可选。获取指定版块的子版块。
        type 待确认，但是我们可能用不到。
        返回值

        list 数组。
        board_category_name 版块名称。
        board_category_id 相当于河畔上的 gid。
        board_category_type
        board_list 数组，包含分栏下的版块。
        board_child 是否有子版块。
        board_content 是否为空版块（不能发帖，只有子版块，例如学院在线）。
        board_id 相当于河畔上的 fid。
        board_img
        board_name 版块名称。
        description
        forumRedirect
        last_posts_date 最后发表时间。
        posts_total_num 总发帖量。
        td_posts_num 今日发帖量。
        topic_total_num 主题总数。
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
        返回值

        list 数组。
        topic_id
        type
        title （包含分类信息）
        subject （仅包含标题）
        imageList
        sourceWebUrl
        user_id
        user_nick_name
        userAvatar
        gender
        last_reply_date
        vote 是否为投票帖。
        hot
        hits
        replies
        essence
        top
        status
        pic_path
        ratio
        recommendAdd
        isHasRecommendAdd
        board_id
        board_name
        topTopicList 数组，包含置顶帖。
        id
        title
        page
        hasNext
        total_num
        newTopicPanel 数组。
        type （nomal、vote……）
        action （空）
        title （发表帖子、发起投票）
        classificationTop_list 数组。
        classificationType_list 数组。
        classificationType_id
        classificationType_name
        isOnlyTopicType
        anno_list 数组。
        forumInfo
        id
        title
        description
        icon
     */
    const val BBS_TOPOIC_LIST_URL = "mobcent/app/web/index.php?r=forum/topiclist"

    /**
     * 获取帖子的回复列表。
        topicId
        authorId 只返回指定作者的回复，默认为 0 返回所有回复。
        order 0 或 1（回帖倒序排列）
        page
        pageSize
        返回值

        page
        has_next
        total_num
        list 数组。回复列表，不包含楼主。
        reply_id 内部使用？
        reply_content 回复内容（结构参考发帖时的 content 字段）。
        reply_type
        reply_name 用户名
        reply_posts_id 回复 pid。
        position 楼层编号。
        posts_date 回复时间。
        icon 头像 URL。
        level
        userTitle 用户组
        location
        mobileSign
        reply_status
        status
        role_num
        title
        is_quote
        quote_pid
        quote_content
        quote_user_name
        managePanel
        extraPanel 参见 topic 字段。
        topic 字段与 forum/topiclist 返回值中 list 项目类似
        两者均包含 topic_id, title, type, user_id, user_nick_name, replies, hits, essence, vote, hot, top, status, gender 这些字段，topic 字段不包含 board_id, board_name, last_reply_date, subject, pic_path, ratio, userAvatar, recommendAdd, isHasRecommendAdd, imageList, sourceWebUrl，此外还包含下列字段：
        sortId
        is_favor
        create_date 发帖时间
        icon 头像？
        level
        userTitle 用户组
        content 主题帖内容（结构参考发帖时的 content 字段）。
        poll_info
        deadline
        is_visible
        voters
        type
        poll_status
        poll_id 数组。
        poll_item_list 数组。
        name
        poll_item_id
        total_num
        percent
        activityInfo
        location
        managePanel
        extraPanel 数组。
        action 相当于 HTML <form> action 属性？
        title 操作（例如“评分”）。
        type 如果是评分则为 'rate'。
        extParams
        beforeAction 执行前请求的 URL？
        mobileSign
        reply_status
        flag
        reply_posts_id 楼主（1 楼） pid。
        rateList
        padding rateList 为空时包含该字段。
        head 表头字段名
        field1
        field2
        field3
        body 数组。相当于 <tbody> 每一行。
        field1
        field2
        field3
        total 总计。
        field1
        field2
        field3
        showAllUrl 显示所有评分记录的 URL（forum/ratelistview）。
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
     * 投票。
        tid
        options 投票选项，逗号隔开。
        返回值

        vote_rs 数组。
        name
        pollItemId
        totalNum
     */
    const val BBS_VOTE_URL = "mobcent/app/web/index.php?r=forum/sendattachmentex"


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
        uid
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
     * 提醒列表。
        type 'post'（帖子）, 'at'（@消息）, 'friend'（好友？）
        page
        pageSize
     */
    const val BBS_MESSAGE_NOTYFY_LIST = "mobcent/app/web/index.php?r=message/notifylist"

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
        "plid": 123, // delplid（并非 uid）
        "pmid": 123  // delpmid
        }
     */
    const val BBS_MESSAGE_PM_ADMIN = "mobcent/app/web/index.php?r=message/pmadmin"

    /**
    获取短消息会话列表。
     * json 必选。
     * JSON 格式：
        {
        "page": 1, // 可选，默认为 1。
        "pageSize": 10 // 可选，默认为 10。
        }
     */
    const val BBS_MESSAGE_PM_SESSION_LIST = "mobcent/app/web/index.php?r=message/pmsessionlist"

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
}