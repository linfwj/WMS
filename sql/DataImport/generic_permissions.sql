-- Add base management permissions
INSERT INTO wms_action (ACTION_NAME, ACTION_DESC, ACTION_PARAM) VALUES
('查看实体列表', '查看实体管理列表', '/baseManage/getList'),
('添加实体', '添加新实体', '/baseManage/addEntity'),
('编辑实体', '编辑现有实体', '/baseManage/updateEntity'),
('删除实体', '删除实体', '/baseManage/deleteEntity'),
('导入实体', '从Excel导入实体', '/baseManage/importEntities'),
('导出实体', '导出实体到Excel', '/baseManage/exportEntities'),
('查看实体详情', '查看实体详细信息', '/baseManage/getInfo');

-- Add role-action mappings for admin role (assuming role_id = 1 for admin)
INSERT INTO wms_role_action (ACTION_ID, ROLE_ID)
SELECT ACTION_ID, 1 FROM wms_action 
WHERE ACTION_PARAM LIKE '/baseManage/%';

-- Add role-action mappings for manager role (assuming role_id = 2 for manager)
INSERT INTO wms_role_action (ACTION_ID, ROLE_ID)
SELECT ACTION_ID, 2 FROM wms_action 
WHERE ACTION_PARAM LIKE '/baseManage/%'
AND ACTION_NAME NOT IN ('删除实体'); -- Managers can't delete
