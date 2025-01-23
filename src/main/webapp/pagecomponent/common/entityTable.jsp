<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">数据列表</h3>
    </div>
    <div class="panel-body">
        <div id="toolbar" class="btn-group">
            <button id="addBtn" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-plus"></span> 新增
            </button>
            <button id="deleteBtn" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-minus"></span> 删除
            </button>
            <button id="importBtn" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-import"></span> 导入
            </button>
            <button id="exportBtn" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-export"></span> 导出
            </button>
        </div>
        <table id="entityTable" 
               data-toggle="table"
               data-url=""
               data-pagination="true"
               data-side-pagination="server"
               data-page-list="[5, 10, 20, 50]"
               data-search="false"
               data-show-refresh="true"
               data-show-toggle="true"
               data-show-columns="true"
               data-toolbar="#toolbar">
            <thead>
                <tr>
                    <th data-field="state" data-checkbox="true"></th>
                    <th data-field="id" data-sortable="true">ID</th>
                    <th data-field="name" data-sortable="true">名称</th>
                    <th data-field="action" data-formatter="actionFormatter">操作</th>
                </tr>
            </thead>
        </table>
    </div>
</div>

<script>
function actionFormatter(value, row, index) {
    return [
        '<button class="edit btn btn-xs btn-info" onclick="editEntity(' + row.id + ')">',
        '<span class="glyphicon glyphicon-pencil"></span> 编辑',
        '</button> ',
        '<button class="remove btn btn-xs btn-danger" onclick="deleteEntity(' + row.id + ')">',
        '<span class="glyphicon glyphicon-remove"></span> 删除',
        '</button>'
    ].join('');
}
</script>
