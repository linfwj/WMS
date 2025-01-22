package com.ken.wms.dao;

import com.ken.wms.domain.MenuDO;
import com.ken.wms.domain.RoleMenuDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Role-Menu mapping mapper interface
 * @author devin
 * @since 2024/1/22
 */
@Repository
public interface RoleMenuMapper {
    
    /**
     * Select menus by role ID
     * @param roleId role ID
     * @return List of menus associated with the role
     */
    List<MenuDO> selectMenusByRoleId(Integer roleId);

    /**
     * Insert new role-menu mapping
     * @param roleMenu RoleMenuDO object to insert
     * @return number of rows affected
     */
    int insert(RoleMenuDO roleMenu);

    /**
     * Delete role-menu mapping
     * @param roleId role ID
     * @param menuId menu ID
     * @return number of rows affected
     */
    int delete(Integer roleId, Integer menuId);

    /**
     * Delete all menu mappings for a role
     * @param roleId role ID
     * @return number of rows affected
     */
    int deleteByRoleId(Integer roleId);

    /**
     * Delete all role mappings for a menu
     * @param menuId menu ID
     * @return number of rows affected
     */
    int deleteByMenuId(Integer menuId);
}
