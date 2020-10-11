package com.myqq.service.youza.entity;


import java.io.Serializable;

public class IntendOrderDTO implements Serializable {
    private String goodShotName;
    private String goodShotNameExtra;
    private String goodColorList;
    private String goodSizeList;
    private String goodStyleList;
    private String goodSpecList;
    private int goodNum;
    private String receptNameList;
    private boolean needPresale;
    private String quantifierNum;

    private boolean mergeOrder;
    private String goodOtherAttrList;

    public String getGoodOtherAttrList() {
        return goodOtherAttrList;
    }

    public void setGoodOtherAttrList(String goodOtherAttrList) {
        this.goodOtherAttrList = goodOtherAttrList;
    }

    public boolean isMergeOrder() {
        return mergeOrder;
    }

    public void setMergeOrder(boolean mergeOrder) {
        this.mergeOrder = mergeOrder;
    }

    public String getGoodShotNameExtra() {
        return goodShotNameExtra;
    }

    public void setGoodShotNameExtra(String goodShotNameExtra) {
        this.goodShotNameExtra = goodShotNameExtra;
    }

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

    /**
     * 预售现货类型
     * 0-不区分
     * 1-现货
     * 2-预售
     */
    private int toBuySellType;

    public boolean isNeedPresale() {
        return needPresale;
    }

    public void setNeedPresale(boolean needPresale) {
        this.needPresale = needPresale;
    }

    public String getGoodShotName() {
        return goodShotName;
    }

    public void setGoodShotName(String goodShotName) {
        this.goodShotName = goodShotName;
    }

    public String getGoodColorList() {
        return goodColorList;
    }

    public void setGoodColorList(String goodColorList) {
        this.goodColorList = goodColorList;
    }

    public String getGoodSizeList() {
        return goodSizeList;
    }

    public void setGoodSizeList(String goodSizeList) {
        this.goodSizeList = goodSizeList;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public String getReceptNameList() {
        return receptNameList;
    }

    public void setReceptNameList(String receptNameList) {
        this.receptNameList = receptNameList;
    }

    public String getGoodStyleList() {
        return goodStyleList;
    }

    public void setGoodStyleList(String goodStyleList) {
        this.goodStyleList = goodStyleList;
    }

    public String getGoodSpecList() {
        return goodSpecList;
    }

    public void setGoodSpecList(String goodSpecList) {
        this.goodSpecList = goodSpecList;
    }

    @Override
    public String toString() {
        return "IntendOrderDTO{" +
                "goodShotName='" + goodShotName + '\'' +
                ", goodColorList='" + goodColorList + '\'' +
                ", goodSizeList='" + goodSizeList + '\'' +
                ", goodStyleList='" + goodStyleList + '\'' +
                ", goodSpecList='" + goodSpecList + '\'' +
                ", goodNum=" + goodNum +
                ", receptNameList='" + receptNameList + '\'' +
                ", needPresale=" + needPresale +
                '}';
    }
}
