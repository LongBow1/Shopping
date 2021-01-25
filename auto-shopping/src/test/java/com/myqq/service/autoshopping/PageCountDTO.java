package com.myqq.service.autoshopping;

import java.io.Serializable;

/**
 * @Description 分页统计信息
 * @Author lisongjun
 * @Date 2020/9/28 21:24
 */
public class PageCountDTO implements Serializable {
    private static final long serialVersionUID = -4423231800445860686L;
    /**
     * 总记录数量
     */
    private Integer total;
    /**
     * 当前页码
     */
    private Integer current;
    /**
     * 总页数
     */
    private Integer pageCount;
    /**
     * 每页数量，单页查询最大200
     */
    private Integer pageSize;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
