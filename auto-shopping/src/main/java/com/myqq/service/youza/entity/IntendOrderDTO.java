package com.myqq.service.youza.entity;


import java.io.Serializable;
import java.util.List;

public class IntendOrderDTO implements Serializable {
    private String goodShotName;
    private String goodColorList;
    private String goodSizeList;
    private String goodStyleList;
    private String goodSpecList;
    private int goodNum;
    private String receptNameList;
    private boolean needPresale;

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
