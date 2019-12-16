package com.myqq.service.youza.entity;


import java.util.List;

public class ToBuyGoodInfoAppDTO {
    public static class ToBuyGoodAndAddressInfoDTO{
        private String orderDate;
        private String receiptName;
        private String shotGoodName;
        /**
         * 名称中的量词
         */
        private String quantifierNum;
        private int toBuyNum;
        private String desc;
        private List<String> goodColorKeyWords;
        private List<String> goodSizeKeyWords;
        private List<String> goodStyleKeyWords;
        private List<String> goodSpecKeyWords;
        /**
         * 待下单信息
         */
        private List<ShoppingForAppDTO.GoodDataStockDetailDTO> toBuyGoodInfoList;
        private ShoppingForAppDTO.AddressDataRowDetailDTO addressDetailInfo;
        private List<ShoppingForAppDTO.CommitOrderDTO> commitOrderInfoList;
        private String localNo;//UUID
        private boolean needPresale;
        private long readyToBuyGoodNum;
        private String kdtSession;
        /**
         * 预售现货类型
         * 0-不区分
         * 1-现货
         * 2-预售
         */
        private int toBuySellType;

        public String getQuantifierNum() {
            return quantifierNum;
        }

        public void setQuantifierNum(String quantifierNum) {
            this.quantifierNum = quantifierNum;
        }

        public int getToBuySellType() {
            return toBuySellType;
        }

        public void setToBuySellType(int toBuySellType) {
            this.toBuySellType = toBuySellType;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getReceiptName() {
            return receiptName;
        }

        public void setReceiptName(String receiptName) {
            this.receiptName = receiptName;
        }

        public String getShotGoodName() {
            return shotGoodName;
        }

        public void setShotGoodName(String shotGoodName) {
            this.shotGoodName = shotGoodName;
        }

        public int getToBuyNum() {
            return toBuyNum;
        }

        public void setToBuyNum(int toBuyNum) {
            this.toBuyNum = toBuyNum;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<String> getGoodColorKeyWords() {
            return goodColorKeyWords;
        }

        public void setGoodColorKeyWords(List<String> goodColorKeyWords) {
            this.goodColorKeyWords = goodColorKeyWords;
        }

        public List<String> getGoodSizeKeyWords() {
            return goodSizeKeyWords;
        }

        public void setGoodSizeKeyWords(List<String> goodSizeKeyWords) {
            this.goodSizeKeyWords = goodSizeKeyWords;
        }

        public List<String> getGoodStyleKeyWords() {
            return goodStyleKeyWords;
        }

        public void setGoodStyleKeyWords(List<String> goodStyleKeyWords) {
            this.goodStyleKeyWords = goodStyleKeyWords;
        }

        public List<String> getGoodSpecKeyWords() {
            return goodSpecKeyWords;
        }

        public void setGoodSpecKeyWords(List<String> goodSpecKeyWords) {
            this.goodSpecKeyWords = goodSpecKeyWords;
        }

        public List<ShoppingForAppDTO.GoodDataStockDetailDTO> getToBuyGoodInfoList() {
            return toBuyGoodInfoList;
        }

        public void setToBuyGoodInfoList(List<ShoppingForAppDTO.GoodDataStockDetailDTO> toBuyGoodInfoList) {
            this.toBuyGoodInfoList = toBuyGoodInfoList;
        }

        public ShoppingForAppDTO.AddressDataRowDetailDTO getAddressDetailInfo() {
            return addressDetailInfo;
        }

        public void setAddressDetailInfo(ShoppingForAppDTO.AddressDataRowDetailDTO addressDetailInfo) {
            this.addressDetailInfo = addressDetailInfo;
        }

        public List<ShoppingForAppDTO.CommitOrderDTO> getCommitOrderInfoList() {
            return commitOrderInfoList;
        }

        public void setCommitOrderInfoList(List<ShoppingForAppDTO.CommitOrderDTO> commitOrderInfoList) {
            this.commitOrderInfoList = commitOrderInfoList;
        }

        public String getLocalNo() {
            return localNo;
        }

        public void setLocalNo(String localNo) {
            this.localNo = localNo;
        }

        public boolean isNeedPresale() {
            return needPresale;
        }

        public void setNeedPresale(boolean needPresale) {
            this.needPresale = needPresale;
        }

        public long getReadyToBuyGoodNum() {
            return readyToBuyGoodNum;
        }

        public void setReadyToBuyGoodNum(long readyToBuyGoodNum) {
            this.readyToBuyGoodNum = readyToBuyGoodNum;
        }

        public String getKdtSession() {
            return kdtSession;
        }

        public void setKdtSession(String kdtSession) {
            this.kdtSession = kdtSession;
        }

        @Override
        public String toString() {
            return "ToBuyGoodAndAddressInfoDTO{" +
                    "orderDate='" + orderDate + '\'' +
                    ", receiptName='" + receiptName + '\'' +
                    ", shotGoodName='" + shotGoodName + '\'' +
                    ", toBuyNum=" + toBuyNum +
                    ", desc='" + desc + '\'' +
                    ", goodColorKeyWords=" + goodColorKeyWords +
                    ", goodSizeKeyWords=" + goodSizeKeyWords +
                    ", goodStyleKeyWords=" + goodStyleKeyWords +
                    ", goodSpecKeyWords=" + goodSpecKeyWords +
                    ", toBuyGoodInfoList=" + toBuyGoodInfoList +
                    ", addressDetailInfo=" + addressDetailInfo +
                    ", commitOrderInfoList=" + commitOrderInfoList +
                    ", localNo='" + localNo + '\'' +
                    ", needPresale=" + needPresale +
                    ", readyToBuyGoodNum=" + readyToBuyGoodNum +
                    ", kdtSession='" + kdtSession + '\'' +
                    ", toBuySellType=" + toBuySellType +
                    '}';
        }
    }

    public static class KV{
        private String k;
        private String v;

        public KV(String k, String v) {
            this.k = k;
            this.v = v;
        }

        public String getK() {
            return k;
        }

        public void setK(String k) {
            this.k = k;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }

        @Override
        public String toString() {
            return "KV{" +
                    "k='" + k + '\'' +
                    ", v='" + v + '\'' +
                    '}';
        }
    }
}
