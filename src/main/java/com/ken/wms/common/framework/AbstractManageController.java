package com.ken.wms.common.framework;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Abstract base controller that provides standard REST endpoints
 * Entity-specific controllers can extend this class
 * 
 * @author Devin
 * @param <T> The entity type this controller handles
 */
@RestController
public abstract class AbstractManageController<T> {
    
    /**
     * Get the service for this entity type
     * Subclasses must implement this to provide their specific service
     */
    protected abstract BaseService<T> getService();
    
    @GetMapping("/{id}")
    public Map<String, Object> getById(@PathVariable Integer id) {
        return getService().selectById(id);
    }
    
    @GetMapping("/")
    public Map<String, Object> getAll(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return getService().selectAll(offset, limit);
    }
    
    @GetMapping("/search")
    public Map<String, Object> search(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam String name) {
        return getService().selectByName(offset, limit, name);
    }
    
    @PostMapping("/")
    public boolean add(@RequestBody T entity) {
        return getService().add(entity);
    }
    
    @PutMapping("/")
    public boolean update(@RequestBody T entity) {
        return getService().update(entity);
    }
    
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return getService().delete(id);
    }
    
    @PostMapping("/import")
    public Map<String, Object> importEntities(@RequestParam("file") MultipartFile file) {
        return getService().importFromExcel(file);
    }
    
    @GetMapping("/export")
    public File exportEntities(@RequestBody(required = false) List<T> entities) {
        return getService().exportToExcel(entities);
    }
}
