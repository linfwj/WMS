package com.ken.wms.framework.service;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.List;

/**
 * Base service interface for entity management operations.
 * Provides standardized CRUD operations with pagination support.
 *
 * @param <T> The entity type this service handles
 * @author Devin
 */
public interface BaseService<T> {
    
    /**
     * Select an entity by its ID
     *
     * @param id The ID of the entity to select
     * @return Map containing "data" (List of entity) and "total" (count)
     * @throws Exception if operation fails
     */
    Map<String, Object> selectById(Integer id) throws Exception;

    /**
     * Select entities with pagination
     *
     * @param offset Pagination offset
     * @param limit Maximum number of records to return
     * @return Map containing "data" (List of entities) and "total" (total count)
     * @throws Exception if operation fails
     */
    Map<String, Object> selectAll(int offset, int limit) throws Exception;

    /**
     * Select all entities without pagination
     *
     * @return Map containing "data" (List of entities) and "total" (total count)
     * @throws Exception if operation fails
     */
    Map<String, Object> selectAll() throws Exception;

    /**
     * Select entities by name with pagination support
     *
     * @param offset Pagination offset
     * @param limit Maximum number of records to return
     * @param name Name to search for (supports approximate match)
     * @return Map containing "data" (List of entities) and "total" (total count)
     * @throws Exception if operation fails
     */
    Map<String, Object> selectByName(int offset, int limit, String name) throws Exception;

    /**
     * Select entities by name without pagination
     *
     * @param name Name to search for (supports approximate match)
     * @return Map containing "data" (List of entities) and "total" (total count)
     * @throws Exception if operation fails
     */
    Map<String, Object> selectByName(String name) throws Exception;

    /**
     * Add a new entity
     *
     * @param entity Entity to add
     * @return true if successful, false otherwise
     * @throws Exception if operation fails
     */
    boolean add(T entity) throws Exception;

    /**
     * Update an existing entity
     *
     * @param entity Entity to update
     * @return true if successful, false otherwise
     * @throws Exception if operation fails
     */
    boolean update(T entity) throws Exception;

    /**
     * Delete an entity by its ID
     *
     * @param id ID of entity to delete
     * @return true if successful, false otherwise
     * @throws Exception if operation fails
     */
    boolean delete(Integer id) throws Exception;

    /**
     * Import entities from a file
     *
     * @param file File containing entity data
     * @return Map containing import results ("total" and "available" counts)
     * @throws Exception if operation fails
     */
    Map<String, Object> importEntities(MultipartFile file) throws Exception;

    /**
     * Export entities to a file
     *
     * @param entities List of entities to export
     * @return File containing exported data
     * @throws Exception if operation fails
     */
    File exportEntities(List<T> entities) throws Exception;
}
