package com.ken.wms.common.service.Interface;

import com.ken.wms.common.framework.service.BaseEntityService;
import com.ken.wms.domain.Goods;
import com.ken.wms.exception.GoodsManageServiceException;

/**
 * Goods management service interface.
 * Extends the generic BaseEntityService for standard CRUD operations.
 * @author Ken
 * @author Devin
 */
public interface GoodsManageService extends BaseEntityService<Goods> {
    // All standard CRUD operations are inherited from BaseEntityService<Goods>
    // Only add Goods-specific methods here if needed
}
