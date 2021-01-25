package com.myqq.service.autoshopping;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by cdzhangjunfeng1@jd.com on 2019/5/10.
 */
public class BaseBean implements Serializable {

    private static final long serialVersionUID = -592112284876928383L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建人erp
     */
    private String creator;
    /**
     * 数据创建时间
     */
    private Date created;
    /**
     * 修改人erp
     */
    private String mender;
    /**
     * 数据修改时间
     */
    private Date modified;
    /**
     * 删除标记：1已删除，0未删除
     */
    private Integer deleted;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
