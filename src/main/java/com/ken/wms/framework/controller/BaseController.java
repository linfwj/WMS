package com.ken.wms.framework.controller;

import com.ken.wms.framework.service.BaseService;
import com.ken.wms.framework.exception.ServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Base controller providing standardized REST endpoints for entity management.
 * Subclasses should extend this and specify their entity type and service.
 *
 * @param <T> The entity type this controller handles
 * @author Devin
 */
@RestController
public abstract class BaseController<T> {

    /**
     * Get the service instance for entity operations
     */
    protected abstract BaseService<T> getService();

    /**
     * Get paginated list of entities
     *
     * @param offset Pagination offset
     * @param limit Maximum number of records
     * @return Map containing data and total count
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        try {
            Map<String, Object> result = getService().selectAll(offset, limit);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Search entities by name
     *
     * @param name Name to search for
     * @param offset Pagination offset
     * @param limit Maximum number of records
     * @return Map containing matching entities and total count
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        try {
            Map<String, Object> result = getService().selectByName(offset, limit, name);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Add a new entity
     *
     * @param entity Entity to add
     * @return Success/failure response
     */
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody T entity) {
        try {
            boolean success = getService().add(entity);
            return success ? 
                ResponseEntity.ok("Added successfully") :
                ResponseEntity.badRequest().body("Validation failed");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Update an existing entity
     *
     * @param entity Entity to update
     * @return Success/failure response
     */
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody T entity) {
        try {
            boolean success = getService().update(entity);
            return success ?
                ResponseEntity.ok("Updated successfully") :
                ResponseEntity.badRequest().body("Validation failed");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Delete an entity
     *
     * @param id ID of entity to delete
     * @return Success/failure response
     */
    @GetMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam Integer id) {
        try {
            boolean success = getService().delete(id);
            return success ?
                ResponseEntity.ok("Deleted successfully") :
                ResponseEntity.badRequest().body("Delete failed");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Import entities from file
     *
     * @param file File containing entity data
     * @return Import results
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importEntities(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = getService().importEntities(file);
            return ResponseEntity.ok(result);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Export entities to file
     *
     * @param ids Optional list of entity IDs to export
     * @return Generated file
     */
    @GetMapping("/export")
    public ResponseEntity<File> exportEntities(@RequestParam(required = false) List<Integer> ids) {
        try {
            Map<String, Object> result;
            if (ids != null && !ids.isEmpty()) {
                // Export specific entities
                result = getService().selectAll(); // TODO: Add selectByIds to BaseService
            } else {
                // Export all
                result = getService().selectAll();
            }
            @SuppressWarnings("unchecked")
            List<T> entities = (List<T>) result.get("data");
            File exportFile = getService().exportEntities(entities);
            return ResponseEntity.ok(exportFile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
