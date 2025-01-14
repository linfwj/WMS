package com.ken.wms.common.service.Interface;

import com.ken.wms.common.domain.BaseEntity;
import com.ken.wms.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Base service interface for all entities in the WMS system
 */
public interface BaseService<T extends BaseEntity> {
    T getById(Integer id) throws ServiceException;
    List<T> getByName(String name) throws ServiceException;
    List<T> getAll() throws ServiceException;
    Map<String, Object> search(String searchType, String keyword, int offset, int limit) throws ServiceException;
    boolean add(T entity) throws ServiceException;
    boolean update(T entity) throws ServiceException;
    boolean delete(Integer id) throws ServiceException;
    Map<String, Object> importEntities(MultipartFile file) throws ServiceException;
    void exportEntities(String searchType, String keyword, HttpServletResponse response) throws ServiceException;
}
