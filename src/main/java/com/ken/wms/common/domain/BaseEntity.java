package com.ken.wms.common.domain;

import java.util.Date;

/**
 * Base interface for all entities in the WMS system
 */
public interface BaseEntity {
    Integer getId();
    void setId(Integer id);
    String getName();
    void setName(String name);
    Date getCreateTime();
    void setCreateTime(Date createTime);
    Date getUpdateTime(); 
    void setUpdateTime(Date updateTime);
}
