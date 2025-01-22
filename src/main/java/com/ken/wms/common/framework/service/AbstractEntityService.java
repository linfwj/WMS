package com.ken.wms.common.framework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ken.wms.common.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base implementation of BaseEntityService.
 * Provides common functionality and helper methods for entity services.
 *
 * @param <T> The entity type this service manages
 * @author Devin
 */
public abstract class AbstractEntityService<T> implements BaseEntityService<T> {

    @Autowired
    protected ExcelUtil excelUtil;

    /**
     * Creates a standard result map with data and total count
     * @param data List of entities
     * @param total Total count
     * @return Map with standardized structure
     */
    protected Map<String, Object> createResultMap(List<T> data, long total) {
        Map<String, Object> resultSet = new HashMap<>();
        if (data == null) {
            data = new ArrayList<>();
        }
        resultSet.put("data", data);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * Applies pagination using PageHelper if offset and limit are valid
     * @param offset Pagination offset
     * @param limit Maximum number of records
     * @return true if pagination was applied, false if using unlimited query
     */
    protected boolean applyPagination(int offset, int limit) {
        if (offset >= 0 && limit > 0) {
            PageHelper.offsetPage(offset, limit);
            return true;
        }
        return false;
    }

    /**
     * Basic entity validation
     * @param entity Entity to validate
     * @return true if valid, false otherwise
     */
    protected abstract boolean validateEntity(T entity);

    /**
     * Handles the common logic for importing entities from a file
     * @param file File to import
     * @param entityClass Class of the entity
     * @return Map containing import results
     */
    protected Map<String, Object> handleImport(MultipartFile file, Class<T> entityClass) {
        Map<String, Object> resultMap = new HashMap<>();
        int total = 0;
        int available = 0;

        if (file != null) {
            List<Object> importedObjects = excelUtil.excelReader(entityClass, file);
            if (importedObjects != null) {
                total = importedObjects.size();
                List<T> validEntities = new ArrayList<>();
                
                for (Object obj : importedObjects) {
                    @SuppressWarnings("unchecked")
                    T entity = (T) obj;
                    if (validateEntity(entity)) {
                        validEntities.add(entity);
                    }
                }
                
                available = validEntities.size();
                if (available > 0) {
                    saveImportedEntities(validEntities);
                }
            }
        }

        resultMap.put("total", total);
        resultMap.put("available", available);
        return resultMap;
    }

    /**
     * Saves a batch of imported entities
     * @param entities List of valid entities to save
     */
    protected abstract void saveImportedEntities(List<T> entities);

    /**
     * Handles the common logic for exporting entities to a file
     * @param entities List of entities to export
     * @param entityClass Class of the entity
     * @return File containing exported data
     */
    protected File handleExport(List<T> entities, Class<T> entityClass) {
        if (entities == null) {
            return null;
        }
        return excelUtil.excelWriter(entityClass, entities);
    }
}
