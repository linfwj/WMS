<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="panel panel-default">
    <div class="panel-body">
        <form role="form" class="form-inline">
            <div class="form-group">
                <label>搜索方式：</label>
                <select id="searchType" class="form-control">
                    <option value="searchByID">按ID查询</option>
                    <option value="searchByName">按名称查询</option>
                    <option value="searchAll">查询所有</option>
                </select>
            </div>
            <div class="form-group" id="searchKeyWord">
                <label for="keyword">关键字：</label>
                <input type="text" id="keyword" class="form-control" placeholder="请输入关键字">
            </div>
            <div class="form-group">
                <button id="searchBtn" type="button" class="btn btn-primary">
                    <span class="glyphicon glyphicon-search"></span> 查询
                </button>
            </div>
        </form>
    </div>
</div>
