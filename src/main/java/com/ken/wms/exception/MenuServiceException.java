package com.ken.wms.exception;

/**
 * Menu service exception
 * @author devin
 * @since 2024/1/22
 */
public class MenuServiceException extends Exception {
    
    public MenuServiceException(String message) {
        super(message);
    }

    public MenuServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
