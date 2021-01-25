package com.myqq.service.autoshopping;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * @Description ISV效果数据查询
 * @Author lisongjun
 * @Date 2020/9/30 17:22
 */
@Service
public class ISVPlanEffectBiz {
    /**
     * 单次查询最大量
     */
    private static final int maxQueryPageSize = 200;
    /**
     * 智能投放sceneid=1000000041
     */
    private static final String INTELLIGENT_DELIVERY_SCENE_ID = "1000000041";
    /**
     * 重构后的效果分析完成日期jimdb key
     */
    private static final String REFACTOR_INTELLIGENT_DELIVERY_EFFECT = "refactor_complete_date_";

    private static List<CouponAnalysisPlanEntity> refactorList = new ArrayList<>();
    private static List<CouponAnalysisPlanEntity> oldList = new ArrayList<>();
    static {
        for(int i=1;i<10;i++){
            CouponAnalysisPlanEntity cc = new CouponAnalysisPlanEntity();
            cc.setPlanId(Long.valueOf(i));
            cc.setPlanName("new"+i);
            refactorList.add(cc);
        }
        for(int i=1;i<11;i++){
            CouponAnalysisPlanEntity cc = new CouponAnalysisPlanEntity();
            cc.setPlanId(Long.valueOf(i));
            cc.setPlanName("old"+i);
            oldList.add(cc);
        }
    }

    public static void main(String[] args) {
        EffectPageInfoDTO effectPageInfoDTO = getIsvPlanEffectByPage(1,5);
        effectPageInfoDTO.getRecords().forEach(item -> System.out.println(item.getPlanId()+" -- "+item.getPlanName()));
        effectPageInfoDTO = getIsvPlanEffectByPage(2,5);
        effectPageInfoDTO.getRecords().forEach(item -> System.out.println(item.getPlanId()+" -- "+item.getPlanName()));
        effectPageInfoDTO = getIsvPlanEffectByPage(3,5);
        effectPageInfoDTO.getRecords().forEach(item -> System.out.println(item.getPlanId()+" -- "+item.getPlanName()));
        effectPageInfoDTO = getIsvPlanEffectByPage(4,5);
        effectPageInfoDTO.getRecords().forEach(item -> System.out.println(item.getPlanId()+" -- "+item.getPlanName()));

        effectPageInfoDTO = getIsvPlanEffectByPage(1,10);
        effectPageInfoDTO.getRecords().forEach(item -> System.out.println(item.getPlanId()+" -- "+item.getPlanName()));
        effectPageInfoDTO = getIsvPlanEffectByPage(2,10);
        effectPageInfoDTO.getRecords().forEach(item -> System.out.println(item.getPlanId()+" -- "+item.getPlanName()));
    }

    /**
     * 分页查询isv效果数据信息
     * 数据为isv提报来源，未删除，成交口径和有效口径效果数据
     * 关联表查询：coupon_pool.forward_source=4,coupon_analysis_plan.deleted=0 and coupon_analysis_plan.caliber in (1,2)
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    public static EffectPageInfoDTO getIsvPlanEffectByPage(Integer pageNo, Integer pageSize) {
        if (pageNo == null || pageSize == null || pageNo <= 0 || pageSize <= 0 || pageSize > maxQueryPageSize) {
            throw new BusinessException(BusinessErrorCode.PARAM_ERROR, MessageFormat.format("pageNo,pageSize为正整数，且pageSize不超过{0}", maxQueryPageSize));
        }
        EffectPageInfoDTO effectPageInfoDTO = new EffectPageInfoDTO();
        //最新效果数据分析完成后再返还信息
        boolean newDataAnalysedComplete = newDataAnalysedComplete();
        boolean refactorNewDataAnalysedComplete = refactorNewDataAnalysedComplete();
        if (!newDataAnalysedComplete && !refactorNewDataAnalysedComplete) {
            return effectPageInfoDTO;
        }
        int total;
        int historyTotal = 10;
        int refactorTotal = 9;
        System.out.println((MessageFormat.format("智投效果数据分析统计效果数据总数：历史数据：{0},重构数据：{1}", historyTotal, refactorTotal)));
        total = historyTotal + refactorTotal;
        if (total == 0) {
            return effectPageInfoDTO;
        }
        PageCountDTO pageCount = new PageCountDTO();
        effectPageInfoDTO.setPage(pageCount);
        pageCount.setTotal(total);
        pageCount.setCurrent(pageNo);
        pageCount.setPageSize(pageSize);
        List<ISVPlanEffectDTO> isvAnalysisDimPlans = new ArrayList<>();
        effectPageInfoDTO.setRecords(isvAnalysisDimPlans);
        int pageCountNum = total / pageSize;
        pageCount.setPageCount(total % pageSize != 0 ? pageCountNum + 1 : pageCountNum);
        if (pageNo > pageCount.getPageCount()) {
            throw new BusinessException(BusinessErrorCode.PARAM_ERROR, MessageFormat.format("查询超过页码范围,不能超过最大页码{0}", pageCount.getPageCount()));
        }
        //先查新数据（都是不限制人群策略,不用查coupon_deliver_strategy策略信息表），新数据查完后查历史数据
        int startIndex = (pageNo - 1) * pageSize;
        if (refactorTotal > 0 && startIndex < refactorTotal) {
            List<CouponAnalysisPlanEntity> analysisPlanEntityList = queryIsvAnalysisPlanEntity(startIndex, pageNo.equals(pageCount.getPageCount()) ? (refactorTotal - startIndex) : pageSize, true);
            for (CouponAnalysisPlanEntity analysisPlanEntity : analysisPlanEntityList) {
                ISVPlanEffectDTO effectDTO = convertAnalysisPlanEntityToPlanEffect(analysisPlanEntity);
                isvAnalysisDimPlans.add(effectDTO);
            }
        }
        //历史数据为0或者不需要查
        if (historyTotal <= 0 || startIndex + pageSize <= refactorTotal) {
            return effectPageInfoDTO;
        }
        //跨新旧数据查询
        boolean dataCrossOldAndNew = startIndex < refactorTotal && (startIndex + pageSize) > refactorTotal;
        int refactorPageNum = refactorTotal / pageSize;
        int newPageSize = dataCrossOldAndNew ? (startIndex + pageSize) - refactorTotal : pageSize;
        int oldExtra = 0;
        if(refactorTotal % pageSize != 0){
            refactorPageNum = refactorPageNum + 1;
            oldExtra = refactorPageNum * pageSize - refactorTotal;
        }
        startIndex = dataCrossOldAndNew ? 0 : (pageNo - refactorPageNum - 1) * pageSize + oldExtra;
        List<CouponAnalysisPlanEntity> analysisPlanEntityList = queryIsvAnalysisPlanEntity(startIndex, pageNo.equals(pageCount.getPageCount()) ? (historyTotal - startIndex) : newPageSize, false);
        for (CouponAnalysisPlanEntity analysisPlanEntity : analysisPlanEntityList) {
            ISVPlanEffectDTO effectDTO = convertAnalysisPlanEntityToPlanEffect(analysisPlanEntity);
            isvAnalysisDimPlans.add(effectDTO);
        }
        return effectPageInfoDTO;
    }

    private static List<CouponAnalysisPlanEntity> queryIsvAnalysisPlanEntity(int startIndex, int i, boolean refactor) {
        if(refactor){
            return refactorList.subList(startIndex,startIndex+i > refactorList.size() ? refactorList.size() : startIndex+i);
        }else {
            return oldList.subList(startIndex,startIndex+i > refactorList.size() ? refactorList.size()+1 : startIndex+i);
        }
    }

    /**
     * 昨日效果数据是否计算完成
     * 根据缓存中计算效果数据的日期判断是否为最新结果，非最新结果不返回数据，给出提示信息。比如：2020-10-10查询返回2020-10-09，则表示2020-10-09效果数据计算分析完成
     * 缓存有效期 2 Day
     *
     * @return 效果数据日期与当前日期相差一天时为最新数据
     */
    private static boolean newDataAnalysedComplete() {
        return true;
    }

    /**
     * 效果数据结果转换
     *
     * @param planEntity
     * @return
     */
    private static ISVPlanEffectDTO convertAnalysisPlanEntityToPlanEffect(CouponAnalysisPlanEntity planEntity) {
        ISVPlanEffectDTO effectDto = new ISVPlanEffectDTO();
        effectDto.setPlanId(planEntity.getPlanId());
        effectDto.setPlanName(planEntity.getPlanName());
        effectDto.setBatchId(planEntity.getBatchId());
        effectDto.setPlanBeginTime(planEntity.getPlanBeginTime());
        effectDto.setPlatform(planEntity.getDeliverPlatform());
        effectDto.setChannel(planEntity.getDeliverChannel());
        effectDto.setPullNewer(planEntity.getPullNewer());
        effectDto.setRepurchase(planEntity.getRepurchase());
        effectDto.setExposure(planEntity.getExposure());
        effectDto.setExposurePersonCount(planEntity.getExposurePersonCount());
        effectDto.setClickNum(planEntity.getClickNum());
        effectDto.setCouponTakenPersonNum(planEntity.getCouponTakenPersonNum());
        effectDto.setCouponTakenNum(planEntity.getCouponTakenNum());
        effectDto.setCouponTakenRate(planEntity.getCouponTakenRate());
        effectDto.setCouponUseNum(planEntity.getCouponUseNum());
        effectDto.setCouponUseUserNum(planEntity.getCouponUseUserNum());
        effectDto.setCouponUseUserRate(planEntity.getCouponUseUserRate());
        effectDto.setCaliber(planEntity.getCaliber());
        return effectDto;
    }

    /**
     * 重构后效果数据
     * 昨日效果数据是否计算完成
     * 根据缓存中计算效果数据的日期判断是否为最新结果，非最新结果不返回数据，给出提示信息。比如：2020-10-10查询返回2020-10-09，则表示2020-10-09效果数据计算分析完成
     * 缓存有效期 2 Day
     *
     * @return 效果数据日期与当前日期相差一天时为最新数据
     */
    private static boolean refactorNewDataAnalysedComplete() {
        return true;
    }
}
