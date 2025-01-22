<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Entity Management Template -->
<div class="panel panel-default">
    <!-- Search Panel -->
    <div class="panel-body">
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <div class="btn-group">
                        <button class="btn btn-default" id="searchType" data-toggle="dropdown">
                            <span id="searchTypeText">Search By ID</span>
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="javascript:void(0)" class="searchTypeItem" data-type="searchByID">Search By ID</a></li>
                            <li><a href="javascript:void(0)" class="searchTypeItem" data-type="searchByName">Search By Name</a></li>
                            <li><a href="javascript:void(0)" class="searchTypeItem" data-type="searchAll">Show All</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <input type="text" id="searchKeyWord" class="form-control" placeholder="Search keyword..." />
                </div>
            </div>
            <div class="col-md-4">
                <div class="form-group">
                    <button id="searchBtn" class="btn btn-primary">
                        <span class="glyphicon glyphicon-search"></span> Search
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Data Table -->
    <table id="entityTable" data-toggle="table" data-url="" data-pagination="true"
           data-side-pagination="server" data-page-list="[5, 10, 20, 50]"
           data-search="false" data-show-refresh="true">
        <thead>
            <tr>
                <!-- Columns will be dynamically added via JavaScript -->
            </tr>
        </thead>
    </table>

    <!-- Action Buttons -->
    <div class="panel-footer">
        <div class="row">
            <div class="col-md-6">
                <button id="addEntityBtn" class="btn btn-success">
                    <span class="glyphicon glyphicon-plus"></span> Add
                </button>
                <button id="importEntityBtn" class="btn btn-info">
                    <span class="glyphicon glyphicon-import"></span> Import
                </button>
                <button id="exportEntityBtn" class="btn btn-warning">
                    <span class="glyphicon glyphicon-export"></span> Export
                </button>
            </div>
        </div>
    </div>
</div>

<!-- Add/Edit Modal -->
<div class="modal fade" id="entityModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="entityModalTitle">Add/Edit Entity</h4>
            </div>
            <div class="modal-body">
                <form id="entityForm">
                    <!-- Form fields will be dynamically added via JavaScript -->
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" id="saveEntityBtn">Save</button>
            </div>
        </div>
    </div>
</div>

<!-- Import Modal -->
<div class="modal fade" id="importModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Import Entities</h4>
            </div>
            <div class="modal-body">
                <form id="importForm" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="importFile">Select File</label>
                        <input type="file" id="importFile" name="file" accept=".xlsx,.xls" />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" id="confirmImportBtn">Import</button>
            </div>
        </div>
    </div>
</div>
