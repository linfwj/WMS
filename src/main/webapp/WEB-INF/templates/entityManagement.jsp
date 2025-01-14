<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="panel panel-default">
    <!-- Search Panel -->
    <div class="panel-heading">
        <form id="searchForm" class="form-inline">
            <div class="form-group">
                <label>搜索方式：</label>
                <div class="btn-group">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                            aria-expanded="false">
                        <span id="search_type">所有</span>
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:void(0)">所有</a></li>
                        <li><a href="javascript:void(0)">ID</a></li>
                        <li><a href="javascript:void(0)">名称</a></li>
                    </ul>
                </div>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" id="search_input" placeholder="搜索..." readonly>
            </div>
            <div class="form-group">
                <button type="button" class="btn btn-primary" id="search_button">
                    <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                    查询
                </button>
            </div>
            <div class="btn-group">
                <button type="button" class="btn btn-success" id="add_button">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    添加
                </button>
                <button type="button" class="btn btn-info" id="import_button">
                    <span class="glyphicon glyphicon-import" aria-hidden="true"></span>
                    导入
                </button>
                <button type="button" class="btn btn-info" id="export_button">
                    <span class="glyphicon glyphicon-export" aria-hidden="true"></span>
                    导出
                </button>
            </div>
        </form>
    </div>

    <!-- Data Table -->
    <table id="dataTable" class="table table-striped table-hover">
        <thead>
        <tr>
            <th>ID</th>
            <th>名称</th>
            <th>创建时间</th>
            <th>更新时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

    <!-- Add/Edit Modal -->
    <div class="modal fade" id="entityModal" tabindex="-1" role="dialog" aria-labelledby="entityModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="entityModalLabel">添加</h4>
                </div>
                <div class="modal-body">
                    <form id="entityForm">
                        <input type="hidden" id="entity_id">
                        <div class="form-group">
                            <label for="entity_name">名称</label>
                            <input type="text" class="form-control" id="entity_name" placeholder="请输入名称">
                        </div>
                        <!-- Additional fields will be added dynamically -->
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="save_button">保存</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Import Modal -->
    <div class="modal fade" id="importModal" tabindex="-1" role="dialog" aria-labelledby="importModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="importModalLabel">导入</h4>
                </div>
                <div class="modal-body">
                    <div id="step1">
                        <h4>第一步</h4>
                        <p>下载模板文件</p>
                        <button type="button" class="btn btn-primary" id="download_template">
                            <span class="glyphicon glyphicon-download" aria-hidden="true"></span>
                            下载模板
                        </button>
                    </div>
                    <div id="step2" class="hide">
                        <h4>第二步</h4>
                        <p>选择文件</p>
                        <input type="file" id="file_input" accept=".xlsx,.xls">
                    </div>
                    <div id="step3" class="hide">
                        <h4>第三步</h4>
                        <p>导入数据</p>
                        <div id="import_progress" class="hide">
                            <div class="progress">
                                <div class="progress-bar progress-bar-striped active"></div>
                            </div>
                        </div>
                        <div id="import_result" class="hide">
                            <div class="alert" id="import_alert">
                                <span id="import_info"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" id="previous_button">上一步</button>
                    <button type="button" class="btn btn-primary" id="next_button">下一步</button>
                    <button type="button" class="btn btn-success hide" id="import_confirm">确认</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Message Modal -->
    <div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-labelledby="messageModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="messageModalLabel">提示</h4>
                </div>
                <div class="modal-body">
                    <p id="message_content"></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Common JavaScript -->
<script>
    // Global variables
    var entityName = '${entityName}';  // Set by controller
    var baseUrl = '${baseUrl}';        // Set by controller
    var searchType = 'searchAll';
    var searchKeyword = '';
    
    $(function() {
        initSearchPanel();
        initDataTable();
        initButtons();
        initImportModal();
    });

    // Initialize search panel
    function initSearchPanel() {
        $('.dropdown-menu li a').click(function() {
            var type = $(this).text();
            $('#search_input').val('');
            if (type === '所有') {
                $('#search_input').attr('readonly', 'readonly');
                searchType = 'searchAll';
            } else if (type === 'ID') {
                $('#search_input').removeAttr('readonly');
                searchType = 'searchById';
            } else if (type === '名称') {
                $('#search_input').removeAttr('readonly');
                searchType = 'searchByName';
            }
            $('#search_type').text(type);
            $('#search_input').attr('placeholder', type);
        });

        $('#search_button').click(function() {
            searchKeyword = $('#search_input').val();
            refreshTable();
        });
    }

    // Initialize data table
    function initDataTable() {
        $('#dataTable').bootstrapTable({
            url: baseUrl + '/getList',
            method: 'get',
            dataType: 'json',
            pagination: true,
            sidePagination: 'server',
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 25, 50, 100],
            queryParams: function(params) {
                return {
                    offset: params.offset,
                    limit: params.limit,
                    searchType: searchType,
                    keyword: searchKeyword
                };
            },
            columns: [{
                field: 'id',
                title: 'ID'
            }, {
                field: 'name',
                title: '名称'
            }, {
                field: 'createTime',
                title: '创建时间'
            }, {
                field: 'updateTime',
                title: '更新时间'
            }, {
                field: 'operate',
                title: '操作',
                formatter: operateFormatter
            }]
        });
    }

    // Format operation buttons
    function operateFormatter(value, row, index) {
        return [
            '<button type="button" class="btn btn-info btn-xs edit">',
            '<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> 编辑',
            '</button> ',
            '<button type="button" class="btn btn-danger btn-xs delete">',
            '<span class="glyphicon glyphicon-trash" aria-hidden="true"></span> 删除',
            '</button>'
        ].join('');
    }

    // Initialize buttons
    function initButtons() {
        // Add button
        $('#add_button').click(function() {
            $('#entityModalLabel').text('添加' + entityName);
            $('#entityForm')[0].reset();
            $('#entityModal').modal('show');
        });

        // Save button
        $('#save_button').click(function() {
            var entity = {
                id: $('#entity_id').val(),
                name: $('#entity_name').val()
                // Additional fields will be added here
            };

            var url = entity.id ? baseUrl + '/update' : baseUrl + '/add';
            $.ajax({
                url: url,
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(entity),
                success: function(response) {
                    if (response.result === 'success') {
                        showMessage('操作成功');
                        $('#entityModal').modal('hide');
                        refreshTable();
                    } else {
                        showMessage('操作失败：' + response.msg);
                    }
                }
            });
        });

        // Edit button
        $('#dataTable').on('click', '.edit', function() {
            var row = $('#dataTable').bootstrapTable('getData')[$(this).closest('tr').data('index')];
            $('#entityModalLabel').text('编辑' + entityName);
            $('#entity_id').val(row.id);
            $('#entity_name').val(row.name);
            // Additional fields will be populated here
            $('#entityModal').modal('show');
        });

        // Delete button
        $('#dataTable').on('click', '.delete', function() {
            var row = $('#dataTable').bootstrapTable('getData')[$(this).closest('tr').data('index')];
            if (confirm('确认删除该' + entityName + '？')) {
                $.ajax({
                    url: baseUrl + '/delete',
                    type: 'GET',
                    data: { id: row.id },
                    success: function(response) {
                        if (response.result === 'success') {
                            showMessage('删除成功');
                            refreshTable();
                        } else {
                            showMessage('删除失败：' + response.msg);
                        }
                    }
                });
            }
        });

        // Import/Export buttons
        $('#import_button').click(function() {
            $('#importModal').modal('show');
        });

        $('#export_button').click(function() {
            window.location.href = baseUrl + '/export?searchType=' + searchType + '&keyword=' + searchKeyword;
        });
    }

    // Initialize import modal
    function initImportModal() {
        var currentStep = 1;
        var totalSteps = 3;

        function showStep(step) {
            for (var i = 1; i <= totalSteps; i++) {
                $('#step' + i).addClass('hide');
            }
            $('#step' + step).removeClass('hide');
            
            $('#previous_button').prop('disabled', step === 1);
            $('#next_button').prop('disabled', step === totalSteps);
            $('#import_confirm').toggleClass('hide', step !== totalSteps);
        }

        $('#previous_button').click(function() {
            if (currentStep > 1) {
                currentStep--;
                showStep(currentStep);
            }
        });

        $('#next_button').click(function() {
            if (currentStep < totalSteps) {
                currentStep++;
                showStep(currentStep);
            }
        });

        $('#download_template').click(function() {
            window.location.href = baseUrl + '/template';
        });

        $('#import_confirm').click(function() {
            var formData = new FormData();
            formData.append('file', $('#file_input')[0].files[0]);

            $.ajax({
                url: baseUrl + '/import',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                beforeSend: function() {
                    $('#import_progress').removeClass('hide');
                },
                success: function(response) {
                    $('#import_progress').addClass('hide');
                    if (response.result === 'success') {
                        $('#import_alert').removeClass('alert-danger').addClass('alert-success');
                        $('#import_info').text('导入成功，共导入 ' + response.total + ' 条记录');
                    } else {
                        $('#import_alert').removeClass('alert-success').addClass('alert-danger');
                        $('#import_info').text('导入失败：' + response.msg);
                    }
                    $('#import_result').removeClass('hide');
                    refreshTable();
                }
            });
        });

        $('#importModal').on('hidden.bs.modal', function() {
            currentStep = 1;
            showStep(currentStep);
            $('#import_progress').addClass('hide');
            $('#import_result').addClass('hide');
            $('#file_input').val('');
        });
    }

    // Refresh table
    function refreshTable() {
        $('#dataTable').bootstrapTable('refresh');
    }

    // Show message
    function showMessage(message) {
        $('#message_content').text(message);
        $('#messageModal').modal('show');
    }
</script>
