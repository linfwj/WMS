package com.ken.wms.common.framework.controller;

import com.ken.wms.common.framework.service.BaseEntityService;
import com.ken.wms.common.util.Response;
import com.ken.wms.common.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Base controller for entity management operations.
 * Provides standard REST endpoints for CRUD operations, search, and import/export.
 *
 * @param <T> The entity type this controller manages
 * @author Devin
 */
public abstract class BaseEntityHandler<T> {

    private static final String SEARCH_BY_ID = "searchByID";
    private static final String SEARCH_BY_NAME = "searchByName";
    private static final String SEARCH_ALL = "searchAll";

    @Autowired
    protected BaseEntityService<T> entityService;

    @Autowired
    protected ResponseUtil responseUtil;

    /**
     * Get the entity class type
     * @return Class object representing T
     */
    protected abstract Class<T> getEntityClass();

    /**
     * Get the filename to use for exports
     * @return String filename with extension
     */
    protected abstract String getExportFilename();

    /**
     * Common query method supporting different search types
     */
    protected Map<String, Object> query(String searchType, String keyWord, int offset, int limit) {
        Map<String, Object> queryResult = null;

        switch (searchType) {
            case SEARCH_BY_ID:
                if (StringUtils.isNumeric(keyWord))
                    queryResult = entityService.selectById(Integer.valueOf(keyWord));
                break;
            case SEARCH_BY_NAME:
                queryResult = entityService.selectByName(offset, limit, keyWord);
                break;
            case SEARCH_ALL:
                queryResult = entityService.selectAll(offset, limit);
                break;
            default:
                break;
        }

        return queryResult;
    }

    /**
     * List entities with search and pagination
     */
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getList(
            @RequestParam("searchType") String searchType,
            @RequestParam("offset") int offset,
            @RequestParam("limit") int limit,
            @RequestParam("keyWord") String keyWord) {
        
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
        return responseContent.generateResponse();
    }

    /**
     * Add new entity
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(@RequestBody T entity) {
        Response responseContent = responseUtil.newResponseInstance();
        String result = entityService.add(entity) ? 
            Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * Get entity by ID
     */
    @RequestMapping(value = "getInfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getInfo(@RequestParam("id") Integer id) {
        Response responseContent = responseUtil.newResponseInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        Map<String, Object> queryResult = entityService.selectById(id);
        T entity = null;
        if (queryResult != null) {
            List<T> data = (List<T>) queryResult.get("data");
            if (data != null && !data.isEmpty()) {
                entity = data.get(0);
                result = Response.RESPONSE_RESULT_SUCCESS;
            }
        }

        responseContent.setResponseResult(result);
        responseContent.setResponseData(entity);
        return responseContent.generateResponse();
    }

    /**
     * Update entity
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(@RequestBody T entity) {
        Response responseContent = responseUtil.newResponseInstance();
        String result = entityService.update(entity) ? 
            Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * Delete entity
     */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delete(@RequestParam("id") Integer id) {
        Response responseContent = responseUtil.newResponseInstance();
        String result = entityService.delete(id) ? 
            Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * Import entities from file
     */
    @RequestMapping(value = "import", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> importEntities(@RequestParam("file") MultipartFile file) {
        Response responseContent = responseUtil.newResponseInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        int total = 0;
        int available = 0;
        if (file != null) {
            Map<String, Object> importInfo = entityService.importEntities(file);
            if (importInfo != null) {
                total = (int) importInfo.get("total");
                available = (int) importInfo.get("available");
                result = Response.RESPONSE_RESULT_SUCCESS;
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
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public void exportEntities(
            @RequestParam("searchType") String searchType,
            @RequestParam("keyWord") String keyWord,
            HttpServletResponse response) throws IOException {
        
        List<T> entities = null;
        Map<String, Object> queryResult = query(searchType, keyWord, -1, -1);
        if (queryResult != null) {
            entities = (List<T>) queryResult.get("data");
        }

        File file = entityService.exportEntities(entities);
        if (file != null) {
            response.addHeader("Content-Disposition", "attachment;filename=" + getExportFilename());
            
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
}
