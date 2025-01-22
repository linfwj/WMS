package com.ken.wms.dao;

import com.ken.wms.domain.ActionDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Role-Action mapping mapper interface
 * @author devin
 * @since 2024/1/22
 */
@Repository
public interface RoleActionMapper {
    
    /**
     * Select actions by role ID
     * @param roleId role ID
     * @return List of actions associated with the role
     */
    List<ActionDO> selectActionsByRoleId(Integer roleId);

    /**
     * Insert new action
     * @param action ActionDO object to insert
     * @return number of rows affected
     */
    int insertAction(ActionDO action);

    /**
     * Insert role-action mapping
     * @param actionName action name
     * @param roleId role ID
     * @return number of rows affected
     */
    int insertRoleAction(String actionName, Integer roleId);

    /**
     * Delete role-action mapping
     * @param actionName action name
     * @param roleId role ID
     * @return number of rows affected
     */
    int deleteRoleAction(String actionName, Integer roleId);

    /**
     * Delete all action mappings for a role
     * @param roleId role ID
     * @return number of rows affected
     */
    int deleteByRoleId(Integer roleId);
}
