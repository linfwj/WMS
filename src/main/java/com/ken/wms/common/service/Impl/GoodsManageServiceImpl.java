package com.ken.wms.common.service.Impl;

import com.ken.wms.common.framework.service.AbstractEntityService;
import com.ken.wms.common.service.Interface.GoodsManageService;
import com.ken.wms.dao.GoodsMapper;
import com.ken.wms.dao.StockInMapper;
import com.ken.wms.dao.StockOutMapper;
import com.ken.wms.dao.StorageMapper;
import com.ken.wms.domain.Goods;
import com.ken.wms.domain.StockInDO;
import com.ken.wms.domain.StockOutDO;
import com.ken.wms.domain.Storage;
import com.ken.wms.exception.GoodsManageServiceException;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Goods management service implementation.
 * Extends AbstractEntityService for standard CRUD operations.
 *
 * @author Ken
 * @author Devin
 */
@Service
public class GoodsManageServiceImpl extends AbstractEntityService<Goods> implements GoodsManageService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private StockInMapper stockInMapper;
    @Autowired
    private StockOutMapper stockOutMapper;
    @Autowired
    private StorageMapper storageMapper;

    @Override
    protected boolean validateEntity(Goods goods) {
        return goods != null && goods.getName() != null && goods.getValue() != null;
    }

    @Override
    protected void saveImportedEntities(List<Goods> entities) {
        if (entities != null && !entities.isEmpty()) {
            try {
                goodsMapper.insertBatch(entities);
            } catch (PersistenceException e) {
                throw new GoodsManageServiceException(e);
            }
        }
    }

    @Override
    public Map<String, Object> selectById(Integer id) throws GoodsManageServiceException {
        try {
            return createResultMap(List.of(goodsMapper.selectById(id)), 1);
        } catch (PersistenceException e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public Map<String, Object> selectByName(int offset, int limit, String name) throws GoodsManageServiceException {
        try {
            boolean usePagination = applyPagination(offset, limit);
            List<Goods> goods = goodsMapper.selectApproximateByName(name);
            long total = usePagination ? new PageInfo<>(goods).getTotal() : goods.size();
            return createResultMap(goods, total);
        } catch (PersistenceException e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public Map<String, Object> selectAll(int offset, int limit) throws GoodsManageServiceException {
        try {
            boolean usePagination = applyPagination(offset, limit);
            List<Goods> goods = goodsMapper.selectAll();
            long total = usePagination ? new PageInfo<>(goods).getTotal() : goods.size();
            return createResultMap(goods, total);
        } catch (PersistenceException e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public boolean add(Goods goods) throws GoodsManageServiceException {
        try {
            if (validateEntity(goods)) {
                goodsMapper.insert(goods);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public boolean update(Goods goods) throws GoodsManageServiceException {
        try {
            if (validateEntity(goods)) {
                goodsMapper.update(goods);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public boolean delete(Integer id) throws GoodsManageServiceException {
        try {
            // Check if the goods has any related records
            List<StockInDO> stockInRecords = stockInMapper.selectByGoodID(id);
            if (stockInRecords != null && !stockInRecords.isEmpty())
                return false;

            List<StockOutDO> stockOutRecords = stockOutMapper.selectByGoodId(id);
            if (stockOutRecords != null && !stockOutRecords.isEmpty())
                return false;

            List<Storage> storageRecords = storageMapper.selectByGoodsIDAndRepositoryID(id, null);
            if (storageRecords != null && !storageRecords.isEmpty())
                return false;

            goodsMapper.deleteById(id);
            return true;
        } catch (PersistenceException e) {
            throw new GoodsManageServiceException(e);
        }
    }
}
