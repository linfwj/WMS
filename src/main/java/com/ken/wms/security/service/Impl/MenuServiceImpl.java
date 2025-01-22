package com.ken.wms.security.service.Impl;

import com.ken.wms.dao.MenuMapper;
import com.ken.wms.dao.RoleMenuMapper;
import com.ken.wms.domain.MenuDO;
import com.ken.wms.exception.MenuServiceException;
import com.ken.wms.security.service.Interface.MenuService;
import com.ken.wms.security.service.Interface.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Menu service implementation
 * @author devin
 * @since 2024/1/22
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public MenuDO getMenuById(Integer menuId) throws MenuServiceException {
        try {
            return menuMapper.selectById(menuId);
        } catch (Exception e) {
            throw new MenuServiceException("Failed to get menu by ID: " + e.getMessage());
        }
    }

    @Override
    public List<MenuDO> getAllMenus() throws MenuServiceException {
        try {
            return menuMapper.selectAll();
        } catch (Exception e) {
            throw new MenuServiceException("Failed to get all menus: " + e.getMessage());
        }
    }

    @Override
    public List<MenuDO> getMenusByParentId(Integer parentId) throws MenuServiceException {
        try {
            return menuMapper.selectByParentId(parentId);
        } catch (Exception e) {
            throw new MenuServiceException("Failed to get menus by parent ID: " + e.getMessage());
        }
    }

    @Override
    public List<MenuDO> getMenusByRoleId(Integer roleId) throws MenuServiceException {
        try {
            return roleMenuMapper.selectMenusByRoleId(roleId);
        } catch (Exception e) {
            throw new MenuServiceException("Failed to get menus by role ID: " + e.getMessage());
        }
    }

    @Override
    public List<MenuDO> getCurrentUserMenus() throws MenuServiceException {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            if (!currentUser.isAuthenticated()) {
                return new ArrayList<>();
            }

            // Get user ID from session
            Integer userId = (Integer) currentUser.getSession().getAttribute("userID");
            if (userId == null) {
                return new ArrayList<>();
            }

            // Get user roles
            Set<String> roles = userInfoService.getUserRoles(userId);
            List<MenuDO> userMenus = new ArrayList<>();
            
            // Get menus for each role
            for (String role : roles) {
                List<MenuDO> roleMenus = roleMenuMapper.selectMenusByRoleId(Integer.valueOf(role));
                for (MenuDO menu : roleMenus) {
                    if (!userMenus.contains(menu)) {
                        userMenus.add(menu);
                    }
                }
            }

            return userMenus;
        } catch (Exception e) {
            throw new MenuServiceException("Failed to get current user menus: " + e.getMessage());
        }
    }

    @Override
    public boolean hasMenuPermission(Integer menuId) throws MenuServiceException {
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
            
            // Check if any role has access to the menu
            for (String role : roles) {
                List<MenuDO> roleMenus = roleMenuMapper.selectMenusByRoleId(Integer.valueOf(role));
                for (MenuDO menu : roleMenus) {
                    if (menu.getMenuId().equals(menuId)) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            throw new MenuServiceException("Failed to check menu permission: " + e.getMessage());
        }
    }
}
