package com.my.mvpframe.bean;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class AppVersionBean {

    /**
     * status : 1
     * msg :
     * data : {"currentversion":"当前app版本号","currentnum":"当前代码版本","updatatext":"升级内容","downloadurl":"app下载地址"}
     */

    private int status;
    private String msg;
    private DataBean data;

    public static class DataBean {
        /**
         * currentversion : 当前app版本号
         * currentnum : 当前代码版本
         * updatatext : 升级内容
         * downloadurl : app下载地址
         */

        private String currentversion;
        private String currentnum;
        private String updatatext;
        private String downloadurl;

        public String getCurrentversion() {
            return currentversion;
        }

        public void setCurrentversion(String currentversion) {
            this.currentversion = currentversion;
        }

        public String getCurrentnum() {
            return currentnum;
        }

        public void setCurrentnum(String currentnum) {
            this.currentnum = currentnum;
        }

        public String getUpdatatext() {
            return updatatext;
        }

        public void setUpdatatext(String updatatext) {
            this.updatatext = updatatext;
        }

        public String getDownloadurl() {
            return downloadurl;
        }

        public void setDownloadurl(String downloadurl) {
            this.downloadurl = downloadurl;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
