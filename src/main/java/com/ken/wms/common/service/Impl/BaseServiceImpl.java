package com.ken.wms.common.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ken.wms.common.service.Interface.BaseService;
import com.ken.wms.common.domain.BaseEntity;
import com.ken.wms.common.dao.BaseMapper;
import com.ken.wms.common.util.ExcelUtil;
import com.ken.wms.common.util.ResponseUtil;
import com.ken.wms.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Base service implementation for all entities in the WMS system
 */
@Service
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {
    
    @Autowired
    protected BaseMapper<T> mapper;
    
    @Autowired
    protected ExcelUtil excelUtil;
    
    @Autowired
    protected ResponseUtil responseUtil;

    /**
     * Validate entity before operations
     */
    protected abstract void validateEntity(T entity) throws ServiceException;

    /**
     * Get entity name for error messages
     */
    protected abstract String getEntityName();

    /**
     * Get entity class for Excel operations
     */
    protected abstract Class<T> getEntityClass();

    @Override
    public T getById(Integer id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException(getEntityName() + " ID cannot be null");
            }
            return mapper.selectById(id);
        } catch (PersistenceException e) {
            throw new ServiceException("Error getting " + getEntityName() + " by ID", e);
        }
    }

    @Override
    public List<T> getByName(String name) throws ServiceException {
        try {
            if (StringUtils.isBlank(name)) {
                throw new ServiceException(getEntityName() + " name cannot be empty");
            }
            return mapper.selectByName(name);
        } catch (PersistenceException e) {
            throw new ServiceException("Error getting " + getEntityName() + " by name", e);
        }
    }

    @Override
    public List<T> getAll() throws ServiceException {
        try {
            return mapper.selectAll();
        } catch (PersistenceException e) {
            throw new ServiceException("Error getting all " + getEntityName() + "s", e);
        }
    }

    @Override
    public Map<String, Object> search(String searchType, String keyword, int offset, int limit) throws ServiceException {
        try {
            // Initialize response
            Map<String, Object> resultMap = new HashMap<>();
            List<T> resultList;

            // Configure pagination if needed
            if (offset >= 0 && limit > 0) {
                PageHelper.offsetPage(offset, limit);
            }

            // Perform search based on type
            switch (searchType) {
                case "searchAll":
                    resultList = mapper.selectAll();
                    break;
                case "searchById":
                    if (!StringUtils.isNumeric(keyword)) {
                        throw new ServiceException("Invalid ID format");
                    }
                    T entity = mapper.selectById(Integer.parseInt(keyword));
                    resultList = entity != null ? Collections.singletonList(entity) : Collections.emptyList();
                    break;
                case "searchByName":
                    resultList = mapper.selectByName(keyword);
                    break;
                default:
                    throw new ServiceException("Invalid search type: " + searchType);
            }

            // Get pagination info if enabled
            if (offset >= 0 && limit > 0) {
                PageInfo<T> pageInfo = new PageInfo<>(resultList);
                resultMap.put("total", pageInfo.getTotal());
            } else {
                resultMap.put("total", resultList.size());
            }

            resultMap.put("data", resultList);
            return resultMap;

        } catch (PersistenceException e) {
            throw new ServiceException("Error searching " + getEntityName(), e);
        }
    }

    @Override
    public boolean add(T entity) throws ServiceException {
        try {
            validateEntity(entity);
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            return mapper.insert(entity) > 0;
        } catch (PersistenceException e) {
            throw new ServiceException("Error adding " + getEntityName(), e);
        }
    }

    @Override
    public boolean update(T entity) throws ServiceException {
        try {
            validateEntity(entity);
            entity.setUpdateTime(new Date());
            return mapper.update(entity) > 0;
        } catch (PersistenceException e) {
            throw new ServiceException("Error updating " + getEntityName(), e);
        }
    }

    @Override
    public boolean delete(Integer id) throws ServiceException {
        try {
            if (id == null) {
                throw new ServiceException(getEntityName() + " ID cannot be null");
            }
            return mapper.deleteById(id) > 0;
        } catch (PersistenceException e) {
            throw new ServiceException("Error deleting " + getEntityName(), e);
        }
    }

    @Override
    public Map<String, Object> importEntities(MultipartFile file) throws ServiceException {
        try {
            if (file == null || file.isEmpty()) {
                throw new ServiceException("Import file cannot be empty");
            }

            // Use ExcelUtil to parse entities
            List<T> entities = excelUtil.excelToList(file, getEntityClass());
            
            // Validate all entities
            for (T entity : entities) {
                validateEntity(entity);
                entity.setCreateTime(new Date());
                entity.setUpdateTime(new Date());
            }

            // Batch insert
            int result = mapper.batchInsert(entities);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("total", entities.size());
            response.put("success", result);
            return response;

        } catch (Exception e) {
            throw new ServiceException("Error importing " + getEntityName() + "s", e);
        }
    }

    @Override
    public void exportEntities(String searchType, String keyword, HttpServletResponse response) throws ServiceException {
        try {
            // Get entities based on search criteria
            Map<String, Object> searchResult = search(searchType, keyword, -1, -1);
            @SuppressWarnings("unchecked")
            List<T> entities = (List<T>) searchResult.get("data");

            // Set response headers
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", 
                "attachment;filename=" + getEntityName().toLowerCase() + "_export.xlsx");

            // Use ExcelUtil to export
            excelUtil.listToExcel(entities, getEntityClass(), response.getOutputStream());

        } catch (Exception e) {
            throw new ServiceException("Error exporting " + getEntityName() + "s", e);
        }
    }
}
