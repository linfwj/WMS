package com.ken.wms.domain;

/**
 * Role-Menu mapping domain object
 * @author devin
 * @since 2024/1/22
 */
public class RoleMenuDO {
    private Integer roleId;
    private Integer menuId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
