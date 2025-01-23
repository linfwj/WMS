package com.ken.wms.common.service.Interface;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Generic base service interface for entity management
 * Provides common CRUD operations, import/export functionality, and pagination support
 * @author Devin
 *
 * @param <T> The entity type this service handles
 */
public interface BaseEntityService<T> {

    /**
     * Return entity record by ID
     * @param entityId Entity ID
     * @return Map containing: key "data" for record data, key "total" for result count
     * @throws Exception if operation fails
     */
    Map<String, Object> selectById(Integer entityId) throws Exception;
    
    /**
     * Return entity records by name with pagination
     * Supports fuzzy name matching
     * @param offset Pagination offset
     * @param limit Pagination size
     * @param name Entity name to search
     * @return Map containing: key "data" for records, key "total" for total matching records
     * @throws Exception if operation fails
     */
    Map<String, Object> selectByName(int offset, int limit, String name) throws Exception;
    
    /**
     * Return entity records by exact name match
     * @param name Entity name
     * @return Map containing: key "data" for records, key "total" for total matching records
     * @throws Exception if operation fails
     */
    Map<String, Object> selectByName(String name) throws Exception;
    
    /**
     * Return all entity records with pagination
     * @param offset Pagination offset
     * @param limit Pagination size
     * @return Map containing: key "data" for records, key "total" for total records
     * @throws Exception if operation fails
     */
    Map<String, Object> selectAll(int offset, int limit) throws Exception;
    
    /**
     * Return all entity records
     * @return Map containing: key "data" for records, key "total" for total records
     * @throws Exception if operation fails
     */
    Map<String, Object> selectAll() throws Exception;
    
    /**
     * Add new entity
     * @param entity Entity to add
     * @return true if successful, false otherwise
     * @throws Exception if operation fails
     */
    boolean addEntity(T entity) throws Exception;
    
    /**
     * Update existing entity
     * @param entity Entity to update
     * @return true if successful, false otherwise
     * @throws Exception if operation fails
     */
    boolean updateEntity(T entity) throws Exception;
    
    /**
     * Delete entity by ID
     * @param entityId ID of entity to delete
     * @return true if successful, false otherwise
     * @throws Exception if operation fails
     */
    boolean deleteEntity(Integer entityId) throws Exception;
    
    /**
     * Import entities from file
     * @param file File containing entity data
     * @return Map containing: key "total" for total records processed, key "available" for successfully imported records
     * @throws Exception if operation fails
     */
    Map<String, Object> importEntities(MultipartFile file) throws Exception;
    
    /**
     * Export entities to file
     * @param entities List of entities to export
     * @return File containing exported data
     * @throws Exception if operation fails
     */
    File exportEntities(List<T> entities) throws Exception;
}
