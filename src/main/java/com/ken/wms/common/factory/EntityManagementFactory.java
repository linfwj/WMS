package com.ken.wms.common.factory;

import com.ken.wms.common.controller.BaseController;
import com.ken.wms.common.dao.BaseMapper;
import com.ken.wms.common.domain.BaseEntity;
import com.ken.wms.common.service.Interface.BaseService;
import com.ken.wms.common.service.Impl.BaseServiceImpl;
import com.ken.wms.common.util.ExcelUtil;
import com.ken.wms.common.util.ResponseUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.lang.reflect.Proxy;

/**
 * Factory for creating entity management components
 */
@Component
public class EntityManagementFactory {

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Autowired
    private ExcelUtil excelUtil;

    @Autowired
    private ResponseUtil responseUtil;

    /**
     * Create a MyBatis mapper for the entity
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> BaseMapper<T> createMapper(Class<T> entityClass) {
        return sqlSession.getMapper((Class<BaseMapper<T>>) 
            Proxy.getProxyClass(entityClass.getClassLoader(), BaseMapper.class));
    }

    /**
     * Create a service implementation for the entity
     */
    public <T extends BaseEntity> BaseService<T> createService(
            final Class<T> entityClass,
            final String entityName,
            final BaseMapper<T> mapper) {
        
        return new BaseServiceImpl<T>() {
            @Override
            protected void validateEntity(T entity) {
                if (entity == null) {
                    throw new IllegalArgumentException(entityName + " cannot be null");
                }
                if (entity.getName() == null || entity.getName().trim().isEmpty()) {
                    throw new IllegalArgumentException(entityName + " name cannot be empty");
                }
            }

            @Override
            protected String getEntityName() {
                return entityName;
            }

            @Override
            protected Class<T> getEntityClass() {
                return entityClass;
            }
        };
    }

    /**
     * Create a controller for the entity
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> BaseController<T> createController(
            final Class<T> entityClass,
            final String entityName,
            final String baseUrl,
            final BaseService<T> service) {
        
        return (BaseController<T>) Proxy.newProxyInstance(
            entityClass.getClassLoader(),
            new Class<?>[] { BaseController.class },
            (proxy, method, args) -> {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String mappingValue = method.getAnnotation(RequestMapping.class).value()[0];
                    
                    switch (mappingValue) {
                        case "getList":
                            return handleGetList(service, args);
                        case "add":
                            return handleAdd(service, args);
                        case "update":
                            return handleUpdate(service, args);
                        case "delete":
                            return handleDelete(service, args);
                        case "import":
                            return handleImport(service, args);
                        case "export":
                            return handleExport(service, args);
                        default:
                            throw new UnsupportedOperationException("Unknown mapping: " + mappingValue);
                    }
                }
                return method.invoke(proxy, args);
            });
    }

    /**
     * Handle getList request
     */
    private <T extends BaseEntity> Map<String, Object> handleGetList(
            BaseService<T> service,
            Object[] args) {
        String searchType = (String) args[0];
        String keyword = (String) args[1];
        int offset = (int) args[2];
        int limit = (int) args[3];
        
        try {
            return service.search(searchType, keyword, offset, limit);
        } catch (Exception e) {
            return responseUtil.createErrorResponse(e.getMessage());
        }
    }

    /**
     * Handle add request
     */
    private <T extends BaseEntity> Map<String, Object> handleAdd(
            BaseService<T> service,
            Object[] args) {
        T entity = (T) args[0];
        
        try {
            boolean success = service.add(entity);
            return success ? 
                responseUtil.createSuccessResponse() :
                responseUtil.createErrorResponse("Add failed");
        } catch (Exception e) {
            return responseUtil.createErrorResponse(e.getMessage());
        }
    }

    /**
     * Handle update request
     */
    private <T extends BaseEntity> Map<String, Object> handleUpdate(
            BaseService<T> service,
            Object[] args) {
        T entity = (T) args[0];
        
        try {
            boolean success = service.update(entity);
            return success ?
                responseUtil.createSuccessResponse() :
                responseUtil.createErrorResponse("Update failed");
        } catch (Exception e) {
            return responseUtil.createErrorResponse(e.getMessage());
        }
    }

    /**
     * Handle delete request
     */
    private <T extends BaseEntity> Map<String, Object> handleDelete(
            BaseService<T> service,
            Object[] args) {
        Integer id = (Integer) args[0];
        
        try {
            boolean success = service.delete(id);
            return success ?
                responseUtil.createSuccessResponse() :
                responseUtil.createErrorResponse("Delete failed");
        } catch (Exception e) {
            return responseUtil.createErrorResponse(e.getMessage());
        }
    }

    /**
     * Handle import request
     */
    private <T extends BaseEntity> Map<String, Object> handleImport(
            BaseService<T> service,
            Object[] args) {
        MultipartFile file = (MultipartFile) args[0];
        
        try {
            return service.importEntities(file);
        } catch (Exception e) {
            return responseUtil.createErrorResponse(e.getMessage());
        }
    }

    /**
     * Handle export request
     */
    private <T extends BaseEntity> void handleExport(
            BaseService<T> service,
            Object[] args) {
        String searchType = (String) args[0];
        String keyword = (String) args[1];
        HttpServletResponse response = (HttpServletResponse) args[2];
        
        try {
            service.exportEntities(searchType, keyword, response);
        } catch (Exception e) {
            // Handle export error
        }
    }
}
