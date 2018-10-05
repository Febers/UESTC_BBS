package com.febers.uestc_bbs.entity;

import java.util.List;

public class MsgHeartBean {

    /**
     * rs : 1
     * errcode :
     * head : {"errCode":"00000000","errInfo":"调用成功,没有任何错误","version":"2.6.1.7","alert":0}
     * body : {"externInfo":{"padding":"","heartPeriod":"120000","pmPeriod":"20000"},"replyInfo":{"count":1,"time":"1538750473000"},"atMeInfo":{"count":1,"time":"1538750473000"},"pmInfos":[{"fromUid":214009,"plid":4022360,"pmid":4022360,"time":"1538750459000"}],"friendInfo":{"count":1,"time":"1538750422000"}}
     */

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
         * externInfo : {"padding":"","heartPeriod":"120000","pmPeriod":"20000"}
         * replyInfo : {"count":1,"time":"1538750473000"}
         * atMeInfo : {"count":1,"time":"1538750473000"}
         * pmInfos : [{"fromUid":214009,"plid":4022360,"pmid":4022360,"time":"1538750459000"}]
         * friendInfo : {"count":1,"time":"1538750422000"}
         */

        private ExternInfoBean externInfo;
        private ReplyInfoBean replyInfo;
        private AtMeInfoBean atMeInfo;
        private FriendInfoBean friendInfo;
        private List<PmInfosBean> pmInfos;

        public ExternInfoBean getExternInfo() {
            return externInfo;
        }

        public void setExternInfo(ExternInfoBean externInfo) {
            this.externInfo = externInfo;
        }

        public ReplyInfoBean getReplyInfo() {
            return replyInfo;
        }

        public void setReplyInfo(ReplyInfoBean replyInfo) {
            this.replyInfo = replyInfo;
        }

        public AtMeInfoBean getAtMeInfo() {
            return atMeInfo;
        }

        public void setAtMeInfo(AtMeInfoBean atMeInfo) {
            this.atMeInfo = atMeInfo;
        }

        public FriendInfoBean getFriendInfo() {
            return friendInfo;
        }

        public void setFriendInfo(FriendInfoBean friendInfo) {
            this.friendInfo = friendInfo;
        }

        public List<PmInfosBean> getPmInfos() {
            return pmInfos;
        }

        public void setPmInfos(List<PmInfosBean> pmInfos) {
            this.pmInfos = pmInfos;
        }

        public static class ExternInfoBean {
            /**
             * padding :
             * heartPeriod : 120000
             * pmPeriod : 20000
             */

            private String padding;
            private String heartPeriod;
            private String pmPeriod;

            public String getPadding() {
                return padding;
            }

            public void setPadding(String padding) {
                this.padding = padding;
            }

            public String getHeartPeriod() {
                return heartPeriod;
            }

            public void setHeartPeriod(String heartPeriod) {
                this.heartPeriod = heartPeriod;
            }

            public String getPmPeriod() {
                return pmPeriod;
            }

            public void setPmPeriod(String pmPeriod) {
                this.pmPeriod = pmPeriod;
            }
        }

        public static class ReplyInfoBean {
            /**
             * count : 1
             * time : 1538750473000
             */

            private int count;
            private String time;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class AtMeInfoBean {
            /**
             * count : 1
             * time : 1538750473000
             */

            private int count;
            private String time;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class FriendInfoBean {
            /**
             * count : 1
             * time : 1538750422000
             */

            private int count;
            private String time;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class PmInfosBean {
            /**
             * fromUid : 214009
             * plid : 4022360
             * pmid : 4022360
             * time : 1538750459000
             */

            private int fromUid;
            private int plid;
            private int pmid;
            private String time;

            public int getFromUid() {
                return fromUid;
            }

            public void setFromUid(int fromUid) {
                this.fromUid = fromUid;
            }

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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
