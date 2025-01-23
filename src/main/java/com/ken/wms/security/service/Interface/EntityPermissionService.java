package com.ken.wms.security.service.Interface;

/**
 * Service interface for entity-related permissions
 */
public interface EntityPermissionService {
    
    /**
     * Check if current user has permission to access entity management
     * @param actionParam The action parameter to check
     * @return true if user has permission, false otherwise
     */
    boolean hasEntityPermission(String actionParam);
    
    /**
     * Get all permitted entity actions for current user
     * @return Array of permitted action parameters
     */
    String[] getPermittedEntityActions();
}
