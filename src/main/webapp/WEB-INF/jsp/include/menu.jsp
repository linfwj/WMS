<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="sidebar-menu">
    <c:if test="${not empty userMenus}">
        <c:forEach items="${userMenus}" var="menu">
            <c:if test="${empty menu.menuParentId}">
                <div class="menu-item">
                    <a href="${pageContext.request.contextPath}${menu.menuUrl}" class="menu-link">
                        <c:if test="${not empty menu.menuIcon}">
                            <i class="${menu.menuIcon}"></i>
                        </c:if>
                        <span>${menu.menuName}</span>
                    </a>
                    <!-- 子菜单 -->
                    <c:forEach items="${userMenus}" var="submenu">
                        <c:if test="${submenu.menuParentId eq menu.menuId}">
                            <div class="submenu-item">
                                <a href="${pageContext.request.contextPath}${submenu.menuUrl}" class="submenu-link">
                                    <c:if test="${not empty submenu.menuIcon}">
                                        <i class="${submenu.menuIcon}"></i>
                                    </c:if>
                                    <span>${submenu.menuName}</span>
                                </a>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:if>
        </c:forEach>
    </c:if>
</div>
