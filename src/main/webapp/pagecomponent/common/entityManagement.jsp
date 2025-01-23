<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>实体管理</title>
    
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-table.min.css" rel="stylesheet">
    
    <!-- HTML5 shim and Respond.js for IE8 support -->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.min.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
    <div class="container">
        <!-- Include search bar -->
        <jsp:include page="searchBar.jsp" />
        
        <!-- Include entity table -->
        <jsp:include page="entityTable.jsp" />
        
        <!-- Include entity form modal -->
        <jsp:include page="entityForm.jsp" />
        
        <!-- Include import modal -->
        <jsp:include page="importModal.jsp" />
    </div>

    <!-- jQuery and Bootstrap JS -->
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/bootstrap-table.min.js"></script>
    
    <!-- Common entity management JS -->
    <script src="js/common/entityManagement.js"></script>
    
    <!-- Entity-specific configuration -->
    <script>
    // Override this in your specific entity page
    var entityConfig = {
        getListUrl: '${pageContext.request.contextPath}/xxx/getList',
        addUrl: '${pageContext.request.contextPath}/xxx/addEntity',
        updateUrl: '${pageContext.request.contextPath}/xxx/updateEntity',
        deleteUrl: '${pageContext.request.contextPath}/xxx/deleteEntity',
        importUrl: '${pageContext.request.contextPath}/xxx/importEntities',
        exportUrl: '${pageContext.request.contextPath}/xxx/exportEntities'
    };
    
    // Add any entity-specific initialization here
    $(function() {
        // Example: Add custom columns to the table
        $('#entityTable').bootstrapTable('hideColumn', 'action');
        $('#entityTable').bootstrapTable('showColumn', 'customField');
    });
    </script>
</body>
</html>
