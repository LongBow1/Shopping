package com.myqq.service.autoshopping;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 效果分页信息
 * @Author lisongjun
 * @Date 2020/9/28 21:26
 */
public class EffectPageInfoDTO implements Serializable {
    private static final long serialVersionUID = -7031668921808171138L;
    /**
     * 分页记录信息
     */
    private List<ISVPlanEffectDTO> records;
    /**
     * 分页统计信息
     */
    private PageCountDTO page;

    public List<ISVPlanEffectDTO> getRecords() {
        return records;
    }

    public void setRecords(List<ISVPlanEffectDTO> records) {
        this.records = records;
    }

    public PageCountDTO getPage() {
        return page;
    }

    public void setPage(PageCountDTO page) {
        this.page = page;
    }
}
