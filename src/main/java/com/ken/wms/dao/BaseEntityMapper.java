package com.ken.wms.dao;

import java.util.List;

/**
 * Generic base mapper interface for entity management
 * Provides common CRUD operations that can be implemented by specific entity mappers
 * @author Devin
 *
 * @param <T> The entity type this mapper handles
 */
public interface BaseEntityMapper<T> {

    /**
     * Select all entities
     * @return List of all entities
     */
    List<T> selectAll();
    
    /**
     * Select entity by ID
     * @param id Entity ID
     * @return Entity with specified ID
     */
    T selectById(Integer id);
    
    /**
     * Select entity by exact name match
     * @param name Entity name
     * @return Entity with specified name
     */
    T selectByName(String name);
    
    /**
     * Select entities by approximate name match
     * Supports fuzzy matching of names
     * @param name Entity name to match approximately
     * @return List of entities matching the name pattern
     */
    List<T> selectApproximateByName(String name);
    
    /**
     * Insert a new entity
     * Does not require ID specification (uses database AI)
     * @param entity Entity instance to insert
     */
    void insert(T entity);
    
    /**
     * Batch insert multiple entities
     * @param entities List of entity instances to insert
     */
    void insertBatch(List<T> entities);
    
    /**
     * Update an existing entity
     * Entity must exist in database (have ID)
     * @param entity Entity instance to update
     */
    void update(T entity);
    
    /**
     * Delete entity by ID
     * @param id Entity ID to delete
     */
    void deleteById(Integer id);
    
    /**
     * Delete entity by name
     * @param name Entity name to delete
     */
    void deleteByName(String name);
}
