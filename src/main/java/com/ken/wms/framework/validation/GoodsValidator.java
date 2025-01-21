package com.ken.wms.framework.validation;

import com.ken.wms.domain.Goods;

/**
 * Concrete validator implementation for Goods entity.
 * Demonstrates how to implement entity-specific validation rules.
 *
 * @author Devin
 */
public class GoodsValidator {
    
    /**
     * Validate a Goods entity
     * 
     * @param goods The goods entity to validate
     * @return true if valid, false otherwise
     */
    public static boolean validate(Goods goods) {
        if (goods == null) {
            return false;
        }

        // Use the EntityValidator for basic validation
        EntityValidator.ValidationResult result = EntityValidator.validate(goods);
        if (!result.isValid()) {
            return false;
        }

        // Additional goods-specific validation
        if (goods.getValue() != null && goods.getValue() < 0) {
            return false;
        }

        return true;
    }
}
