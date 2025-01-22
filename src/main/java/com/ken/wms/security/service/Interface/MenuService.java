package com.ken.wms.security.service.Interface;

import com.ken.wms.domain.MenuDO;
import com.ken.wms.exception.MenuServiceException;

import java.util.List;
import java.util.Set;

/**
 * Menu service interface
 * @author devin
 * @since 2024/1/22
 */
public interface MenuService {

    /**
     * Get menu by ID
     * @param menuId menu ID
     * @return MenuDO if found, null otherwise
     */
    MenuDO getMenuById(Integer menuId) throws MenuServiceException;

    /**
     * Get all menus
     * @return List of all menus
     */
    List<MenuDO> getAllMenus() throws MenuServiceException;

    /**
     * Get menus by parent ID
     * @param parentId parent menu ID
     * @return List of child menus
     */
    List<MenuDO> getMenusByParentId(Integer parentId) throws MenuServiceException;

    /**
     * Get menus accessible by a role
     * @param roleId role ID
     * @return List of accessible menus
     */
    List<MenuDO> getMenusByRoleId(Integer roleId) throws MenuServiceException;

    /**
     * Get menus accessible by current user
     * @return List of accessible menus
     */
    List<MenuDO> getCurrentUserMenus() throws MenuServiceException;

    /**
     * Check if current user has access to menu
     * @param menuId menu ID to check
     * @return true if user has access, false otherwise
     */
    boolean hasMenuPermission(Integer menuId) throws MenuServiceException;
}
