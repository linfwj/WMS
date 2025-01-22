package com.ken.wms.dao;

import com.ken.wms.domain.MenuDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Menu mapper interface
 * @author devin
 * @since 2024/1/22
 */
@Repository
public interface MenuMapper {
    
    /**
     * Select menu by ID
     * @param menuId menu ID
     * @return MenuDO object if found, null otherwise
     */
    MenuDO selectById(Integer menuId);

    /**
     * Select all menus
     * @return List of all menus
     */
    List<MenuDO> selectAll();

    /**
     * Select menus by parent ID
     * @param parentId parent menu ID
     * @return List of child menus
     */
    List<MenuDO> selectByParentId(Integer parentId);

    /**
     * Insert new menu
     * @param menu MenuDO object to insert
     * @return number of rows affected
     */
    int insert(MenuDO menu);

    /**
     * Update menu
     * @param menu MenuDO object with updated values
     * @return number of rows affected
     */
    int update(MenuDO menu);

    /**
     * Delete menu by ID
     * @param menuId menu ID to delete
     * @return number of rows affected
     */
    int deleteById(Integer menuId);
}
