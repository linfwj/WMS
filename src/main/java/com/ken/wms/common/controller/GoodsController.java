package com.ken.wms.common.controller;

import com.ken.wms.common.service.Interface.GoodsManageService;
import com.ken.wms.domain.Goods;
import com.ken.wms.framework.controller.BaseController;
import com.ken.wms.framework.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for Goods management operations.
 * Extends BaseController to inherit standard CRUD endpoints.
 *
 * @author Devin
 */
@RestController
@RequestMapping("/goodsManage")
public class GoodsController extends BaseController<Goods> {

    @Autowired
    private GoodsManageService goodsManageService;

    @Override
    protected BaseService<Goods> getService() {
        return goodsManageService;
    }

    /**
     * Legacy endpoint mapping for updateGoods to maintain compatibility
     * with existing frontend
     */
    @PostMapping("/updateGoods")
    public ResponseEntity<String> updateGoods(@RequestBody Goods goods) {
        return update(goods);
    }

    /**
     * Legacy endpoint mapping for addGoods to maintain compatibility
     * with existing frontend
     */
    @PostMapping("/addGoods")
    public ResponseEntity<String> addGoods(@RequestBody Goods goods) {
        return add(goods);
    }

    /**
     * Legacy endpoint mapping for deleteGoods to maintain compatibility
     * with existing frontend
     */
    @GetMapping("/deleteGoods")
    public ResponseEntity<String> deleteGoods(@RequestParam Integer goodsID) {
        return delete(goodsID);
    }

    /**
     * Legacy endpoint mapping for getGoodsList to maintain compatibility
     * with existing frontend
     */
    @GetMapping("/getGoodsList")
    public ResponseEntity<Object> getGoodsList(
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(list(offset, limit).getBody());
    }

    /**
     * Legacy endpoint mapping for searchGoods to maintain compatibility
     * with existing frontend
     */
    @GetMapping("/searchGoods")
    public ResponseEntity<Object> searchGoods(
            @RequestParam String goodsName,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(search(goodsName, offset, limit).getBody());
    }
}
