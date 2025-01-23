package com.ken.wms.common.controller;

import com.ken.wms.common.service.Interface.BaseEntityService;
import com.ken.wms.security.service.Interface.EntityPermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.ken.wms.common.util.Response;
import com.ken.wms.common.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Generic base controller for entity management
 * Provides standard REST endpoints for CRUD operations
 * @author Devin
 *
 * @param <T> The entity type this controller handles
 */
@Controller
public abstract class BaseManagementHandler<T> {
    
    @Autowired
    private EntityPermissionService entityPermissionService;

    @Autowired
    protected BaseEntityService<T> baseService;
    
    @Autowired
    protected ResponseUtil responseUtil;

    protected static final String SEARCH_BY_ID = "searchByID";
    protected static final String SEARCH_BY_NAME = "searchByName";
    protected static final String SEARCH_ALL = "searchAll";

    /**
     * Generic query method supporting different search types
     * @param searchType Type of search (ID, name, or all)
     * @param keyWord Search keyword
     * @param offset Pagination offset
     * @param limit Pagination size
     * @return Query results
     */
    protected Map<String, Object> query(String searchType, String keyWord, int offset, int limit) throws Exception {
        Map<String, Object> queryResult = null;

        switch (searchType) {
            case SEARCH_BY_ID:
                if (StringUtils.isNumeric(keyWord))
                    queryResult = baseService.selectById(Integer.valueOf(keyWord));
                break;
            case SEARCH_BY_NAME:
                queryResult = baseService.selectByName(offset, limit, keyWord);
                break;
            case SEARCH_ALL:
                queryResult = baseService.selectAll(offset, limit);
                break;
            default:
                break;
        }

        return queryResult;
    }

    /**
     * Get list of entities with search and pagination
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @RequiresPermissions("/baseManage/getList")
    public Map<String, Object> getList(@RequestParam("searchType") String searchType,
                                     @RequestParam("offset") int offset,
                                     @RequestParam("limit") int limit,
                                     @RequestParam("keyWord") String keyWord) throws Exception {
        Response responseContent = responseUtil.newResponseInstance();
        
        List<T> rows = null;
        long total = 0;

        Map<String, Object> queryResult = query(searchType, keyWord, offset, limit);
        if (queryResult != null) {
            rows = (List<T>) queryResult.get("data");
            total = (long) queryResult.get("total");
        }

        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        responseContent.setResponseResult(Response.RESPONSE_RESULT_SUCCESS);
        return responseContent.generateResponse();
    }

    /**
     * Add new entity
     */
    @ResponseBody
    @RequestMapping(value = "addEntity", method = RequestMethod.POST)
    @RequiresPermissions("/baseManage/addEntity")
    public Map<String, Object> addEntity(@RequestBody T entity) throws Exception {
        Response responseContent = responseUtil.newResponseInstance();
        
        String result = baseService.addEntity(entity) 
            ? Response.RESPONSE_RESULT_SUCCESS 
            : Response.RESPONSE_RESULT_ERROR;
        
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * Get entity details by ID
     */
    @ResponseBody
    @RequestMapping(value = "getInfo", method = RequestMethod.GET)
    @RequiresPermissions("/baseManage/getInfo")
    public Map<String, Object> getInfo(@RequestParam("id") Integer id) throws Exception {
        Response responseContent = responseUtil.newResponseInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        Map<String, Object> queryResult = baseService.selectById(id);
        T entity = null;
        
        if (queryResult != null) {
            entity = (T) queryResult.get("data");
            if (entity != null) {
                result = Response.RESPONSE_RESULT_SUCCESS;
            }
        }

        responseContent.setResponseResult(result);
        responseContent.setResponseData(entity);
        return responseContent.generateResponse();
    }

    /**
     * Update existing entity
     */
    @ResponseBody
    @RequestMapping(value = "updateEntity", method = RequestMethod.POST)
    @RequiresPermissions("/baseManage/updateEntity")
    public Map<String, Object> updateEntity(@RequestBody T entity) throws Exception {
        Response responseContent = responseUtil.newResponseInstance();
        
        String result = baseService.updateEntity(entity)
            ? Response.RESPONSE_RESULT_SUCCESS
            : Response.RESPONSE_RESULT_ERROR;
        
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * Delete entity by ID
     */
    @ResponseBody
    @RequestMapping(value = "deleteEntity", method = RequestMethod.GET)
    @RequiresPermissions("/baseManage/deleteEntity")
    public Map<String, Object> deleteEntity(@RequestParam("id") Integer id) throws Exception {
        Response responseContent = responseUtil.newResponseInstance();
        
        String result = baseService.deleteEntity(id)
            ? Response.RESPONSE_RESULT_SUCCESS
            : Response.RESPONSE_RESULT_ERROR;
        
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * Import entities from file
     */
    @ResponseBody
    @RequestMapping(value = "importEntities", method = RequestMethod.POST)
    @RequiresPermissions("/baseManage/importEntities")
    public Map<String, Object> importEntities(@RequestParam("file") MultipartFile file) throws Exception {
        Response responseContent = responseUtil.newResponseInstance();
        String result = Response.RESPONSE_RESULT_SUCCESS;

        int total = 0;
        int available = 0;
        
        if (file == null) {
            result = Response.RESPONSE_RESULT_ERROR;
        } else {
            Map<String, Object> importInfo = baseService.importEntities(file);
            if (importInfo != null) {
                total = (int) importInfo.get("total");
                available = (int) importInfo.get("available");
            }
        }

        responseContent.setResponseResult(result);
        responseContent.setResponseTotal(total);
        responseContent.setCustomerInfo("available", available);
        return responseContent.generateResponse();
    }

    /**
     * Export entities to file
     */
    @RequestMapping(value = "exportEntities", method = RequestMethod.GET)
    @RequiresPermissions("/baseManage/exportEntities")
    public void exportEntities(@RequestParam("searchType") String searchType,
                             @RequestParam("keyWord") String keyWord,
                             HttpServletResponse response) throws Exception {
        String fileName = getExportFileName();

        List<T> entities = null;
        Map<String, Object> queryResult = query(searchType, keyWord, -1, -1);

        if (queryResult != null) {
            entities = (List<T>) queryResult.get("data");
        }

        File file = baseService.exportEntities(entities);

        if (file != null) {
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            
            try (FileInputStream inputStream = new FileInputStream(file);
                 OutputStream outputStream = response.getOutputStream()) {
                
                byte[] buffer = new byte[8192];
                int len;
                while ((len = inputStream.read(buffer, 0, buffer.length)) > 0) {
                    outputStream.write(buffer, 0, len);
                    outputStream.flush();
                }
            }
        }
    }

    /**
     * Get filename for exported file
     * Should be overridden by concrete classes
     */
    protected abstract String getExportFileName();
}
