package com.ken.wms.common.dao;

import com.ken.wms.common.domain.BaseEntity;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * Base mapper interface for all entities in the WMS system
 */
public interface BaseMapper<T extends BaseEntity> {
    T selectById(Integer id);
    List<T> selectByName(String name);
    List<T> selectAll();
    List<T> selectByCondition(Map<String, Object> params);
    int insert(T entity);
    int update(T entity);
    int deleteById(Integer id);
    int batchInsert(@Param("entities") List<T> entities);
    int batchUpdate(@Param("entities") List<T> entities);
    int batchDelete(@Param("ids") List<Integer> ids);
}
