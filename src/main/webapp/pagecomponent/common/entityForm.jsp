<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="entityModal" tabindex="-1" role="dialog" aria-labelledby="entityModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="entityModalLabel">实体信息</h4>
            </div>
            <div class="modal-body">
                <form id="entityForm">
                    <input type="hidden" id="id" name="id">
                    <div class="form-group">
                        <label for="name">名称</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="请输入名称">
                    </div>
                    <!-- Additional fields will be added by concrete implementations -->
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="saveEntity()">保存</button>
            </div>
        </div>
    </div>
</div>
