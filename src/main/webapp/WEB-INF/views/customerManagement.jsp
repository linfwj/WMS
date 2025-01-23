<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>客户管理</title>
    
    <!-- Include base entity management page -->
    <jsp:include page="/pagecomponent/common/entityManagement.jsp" />
    
    <!-- Customer-specific JS -->
    <script>
    // Configure URLs for customer management
    entityConfig = {
        getListUrl: '${pageContext.request.contextPath}/customerManage/getCustomerList',
        addUrl: '${pageContext.request.contextPath}/customerManage/addCustomer',
        updateUrl: '${pageContext.request.contextPath}/customerManage/updateCustomer',
        deleteUrl: '${pageContext.request.contextPath}/customerManage/deleteCustomer',
        importUrl: '${pageContext.request.contextPath}/customerManage/importCustomer',
        exportUrl: '${pageContext.request.contextPath}/customerManage/exportCustomer'
    };
    
    // Add customer-specific columns
    $(function() {
        $('#entityTable').bootstrapTable({
            columns: [{
                field: 'state',
                checkbox: true
            }, {
                field: 'id',
                title: 'ID',
                sortable: true
            }, {
                field: 'name',
                title: '客户名称',
                sortable: true
            }, {
                field: 'personInCharge',
                title: '负责人'
            }, {
                field: 'tel',
                title: '联系电话'
            }, {
                field: 'email',
                title: '电子邮件'
            }, {
                field: 'address',
                title: '地址'
            }, {
                field: 'action',
                title: '操作',
                formatter: actionFormatter
            }]
        });
    });
    </script>
</head>
<body>
    <!-- Customer-specific form fields -->
    <script>
    $(function() {
        var extraFields = `
            <div class="form-group">
                <label for="personInCharge">负责人</label>
                <input type="text" class="form-control" id="personInCharge" name="personInCharge" placeholder="请输入负责人">
            </div>
            <div class="form-group">
                <label for="tel">联系电话</label>
                <input type="text" class="form-control" id="tel" name="tel" placeholder="请输入联系电话">
            </div>
            <div class="form-group">
                <label for="email">电子邮件</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="请输入电子邮件">
            </div>
            <div class="form-group">
                <label for="address">地址</label>
                <input type="text" class="form-control" id="address" name="address" placeholder="请输入地址">
            </div>
        `;
        $('#entityForm .modal-body form').append(extraFields);
    });
    </script>
</body>
</html>
