package com.myqq.service.youza.bll;

import java.io.Serializable;
import java.util.List;

public class ShopInfoDTO {
    static class GoodsList{
        public int code;
        public String msg;
        public DataInfo data;
    }

    static class DataInfo{
        public boolean has_more;
        public List<GoodInfo> list;

    }

    static class GoodInfo{
        public String alias;
        public Integer id;
        public String image_url;
        public String title;
        public boolean pre_sale;
    }

    /**
     * sku信息
     */
    static class GoodStockData{
        public GoodStockBasicInfo goods;

        public GoodStockSKUInfo sku;
    }

    static class GoodStockBasicInfo{
        public String content;
        public String title;
    }
    static class GoodStockSKUInfo{
        public List<GoodStockSKUListInfo> list;
        public int stockNum;
        public int collectionId;

        @Override
        public String toString() {
            return "GoodStockSKUInfo{" +
                    "list=" + list +
                    ", stockNum=" + stockNum +
                    ", collectionId=" + collectionId +
                    '}';
        }

        //public List<GoodStockSKUTreeInfo> tree;
    }
    static class GoodStockSKUListInfo{
        //sku id
        public Integer id;
        //尺码关系
        //public Integer s1;
        //规格 尺码信息
        public String sku;
        public List<KV> skuList;
        public Integer soldNum;
        public Integer stockNum;

        @Override
        public String toString() {
            return "GoodStockSKUListInfo{" +
                    "id=" + id +
                    ", sku='" + sku + '\'' +
                    ", skuList=" + skuList +
                    ", soldNum=" + soldNum +
                    ", stockNum=" + stockNum +
                    '}';
        }
    }
    static class GoodStockSKUTreeInfo{
        public int count;
        public List<IdName> v;

    }
    static class IdName{
        public int id;
        public String name;
    }

    static class KV{
        private String k;
        private String v;

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

    //to buy good
    public static class ToBuyGoodInfo {
        public Integer goodsId;
        public String googsName;
        public List<ToBuyGoodSkuInfo> toBuyGoodSkuInfos;

        public Integer getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Integer goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoogsName() {
            return googsName;
        }

        public void setGoogsName(String googsName) {
            this.googsName = googsName;
        }

        public List<ToBuyGoodSkuInfo> getToBuyGoodSkuInfos() {
            return toBuyGoodSkuInfos;
        }

        public void setToBuyGoodSkuInfos(List<ToBuyGoodSkuInfo> toBuyGoodSkuInfos) {
            this.toBuyGoodSkuInfos = toBuyGoodSkuInfos;
        }

        @Override
        public String toString() {
            return "ToBuyGoodInfo{" +
                    "goodsId=" + goodsId +
                    ", googsName='" + googsName + '\'' +
                    ", toBuyGoodSkuInfos=" + toBuyGoodSkuInfos +
                    '}';
        }
    }
    public static class ToBuyGoodSkuInfo implements Serializable {
        public Integer num;
        public Integer skuId;
        public String sku;
        /**
         * 下单成功后订单号
         */
        public String orderNo;
        public ToBuyGoodSkuInfo(Integer skuId,Integer num,String sku){
            this.skuId = skuId;
            this.num = num;
            this.sku = sku;
        }

        @Override
        public String toString() {
            return "ToBuyGoodSkuInfo{" +
                    "num=" + num +
                    ", skuId=" + skuId +
                    ", sku='" + sku + '\'' +
                    ", orderNo='" + orderNo + '\'' +
                    '}';
        }
    }

    static class AddressInfoDTO implements Serializable{
        public List<AddressDetailInfoDTO> addressList;

        @Override
        public String toString() {
            return "AddressInfoDTO{" +
                    "addressList=" + addressList +
                    '}';
        }
    }

    static class AddressDetailInfoDTO{
        public String addressDetail;// : "浙江省杭州市拱墅区半山街道夏莲路浙江三丰建设，吴兰琼，15268576311",
        public String areaCode;// : "330105",
        public String city;//" : "杭州市",
        public String community;//" : "",
        public String country;//" : "中国",
        public int countryType;//" : 1,
        public String county;//" : "拱墅区",
        public long id;//" : 187449040,
        public int isDefault;//" : 0,
        public String lat;//" : "30.35263229710601",
        public String lon;//" : "120.18553416929684",
        public String postalCode;//" : "",
        public String province;//" : "浙江省",
        public int source;// : 1,
        public String tel;// : "15268576311",
        public long userId;// : 387219108,
        public String userName;// : "吴兰琼"

        @Override
        public String toString() {
            return "AddressDetailInfoDTO{" +
                    "addressDetail='" + addressDetail + '\'' +
                    ", areaCode='" + areaCode + '\'' +
                    ", city='" + city + '\'' +
                    ", community='" + community + '\'' +
                    ", country='" + country + '\'' +
                    ", countryType=" + countryType +
                    ", county='" + county + '\'' +
                    ", id='" + id + '\'' +
                    ", isDefault=" + isDefault +
                    ", lat='" + lat + '\'' +
                    ", lon='" + lon + '\'' +
                    ", postalCode='" + postalCode + '\'' +
                    ", province='" + province + '\'' +
                    ", source=" + source +
                    ", tel='" + tel + '\'' +
                    ", userId=" + userId +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }

    static class ToPayShopInfoDTO{
        public ToPayDataInfo data;
    }

    static class ToPayDataInfo{
        public boolean has_next;
        public List<TopayOrderInfo> list;
    }

    static class TopayOrderInfo{
        public String id;
        public String order_no;
    }

    /**
     * 商品对应收件人
     */
    public static class ToBuyGoodAndAddressInfo{
        private String orderDate;
        private String receiptName;
        private String shotGoodName;
        private int toBuyNum;
        private String desc;
        private List<String> goodColorKeyWords;
        private List<String> goodSizeKeyWords;
        private List<String> goodStyleKeyWords;
        private List<String> goodSpecKeyWords;
        private List<ToBuyGoodInfo> toBuyGoodInfoList;
        private AddressDetailInfoDTO addressDetailInfo;
        private List<CommitOrderInfo> commitOrderInfoList;
        private String localNo;//UUID
        private boolean needPresale;
        private long readyToBuyGoodNum;
        private String kdtSession;


        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public List<String> getGoodSpecKeyWords() {
            return goodSpecKeyWords;
        }

        public void setGoodSpecKeyWords(List<String> goodSpecKeyWords) {
            this.goodSpecKeyWords = goodSpecKeyWords;
        }

        public long getReadyToBuyGoodNum() {
            return readyToBuyGoodNum;
        }

        public void setReadyToBuyGoodNum(long readyToBuyGoodNum) {
            this.readyToBuyGoodNum = readyToBuyGoodNum;
        }

        public boolean isNeedPresale() {
            return needPresale;
        }

        public void setNeedPresale(boolean needPresale) {
            this.needPresale = needPresale;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getLocalNo() {
            return localNo;
        }

        public void setLocalNo(String localNo) {
            this.localNo = localNo;
        }

        public int getToBuyNum() {
            return toBuyNum;
        }

        public void setToBuyNum(int toBuyNum) {
            this.toBuyNum = toBuyNum;
        }

        public List<CommitOrderInfo> getCommitOrderInfoList() {
            return commitOrderInfoList;
        }

        public void setCommitOrderInfoList(List<CommitOrderInfo> commitOrderInfoList) {
            this.commitOrderInfoList = commitOrderInfoList;
        }

        public String getShotGoodName() {
            return shotGoodName;
        }

        public void setShotGoodName(String shotGoodName) {
            this.shotGoodName = shotGoodName;
        }

        public String getReceiptName() {
            return receiptName;
        }

        public void setReceiptName(String receiptName) {
            this.receiptName = receiptName;
        }


        public List<ToBuyGoodInfo> getToBuyGoodInfoList() {
            return toBuyGoodInfoList;
        }

        public void setToBuyGoodInfoList(List<ToBuyGoodInfo> toBuyGoodInfoList) {
            this.toBuyGoodInfoList = toBuyGoodInfoList;
        }

        public AddressDetailInfoDTO getAddressDetailInfo() {
            return addressDetailInfo;
        }

        public void setAddressDetailInfo(AddressDetailInfoDTO addressDetailInfo) {
            this.addressDetailInfo = addressDetailInfo;
        }
        public String getKdtSession() {
            return kdtSession;
        }

        public void setKdtSession(String kdtSession) {
            this.kdtSession = kdtSession;
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

        @Override
        public String toString() {
            return "ToBuyGoodAndAddressInfo{" +
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
                    '}';
        }
    }

    /**
     * 下单提交成功订单信息
     */
    public static class CommitOrderInfo{
        private OrderDataInfo data;

        public OrderDataInfo getData() {
            return data;
        }

        public void setData(OrderDataInfo data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "CommitOrderInfo{" +
                    "data=" + data +
                    '}';
        }
    }

    /**
     * 下单成功后订单信息
     */
    static class OrderDataInfo{
        private String orderNo;
        private List<String> orderNos;

        private OrderDataPaymentInfo paymentPreparation;
        private String skuInfo;

        public String getSkuInfo() {
            return skuInfo;
        }

        public void setSkuInfo(String skuInfo) {
            this.skuInfo = skuInfo;
        }

        public OrderDataPaymentInfo getPaymentPreparation() {
            return paymentPreparation;
        }

        public void setPaymentPreparation(OrderDataPaymentInfo paymentPreparation) {
            this.paymentPreparation = paymentPreparation;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public List<String> getOrderNos() {
            return orderNos;
        }

        public void setOrderNos(List<String> orderNos) {
            this.orderNos = orderNos;
        }

        @Override
        public String toString() {
            return "OrderDataInfo{" +
                    "orderNo='" + orderNo + '\'' +
                    ", orderNos=" + orderNos +
                    ", paymentPreparation=" + paymentPreparation +
                    ", skuInfo='" + skuInfo + '\'' +
                    '}';
        }
    }

    static class OrderDataPaymentInfo{
        private String tradeDesc;
        private String expiredAt;
        private Long payAmount;

        @Override
        public String toString() {
            return "OrderDataPaymentInfo{" +
                    "tradeDesc='" + tradeDesc + '\'' +
                    ", expiredAt='" + expiredAt + '\'' +
                    ", payAmount=" + payAmount +
                    '}';
        }

        public String getTradeDesc() {
            return tradeDesc;
        }

        public void setTradeDesc(String tradeDesc) {
            this.tradeDesc = tradeDesc;
        }

        public String getExpiredAt() {
            return expiredAt;
        }

        public void setExpiredAt(String expiredAt) {
            this.expiredAt = expiredAt;
        }

        public Long getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(Long payAmount) {
            this.payAmount = payAmount;
        }
    }

    static class StandToPayData{
        private StandToPayOrderInfo data;

        public StandToPayOrderInfo getData() {
            return data;
        }

        public void setData(StandToPayOrderInfo data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "ToPayData{" +
                    "data=" + data +
                    '}';
        }
    }

    static class StandToPayOrderInfo{
        private Boolean has_next;
        private List<ToPayDataListItem> list;

        public Boolean getHas_next() {
            return has_next;
        }

        public void setHas_next(Boolean has_next) {
            this.has_next = has_next;
        }

        public List<ToPayDataListItem> getList() {
            return list;
        }

        public void setList(List<ToPayDataListItem> list) {
            this.list = list;
        }
    }
    static class ToPayDataListItem{
        private String order_no;
        private List<OrderItemInfo> order_items;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public List<OrderItemInfo> getOrder_items() {
            return order_items;
        }

        public void setOrder_items(List<OrderItemInfo> order_items) {
            this.order_items = order_items;
        }
    }
    static class OrderItemInfo{
        private String order_no;
        private Long total_price;
        private List<OrderItemAttr> items;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public Long getTotal_price() {
            return total_price;
        }

        public void setTotal_price(Long total_price) {
            this.total_price = total_price;
        }

        public List<OrderItemAttr> getItems() {
            return items;
        }

        public void setItems(List<OrderItemAttr> items) {
            this.items = items;
        }
    }

    static class OrderItemAttr{
        private String image;
        private Long price;
        private String title;
        private List<OrderItemSku> sku;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<OrderItemSku> getSku() {
            return sku;
        }

        public void setSku(List<OrderItemSku> sku) {
            this.sku = sku;
        }
    }
    static class OrderItemSku{
        //属性值
        private String v;
        //属性
        private String k;

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }

        public String getK() {
            return k;
        }

        public void setK(String k) {
            this.k = k;
        }
    }

    /**
     * 待支付订单
     */
    static class ToPaySingleOrderInfo{
        private ToPayOrderAddressInfo orderAddressInfo;

        public ToPayOrderAddressInfo getOrderAddressInfo() {
            return orderAddressInfo;
        }

        public void setOrderAddressInfo(ToPayOrderAddressInfo orderAddressInfo) {
            this.orderAddressInfo = orderAddressInfo;
        }
    }
    /**
     * 待支付订单地址信息
     */
    static class ToPayOrderAddressInfo{
        private String orderNo;
        private String receiverName;
        private String deliveryStreet;

        public String getDeliveryStreet() {
            return deliveryStreet;
        }

        public void setDeliveryStreet(String deliveryStreet) {
            this.deliveryStreet = deliveryStreet;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        @Override
        public String toString() {
            return "ToPayOrderAddressInfo{" +
                    "orderNo='" + orderNo + '\'' +
                    ", receiverName='" + receiverName + '\'' +
                    ", deliveryStreet='" + deliveryStreet + '\'' +
                    '}';
        }
    }
}
