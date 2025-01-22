package com.ken.wms.security.service.Interface;

import com.ken.wms.exception.FeatureServiceException;

/**
 * Feature-based access control service
 * @author devin
 * @since 2024/1/22
 */
public interface FeatureService {

    /**
     * Check if current user has permission to perform an action
     * @param actionName name of the action to check
     * @return true if user has permission, false otherwise
     * @throws FeatureServiceException if check fails
     */
    boolean hasPermission(String actionName) throws FeatureServiceException;

    /**
     * Check if current user has permission to perform an action with specific parameters
     * @param actionName name of the action to check
     * @param params parameters for the action
     * @return true if user has permission, false otherwise
     * @throws FeatureServiceException if check fails
     */
    boolean hasPermission(String actionName, String params) throws FeatureServiceException;

    /**
     * Get all actions available to current user
     * @return array of action names
     * @throws FeatureServiceException if retrieval fails
     */
    String[] getCurrentUserActions() throws FeatureServiceException;

    /**
     * Add a new action to the system
     * @param actionName name of the action
     * @param description description of the action
     * @param params parameters required for the action
     * @throws FeatureServiceException if addition fails
     */
    void addAction(String actionName, String description, String params) throws FeatureServiceException;

    /**
     * Assign an action to a role
     * @param actionName name of the action
     * @param roleId ID of the role
     * @throws FeatureServiceException if assignment fails
     */
    void assignActionToRole(String actionName, Integer roleId) throws FeatureServiceException;

    /**
     * Remove an action from a role
     * @param actionName name of the action
     * @param roleId ID of the role
     * @throws FeatureServiceException if removal fails
     */
    void removeActionFromRole(String actionName, Integer roleId) throws FeatureServiceException;
}
