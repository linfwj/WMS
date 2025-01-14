# Entity Management Framework Guide

## Overview
The Entity Management Framework provides a standardized approach to implementing CRUD operations and management interfaces for entities in the WMS system. It follows the existing architecture patterns and integrates with the current technology stack:
- Spring MVC for controllers
- MyBatis for database access
- Bootstrap + jQuery for UI components
- Apache POI for Excel import/export capabilities

## Creating New Entity Modules

### 1. Implement BaseEntity Interface
```java
public class YourEntity implements BaseEntity {
    private Integer id;
    private String name;
    private Date createTime;
    private Date updateTime;
    
    // Implement all required methods from BaseEntity
    @Override
    public Integer getId() { return id; }
    
    @Override
    public void setId(Integer id) { this.id = id; }
    
    // ... implement other required methods
}
```

### 2. Create Entity-Specific Components
1. Create database table following the base schema:
```sql
CREATE TABLE your_entity (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
    -- Add additional fields as needed
);
```

2. Configure entity management components using EntityManagementFactory:
```java
@Controller
@RequestMapping("/yourEntity")
public class YourEntityController {
    @Autowired
    private EntityManagementFactory factory;
    
    @PostConstruct
    public void init() {
        // Create components
        BaseMapper<YourEntity> mapper = factory.createMapper(YourEntity.class);
        BaseService<YourEntity> service = factory.createService(
            YourEntity.class, "YourEntity", mapper);
        BaseController<YourEntity> controller = factory.createController(
            YourEntity.class, "YourEntity", "/yourEntity", service);
    }
}
```

3. Include the UI template in your JSP:
```jsp
<jsp:include page="/WEB-INF/templates/entityManagement.jsp">
    <jsp:param name="entityName" value="YourEntity"/>
    <jsp:param name="baseUrl" value="/yourEntity"/>
</jsp:include>
```

## Customizing Validation
The framework provides several extension points for custom validation:

### 1. Service Layer Validation
Override the validateEntity method in your service implementation:
```java
@Override
protected void validateEntity(YourEntity entity) throws ServiceException {
    super.validateEntity(entity);  // Call base validation
    
    // Add custom validation
    if (entity.getCustomField() == null) {
        throw new ServiceException("Custom field cannot be null");
    }
}
```

### 2. Controller Layer Validation
Add custom validation annotations to your entity:
```java
public class YourEntity implements BaseEntity {
    @NotNull
    @Size(min = 3, max = 50)
    private String name;
    
    // ... other fields and methods
}
```

## Extending Base Functionality

### 1. Custom Search Implementation
Override the search method in your service to add custom search logic:
```java
@Override
public Map<String, Object> search(String searchType, String keyword, 
    int offset, int limit) throws ServiceException {
    
    if ("customSearch".equals(searchType)) {
        // Implement custom search logic
        return customSearchImplementation(keyword, offset, limit);
    }
    
    // Fall back to base implementation
    return super.search(searchType, keyword, offset, limit);
}
```

### 2. Additional Business Logic
Add custom methods to your service implementation:
```java
public class YourEntityServiceImpl extends BaseServiceImpl<YourEntity> {
    public void customBusinessOperation(YourEntity entity) {
        // Implement custom business logic
    }
}
```

### 3. UI Customization
Customize the entity management template by adding custom columns or buttons:
```javascript
// Add custom columns to the data table
$('#dataTable').bootstrapTable({
    columns: [{
        field: 'customField',
        title: 'Custom Field'
    }, ...baseColumns]
});

// Add custom buttons
$('#customButton').click(function() {
    // Custom button handler
});
```

## Common Patterns and Best Practices

### 1. Error Handling
- Use ServiceException for business logic errors
- Implement proper error messages in validation
- Handle UI errors gracefully with message modal

### 2. Transaction Management
- Use @Transactional for service methods
- Consider transaction boundaries in batch operations
- Handle rollbacks appropriately

### 3. Performance Considerations
- Use pagination for large datasets
- Implement efficient search queries
- Consider caching strategies

### 4. Security
- Implement proper access control
- Validate input data
- Use CSRF protection

### 5. Testing
- Unit test custom validation logic
- Integration test with database operations
- UI testing for custom components

## Troubleshooting

### Common Issues
1. Entity not found
   - Check table name configuration
   - Verify database connectivity
   
2. Validation errors
   - Review custom validation logic
   - Check error messages
   
3. UI issues
   - Verify proper template inclusion
   - Check browser console for errors

### Support
For additional support:
1. Review existing entity implementations
2. Check framework documentation
3. Contact the development team

## API Reference

### BaseEntity Interface
```java
public interface BaseEntity {
    Integer getId();
    void setId(Integer id);
    String getName();
    void setName(String name);
    Date getCreateTime();
    void setCreateTime(Date createTime);
    Date getUpdateTime(); 
    void setUpdateTime(Date updateTime);
}
```

### BaseService Interface
```java
public interface BaseService<T extends BaseEntity> {
    T getById(Integer id) throws ServiceException;
    List<T> getByName(String name) throws ServiceException;
    List<T> getAll() throws ServiceException;
    Map<String, Object> search(String searchType, String keyword, 
        int offset, int limit) throws ServiceException;
    boolean add(T entity) throws ServiceException;
    boolean update(T entity) throws ServiceException;
    boolean delete(Integer id) throws ServiceException;
    Map<String, Object> importEntities(MultipartFile file) throws ServiceException;
    void exportEntities(String searchType, String keyword, 
        HttpServletResponse response) throws ServiceException;
}
```
