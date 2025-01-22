package com.ken.wms.security.service.Impl;

import com.ken.wms.dao.RoleActionMapper;
import com.ken.wms.domain.ActionDO;
import com.ken.wms.exception.FeatureServiceException;
import com.ken.wms.security.service.Interface.FeatureService;
import com.ken.wms.security.service.Interface.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Feature service implementation
 * @author devin
 * @since 2024/1/22
 */
@Service
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private RoleActionMapper roleActionMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean hasPermission(String actionName) throws FeatureServiceException {
        return hasPermission(actionName, null);
    }

    @Override
    public boolean hasPermission(String actionName, String params) throws FeatureServiceException {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            if (!currentUser.isAuthenticated()) {
                return false;
            }

            // Get user ID from session
            Integer userId = (Integer) currentUser.getSession().getAttribute("userID");
            if (userId == null) {
                return false;
            }

            // Get user roles
            Set<String> roles = userInfoService.getUserRoles(userId);
            
            // Check if any role has the required action
            for (String role : roles) {
                List<ActionDO> actions = roleActionMapper.selectActionsByRoleId(Integer.valueOf(role));
                for (ActionDO action : actions) {
                    if (action.getActionName().equals(actionName)) {
                        // If params is null, we only check action name
                        if (params == null) {
                            return true;
                        }
                        // If params is provided, check if they match
                        if (params.equals(action.getActionParam())) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } catch (Exception e) {
            throw new FeatureServiceException("Failed to check permission: " + e.getMessage());
        }
    }

    @Override
    public String[] getCurrentUserActions() throws FeatureServiceException {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            if (!currentUser.isAuthenticated()) {
                return new String[0];
            }

            // Get user ID from session
            Integer userId = (Integer) currentUser.getSession().getAttribute("userID");
            if (userId == null) {
                return new String[0];
            }

            // Get user roles
            Set<String> roles = userInfoService.getUserRoles(userId);
            List<String> actions = new ArrayList<>();
            
            // Get actions for each role
            for (String role : roles) {
                List<ActionDO> roleActions = roleActionMapper.selectActionsByRoleId(Integer.valueOf(role));
                for (ActionDO action : roleActions) {
                    if (!actions.contains(action.getActionName())) {
                        actions.add(action.getActionName());
                    }
                }
            }

            return actions.toArray(new String[0]);
        } catch (Exception e) {
            throw new FeatureServiceException("Failed to get current user actions: " + e.getMessage());
        }
    }

    @Override
    public void addAction(String actionName, String description, String params) throws FeatureServiceException {
        try {
            ActionDO action = new ActionDO();
            action.setActionName(actionName);
            action.setActionDesc(description);
            action.setActionParam(params);
            roleActionMapper.insertAction(action);
        } catch (Exception e) {
            throw new FeatureServiceException("Failed to add action: " + e.getMessage());
        }
    }

    @Override
    public void assignActionToRole(String actionName, Integer roleId) throws FeatureServiceException {
        try {
            roleActionMapper.insertRoleAction(actionName, roleId);
        } catch (Exception e) {
            throw new FeatureServiceException("Failed to assign action to role: " + e.getMessage());
        }
    }

    @Override
    public void removeActionFromRole(String actionName, Integer roleId) throws FeatureServiceException {
        try {
            roleActionMapper.deleteRoleAction(actionName, roleId);
        } catch (Exception e) {
            throw new FeatureServiceException("Failed to remove action from role: " + e.getMessage());
        }
    }
}
