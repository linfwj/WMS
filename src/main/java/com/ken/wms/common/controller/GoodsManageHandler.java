package com.ken.wms.common.controller;

import com.ken.wms.common.framework.controller.BaseEntityHandler;
import com.ken.wms.domain.Goods;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Goods management request handler.
 * Extends BaseEntityHandler for standard CRUD operations.
 *
 * @author Ken
 * @author Devin
 */
@RequestMapping(value = "/**/goodsManage")
@Controller
public class GoodsManageHandler extends BaseEntityHandler<Goods> {

    @Override
    protected Class<Goods> getEntityClass() {
        return Goods.class;
    }

    @Override
    protected String getExportFilename() {
        return "goodsInfo.xlsx";
    }
}
