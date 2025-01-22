package com.ken.wms.exception;

/**
 * Feature service exception
 * @author devin
 * @since 2024/1/22
 */
public class FeatureServiceException extends Exception {
    
    public FeatureServiceException(String message) {
        super(message);
    }

    public FeatureServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
