package com.ken.wms.common.framework;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

/**
 * Generic base service interface that defines standard operations
 * All entity-specific services can implement this interface
 * 
 * @author Devin
 * @param <T> The entity type this service handles
 */
public interface BaseService<T> {
    
    /**
     * Add a new entity
     * @param entity The entity to add
     * @return true if successful
     */
    boolean add(T entity);
    
    /**
     * Update an existing entity
     * @param entity The entity to update
     * @return true if successful
     */
    boolean update(T entity);
    
    /**
     * Delete an entity by ID
     * @param id The ID of the entity to delete
     * @return true if successful
     */
    boolean delete(Integer id);
    
    /**
     * Select an entity by ID
     * @param id The ID to select
     * @return Map containing the entity data and total count
     */
    Map<String, Object> selectById(Integer id);
    
    /**
     * Select entities with pagination
     * @param offset Pagination offset
     * @param limit Maximum number of records to return
     * @return Map containing the entities and total count
     */
    Map<String, Object> selectAll(int offset, int limit);
    
    /**
     * Select all entities without pagination
     * @return Map containing all entities and total count
     */
    Map<String, Object> selectAll();
    
    /**
     * Search entities by name with pagination
     * @param offset Pagination offset
     * @param limit Maximum number of records to return
     * @param name Name to search for
     * @return Map containing matching entities and total count
     */
    Map<String, Object> selectByName(int offset, int limit, String name);
    
    /**
     * Import entities from Excel file
     * @param file The Excel file to import
     * @return Map containing import results
     */
    Map<String, Object> importFromExcel(MultipartFile file);
    
    /**
     * Export entities to Excel file
     * @param entities List of entities to export
     * @return The generated Excel file
     */
    File exportToExcel(java.util.List<T> entities);
}
