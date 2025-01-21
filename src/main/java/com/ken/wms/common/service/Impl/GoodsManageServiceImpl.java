package com.ken.wms.common.service.Impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ken.wms.common.service.Interface.GoodsManageService;
import com.ken.wms.common.util.ExcelUtil;
import com.ken.wms.dao.GoodsMapper;
import com.ken.wms.dao.StockInMapper;
import com.ken.wms.dao.StockOutMapper;
import com.ken.wms.dao.StorageMapper;
import com.ken.wms.domain.Goods;
import com.ken.wms.domain.StockInDO;
import com.ken.wms.domain.StockOutDO;
import com.ken.wms.domain.Storage;
import com.ken.wms.exception.GoodsManageServiceException;
import com.ken.wms.framework.service.BaseServiceImpl;
import com.ken.wms.framework.validation.GoodsValidator;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 货物信息管理Service 实现类
 *
 * @author Ken
 */
@Service
public class GoodsManageServiceImpl extends BaseServiceImpl<Goods> implements GoodsManageService {

    @Override
    protected Class<Goods> getEntityClass() {
        return Goods.class;
    }

    @Override
    protected boolean validateEntity(Goods entity) {
        return GoodsValidator.validate(entity);
    }

    @Override
    protected boolean canDelete(Integer id) {
        try {
            // Check if goods has any stock in records
            List<StockInDO> stockInDORecord = stockInMapper.selectByGoodID(id);
            if (stockInDORecord != null && !stockInDORecord.isEmpty())
                return false;

            // Check if goods has any stock out records
            List<StockOutDO> stockOutDORecord = stockOutMapper.selectByGoodId(id);
            if (stockOutDORecord != null && !stockOutDORecord.isEmpty())
                return false;

            // Check if goods has any storage records
            List<Storage> storageRecord = storageMapper.selectByGoodsIDAndRepositoryID(id, null);
            if (storageRecord != null && !storageRecord.isEmpty())
                return false;

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private StockInMapper stockInMapper;
    @Autowired
    private StockOutMapper stockOutMapper;
    @Autowired
    private StorageMapper storageMapper;
    @Autowired
    private ExcelUtil excelUtil;

    @Override
    public Map<String, Object> selectById(Integer goodsId) throws GoodsManageServiceException {
        try {
            return super.selectById(goodsId);
        } catch (Exception e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public Map<String, Object> selectByName(int offset, int limit, String goodsName) throws GoodsManageServiceException {
        try {
            return super.selectByName(offset, limit, goodsName);
        } catch (Exception e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public Map<String, Object> selectByName(String goodsName) throws GoodsManageServiceException {
        return selectByName(-1, -1, goodsName);
    }

    @Override
    public Map<String, Object> selectAll(int offset, int limit) throws GoodsManageServiceException {
        try {
            return super.selectAll(offset, limit);
        } catch (Exception e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public Map<String, Object> selectAll() throws GoodsManageServiceException {
        return selectAll(-1, -1);
    }

    @Override
    public boolean addGoods(Goods goods) throws GoodsManageServiceException {
        try {
            return super.add(goods);
        } catch (Exception e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public boolean updateGoods(Goods goods) throws GoodsManageServiceException {
        try {
            return super.update(goods);
        } catch (Exception e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public boolean deleteGoods(Integer goodsId) throws GoodsManageServiceException {
        try {
            return super.delete(goodsId);
        } catch (Exception e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public Map<String, Object> importGoods(MultipartFile file) throws GoodsManageServiceException {
        try {
            return super.importEntities(file);
        } catch (Exception e) {
            throw new GoodsManageServiceException(e);
        }
    }

    @Override
    public File exportGoods(List<Goods> goods) {
        try {
            return super.exportEntities(goods);
        } catch (Exception e) {
            return null;
        }
    }
}
