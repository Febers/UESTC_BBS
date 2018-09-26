package com.febers.uestc_bbs.entity

class UserPListBean {
    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":""}}
     * list : [{"pic_path":"","board_id":70,"board_name":"程序员","topic_id":1718134,"type_id":127,"sort_id":0,"title":"[Java]Android端校园服务App\u201ci成电\u201d开始公测啦","subject":"利用课余时间，开发了一个Android端应用\u201ci成电\u201d，","user_id":196486,"last_reply_date":"1527518249000","user_nick_name":"四条眉毛","hits":295,"replies":23,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":61,"board_name":"二手专区","topic_id":1722000,"type_id":292,"sort_id":0,"title":"[清水河-其他]null","subject":"...null","user_id":196486,"last_reply_date":"1529297294000","user_nick_name":"四条眉毛","hits":119,"replies":6,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":70,"board_name":"程序员","topic_id":1720163,"type_id":127,"sort_id":0,"title":"[Java]Android校园服务平台\u201ci成电\u201d开源啦","subject":"关于\u201ci成电\u201d，可以参考前段时间发过的一个帖子：  。当","user_id":196486,"last_reply_date":"1528437797000","user_nick_name":"四条眉毛","hits":69,"replies":2,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":70,"board_name":"程序员","topic_id":1710104,"type_id":127,"sort_id":0,"title":"[Java]【已解决】Android自定义dialog无法隐藏问题","subject":"Android自定义一个登录dialog，点击取消按钮之","user_id":196486,"last_reply_date":"1522909447000","user_nick_name":"四条眉毛","hits":19,"replies":4,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":70,"board_name":"程序员","topic_id":1707366,"type_id":127,"sort_id":0,"title":"[Java]已解决","subject":"代码水平问题","user_id":196486,"last_reply_date":"1521017454000","user_nick_name":"四条眉毛","hits":0,"replies":0,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":70,"board_name":"程序员","topic_id":1703747,"type_id":127,"sort_id":0,"title":"[Java]字符串提取求助","subject":"字符串示例如下：          Android De","user_id":196486,"last_reply_date":"1517904708000","user_nick_name":"四条眉毛","hits":453,"replies":14,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":25,"board_name":"水手之家","topic_id":1700376,"type_id":319,"sort_id":0,"title":"[求助]求寒假宿舍住宿截止时间","subject":"如题，按照校历，寒假是从1月22日开始。春节不留校 ，但","user_id":196486,"last_reply_date":"1515036647000","user_nick_name":"四条眉毛","hits":418,"replies":14,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":61,"board_name":"二手专区","topic_id":1697705,"type_id":287,"sort_id":0,"title":"[清水河-数码硬件]【已失效】","subject":"。。。","user_id":196486,"last_reply_date":"1513393405000","user_nick_name":"四条眉毛","hits":829,"replies":23,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":25,"board_name":"水手之家","topic_id":1698035,"type_id":0,"sort_id":0,"title":"如何登录学校GP正版激活软件？","subject":"如题如图，试过信息门户、学生邮箱、甚至河畔帐号","user_id":196486,"last_reply_date":"1513615135000","user_nick_name":"四条眉毛","hits":217,"replies":6,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0},{"pic_path":"","board_id":61,"board_name":"二手专区","topic_id":1697415,"type_id":287,"sort_id":0,"title":"[清水河-数码硬件]清水河收SSD","subject":"rt，台式，100g以上","user_id":196486,"last_reply_date":"1513215397000","user_nick_name":"四条眉毛","hits":133,"replies":4,"top":0,"status":32,"essence":0,"hot":0,"userAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?mUid=196486&size=middle","special":0}]
     * page : 1
     * has_next : 1
     * total_num : 15
     */

    var rs: Int = 0
    var errcode: String? = null
    var head: HeadBean? = null
    var body: BodyBean? = null
    var page: Int = 0
    var has_next: Int = 0
    var total_num: Int = 0
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

    class ListBean {
        /**
         * pic_path :
         * board_id : 70
         * board_name : 程序员
         * topic_id : 1718134
         * type_id : 127
         * sort_id : 0
         * title : [Java]Android端校园服务App“i成电”开始公测啦
         * subject : 利用课余时间，开发了一个Android端应用“i成电”，
         * user_id : 196486
         * last_reply_date : 1527518249000
         * user_nick_name : 四条眉毛
         * hits : 295
         * replies : 23
         * top : 0
         * status : 32
         * essence : 0
         * hot : 0
         * userAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle
         * special : 0
         */

        var pic_path: String? = null
        var board_id: Int = 0
        var board_name: String? = null
        var topic_id: Int = 0
        var type_id: Int = 0
        var sort_id: Int = 0
        var title: String? = null
        var subject: String? = null
        var user_id: Int = 0
        var last_reply_date: String? = null
        var user_nick_name: String? = null
        var hits: Int = 0
        var replies: Int = 0
        var top: Int = 0
        var status: Int = 0
        var essence: Int = 0
        var hot: Int = 0
        var userAvatar: String? = null
        var special: Int = 0
    }
}
