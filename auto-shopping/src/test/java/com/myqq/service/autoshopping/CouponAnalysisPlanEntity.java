package com.myqq.service.autoshopping;

import java.util.Date;

/**
 * Created by cdzhangjunfeng1@jd.com on 2019/9/10.
 */
public class CouponAnalysisPlanEntity extends BaseBean {

    private long planId;

    private String planName;

    private Date planBeginTime;

    private long batchId;

    private int deliverPlatform;

    private int deliverChannel;

    /**
     * 当前数据库中的policyType字段存储的数据实际为投放策略信息（deliverTarget）
     * 历史遗留问题
     */
    private int policyType;

    private long exposure;

    private long exposurePersonCount;

    private long clickNum;

    private long couponTakenNum;

    private long couponTakenPersonNum;

    private Date time;

    private int forwardSource;

    private double couponTakenRate;

    private long couponUseNum;

    private long couponUseUserNum;

    private double couponUseUserRate;
    /**
     * 拉新策略 0-不限制 1-一年拉新 2-三年拉新
     */
    private Integer pullNewer;
    /**
     * 复购策略 0-不限制 1-3个月复购 2-6个月复购
     */
    private Integer repurchase;
    /**
     * 效果分析口径 0-原始口径 1-成交口径 2-有效口径
     */
    private Integer caliber;

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Date getPlanBeginTime() {
        return planBeginTime;
    }

    public void setPlanBeginTime(Date planBeginTime) {
        this.planBeginTime = planBeginTime;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public int getDeliverPlatform() {
        return deliverPlatform;
    }

    public void setDeliverPlatform(int deliverPlatform) {
        this.deliverPlatform = deliverPlatform;
    }

    public int getDeliverChannel() {
        return deliverChannel;
    }

    public void setDeliverChannel(int deliverChannel) {
        this.deliverChannel = deliverChannel;
    }

    public int getPolicyType() {
        return policyType;
    }

    public void setPolicyType(int policyType) {
        this.policyType = policyType;
    }

    public long getExposure() {
        return exposure;
    }

    public void setExposure(long exposure) {
        this.exposure = exposure;
    }

    public long getExposurePersonCount() {
        return exposurePersonCount;
    }

    public void setExposurePersonCount(long exposurePersonCount) {
        this.exposurePersonCount = exposurePersonCount;
    }

    public long getClickNum() {
        return clickNum;
    }

    public void setClickNum(long clickNum) {
        this.clickNum = clickNum;
    }

    public long getCouponTakenNum() {
        return couponTakenNum;
    }

    public void setCouponTakenNum(long couponTakenNum) {
        this.couponTakenNum = couponTakenNum;
    }

    public long getCouponTakenPersonNum() {
        return couponTakenPersonNum;
    }

    public void setCouponTakenPersonNum(long couponTakenPersonNum) {
        this.couponTakenPersonNum = couponTakenPersonNum;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getForwardSource() {
        return forwardSource;
    }

    public void setForwardSource(int forwardSource) {
        this.forwardSource = forwardSource;
    }

    public double getCouponTakenRate() {
        return couponTakenRate;
    }

    public void setCouponTakenRate(double couponTakenRate) {
        this.couponTakenRate = couponTakenRate;
    }

    public long getCouponUseNum() {
        return couponUseNum;
    }

    public void setCouponUseNum(long couponUseNum) {
        this.couponUseNum = couponUseNum;
    }

    public long getCouponUseUserNum() {
        return couponUseUserNum;
    }

    public void setCouponUseUserNum(long couponUseUserNum) {
        this.couponUseUserNum = couponUseUserNum;
    }

    public double getCouponUseUserRate() {
        return couponUseUserRate;
    }

    public void setCouponUseUserRate(double couponUseUserRate) {
        this.couponUseUserRate = couponUseUserRate;
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

    public Integer getCaliber() {
        return caliber;
    }

    public void setCaliber(Integer caliber) {
        this.caliber = caliber;
    }
}
