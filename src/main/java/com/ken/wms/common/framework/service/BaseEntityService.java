package com.ken.wms.common.framework.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Generic base service interface for entity management.
 * Provides standard CRUD operations, search with pagination, and import/export functionality.
 *
 * @param <T> The entity type this service manages
 * @author Devin
 */
public interface BaseEntityService<T> {

    /**
     * Returns the entity with the specified ID
     * @param id Entity ID
     * @return Map containing: 
     *         - "data": List of matching entities (size 0 or 1)
     *         - "total": Total count (0 or 1)
     */
    Map<String, Object> selectById(Integer id);

    /**
     * Returns entities matching the specified name with pagination
     * @param offset Pagination offset
     * @param limit Maximum number of records to return
     * @param name Name to search for (supports partial matches)
     * @return Map containing:
     *         - "data": List of matching entities
     *         - "total": Total count of matches
     */
    Map<String, Object> selectByName(int offset, int limit, String name);

    /**
     * Returns all entities with pagination
     * @param offset Pagination offset
     * @param limit Maximum number of records to return
     * @return Map containing:
     *         - "data": List of entities
     *         - "total": Total count of all entities
     */
    Map<String, Object> selectAll(int offset, int limit);

    /**
     * Returns all entities without pagination
     * @return Map containing:
     *         - "data": List of all entities
     *         - "total": Total count
     */
    Map<String, Object> selectAll();

    /**
     * Adds a new entity
     * @param entity Entity to add
     * @return true if successful, false otherwise
     */
    boolean add(T entity);

    /**
     * Updates an existing entity
     * @param entity Entity with updated values
     * @return true if successful, false otherwise
     */
    boolean update(T entity);

    /**
     * Deletes an entity by ID
     * @param id ID of entity to delete
     * @return true if successful, false otherwise
     */
    boolean delete(Integer id);

    /**
     * Imports entities from a file
     * @param file File containing entity data
     * @return Map containing:
     *         - "total": Total records processed
     *         - "available": Number of valid records imported
     */
    Map<String, Object> importEntities(MultipartFile file);

    /**
     * Exports entities to a file
     * @param entities List of entities to export
     * @return File containing the exported data
     */
    File exportEntities(List<T> entities);
}
