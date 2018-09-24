package com.febers.uestc_bbs.entity;

import java.util.List;

public class MsgPrivateBean extends MsgBaseBean{

    private int rs;
    private String errcode;
    private HeadBean head;
    private BodyBean body;

    public int getRs() {
        return rs;
    }

    public void setRs(int rs) {
        this.rs = rs;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class HeadBean {
        /**
         * errCode : 00000000
         * errInfo : 调用成功,没有任何错误
         * version : 2.6.1.7
         * alert : 0
         */

        private String errCode;
        private String errInfo;
        private String version;
        private int alert;

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getErrInfo() {
            return errInfo;
        }

        public void setErrInfo(String errInfo) {
            this.errInfo = errInfo;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getAlert() {
            return alert;
        }

        public void setAlert(int alert) {
            this.alert = alert;
        }
    }

    public static class BodyBean {
        /**
         * externInfo : {"padding":""}
         * list : [{"plid":4021478,"pmid":4021478,"lastUserId":216786,"lastUserName":"xylly123","lastSummary":"发图给你看","lastDateline":"1537273815000","toUserId":216786,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=216786&size=middle","toUserName":"xylly123","toUserIsBlack":0,"isNew":0},{"plid":4017884,"pmid":4017884,"lastUserId":97380,"lastUserName":"有道词典","lastSummary":"关于您在\u201cAndroid端校园服务App\u201ci成电\u201d开始公测啦\u201d的帖子\n如果考虑请加我微信18258257729","lastDateline":"1531132114000","toUserId":97380,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=97380&size=middle","toUserName":"有道词典","toUserIsBlack":0,"isNew":0},{"plid":4017628,"pmid":4017628,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"你好，已经转了","lastDateline":"1530870506000","toUserId":195320,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=195320&size=middle","toUserName":"趁着天黑撒个野","toUserIsBlack":0,"isNew":0},{"plid":4015729,"pmid":4015729,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"Qq","lastDateline":"1529337772000","toUserId":210964,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=210964&size=middle","toUserName":"元寒","toUserIsBlack":0,"isNew":0},{"plid":4014088,"pmid":4014088,"lastUserId":181657,"lastUserName":"superdan","lastSummary":"其实还好&nbsp;&nbsp;但是Java 和oc还是要会才行","lastDateline":"1527895419000","toUserId":181657,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=181657&size=middle","toUserName":"superdan","toUserIsBlack":0,"isNew":0},{"plid":4012000,"pmid":4012000,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":":D 可以的","lastDateline":"1527609132000","toUserId":185896,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185896&size=middle","toUserName":"vizards","toUserIsBlack":0,"isNew":0},{"plid":4012872,"pmid":4012872,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"\u2026好吧，我也不怎么清楚😅","lastDateline":"1527524405000","toUserId":109601,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=109601&size=middle","toUserName":"20111216","toUserIsBlack":0,"isNew":0},{"plid":4013458,"pmid":4013458,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"不好意思，已经不在了","lastDateline":"1527518503000","toUserId":152584,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=152584&size=middle","toUserName":"没有昵称","toUserIsBlack":0,"isNew":0},{"plid":4012879,"pmid":4012879,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"嗯，我这个是老版本，2200吧，应该挺良心的，如果你有意向的话来看一下机器吧，因为屏幕有个很小的白色亮点，平时注意不到，我好像发不了图片，你可以加我的qq，2303395307 ... ...","lastDateline":"1526048895000","toUserId":200115,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=200115&size=middle","toUserName":"畔畔雪饼","toUserIsBlack":0,"isNew":0},{"plid":4007273,"pmid":4007273,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"\u2026\u2026不好意思，很久没上河畔。显卡很早就不在了。","lastDateline":"1524336313000","toUserId":185483,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185483&size=middle","toUserName":"313649707","toUserIsBlack":0,"isNew":0},{"plid":4008058,"pmid":4008058,"lastUserId":181000,"lastUserName":"幽梦随风","lastSummary":"关于您在\u201c收一个750ti 显卡\u201d的帖子\n在吗。750ti还在吗","lastDateline":"1516089026000","toUserId":181000,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=181000&size=middle","toUserName":"幽梦随风","toUserIsBlack":0,"isNew":0},{"plid":4006937,"pmid":4006937,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"你可以直接加我的qq，2303395307","lastDateline":"1515149387000","toUserId":120445,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=120445&size=middle","toUserName":"兰茝","toUserIsBlack":0,"isNew":0},{"plid":4007285,"pmid":4007285,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"我的qq 2303395307","lastDateline":"1514435509000","toUserId":162922,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=162922&size=middle","toUserName":"pengliwen_001","toUserIsBlack":0,"isNew":0},{"plid":4007266,"pmid":4007266,"lastUserId":206908,"lastUserName":"一往无前虎山行","lastSummary":"120出给我行嘛","lastDateline":"1514352007000","toUserId":206908,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=206908&size=middle","toUserName":"一往无前虎山行","toUserIsBlack":0,"isNew":0},{"plid":4006320,"pmid":4006320,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"同学，我没法加你啊，验证通不过","lastDateline":"1512441663000","toUserId":211852,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=211852&size=middle","toUserName":"chenqiao327","toUserIsBlack":0,"isNew":0},{"plid":4006129,"pmid":4006129,"lastUserId":210827,"lastUserName":"洋宝贝","lastSummary":"这个要怎么看","lastDateline":"1512056610000","toUserId":210827,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=210827&size=middle","toUserName":"洋宝贝","toUserIsBlack":0,"isNew":0},{"plid":4005956,"pmid":4005956,"lastUserId":185610,"lastUserName":"luanliangjidong","lastSummary":"首字母 l","lastDateline":"1511795246000","toUserId":185610,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185610&size=middle","toUserName":"luanliangjidong","toUserIsBlack":0,"isNew":0},{"plid":4005639,"pmid":4005639,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"不出了","lastDateline":"1511520211000","toUserId":193821,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=193821&size=middle","toUserName":"他夏了夏天","toUserIsBlack":0,"isNew":0},{"plid":4005574,"pmid":4005574,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"关于您在\u201c出java和Hadoop相关书籍，有买有送\u201d的帖子\n你好，预定maven实战，重构，写给大忙人的java，java web四本书 ...","lastDateline":"1511083642000","toUserId":128268,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=128268&size=middle","toUserName":"煙雨夕陽","toUserIsBlack":0,"isNew":0},{"plid":4005402,"pmid":4005402,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"请问se出吗","lastDateline":"1510797464000","toUserId":91132,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=91132&size=middle","toUserName":"铜镜","toUserIsBlack":0,"isNew":0},{"plid":4005318,"pmid":4005318,"lastUserId":199793,"lastUserName":"cztt123","lastSummary":"756352872&nbsp;&nbsp;备注内存卡/滑稽","lastDateline":"1510655881000","toUserId":199793,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=199793&size=middle","toUserName":"cztt123","toUserIsBlack":0,"isNew":0},{"plid":4005107,"pmid":4005107,"lastUserId":186603,"lastUserName":"树袋笨熊","lastSummary":"不好意思，5s已出手","lastDateline":"1510469996000","toUserId":186603,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=186603&size=middle","toUserName":"树袋笨熊","toUserIsBlack":0,"isNew":0},{"plid":4005131,"pmid":4005131,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"关于您在\u201c出索尼MP3（A35)+索尼蓝牙耳机(EX750BT)-多图\u201d的帖子\n你好，有意MP3和耳机，请问怎么联系","lastDateline":"1510392199000","toUserId":19694,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=19694&size=middle","toUserName":"snowtony119","toUserIsBlack":0,"isNew":0},{"plid":4004695,"pmid":4004695,"lastUserId":185098,"lastUserName":"smilezxin","lastSummary":"关于您在\u201c收一个6s\u201d的帖子\n我要，手机、微信：18355302906","lastDateline":"1509678255000","toUserId":185098,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185098&size=middle","toUserName":"smilezxin","toUserIsBlack":0,"isNew":0},{"plid":4004410,"pmid":4004410,"lastUserId":189987,"lastUserName":"云中飘摇","lastSummary":"还在，还有一块","lastDateline":"1509632053000","toUserId":189987,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=189987&size=middle","toUserName":"云中飘摇","toUserIsBlack":0,"isNew":0},{"plid":4004562,"pmid":4004562,"lastUserId":182619,"lastUserName":"zjjxt","lastSummary":"都可以，","lastDateline":"1509539159000","toUserId":182619,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=182619&size=middle","toUserName":"zjjxt","toUserIsBlack":0,"isNew":0},{"plid":4004414,"pmid":4004414,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"不好意思，已出","lastDateline":"1509339584000","toUserId":192146,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=192146&size=middle","toUserName":"chen1991","toUserIsBlack":0,"isNew":0},{"plid":4004424,"pmid":4004424,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"我擦，点错了，本来是回复评论的","lastDateline":"1509338960000","toUserId":90344,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=90344&size=middle","toUserName":"幻想余","toUserIsBlack":0,"isNew":0},{"plid":4004412,"pmid":4004412,"lastUserId":121617,"lastUserName":"唤起朝阳","lastSummary":"关于您在\u201c出64g玫瑰金iPhone6s\u201d的帖子\n6S多少出\r\n？","lastDateline":"1509325148000","toUserId":121617,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=121617&size=middle","toUserName":"唤起朝阳","toUserIsBlack":0,"isNew":0},{"plid":4004406,"pmid":4004406,"lastUserId":188571,"lastUserName":"钧娃1","lastSummary":"我回了啊，norwiziki","lastDateline":"1509285226000","toUserId":188571,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=188571&size=middle","toUserName":"钧娃1","toUserIsBlack":0,"isNew":0},{"plid":4004401,"pmid":4004401,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"已出","lastDateline":"1509284616000","toUserId":181253,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=181253&size=middle","toUserName":"201102305656","toUserIsBlack":0,"isNew":0},{"plid":4004344,"pmid":4004344,"lastUserId":185750,"lastUserName":"七月流火92","lastSummary":"可以的，我在KB504，电话15528121686","lastDateline":"1509256261000","toUserId":185750,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=185750&size=middle","toUserName":"七月流火92","toUserIsBlack":0,"isNew":0},{"plid":4004373,"pmid":4004373,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"去年高考的礼物，用了一年","lastDateline":"1509253407000","toUserId":183642,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=183642&size=middle","toUserName":"嫊颜","toUserIsBlack":0,"isNew":0},{"plid":4004341,"pmid":4004341,"lastUserId":192649,"lastUserName":"hyacinth.9697","lastSummary":"关于您在\u201c收一个6s\u201d的帖子\n同学你的6s还在吗？有意，给个联系方式吧","lastDateline":"1509164159000","toUserId":192649,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=192649&size=middle","toUserName":"hyacinth.9697","toUserIsBlack":0,"isNew":0},{"plid":4004338,"pmid":4004338,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"关于您在\u201c收一个6s\u201d的帖子\nqq 2303395307","lastDateline":"1509163046000","toUserId":190495,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=190495&size=middle","toUserName":"760369653","toUserIsBlack":0,"isNew":0},{"plid":4004296,"pmid":4004296,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"你好，有意java面试宝典，qq2303395307","lastDateline":"1508955077000","toUserId":118541,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=118541&size=middle","toUserName":"lxy789123","toUserIsBlack":0,"isNew":0},{"plid":4004160,"pmid":4004160,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"关于您在\u201cddr4 8g 2400 全新，还有送两本算是。。摄影书？\u201d的帖子\n请问楼主内存条还在吗","lastDateline":"1508600450000","toUserId":100156,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=100156&size=middle","toUserName":"mynamefalcon","toUserIsBlack":0,"isNew":0},{"plid":4002518,"pmid":4002518,"lastUserId":199767,"lastUserName":"acne","lastSummary":"您好&nbsp;&nbsp;暂时还没出 我的微信","lastDateline":"1505302944000","toUserId":199767,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=199767&size=middle","toUserName":"acne","toUserIsBlack":0,"isNew":0},{"plid":4002514,"pmid":4002514,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"关于您在\u201c出一个21.5显示器\u201d的帖子\n你好，显示器380能出吗，我在清水河","lastDateline":"1505298371000","toUserId":93370,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=93370&size=middle","toUserName":"foreverhit","toUserIsBlack":0,"isNew":0},{"plid":4002359,"pmid":4002359,"lastUserId":196486,"lastUserName":"四条眉毛","lastSummary":"你好，第一行代码第二版还出吗？","lastDateline":"1504975256000","toUserId":181726,"toUserAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=181726&size=middle","toUserName":"损人不倦","toUserIsBlack":0,"isNew":0}]
         * hasNext : 0
         * count : 40
         */

        private ExternInfoBean externInfo;
        private int hasNext;
        private int count;
        private List<ListBean> list;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public int getHasNext() {
            return hasNext;
        }

        public void setHasNext(int hasNext) {
            this.hasNext = hasNext;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ExternInfoBean {
            /**
             * padding :
             */

            private String padding;

            public String getPadding() {
                return padding;
            }

            public void setPadding(String padding) {
                this.padding = padding;
            }
        }

        public static class ListBean {
            /**
             * plid : 4021478
             * pmid : 4021478
             * lastUserId : 216786
             * lastUserName : xylly123
             * lastSummary : 发图给你看
             * lastDateline : 1537273815000
             * toUserId : 216786
             * toUserAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=216786&size=middle
             * toUserName : xylly123
             * toUserIsBlack : 0
             * isNew : 0
             */

            private int plid;
            private int pmid;
            private int lastUserId;
            private String lastUserName;
            private String lastSummary;
            private String lastDateline;
            private int toUserId;
            private String toUserAvatar;
            private String toUserName;
            private int toUserIsBlack;
            private int isNew;

            public int getPlid() {
                return plid;
            }

            public void setPlid(int plid) {
                this.plid = plid;
            }

            public int getPmid() {
                return pmid;
            }

            public void setPmid(int pmid) {
                this.pmid = pmid;
            }

            public int getLastUserId() {
                return lastUserId;
            }

            public void setLastUserId(int lastUserId) {
                this.lastUserId = lastUserId;
            }

            public String getLastUserName() {
                return lastUserName;
            }

            public void setLastUserName(String lastUserName) {
                this.lastUserName = lastUserName;
            }

            public String getLastSummary() {
                return lastSummary;
            }

            public void setLastSummary(String lastSummary) {
                this.lastSummary = lastSummary;
            }

            public String getLastDateline() {
                return lastDateline;
            }

            public void setLastDateline(String lastDateline) {
                this.lastDateline = lastDateline;
            }

            public int getToUserId() {
                return toUserId;
            }

            public void setToUserId(int toUserId) {
                this.toUserId = toUserId;
            }

            public String getToUserAvatar() {
                return toUserAvatar;
            }

            public void setToUserAvatar(String toUserAvatar) {
                this.toUserAvatar = toUserAvatar;
            }

            public String getToUserName() {
                return toUserName;
            }

            public void setToUserName(String toUserName) {
                this.toUserName = toUserName;
            }

            public int getToUserIsBlack() {
                return toUserIsBlack;
            }

            public void setToUserIsBlack(int toUserIsBlack) {
                this.toUserIsBlack = toUserIsBlack;
            }

            public int getIsNew() {
                return isNew;
            }

            public void setIsNew(int isNew) {
                this.isNew = isNew;
            }
        }
    }
}
