package com.ken.wms.security.service.Impl;

import com.ken.wms.security.service.Interface.EntityPermissionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

@Service
public class EntityPermissionServiceImpl implements EntityPermissionService {
    
    @Override
    public boolean hasEntityPermission(String actionParam) {
        Subject currentUser = SecurityUtils.getSubject();
        return currentUser.isPermitted(actionParam);
    }
    
    @Override
    public String[] getPermittedEntityActions() {
        Subject currentUser = SecurityUtils.getSubject();
        return new String[] {
            "/baseManage/getList",
            "/baseManage/addEntity",
            "/baseManage/updateEntity",
            "/baseManage/deleteEntity",
            "/baseManage/importEntities",
            "/baseManage/exportEntities",
            "/baseManage/getInfo"
        };
    }
}
