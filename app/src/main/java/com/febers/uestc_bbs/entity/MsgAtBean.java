package com.febers.uestc_bbs.entity;

import java.util.List;

public class MsgAtBean extends MsgBaseBean{

    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":""},"data":[{"dateline":"1537967101000","type":"at","note":"四条眉毛 在主题 已解决 中提到了您测试@四条眉毛现在去看看。","fromId":1707366,"fromIdType":"at","author":"四条眉毛","authorId":196486,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle","actions":[]}]}
     * page : 1
     * has_next : 0
     * total_num : 1
     * list : [{"board_name":"程序员","board_id":70,"topic_id":1707366,"topic_subject":"已解决","topic_content":" 本帖最后由 四条眉毛 于 2018-3-14 21:36 编辑 \r\n\r\n代码水平问题\r\n","topic_url":"","reply_content":"测试\r\n \r\n","reply_url":"","reply_remind_id":30934341,"reply_nick_name":"四条眉毛","user_id":196486,"icon":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle","is_read":1,"replied_date":"1537967101000"}]
     */

    private int rs;
    private String errcode;
    private HeadBean head;
    private BodyBean body;
    private int page;
    private int has_next;
    private int total_num;
    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
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
         * data : [{"dateline":"1537967101000","type":"at","note":"四条眉毛 在主题 已解决 中提到了您测试@四条眉毛现在去看看。","fromId":1707366,"fromIdType":"at","author":"四条眉毛","authorId":196486,"authorAvatar":"http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle","actions":[]}]
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
             * dateline : 1537967101000
             * type : at
             * note : 四条眉毛 在主题 已解决 中提到了您测试@四条眉毛现在去看看。
             * fromId : 1707366
             * fromIdType : at
             * author : 四条眉毛
             * authorId : 196486
             * authorAvatar : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle
             * actions : []
             */

            private String dateline;
            private String type;
            private String note;
            private int fromId;
            private String fromIdType;
            private String author;
            private int authorId;
            private String authorAvatar;
            private List<?> actions;

            public String getDateline() {
                return dateline;
            }

            public void setDateline(String dateline) {
                this.dateline = dateline;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public int getFromId() {
                return fromId;
            }

            public void setFromId(int fromId) {
                this.fromId = fromId;
            }

            public String getFromIdType() {
                return fromIdType;
            }

            public void setFromIdType(String fromIdType) {
                this.fromIdType = fromIdType;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public int getAuthorId() {
                return authorId;
            }

            public void setAuthorId(int authorId) {
                this.authorId = authorId;
            }

            public String getAuthorAvatar() {
                return authorAvatar;
            }

            public void setAuthorAvatar(String authorAvatar) {
                this.authorAvatar = authorAvatar;
            }

            public List<?> getActions() {
                return actions;
            }

            public void setActions(List<?> actions) {
                this.actions = actions;
            }
        }
    }

    public static class ListBean {
        /**
         * board_name : 程序员
         * board_id : 70
         * topic_id : 1707366
         * topic_subject : 已解决
         * topic_content :  本帖最后由 四条眉毛 于 2018-3-14 21:36 编辑

         代码水平问题

         * topic_url :
         * reply_content : 测试


         * reply_url :
         * reply_remind_id : 30934341
         * reply_nick_name : 四条眉毛
         * user_id : 196486
         * icon : http://bbs.uestc.edu.cn/uc_server/avatar.php?uid=196486&size=middle
         * is_read : 1
         * replied_date : 1537967101000
         */

        private String board_name;
        private int board_id;
        private int topic_id;
        private String topic_subject;
        private String topic_content;
        private String topic_url;
        private String reply_content;
        private String reply_url;
        private int reply_remind_id;
        private String reply_nick_name;
        private int user_id;
        private String icon;
        private int is_read;
        private String replied_date;

        public String getBoard_name() {
            return board_name;
        }

        public void setBoard_name(String board_name) {
            this.board_name = board_name;
        }

        public int getBoard_id() {
            return board_id;
        }

        public void setBoard_id(int board_id) {
            this.board_id = board_id;
        }

        public int getTopic_id() {
            return topic_id;
        }

        public void setTopic_id(int topic_id) {
            this.topic_id = topic_id;
        }

        public String getTopic_subject() {
            return topic_subject;
        }

        public void setTopic_subject(String topic_subject) {
            this.topic_subject = topic_subject;
        }

        public String getTopic_content() {
            return topic_content;
        }

        public void setTopic_content(String topic_content) {
            this.topic_content = topic_content;
        }

        public String getTopic_url() {
            return topic_url;
        }

        public void setTopic_url(String topic_url) {
            this.topic_url = topic_url;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public String getReply_url() {
            return reply_url;
        }

        public void setReply_url(String reply_url) {
            this.reply_url = reply_url;
        }

        public int getReply_remind_id() {
            return reply_remind_id;
        }

        public void setReply_remind_id(int reply_remind_id) {
            this.reply_remind_id = reply_remind_id;
        }

        public String getReply_nick_name() {
            return reply_nick_name;
        }

        public void setReply_nick_name(String reply_nick_name) {
            this.reply_nick_name = reply_nick_name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public String getReplied_date() {
            return replied_date;
        }

        public void setReplied_date(String replied_date) {
            this.replied_date = replied_date;
        }
    }
}
