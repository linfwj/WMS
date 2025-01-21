package com.ken.wms.framework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ken.wms.framework.mapper.BaseMapper;
import com.ken.wms.framework.exception.ServiceException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.ken.wms.common.util.ExcelUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of BaseService interface.
 * Provides default implementations for common entity management operations.
 *
 * @param <T> The entity type this service handles
 * @author Devin
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    protected BaseMapper<T> baseMapper;

    @Autowired
    protected ExcelUtil excelUtil;

    /**
     * Get the entity class type for Excel operations
     */
    protected abstract Class<T> getEntityClass();

    @Override
    public Map<String, Object> selectById(Integer id) throws ServiceException {
        Map<String, Object> resultSet = new HashMap<>();
        List<T> entityList = new ArrayList<>();
        long total = 0;

        try {
            T entity = baseMapper.selectById(id);
            if (entity != null) {
                entityList.add(entity);
                total = 1;
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }

        resultSet.put("data", entityList);
        resultSet.put("total", total);
        return resultSet;
    }

    @Override
    public Map<String, Object> selectAll(int offset, int limit) throws ServiceException {
        Map<String, Object> resultSet = new HashMap<>();
        List<T> entityList;
        long total = 0;
        boolean isPagination = true;

        if (offset < 0 || limit < 0) {
            isPagination = false;
        }

        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                entityList = baseMapper.selectAll();
                if (entityList != null) {
                    PageInfo<T> pageInfo = new PageInfo<>(entityList);
                    total = pageInfo.getTotal();
                } else {
                    entityList = new ArrayList<>();
                }
            } else {
                entityList = baseMapper.selectAll();
                if (entityList != null) {
                    total = entityList.size();
                } else {
                    entityList = new ArrayList<>();
                }
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }

        resultSet.put("data", entityList);
        resultSet.put("total", total);
        return resultSet;
    }

    @Override
    public Map<String, Object> selectAll() throws ServiceException {
        return selectAll(-1, -1);
    }

    @Override
    public Map<String, Object> selectByName(int offset, int limit, String name) throws ServiceException {
        Map<String, Object> resultSet = new HashMap<>();
        List<T> entityList;
        long total = 0;
        boolean isPagination = true;

        if (offset < 0 || limit < 0) {
            isPagination = false;
        }

        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                entityList = baseMapper.selectApproximateByName(name);
                if (entityList != null) {
                    PageInfo<T> pageInfo = new PageInfo<>(entityList);
                    total = pageInfo.getTotal();
                } else {
                    entityList = new ArrayList<>();
                }
            } else {
                entityList = baseMapper.selectApproximateByName(name);
                if (entityList != null) {
                    total = entityList.size();
                } else {
                    entityList = new ArrayList<>();
                }
            }
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }

        resultSet.put("data", entityList);
        resultSet.put("total", total);
        return resultSet;
    }

    @Override
    public Map<String, Object> selectByName(String name) throws ServiceException {
        return selectByName(-1, -1, name);
    }

    @Override
    public boolean add(T entity) throws ServiceException {
        try {
            if (entity != null && validateEntity(entity)) {
                baseMapper.insert(entity);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean update(T entity) throws ServiceException {
        try {
            if (entity != null && validateEntity(entity)) {
                baseMapper.update(entity);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Integer id) throws ServiceException {
        try {
            if (id != null && canDelete(id)) {
                baseMapper.deleteById(id);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Parse import file into entity objects
     *
     * @param file File to import
     * @return List of parsed entities
     * @throws ServiceException if parsing fails
     */
    protected List<T> parseImportFile(MultipartFile file) throws ServiceException {
        try {
            List<Object> entityList = excelUtil.excelReader(getEntityClass(), file);
            if (entityList == null) {
                return new ArrayList<>();
            }

            List<T> availableList = new ArrayList<>();
            for (Object object : entityList) {
                @SuppressWarnings("unchecked")
                T entity = (T) object;
                if (validateEntity(entity)) {
                    availableList.add(entity);
                }
            }
            return availableList;
        } catch (Exception e) {
            throw new ServiceException("Failed to parse import file", e);
        }
    }

    /**
     * Generate export file from entity objects
     *
     * @param entities Entities to export
     * @return Generated file
     * @throws ServiceException if export fails
     */
    protected File generateExportFile(List<T> entities) throws ServiceException {
        try {
            return excelUtil.excelWriter(getEntityClass(), entities);
        } catch (Exception e) {
            throw new ServiceException("Failed to generate export file", e);
        }
    }

    @Override
    public Map<String, Object> importEntities(MultipartFile file) throws ServiceException {
        Map<String, Object> resultSet = new HashMap<>();
        List<T> parsedEntities = parseImportFile(file);
        
        int total = parsedEntities.size();
        int available = 0;

        try {
            if (!parsedEntities.isEmpty()) {
                baseMapper.insertBatch(parsedEntities);
                available = parsedEntities.size();
            }
        } catch (Exception e) {
            throw new ServiceException("Failed to import entities", e);
        }

        resultSet.put("total", total);
        resultSet.put("available", available);
        return resultSet;
    }

    @Override
    public File exportEntities(List<T> entities) throws ServiceException {
        return generateExportFile(entities);
    }

    /**
     * Validate entity before saving/updating.
     * Default implementation uses EntityValidator.
     * Subclasses can override to add additional validation.
     *
     * @param entity Entity to validate
     * @return true if valid, false otherwise
     */
    protected boolean validateEntity(T entity) {
        EntityValidator.ValidationResult result = EntityValidator.validate(entity);
        return result.isValid();
    }

    /**
     * Check if an entity can be deleted
     * Subclasses should override this method to provide entity-specific deletion rules
     *
     * @param id ID of entity to check
     * @return true if can be deleted, false otherwise
     */
    protected abstract boolean canDelete(Integer id);
}
