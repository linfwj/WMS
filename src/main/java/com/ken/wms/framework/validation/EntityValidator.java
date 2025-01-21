package com.ken.wms.framework.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Validation framework for entity objects.
 * Provides common validation patterns and utilities.
 *
 * @author Devin
 */
public class EntityValidator {

    /**
     * Annotation to mark required fields
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Required {
        String message() default "Field is required";
    }

    /**
     * Validation result containing error messages
     */
    public static class ValidationResult {
        private List<String> errors = new ArrayList<>();
        
        public void addError(String error) {
            errors.add(error);
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
    }

    /**
     * Validate an entity using reflection and annotations
     *
     * @param entity Entity to validate
     * @return ValidationResult containing any error messages
     */
    public static ValidationResult validate(Object entity) {
        ValidationResult result = new ValidationResult();
        
        if (entity == null) {
            result.addError("Entity cannot be null");
            return result;
        }

        // Get all declared fields
        Field[] fields = entity.getClass().getDeclaredFields();
        
        for (Field field : fields) {
            field.setAccessible(true);
            
            // Check @Required annotation
            if (field.isAnnotationPresent(Required.class)) {
                try {
                    Object value = field.get(entity);
                    if (value == null) {
                        Required annotation = field.getAnnotation(Required.class);
                        result.addError(field.getName() + ": " + annotation.message());
                    }
                } catch (IllegalAccessException e) {
                    result.addError("Could not access field: " + field.getName());
                }
            }
        }
        
        return result;
    }

    /**
     * Convenience method to check if an entity is valid
     *
     * @param entity Entity to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(Object entity) {
        return validate(entity).isValid();
    }

    /**
     * Validate common name and value fields that are present in most entities
     *
     * @param entity Entity to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateNameAndValue(Object entity) {
        if (entity == null) {
            return false;
        }

        try {
            Field nameField = entity.getClass().getDeclaredField("name");
            Field valueField = entity.getClass().getDeclaredField("value");
            
            nameField.setAccessible(true);
            valueField.setAccessible(true);
            
            Object name = nameField.get(entity);
            Object value = valueField.get(entity);
            
            return name != null && value != null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
