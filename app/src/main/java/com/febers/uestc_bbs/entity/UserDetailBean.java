package com.febers.uestc_bbs.entity;

import java.util.List;

public class UserDetailBean {

    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":""},"repeatList":[],"profileList":[{"type":"gender","title":"性别","data":"女"},{"type":"birthday","title":"生日","data":"1980 年 1 月 4 日"},{"type":"education","title":"学历","data":"本科"},{"type":"site","title":"个人主页","data":"http://"}],"creditList":[{"type":"credits","title":"积分","data":53008},{"type":"extcredits1","title":"威望","data":271},{"type":"extcredits2","title":"水滴","data":10088},{"type":"extcredits6","title":"奖励券","data":1}],"creditShowList":[{"type":"credits","title":"积分","data":53008},{"type":"extcredits2","title":"水滴","data":10088}]}
     * flag : 0
     * is_black : 0
     * is_follow : 0
     * isFriend : 0
     * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=64523&size=middle
     * level_url :
     * name : 无镞之箭
     * email : qshpan@hmamail.com
     * status : 2
     * gender : 2
     * score : 53008
     * credits : 53008
     * gold_num : 10088
     * topic_num : 3586
     * photo_num : 0
     * reply_posts_num : 26307
     * essence_num : 0
     * friend_num : 1
     * follow_num : 40
     * level : 4
     * sign : 长期提供美亚及各种外币代付,美私地址,USPS直邮一周寄到,也可以走其他华人转运.官网价格,谷歌实时汇率. 另有联想8通道/Dell/HP/QC35等各大品牌员工折扣 微信: wuzuzhijian
     * userTitle : 传奇蝌蚪 (Lv.??)
     * verify : []
     * mobile :
     * info : []
     */

    private int rs;
    private String errcode;
    private HeadBean head;
    private BodyBean body;
    private int flag;
    private int is_black;
    private int is_follow;
    private int isFriend;
    private String icon;
    private String level_url;
    private String name;
    private String email;
    private int status;
    private int gender;
    private int score;
    private int credits;
    private int gold_num;
    private int topic_num;
    private int photo_num;
    private int reply_posts_num;
    private int essence_num;
    private int friend_num;
    private int follow_num;
    private int level;
    private String sign = null;
    private String userTitle;
    private String mobile;
    private List<?> verify;
    private List<?> info;

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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIs_black() {
        return is_black;
    }

    public void setIs_black(int is_black) {
        this.is_black = is_black;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLevel_url() {
        return level_url;
    }

    public void setLevel_url(String level_url) {
        this.level_url = level_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getGold_num() {
        return gold_num;
    }

    public void setGold_num(int gold_num) {
        this.gold_num = gold_num;
    }

    public int getTopic_num() {
        return topic_num;
    }

    public void setTopic_num(int topic_num) {
        this.topic_num = topic_num;
    }

    public int getPhoto_num() {
        return photo_num;
    }

    public void setPhoto_num(int photo_num) {
        this.photo_num = photo_num;
    }

    public int getReply_posts_num() {
        return reply_posts_num;
    }

    public void setReply_posts_num(int reply_posts_num) {
        this.reply_posts_num = reply_posts_num;
    }

    public int getEssence_num() {
        return essence_num;
    }

    public void setEssence_num(int essence_num) {
        this.essence_num = essence_num;
    }

    public int getFriend_num() {
        return friend_num;
    }

    public void setFriend_num(int friend_num) {
        this.friend_num = friend_num;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<?> getVerify() {
        return verify;
    }

    public void setVerify(List<?> verify) {
        this.verify = verify;
    }

    public List<?> getInfo() {
        return info;
    }

    public void setInfo(List<?> info) {
        this.info = info;
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
         * repeatList : []
         * profileList : [{"type":"gender","title":"性别","data":"女"},{"type":"birthday","title":"生日","data":"1980 年 1 月 4 日"},{"type":"education","title":"学历","data":"本科"},{"type":"site","title":"个人主页","data":"http://"}]
         * creditList : [{"type":"credits","title":"积分","data":53008},{"type":"extcredits1","title":"威望","data":271},{"type":"extcredits2","title":"水滴","data":10088},{"type":"extcredits6","title":"奖励券","data":1}]
         * creditShowList : [{"type":"credits","title":"积分","data":53008},{"type":"extcredits2","title":"水滴","data":10088}]
         */

        private ExternInfoBean externInfo;
        private List<?> repeatList;
        private List<ProfileListBean> profileList;
        private List<CreditListBean> creditList;
        private List<CreditShowListBean> creditShowList;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public List<?> getRepeatList() {
            return repeatList;
        }

        public void setRepeatList(List<?> repeatList) {
            this.repeatList = repeatList;
        }

        public List<ProfileListBean> getProfileList() {
            return profileList;
        }

        public void setProfileList(List<ProfileListBean> profileList) {
            this.profileList = profileList;
        }

        public List<CreditListBean> getCreditList() {
            return creditList;
        }

        public void setCreditList(List<CreditListBean> creditList) {
            this.creditList = creditList;
        }

        public List<CreditShowListBean> getCreditShowList() {
            return creditShowList;
        }

        public void setCreditShowList(List<CreditShowListBean> creditShowList) {
            this.creditShowList = creditShowList;
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

        public static class ProfileListBean {
            /**
             * type : gender
             * title : 性别
             * data : 女
             */

            private String type;
            private String title;
            private String data;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }
        }

        public static class CreditListBean {
            /**
             * type : credits
             * title : 积分
             * data : 53008
             */

            private String type;
            private String title;
            private int data;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getData() {
                return data;
            }

            public void setData(int data) {
                this.data = data;
            }
        }

        public static class CreditShowListBean {
            /**
             * type : credits
             * title : 积分
             * data : 53008
             */

            private String type;
            private String title;
            private int data;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getData() {
                return data;
            }

            public void setData(int data) {
                this.data = data;
            }
        }
    }
}
