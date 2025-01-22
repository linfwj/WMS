package com.ken.wms.security.controller;

import com.ken.wms.domain.MenuDO;
import com.ken.wms.domain.RoleMenuDO;
import com.ken.wms.exception.MenuServiceException;
import com.ken.wms.security.service.Interface.MenuService;
import com.ken.wms.security.service.Interface.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Menu management controller
 * @author devin
 * @since 2024/1/22
 */
@Controller
@RequestMapping("/menu")
public class MenuManageController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * Show menu management page
     */
    @RequestMapping("/manage")
    public String showMenuManage() {
        return "menuManage";
    }

    /**
     * Get all menus
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllMenus() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<MenuDO> menus = menuService.getAllMenus();
            resultMap.put("success", true);
            resultMap.put("menus", menus);
        } catch (MenuServiceException e) {
            resultMap.put("success", false);
            resultMap.put("message", "Failed to get menus: " + e.getMessage());
        }
        return resultMap;
    }

    /**
     * Get menus by role ID
     */
    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getMenusByRole(@PathVariable Integer roleId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<MenuDO> menus = menuService.getMenusByRoleId(roleId);
            resultMap.put("success", true);
            resultMap.put("menus", menus);
        } catch (MenuServiceException e) {
            resultMap.put("success", false);
            resultMap.put("message", "Failed to get menus for role: " + e.getMessage());
        }
        return resultMap;
    }

    /**
     * Get all roles
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAllRoles() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            // Get current user ID from session
            Integer userId = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userID");
            if (userId == null) {
                resultMap.put("success", false);
                resultMap.put("message", "User not authenticated");
                return resultMap;
            }

            Set<String> roles = userInfoService.getUserRoles(userId);
            resultMap.put("success", true);
            resultMap.put("roles", roles);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "Failed to get roles: " + e.getMessage());
        }
        return resultMap;
    }

    /**
     * Assign menu to role
     */
    @RequestMapping(value = "/assign", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> assignMenuToRole(@RequestParam Integer roleId, @RequestParam Integer menuId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            RoleMenuDO roleMenu = new RoleMenuDO();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            menuService.assignMenuToRole(roleMenu);
            resultMap.put("success", true);
        } catch (MenuServiceException e) {
            resultMap.put("success", false);
            resultMap.put("message", "Failed to assign menu to role: " + e.getMessage());
        }
        return resultMap;
    }

    /**
     * Remove menu from role
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> removeMenuFromRole(@RequestParam Integer roleId, @RequestParam Integer menuId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            menuService.removeMenuFromRole(roleId, menuId);
            resultMap.put("success", true);
        } catch (MenuServiceException e) {
            resultMap.put("success", false);
            resultMap.put("message", "Failed to remove menu from role: " + e.getMessage());
        }
        return resultMap;
    }
}
