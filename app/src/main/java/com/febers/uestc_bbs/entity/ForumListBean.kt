/*
 * Created by Febers at 18-8-15 下午9:06.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午9:06.
 */

package com.febers.uestc_bbs.entity

/**
 * {
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
"list": [
{
"board_id": 108,
"board_name": "硬件数码",
"description": "",
"board_child": 0,
"board_img": "http://bbs.uestc.edu.cn/data/attachment/common/star/common_108_icon.png",
"last_posts_date": "1534330272000",
"board_content": 1,
"forumRedirect": "",
"favNum": 13,
"td_posts_num": 5,
"topic_total_num": 18840,
"posts_total_num": 247059,
"is_focus": 0
},
{
"board_id": 99,
"board_name": "Unix/Linux",
"description": "",
"board_child": 0,
"board_img": "http://bbs.uestc.edu.cn/data/attachment/common/star/common_99_icon.png",
"last_posts_date": "1534164002000",
"board_content": 1,
"forumRedirect": "",
"favNum": 57,
"td_posts_num": 0,
"topic_total_num": 9587,
"posts_total_num": 129602,
"is_focus": 0
},
{
"board_id": 70,
"board_name": "程序员",
"description": "",
"board_child": 1,
"board_img": "http://bbs.uestc.edu.cn/data/attachment/common/star/common_70_icon.png",
"last_posts_date": "1534250885000",
"board_content": 1,
"forumRedirect": "",
"favNum": 116,
"td_posts_num": 0,
"topic_total_num": 16653,
"posts_total_num": 179913,
"is_focus": 0
},
{
"board_id": 121,
"board_name": "电子设计",
"description": "",
"board_child": 1,
"board_img": "http://bbs.uestc.edu.cn/data/attachment/common/star/common_121_icon.png",
"last_posts_date": "1533569405000",
"board_content": 1,
"forumRedirect": "",
"favNum": 28,
"td_posts_num": 0,
"topic_total_num": 11243,
"posts_total_num": 108704,
"is_focus": 0
}
]
},
{
"board_category_id": 1,
"board_category_name": "站务管理",
"board_category_type": 2,
"board_list": [
{
"board_id": 2,
"board_name": "站务公告",
"description": "",
"board_child": 1,
"board_img": "http://bbs.uestc.edu.cn/data/attachment/common/star/common_2_icon.png",
"last_posts_date": "1534145966000",
"board_content": 1,
"forumRedirect": "",
"favNum": 6,
"td_posts_num": 0,
"topic_total_num": 1767,
"posts_total_num": 319704,
"is_focus": 0
},
{
"board_id": 46,
"board_name": "站务综合",
"description": "",
"board_child": 1,
"board_img": "http://bbs.uestc.edu.cn/data/attachment/common/star/common_46_icon.png",
"last_posts_date": "1534227117000",
"board_content": 1,
"forumRedirect": "",
"favNum": 16,
"td_posts_num": 0,
"topic_total_num": 9220,
"posts_total_num": 119137,
"is_focus": 0
}
]
}
],
"online_user_num": 0,
"td_visitors": 0
}
 */
data class ForumListBean(var rs: String)
data class ForumItemBean(var board_id: String)