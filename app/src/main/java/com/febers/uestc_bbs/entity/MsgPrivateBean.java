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
