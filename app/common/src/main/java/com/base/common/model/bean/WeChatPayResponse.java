package com.base.common.model.bean;


import com.fasterxml.jackson.annotation.JsonProperty;

public class WeChatPayResponse {

    public static class PayInfoBean {
        /**
         * appid : wx4fe1222a1d1d73ab
         * partnerid : 1538678461
         * prepayid : wx1810405823977236ef22bce11513183600
         * timestamp : 1563417658
         * noncestr : khPkEkE8vTHSf7w8
         * package : Sign=WXPay
         * sign : AC77B0A1AF603E1D97B4B157CD65E88C
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        private String timestamp;
        private String noncestr;

        @JsonProperty("package")
        private String packageX;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
