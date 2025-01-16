package com.ken.wms.common.framework;

import java.util.List;

/**
 * Generic base mapper interface that defines standard CRUD operations
 * All entity-specific mappers can extend this interface
 * 
 * @author Devin
 * @param <T> The entity type this mapper handles
 */
public interface BaseMapper<T> {
    
    /**
     * Insert a new entity
     * @param entity The entity to insert
     */
    void insert(T entity);
    
    /**
     * Insert multiple entities in batch
     * @param entities List of entities to insert
     */
    void insertBatch(List<T> entities);
    
    /**
     * Update an existing entity
     * @param entity The entity to update
     */
    void update(T entity);
    
    /**
     * Delete an entity by ID
     * @param id The ID of the entity to delete
     */
    void deleteById(Integer id);
    
    /**
     * Select an entity by ID
     * @param id The ID of the entity to select
     * @return The found entity or null
     */
    T selectById(Integer id);
    
    /**
     * Select all entities
     * @return List of all entities
     */
    List<T> selectAll();
    
    /**
     * Select entities by approximate name match
     * @param name The name to match approximately
     * @return List of matching entities
     */
    List<T> selectApproximateByName(String name);
}
