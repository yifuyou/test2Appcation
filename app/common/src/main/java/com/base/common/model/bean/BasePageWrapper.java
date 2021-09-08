package com.base.common.model.bean;

import java.util.List;

public class BasePageWrapper<T> {

    /**
     * msg : 成功
     * code : 0
     * data : {"totalCount":1,"pageSize":9,"totalPage":1,"currPage":1,"list":[{"id":12346,"replyId":279,"name":"前任键盘侠123","icon":"https://qiuzy.oss-cn-shenzhen.aliyuncs.com/app/20200729/4335b3237deb4fb4a8528ce95e4c4f9a.jpg","userLevel":"普通用户","content":"客家话","likeCount":0,"parentId":0,"toReplyId":"","replyUserName":"","replyUserId":0,"replyCount":0,"atUserId":"","atUserName":"","isLike":0,"createTime":"2020-11-03 16:58:56","nowDate":"2020-11-04 09:44:53","isReply":"","list":[],"uid":1227}]}
     */

    private String msg;
    private int code;
    private DataBean<T> data;
    private DataBean<T> page;
    private int likeCount;
    private int totalCount;

    public DataBean<T> getPage() {
        return page;
    }

    public void setPage(DataBean<T> page) {
        this.page = page;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean<T> getData() {
        return data;
    }

    public void setData(DataBean<T> data) {
        this.data = data;
    }

    public static class DataBean<T> {
        /**
         * totalCount : 1
         * pageSize : 9
         * totalPage : 1
         * currPage : 1
         * list : [{"id":12346,"replyId":279,"name":"前任键盘侠123","icon":"https://qiuzy.oss-cn-shenzhen.aliyuncs.com/app/20200729/4335b3237deb4fb4a8528ce95e4c4f9a.jpg","userLevel":"普通用户","content":"客家话","likeCount":0,"parentId":0,"toReplyId":"","replyUserName":"","replyUserId":0,"replyCount":0,"atUserId":"","atUserName":"","isLike":0,"createTime":"2020-11-03 16:58:56","nowDate":"2020-11-04 09:44:53","isReply":"","list":[],"uid":1227}]
         */

        private int totalCount;
        private int pageSize;
        private int totalPage;
        private int currPage;
        private List<T> list;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getCurrPage() {
            return currPage;
        }

        public void setCurrPage(int currPage) {
            this.currPage = currPage;
        }

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }

    }
}
