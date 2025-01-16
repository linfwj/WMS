package com.ken.wms.common.framework;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;
import com.ken.wms.common.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base implementation of BaseService that provides default implementations
 * Entity-specific services can extend this class
 * 
 * @author Devin
 * @param <T> The entity type this service handles
 */
public abstract class AbstractBaseServiceImpl<T> implements BaseService<T> {
    
    @Autowired
    protected ExcelUtil excelUtil;
    
    /**
     * Get the mapper for this entity type
     * Subclasses must implement this to provide their specific mapper
     */
    protected abstract BaseMapper<T> getMapper();
    
    /**
     * Get the entity class type
     * Subclasses must implement this to provide their specific class
     */
    protected abstract Class<T> getEntityClass();
    
    /**
     * Validate an entity before saving
     * Subclasses should override this to provide entity-specific validation
     */
    protected boolean validateEntity(T entity) {
        return entity != null;
    }
    
    @Override
    public boolean add(T entity) {
        if (validateEntity(entity)) {
            getMapper().insert(entity);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean update(T entity) {
        if (validateEntity(entity)) {
            getMapper().update(entity);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean delete(Integer id) {
        if (id != null) {
            getMapper().deleteById(id);
            return true;
        }
        return false;
    }
    
    @Override
    public Map<String, Object> selectById(Integer id) {
        Map<String, Object> resultSet = new HashMap<>();
        List<T> data = new ArrayList<>();
        
        T entity = getMapper().selectById(id);
        if (entity != null) {
            data.add(entity);
        }
        
        resultSet.put("data", data);
        resultSet.put("total", data.size());
        return resultSet;
    }
    
    @Override
    public Map<String, Object> selectAll(int offset, int limit) {
        Map<String, Object> resultSet = new HashMap<>();
        List<T> data;
        long total = 0;
        boolean isPagination = true;
        
        if (offset < 0 || limit < 0) {
            isPagination = false;
        }
        
        if (isPagination) {
            PageHelper.offsetPage(offset, limit);
            data = getMapper().selectAll();
            if (data != null) {
                PageInfo<T> pageInfo = new PageInfo<>(data);
                total = pageInfo.getTotal();
            } else {
                data = new ArrayList<>();
            }
        } else {
            data = getMapper().selectAll();
            if (data != null) {
                total = data.size();
            } else {
                data = new ArrayList<>();
            }
        }
        
        resultSet.put("data", data);
        resultSet.put("total", total);
        return resultSet;
    }
    
    @Override
    public Map<String, Object> selectAll() {
        return selectAll(-1, -1);
    }
    
    @Override
    public Map<String, Object> selectByName(int offset, int limit, String name) {
        Map<String, Object> resultSet = new HashMap<>();
        List<T> data;
        long total = 0;
        boolean isPagination = true;
        
        if (offset < 0 || limit < 0) {
            isPagination = false;
        }
        
        if (isPagination) {
            PageHelper.offsetPage(offset, limit);
            data = getMapper().selectApproximateByName(name);
            if (data != null) {
                PageInfo<T> pageInfo = new PageInfo<>(data);
                total = pageInfo.getTotal();
            } else {
                data = new ArrayList<>();
            }
        } else {
            data = getMapper().selectApproximateByName(name);
            if (data != null) {
                total = data.size();
            } else {
                data = new ArrayList<>();
            }
        }
        
        resultSet.put("data", data);
        resultSet.put("total", total);
        return resultSet;
    }
    
    @Override
    public Map<String, Object> importFromExcel(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int total = 0;
        int available = 0;
        
        List<Object> entities = excelUtil.excelReader(getEntityClass(), file);
        if (entities != null) {
            total = entities.size();
            
            List<T> availableList = new ArrayList<>();
            for (Object obj : entities) {
                @SuppressWarnings("unchecked")
                T entity = (T) obj;
                if (validateEntity(entity)) {
                    availableList.add(entity);
                }
            }
            
            available = availableList.size();
            if (available > 0) {
                getMapper().insertBatch(availableList);
            }
        }
        
        result.put("total", total);
        result.put("available", available);
        return result;
    }
    
    @Override
    public File exportToExcel(List<T> entities) {
        if (entities == null) {
            return null;
        }
        return excelUtil.excelWriter(getEntityClass(), entities);
    }
}
