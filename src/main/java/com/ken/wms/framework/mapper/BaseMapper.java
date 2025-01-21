package com.ken.wms.framework.mapper;

import java.util.List;

/**
 * Base mapper interface for standardized database operations.
 * All entity-specific mappers should extend this interface.
 *
 * @param <T> The entity type this mapper handles
 * @author Devin
 */
public interface BaseMapper<T> {

    /**
     * Insert a new entity record
     *
     * @param entity The entity to insert
     */
    void insert(T entity);

    /**
     * Insert multiple entity records in batch
     *
     * @param entities List of entities to insert
     */
    void insertBatch(List<T> entities);

    /**
     * Update an existing entity record
     *
     * @param entity The entity to update
     */
    void update(T entity);

    /**
     * Delete an entity by its ID
     *
     * @param id The ID of the entity to delete
     */
    void deleteById(Integer id);

    /**
     * Select an entity by its ID
     *
     * @param id The ID of the entity to select
     * @return The found entity or null if not found
     */
    T selectById(Integer id);

    /**
     * Select all entities
     *
     * @return List of all entities
     */
    List<T> selectAll();

    /**
     * Select entities by name (approximate match)
     *
     * @param name The name to search for
     * @return List of matching entities
     */
    List<T> selectApproximateByName(String name);
}
