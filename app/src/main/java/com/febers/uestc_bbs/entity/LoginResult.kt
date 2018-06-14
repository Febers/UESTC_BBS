/*
 * Created by Febers at 18-6-13 下午5:15.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午8:27.
 */

package com.febers.uestc_bbs.entity

/**
 * 登录正确返回
 * {"rs":1,"errcode":"",
 *
 * "head":{"errCode":"00000000",
 * "errInfo":"\u8c03\u7528\u6210\u529f,\u6ca1\u6709\u4efb\u4f55\u9519\u8bef"(调用成功,没有任何错误),
 * "version":"2.6.1.7","alert":0},
 *
 * "body":{"externInfo":{"padding":""}},
 *
 * "isValidation":0,
 * "token":"f265c4405fa903d493da843e28e63",
 * "secret":"08a733a5054250923e04b4ee534dc",
 * "score":244,
 * "uid":196486,
 * "mUserName":"\u56db\u6761\u7709\u6bdb",
 * "avatar":"http:\/\/bbs.uestc.edu.cn\/uc_server\/avatar.php?uid=196486&size=middle",
 * "gender":0,
 * "userTitle":"\u6cb3\u87f9 (Lv.3)",
 * "repeatList":[],
 * "verify":[],
 * "creditShowList":[{"type":"credits","title":"\u79ef\u5206","data":244},  //积分
 * {"type":"extcredits2","title":"\u6c34\u6ef4","data":228}], //水滴
 * "mobile":"","groupid":10}
 *
 * 错误返回
 * {"rs":0,"errcode":"\u8f93\u5165\u7684\u7528\u6237\u540d\u4e3a\u7a7a"(//登录失败，您还可以尝试 n 次),
 * "head":{"errCode":"03000001",
 * "errInfo":"\u8f93\u5165\u7684\u7528\u6237\u540d\u4e3a\u7a7a",
 * "version":"2.6.1.7","alert":1},
 * "body":{"externInfo":{"padding":""}}}
 */

data class LoginResult(var rs: String, var errcode: String,
                       var head: LoginResultHead, var body: LoginResultBody,
                       var isValidation: String, var token: String, var secret: String,
                       var score: String, var uid: String, var userName: String, var avatar: String,
                       var gender: String, var userTitle: String, var repeatList: Any?, var verify: Any?,
                       var creditShowList: Any?, var mobile: String, var groupid: String)

data class LoginResultHead(var errCode: String, var errInfo: String, var version: String, var alert: String)

data class LoginResultBody(var externInfo: LoginResultExternInfo)

data class LoginResultExternInfo(var padding: String)
