<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Menu Management</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-table.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/select2.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sweetalert.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <h2>Menu Management</h2>
                <hr>
                <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Roles</h3>
                            </div>
                            <div class="panel-body">
                                <select id="roleSelect" class="form-control">
                                    <option value="">Select a role...</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Available Menus</h3>
                            </div>
                            <div class="panel-body">
                                <table id="menuTable" class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th data-field="menuId">ID</th>
                                            <th data-field="menuName">Name</th>
                                            <th data-field="menuUrl">URL</th>
                                            <th data-field="action">Action</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title">Assigned Menus</h3>
                            </div>
                            <div class="panel-body">
                                <table id="assignedMenuTable" class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th data-field="menuId">ID</th>
                                            <th data-field="menuName">Name</th>
                                            <th data-field="menuUrl">URL</th>
                                            <th data-field="action">Action</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap-table.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/select2.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/sweetalert.min.js"></script>
    <script>
        $(document).ready(function() {
            // Initialize select2
            $('#roleSelect').select2();

            // Load roles
            $.get('${pageContext.request.contextPath}/menu/roles', function(response) {
                if (response.success) {
                    response.roles.forEach(function(role) {
                        $('#roleSelect').append(new Option(role, role));
                    });
                }
            });

            // Initialize tables
            $('#menuTable').bootstrapTable({
                url: '${pageContext.request.contextPath}/menu/all',
                responseHandler: function(res) {
                    return res.menus;
                },
                columns: [{
                    field: 'menuId',
                    title: 'ID'
                }, {
                    field: 'menuName',
                    title: 'Name'
                }, {
                    field: 'menuUrl',
                    title: 'URL'
                }, {
                    field: 'action',
                    title: 'Action',
                    formatter: function(value, row) {
                        return '<button class="btn btn-primary btn-sm assign-btn" data-menu-id="' + row.menuId + '">Assign</button>';
                    }
                }]
            });

            $('#assignedMenuTable').bootstrapTable({
                columns: [{
                    field: 'menuId',
                    title: 'ID'
                }, {
                    field: 'menuName',
                    title: 'Name'
                }, {
                    field: 'menuUrl',
                    title: 'URL'
                }, {
                    field: 'action',
                    title: 'Action',
                    formatter: function(value, row) {
                        return '<button class="btn btn-danger btn-sm remove-btn" data-menu-id="' + row.menuId + '">Remove</button>';
                    }
                }]
            });

            // Handle role selection
            $('#roleSelect').on('change', function() {
                var roleId = $(this).val();
                if (roleId) {
                    $('#assignedMenuTable').bootstrapTable('refresh', {
                        url: '${pageContext.request.contextPath}/menu/role/' + roleId
                    });
                }
            });

            // Handle menu assignment
            $(document).on('click', '.assign-btn', function() {
                var menuId = $(this).data('menu-id');
                var roleId = $('#roleSelect').val();
                
                if (!roleId) {
                    swal('Error', 'Please select a role first', 'error');
                    return;
                }

                $.post('${pageContext.request.contextPath}/menu/assign', {
                    roleId: roleId,
                    menuId: menuId
                }, function(response) {
                    if (response.success) {
                        swal('Success', 'Menu assigned successfully', 'success');
                        $('#assignedMenuTable').bootstrapTable('refresh');
                    } else {
                        swal('Error', response.message || 'Failed to assign menu', 'error');
                    }
                });
            });

            // Handle menu removal
            $(document).on('click', '.remove-btn', function() {
                var menuId = $(this).data('menu-id');
                var roleId = $('#roleSelect').val();

                $.post('${pageContext.request.contextPath}/menu/remove', {
                    roleId: roleId,
                    menuId: menuId
                }, function(response) {
                    if (response.success) {
                        swal('Success', 'Menu removed successfully', 'success');
                        $('#assignedMenuTable').bootstrapTable('refresh');
                    } else {
                        swal('Error', response.message || 'Failed to remove menu', 'error');
                    }
                });
            });
        });
    </script>
</body>
</html>
