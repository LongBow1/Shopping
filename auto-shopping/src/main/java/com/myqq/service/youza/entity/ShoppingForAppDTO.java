package com.myqq.service.youza.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 白秋洁 app
 */
public class ShoppingForAppDTO {

    //region 商品列表信息
    public static class GoodsListDTO{
        GoodsListDataDTO data;

        public GoodsListDataDTO getData() {
            return data;
        }

        public void setData(GoodsListDataDTO data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "GoodsListDTO{" +
                    "data=" + data +
                    '}';
        }
    }

    public static class GoodsListDataDTO{
        List<GoodsListDataRowDTO> rows;
        Boolean hasNextPage;
        Integer total;
        List<GoodsListDataRowDTO>  today;

        public List<GoodsListDataRowDTO> getToday() {
            return today;
        }

        public void setToday(List<GoodsListDataRowDTO> today) {
            this.today = today;
        }

        public List<GoodsListDataRowDTO> getRows() {
            return rows;
        }

        public void setRows(List<GoodsListDataRowDTO> rows) {
            this.rows = rows;
        }

        public Boolean getHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(Boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "GoodsListDataDTO{" +
                    "rows=" + rows +
                    ", hasNextPage=" + hasNextPage +
                    ", total=" + total +
                    ", today=" + today +
                    '}';
        }
    }

    public static class GoodsListDataRowDTO{
        String name;
        String goodsId;
        String pic;
        BigDecimal price;
        /**
         * 上架时间
         */
        long shiftedOn;
        int inventoryAmount;

        public String getName() {
            if(name != null){
                name = name.replaceAll("\\s*","");
            }
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public long getShiftedOn() {
            return shiftedOn;
        }

        public void setShiftedOn(long shiftedOn) {
            this.shiftedOn = shiftedOn;
        }

        public int getInventoryAmount() {
            return inventoryAmount;
        }

        public void setInventoryAmount(int inventoryAmount) {
            this.inventoryAmount = inventoryAmount;
        }

        @Override
        public String toString() {
            return "GoodsListDataRowDTO{" +
                    "name='" + name + '\'' +
                    ", goodsId='" + goodsId + '\'' +
                    ", pic='" + pic + '\'' +
                    ", price=" + price +
                    ", shiftedOn=" + shiftedOn +
                    ", inventoryAmount=" + inventoryAmount +
                    '}';
        }
    }
    //endregion

    //region 商品明细信息
    public static class GoodDataDTO{
        GoodDataDetailDTO data;
        /**
         * 0-success
         */
        Integer code;
        String message;

        public GoodDataDetailDTO getData() {
            return data;
        }

        public void setData(GoodDataDetailDTO data) {
            this.data = data;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "GoodDataDTO{" +
                    "data=" + data +
                    ", code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
    public static class GoodDataDetailDTO{
        List<GoodDataSpecDetailDTO> specList;
        List<GoodDataStockDetailDTO> goodsList;
        String activityList;

        @Override
        public String toString() {
            return "GoodDataDetailDTO{" +
                    "specList=" + specList +
                    ", goodsList=" + goodsList +
                    ", activityList='" + activityList + '\'' +
                    '}';
        }

        public String getActivityList() {
            return activityList;
        }

        public void setActivityList(String activityList) {
            this.activityList = activityList;
        }

        public List<GoodDataSpecDetailDTO> getSpecList() {
            return specList;
        }

        public void setSpecList(List<GoodDataSpecDetailDTO> specList) {
            this.specList = specList;
        }

        public List<GoodDataStockDetailDTO> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodDataStockDetailDTO> goodsList) {
            this.goodsList = goodsList;
        }

    }
    public static class GoodDataStockDetailDTO{
        /**
         * 下单用
         */
        String goodsId;
        String name;
        String mainGoodsId;
        BigDecimal price;
        Integer inventory;
        /**
         * spec 为推测; 历史经验最多4项
         */
        String spec1;
        String spec2;
        String spec3;
        String spec4;
        /**
         * 下单成功后订单号
         */
        String orderNo;
        /**
         * 意向下单数量
         */
        Integer toBuyNum;
        /**
         * 属性信息--组合生成
         */
        List<ToBuyGoodInfoAppDTO.KV> specList;
        /**
         * 优惠编号
         */
        List<String> promotionIdList;
        String promotionIdStr;
        /**
         * 是否为购物车下单
         */
        private Boolean mergeOrder;

        public Boolean getMergeOrder() {
            return mergeOrder;
        }

        public void setMergeOrder(Boolean mergeOrder) {
            this.mergeOrder = mergeOrder;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getToBuyNum() {
            return toBuyNum;
        }

        public void setToBuyNum(Integer toBuyNum) {
            this.toBuyNum = toBuyNum;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getMainGoodsId() {
            return mainGoodsId;
        }

        public void setMainGoodsId(String mainGoodsId) {
            this.mainGoodsId = mainGoodsId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getInventory() {
            return inventory;
        }

        public void setInventory(Integer inventory) {
            this.inventory = inventory;
        }

        public String getSpec1() {
            return spec1;
        }

        public void setSpec1(String spec1) {
            this.spec1 = spec1;
        }

        public String getSpec2() {
            return spec2;
        }

        public void setSpec2(String spec2) {
            this.spec2 = spec2;
        }

        public String getSpec3() {
            return spec3;
        }

        public void setSpec3(String spec3) {
            this.spec3 = spec3;
        }

        public String getSpec4() {
            return spec4;
        }

        public void setSpec4(String spec4) {
            this.spec4 = spec4;
        }

        public List<ToBuyGoodInfoAppDTO.KV> getSpecList() {
            return specList;
        }

        public void setSpecList(List<ToBuyGoodInfoAppDTO.KV> specList) {
            this.specList = specList;
        }

        @Override
        public String toString() {
            return "GoodDataStockDetailDTO{" +
                    "goodsId='" + goodsId + '\'' +
                    ", name='" + name + '\'' +
                    ", mainGoodsId='" + mainGoodsId + '\'' +
                    ", price=" + price +
                    ", inventory=" + inventory +
                    ", spec1='" + spec1 + '\'' +
                    ", spec2='" + spec2 + '\'' +
                    ", spec3='" + spec3 + '\'' +
                    ", spec4='" + spec4 + '\'' +
                    ", orderNo='" + orderNo + '\'' +
                    ", toBuyNum=" + toBuyNum +
                    ", specList=" + specList +
                    ", promotionIdList=" + promotionIdList +
                    ", promotionIdStr='" + promotionIdStr + '\'' +
                    '}';
        }

        public String getPromotionIdStr() {
            return promotionIdStr;
        }

        public void setPromotionIdStr(String promotionIdStr) {
            this.promotionIdStr = promotionIdStr;
        }

        public List<String> getPromotionIdList() {
            return promotionIdList;
        }

        public void setPromotionIdList(List<String> promotionIdList) {
            this.promotionIdList = promotionIdList;
        }

    }
    public static class GoodDataSpecDetailDTO{
        /**
         * 颜色/尺码
         */
        String title;
        /**
         * 可选项明细
         */
        List<String> specList;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getSpecList() {
            return specList;
        }

        public void setSpecList(List<String> specList) {
            this.specList = specList;
        }

        @Override
        public String toString() {
            return "GoodDataSpecDetailDTO{" +
                    "title='" + title + '\'' +
                    ", specList=" + specList +
                    '}';
        }
    }
    //endregion

    //region 地址信息
    public static class AddressDataDTO{
        AddressDataRowDTO data;
        /**
         * 0-success
         */
        Integer code;
        String message;

        public AddressDataRowDTO getData() {
            return data;
        }

        public void setData(AddressDataRowDTO data) {
            this.data = data;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "AddressDataDTO{" +
                    "data=" + data +
                    ", code=" + code +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
    public static class AddressDataRowDTO{
        List<AddressDataRowDetailDTO> rows;
        private Integer total;

        public List<AddressDataRowDetailDTO> getRows() {
            return rows;
        }

        public void setRows(List<AddressDataRowDetailDTO> rows) {
            this.rows = rows;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        @Override
        public String toString() {
            return "AddressDataRowDTO{" +
                    "rows=" + rows +
                    ", total=" + total +
                    '}';
        }
    }
    public static class AddressDataRowDetailDTO{
        String addressId;
        String receiveName;
        String receivePhone;
        String province;
        String city;
        String area;
        String address;
        Boolean def;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getReceiveName() {
            return receiveName;
        }

        public void setReceiveName(String receiveName) {
            this.receiveName = receiveName;
        }

        public String getReceivePhone() {
            return receivePhone;
        }

        public void setReceivePhone(String receivePhone) {
            this.receivePhone = receivePhone;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Boolean getDef() {
            return def;
        }

        public void setDef(Boolean def) {
            this.def = def;
        }

        @Override
        public String toString() {
            return "AddressDataRowDetailDTO{" +
                    "addressId='" + addressId + '\'' +
                    ", receiveName='" + receiveName + '\'' +
                    ", receivePhone='" + receivePhone + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", area='" + area + '\'' +
                    ", address='" + address + '\'' +
                    ", def=" + def +
                    '}';
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + Objects.hashCode(this.receiveName);
            result = 31 * result + Objects.hashCode(this.receivePhone);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || getClass() != obj.getClass()){
                return false;
            }
            AddressDataRowDetailDTO other = (AddressDataRowDetailDTO)obj;
            return Objects.equals(this.receiveName, other.receiveName) && Objects.equals(this.receivePhone, other.receivePhone);
        }
    }
    //endregion
    //region 订单信息--待支付、待发货、待收货、已完成
    public static class OrderDataDTO{
        /**
         * 0-success
         */
        Integer code;
        String message;
        OrderDataRowsDTO data;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public OrderDataRowsDTO getData() {
            return data;
        }

        public void setData(OrderDataRowsDTO data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "OrderDataDTO{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
    public static class OrderDataRowsDTO{
        List<OrderDataRowDetailDTO> rows;

        public List<OrderDataRowDetailDTO> getRows() {
            return rows;
        }

        public void setRows(List<OrderDataRowDetailDTO> rows) {
            this.rows = rows;
        }

        @Override
        public String toString() {
            return "OrderDataRowsDTO{" +
                    "rows=" + rows +
                    '}';
        }
    }
    public static class OrderDataRowDetailDTO{
        String receiverName;
        String orderId;
        String status;
        String addressProvince;
        String addressCity;
        String addressArea;
        String addressStreet;
        String address;
        BigDecimal price;
        Long expireTime;
        Long createTime;
        String receiverTelephone;
        List<OrderDataRowDetailGoodDetailDTO> goodsList;

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddressProvince() {
            return addressProvince;
        }

        public void setAddressProvince(String addressProvince) {
            this.addressProvince = addressProvince;
        }

        public String getAddressCity() {
            return addressCity;
        }

        public void setAddressCity(String addressCity) {
            this.addressCity = addressCity;
        }

        public String getAddressArea() {
            return addressArea;
        }

        public void setAddressArea(String addressArea) {
            this.addressArea = addressArea;
        }

        public String getAddressStreet() {
            return addressStreet;
        }

        public void setAddressStreet(String addressStreet) {
            this.addressStreet = addressStreet;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Long expireTime) {
            this.expireTime = expireTime;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public String getReceiverTelephone() {
            return receiverTelephone;
        }

        public void setReceiverTelephone(String receiverTelephone) {
            this.receiverTelephone = receiverTelephone;
        }

        public List<OrderDataRowDetailGoodDetailDTO> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<OrderDataRowDetailGoodDetailDTO> goodsList) {
            this.goodsList = goodsList;
        }

        @Override
        public String toString() {
            return "OrderDataRowDetailDTO{" +
                    "receiverName='" + receiverName + '\'' +
                    ", orderId='" + orderId + '\'' +
                    ", status='" + status + '\'' +
                    ", addressProvince='" + addressProvince + '\'' +
                    ", addressCity='" + addressCity + '\'' +
                    ", addressArea='" + addressArea + '\'' +
                    ", addressStreet='" + addressStreet + '\'' +
                    ", address='" + address + '\'' +
                    ", price=" + price +
                    ", expireTime=" + expireTime +
                    ", createTime=" + createTime +
                    ", receiverTelephone='" + receiverTelephone + '\'' +
                    ", goodsList=" + goodsList +
                    '}';
        }
    }
    public static class OrderDataRowDetailGoodDetailDTO{
        String goodsId;
        String goodsName;
        String goodsPic;
        String count;
        String price;
        String specTitle;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsPic() {
            return goodsPic;
        }

        public void setGoodsPic(String goodsPic) {
            this.goodsPic = goodsPic;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSpecTitle() {
            return specTitle;
        }

        public void setSpecTitle(String specTitle) {
            this.specTitle = specTitle;
        }

        @Override
        public String toString() {
            return "OrderDataRowDetailGoodDetailDTO{" +
                    "goodsId='" + goodsId + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsPic='" + goodsPic + '\'' +
                    ", count='" + count + '\'' +
                    ", price='" + price + '\'' +
                    ", specTitle='" + specTitle + '\'' +
                    '}';
        }
    }

    public static class CommitOrderErrorDTO{
        /**
         * 0-success
         * 80001-库存不足
         * 80005-商品还未开始抢购
         */
        private Integer code;
        private String message;
        private String data;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "CommitOrderErrorDTO{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }

    /**
     * 下单返回信息
     */
    public static class CommitOrderDTO{
        /**
         * 0-success
         * 80001-库存不足
         * 80005-商品还未开始抢购
         */
        private Integer code;
        private String message;
        private CommitOrderDataDTO data;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public CommitOrderDataDTO getData() {
            return data;
        }

        public void setData(CommitOrderDataDTO data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "CommitOrderDTO{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
    public static class CommitOrderDataDTO{
        private String orderId;
        private String price;
        private String postFee;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPostFee() {
            return postFee;
        }

        public void setPostFee(String postFee) {
            this.postFee = postFee;
        }

        @Override
        public String toString() {
            return "CommitOrderDataDTO{" +
                    "orderId='" + orderId + '\'' +
                    ", price='" + price + '\'' +
                    ", postFee='" + postFee + '\'' +
                    '}';
        }
    }
    //endregion
}
