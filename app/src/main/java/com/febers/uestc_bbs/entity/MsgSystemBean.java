package com.febers.uestc_bbs.entity;

import java.util.List;

public class MsgSystemBean extends MsgBaseBean{

    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":""},"data":[{"replied_date":"1516783261000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"坏消息，坏消息，萌萌熊 看你不顺眼了,试图给你下头像变猪头的诅咒，不过这次他没成功。不知道他会不会再来一次的哦。想诅咒TA?点击这里试试吧","is_read":"0"},{"replied_date":"1514121800000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"您的用户组升级为 河蟹 (Lv.3)   看看我能做什么 \u203a","is_read":"0"},{"replied_date":"1511485574000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"11月25日线下活动通告！11月25日下午2点的开幕式改在活动中心1楼咖啡厅进行！！！到时候有大蛋糕吃！！！大家先过来看看，吃吃，再听听游戏的具体规则！我们不见不散！","is_read":"0"},{"replied_date":"1511449182000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"十年同舟共济，一路河畔有你清河畔今年十岁了！在这十年里，河畔陪伴着大家走过风风雨雨、河畔分享着大家的喜怒哀乐、河畔陪伴着大家一起成长！戳戳这里：http://bbs.uestc.edu.cn/tenth_anniversary/index.php，看看自己与河畔有着怎样的故事~送祝福给河畔有神秘加成哦~","is_read":"0"},{"replied_date":"1510217118000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"您的用户组升级为 虾米 (Lv.2)   看看我能做什么 \u203a","is_read":"0"},{"replied_date":"1471955234000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"尊敬的四条眉毛，您已经注册成为清水河畔－电子科技大学官方论坛的会员，请你务必阅读新手导航以了解河畔。点此下载手机客户端如果您有什么疑问可以联系管理员，Email: uestcbbs@163.com。\r\n\r\n\r\n清水河畔－电子科技大学官方论坛\r\n2016-8-23 20:27","is_read":"0"}]}
     * page : 1
     * has_next : 0
     * total_num : 6
     */

    private int rs;
    private String errcode;
    private HeadBean head;
    private BodyBean body;
    private int page;
    private int has_next;
    private int total_num;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getHas_next() {
        return has_next;
    }

    public void setHas_next(int has_next) {
        this.has_next = has_next;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
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
         * data : [{"replied_date":"1516783261000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"坏消息，坏消息，萌萌熊 看你不顺眼了,试图给你下头像变猪头的诅咒，不过这次他没成功。不知道他会不会再来一次的哦。想诅咒TA?点击这里试试吧","is_read":"0"},{"replied_date":"1514121800000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"您的用户组升级为 河蟹 (Lv.3)   看看我能做什么 \u203a","is_read":"0"},{"replied_date":"1511485574000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"11月25日线下活动通告！11月25日下午2点的开幕式改在活动中心1楼咖啡厅进行！！！到时候有大蛋糕吃！！！大家先过来看看，吃吃，再听听游戏的具体规则！我们不见不散！","is_read":"0"},{"replied_date":"1511449182000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"十年同舟共济，一路河畔有你清河畔今年十岁了！在这十年里，河畔陪伴着大家走过风风雨雨、河畔分享着大家的喜怒哀乐、河畔陪伴着大家一起成长！戳戳这里：http://bbs.uestc.edu.cn/tenth_anniversary/index.php，看看自己与河畔有着怎样的故事~送祝福给河畔有神秘加成哦~","is_read":"0"},{"replied_date":"1510217118000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"您的用户组升级为 虾米 (Lv.2)   看看我能做什么 \u203a","is_read":"0"},{"replied_date":"1471955234000","type":"system","icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle","user_name":"系统","user_id":"0","mod":"admin","note":"尊敬的四条眉毛，您已经注册成为清水河畔－电子科技大学官方论坛的会员，请你务必阅读新手导航以了解河畔。点此下载手机客户端如果您有什么疑问可以联系管理员，Email: uestcbbs@163.com。\r\n\r\n\r\n清水河畔－电子科技大学官方论坛\r\n2016-8-23 20:27","is_read":"0"}]
         */

        private ExternInfoBean externInfo;
        private List<DataBean> data;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
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

        public static class DataBean {
            /**
             * replied_date : 1516783261000
             * type : system
             * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=0&size=middle
             * user_name : 系统
             * user_id : 0
             * mod : admin
             * note : 坏消息，坏消息，萌萌熊 看你不顺眼了,试图给你下头像变猪头的诅咒，不过这次他没成功。不知道他会不会再来一次的哦。想诅咒TA?点击这里试试吧
             * is_read : 0
             */

            private String replied_date;
            private String type;
            private String icon;
            private String user_name;
            private String user_id;
            private String mod;
            private String note;
            private String is_read;

            public String getReplied_date() {
                return replied_date;
            }

            public void setReplied_date(String replied_date) {
                this.replied_date = replied_date;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getMod() {
                return mod;
            }

            public void setMod(String mod) {
                this.mod = mod;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getIs_read() {
                return is_read;
            }

            public void setIs_read(String is_read) {
                this.is_read = is_read;
            }
        }
    }
}
