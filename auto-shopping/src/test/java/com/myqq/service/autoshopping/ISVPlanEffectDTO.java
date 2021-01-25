package com.myqq.service.autoshopping;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description ISV效果信息
 * @Author lisongjun
 * @Date 2020/9/28 21:15
 */
public class ISVPlanEffectDTO implements Serializable {
    private static final long serialVersionUID = -7587170956843805618L;
    /**
     * 计划编号
     */
    private Long planId;
    /**
     * 计划名称
     */
    private String planName;
    /**
     * 优惠券批次号
     */
    private Long batchId;
    /**
     * 计划有效期-开始时间
     */
    private Date planBeginTime;
    /**
     * 投放平台： 1—app
     */
    private Integer platform;
    /**
     * 投放渠道枚举
     */
    private Integer channel;
    /**
     * 拉新策略， 0--不限制，1--1年，2--3年 //投放人群
     */
    private Integer pullNewer;
    /**
     * 复购策略：0—不限制，1—3个月，2—6个月 //投放人群
     */
    private Integer repurchase;
    /**
     * 曝光量
     */
    private Long exposure;
    /**
     * 曝光人数
     */
    private Long exposurePersonCount;
    /**
     * 点击次数
     */
    private Long clickNum;
    /**
     * 优惠券领取人数
     */
    private Long couponTakenPersonNum;
    /**
     * 优惠券领取量
     */
    private Long couponTakenNum;
    /**
     * 优惠券领取率
     */
    private Double couponTakenRate;
    /**
     * 优惠券使用量
     */
    private Long couponUseNum;
    /**
     * 优惠券使用人数
     */
    private Long couponUseUserNum;
    /**
     * 优惠券使用率
     */
    private Double couponUseUserRate;
    /**
     * 效果分析口径 1-成交口径 2-有效口径
     */
    private Integer caliber;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Date getPlanBeginTime() {
        return planBeginTime;
    }

    public void setPlanBeginTime(Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getPullNewer() {
        return pullNewer;
    }

    public void setPullNewer(Integer pullNewer) {
        this.pullNewer = pullNewer;
    }

    public Integer getRepurchase() {
        return repurchase;
    }

    public void setRepurchase(Integer repurchase) {
        this.repurchase = repurchase;
    }

    public Long getExposure() {
        return exposure;
    }

    public void setExposure(Long exposure) {
        this.exposure = exposure;
    }

    public Long getExposurePersonCount() {
        return exposurePersonCount;
    }

    public void setExposurePersonCount(Long exposurePersonCount) {
        this.exposurePersonCount = exposurePersonCount;
    }

    public Long getClickNum() {
        return clickNum;
    }

    public void setClickNum(Long clickNum) {
        this.clickNum = clickNum;
    }

    public Long getCouponTakenPersonNum() {
        return couponTakenPersonNum;
    }

    public void setCouponTakenPersonNum(Long couponTakenPersonNum) {
        this.couponTakenPersonNum = couponTakenPersonNum;
    }

    public Long getCouponTakenNum() {
        return couponTakenNum;
    }

    public void setCouponTakenNum(Long couponTakenNum) {
        this.couponTakenNum = couponTakenNum;
    }

    public Double getCouponTakenRate() {
        return couponTakenRate;
    }

    public void setCouponTakenRate(Double couponTakenRate) {
        this.couponTakenRate = couponTakenRate;
    }

    public Long getCouponUseNum() {
        return couponUseNum;
    }

    public void setCouponUseNum(Long couponUseNum) {
        this.couponUseNum = couponUseNum;
    }

    public Long getCouponUseUserNum() {
        return couponUseUserNum;
    }

    public void setCouponUseUserNum(Long couponUseUserNum) {
        this.couponUseUserNum = couponUseUserNum;
    }

    public Double getCouponUseUserRate() {
        return couponUseUserRate;
    }

    public void setCouponUseUserRate(Double couponUseUserRate) {
        this.couponUseUserRate = couponUseUserRate;
    }

    public Integer getCaliber() {
        return caliber;
    }

    public void setCaliber(Integer caliber) {
        this.caliber = caliber;
    }
}
