<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home — Nchoufou Hal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>

<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/Controller?action=home">
        🇹🇳 Nchoufou Hal
    </a>
    <div class="nav-links">
        <span style="color:#bee3f8; font-size:0.85rem;">
            👤 ${sessionUser.username}
            <span style="opacity:0.6;">(${sessionUser.role})</span>
        </span>

        <c:if test="${sessionUser.role == 'citizen'}">
            <a href="${pageContext.request.contextPath}/Controller?action=createReport">
                ＋ New Report
            </a>
        </c:if>

        <a href="${pageContext.request.contextPath}/Controller?action=logout"
           class="btn btn-logout">Logout</a>
    </div>
</nav>

<div class="container">

    <div class="page-header">
        <h2>
            <c:choose>
                <c:when test="${sessionUser.role == 'citizen'}">My Reports</c:when>
                <c:otherwise>All Reports</c:otherwise>
            </c:choose>
        </h2>
    </div>

    <c:if test="${empty reports}">
        <div class="empty-state">
            <p>No reports found.</p>
            <c:if test="${sessionUser.role == 'citizen'}">
                <a href="${pageContext.request.contextPath}/Controller?action=createReport"
                   class="btn btn-primary">Create your first report</a>
            </c:if>
        </div>
    </c:if>

    <c:forEach var="r" items="${reports}">
        <div class="card">

            <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                <h3>
                    <a href="${pageContext.request.contextPath}/Controller?action=viewReport&id=${r.reportId}"
                       style="color:#1a365d; text-decoration:none;">
                        ${r.title}
                    </a>
                </h3>
                <span class="badge badge-${r.status}">${r.status}</span>
            </div>

            <p>${r.description}</p>

            <div class="meta">
                By <strong>${r.username}</strong> · ${r.createdAt}
            </div>

            <div style="margin-top:0.8rem; display:flex; gap:0.6rem; flex-wrap:wrap;">

                <a href="${pageContext.request.contextPath}/Controller?action=viewReport&id=${r.reportId}"
                   class="btn btn-primary">View Details</a>

                <c:if test="${sessionUser.role == 'admin'}">
                    <form action="${pageContext.request.contextPath}/Controller?action=deleteReport"
                          method="post" style="display:inline;"
                          onsubmit="return confirm('Delete this report permanently?')">
                        <input type="hidden" name="reportId" value="${r.reportId}">
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </c:if>

            </div>
        </div>
    </c:forEach>

</div>
</body>
</html>