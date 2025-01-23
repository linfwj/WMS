package com.ken.wms.common.service.Impl;

import com.ken.wms.common.service.Interface.BaseEntityService;
import com.ken.wms.common.util.ExcelUtil;
import com.ken.wms.dao.BaseEntityMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base implementation of BaseEntityService
 * Provides common functionality for entity management
 * @author Devin
 *
 * @param <T> The entity type this service handles
 */
public abstract class AbstractEntityService<T> implements BaseEntityService<T> {

    @Autowired
    protected BaseEntityMapper<T> mapper;
    
    @Autowired
    protected ExcelUtil excelUtil;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> selectById(Integer entityId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        T entity = mapper.selectById(entityId);
        resultMap.put("data", entity);
        resultMap.put("total", entity != null ? 1L : 0L);
        return resultMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> selectByName(int offset, int limit, String name) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<T> entities = mapper.selectApproximateByName(name);
        
        // Handle pagination
        int start = Math.min(offset, entities.size());
        int end = Math.min(offset + limit, entities.size());
        List<T> pagedEntities = entities.subList(start, end);
        
        resultMap.put("data", pagedEntities);
        resultMap.put("total", (long) entities.size());
        return resultMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> selectByName(String name) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        T entity = mapper.selectByName(name);
        resultMap.put("data", entity);
        resultMap.put("total", entity != null ? 1L : 0L);
        return resultMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> selectAll(int offset, int limit) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<T> allEntities = mapper.selectAll();
        
        // Handle pagination
        int start = Math.min(offset, allEntities.size());
        int end = Math.min(offset + limit, allEntities.size());
        List<T> pagedEntities = allEntities.subList(start, end);
        
        resultMap.put("data", pagedEntities);
        resultMap.put("total", (long) allEntities.size());
        return resultMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> selectAll() throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<T> entities = mapper.selectAll();
        resultMap.put("data", entities);
        resultMap.put("total", (long) entities.size());
        return resultMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addEntity(T entity) throws Exception {
        if (!validateEntity(entity)) {
            return false;
        }
        mapper.insert(entity);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateEntity(T entity) throws Exception {
        if (!validateEntity(entity)) {
            return false;
        }
        mapper.update(entity);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteEntity(Integer entityId) throws Exception {
        if (entityId == null || entityId <= 0) {
            return false;
        }
        mapper.deleteById(entityId);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> importEntities(MultipartFile file) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        if (file == null || file.isEmpty()) {
            resultMap.put("total", 0);
            resultMap.put("available", 0);
            return resultMap;
        }

        // Actual import logic should be implemented by concrete classes
        // since they know the specific entity type and Excel structure
        return doImportEntities(file);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File exportEntities(List<T> entities) throws Exception {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        
        // Actual export logic should be implemented by concrete classes
        // since they know the specific entity type and Excel structure
        return doExportEntities(entities);
    }

    /**
     * Validate entity before insert/update
     * @param entity Entity to validate
     * @return true if valid, false otherwise
     */
    protected abstract boolean validateEntity(T entity);

    /**
     * Implement specific import logic for entity type
     * @param file File to import from
     * @return Map containing import results
     */
    protected abstract Map<String, Object> doImportEntities(MultipartFile file) throws Exception;

    /**
     * Implement specific export logic for entity type
     * @param entities Entities to export
     * @return File containing exported data
     */
    protected abstract File doExportEntities(List<T> entities) throws Exception;
}
