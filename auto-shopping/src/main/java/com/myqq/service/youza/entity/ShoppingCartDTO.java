package com.myqq.service.youza.entity;

import java.util.List;

public class ShoppingCartDTO {
    private String orderDate;
    private String receiptName;
    private String shotGoodName;
    private String shotGoodNameExtra;

    private List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> toBuyGoodAndAddressInfoDTOS;

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

    public String getShotGoodNameExtra() {
        return shotGoodNameExtra;
    }

    public void setShotGoodNameExtra(String shotGoodNameExtra) {
        this.shotGoodNameExtra = shotGoodNameExtra;
    }

    public List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> getToBuyGoodAndAddressInfoDTOS() {
        return toBuyGoodAndAddressInfoDTOS;
    }

    public void setToBuyGoodAndAddressInfoDTOS(List<ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO> toBuyGoodAndAddressInfoDTOS) {
        this.toBuyGoodAndAddressInfoDTOS = toBuyGoodAndAddressInfoDTOS;
    }
}
