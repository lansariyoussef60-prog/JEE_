<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${report.title} — Nchoufou Hal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>

<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/Controller?action=home">
        🇹🇳 Nchoufou Hal
    </a>
    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/Controller?action=home">← Back</a>
        <a href="${pageContext.request.contextPath}/Controller?action=logout"
           class="btn btn-logout">Logout</a>
    </div>
</nav>

<div class="container">

    <%-- Report Detail --%>
    <div class="card">
        <div style="display:flex; justify-content:space-between; align-items:flex-start;">
            <h3 style="font-size:1.3rem;">${report.title}</h3>
            <span class="badge badge-${report.status}">${report.status}</span>
        </div>

        <p style="margin-top:0.8rem; line-height:1.7;">${report.description}</p>

        <div class="meta">
            Reported by <strong>${report.username}</strong> · ${report.createdAt}
        </div>

        <%-- Agent: update status --%>
        <c:if test="${sessionUser.role == 'agent'}">
            <form action="${pageContext.request.contextPath}/Controller?action=updateStatus"
                  method="post"
                  style="margin-top:1rem; display:flex; gap:0.6rem; align-items:center;">
                <input type="hidden" name="reportId" value="${report.reportId}">
                <select name="status"
                        style="padding:0.5rem 0.8rem; border-radius:8px; border:1.5px solid #e2e8f0;">
                    <option value="in_progress" ${report.status == 'in_progress' ? 'selected' : ''}>
                        In Progress
                    </option>
                    <option value="resolved" ${report.status == 'resolved' ? 'selected' : ''}>
                        Resolved
                    </option>
                </select>
                <button type="submit" class="btn btn-warning">Update Status</button>
            </form>
        </c:if>

        <%-- Admin: delete --%>
        <c:if test="${sessionUser.role == 'admin'}">
            <form action="${pageContext.request.contextPath}/Controller?action=deleteReport"
                  method="post" style="margin-top:1rem;"
                  onsubmit="return confirm('Delete this report permanently?')">
                <input type="hidden" name="reportId" value="${report.reportId}">
                <button type="submit" class="btn btn-danger">Delete Report</button>
            </form>
        </c:if>
    </div>

    <%-- Comments --%>
    <h3 style="color:#1a365d; margin-bottom:1rem;">
        💬 Comments (${comments.size()})
    </h3>

    <c:if test="${empty comments}">
        <div class="alert alert-info">No comments yet. Be the first!</div>
    </c:if>

    <c:forEach var="cm" items="${comments}">
        <div class="comment-item">
            <div class="comment-author">👤 ${cm.username}</div>
            <div class="comment-body">${cm.body}</div>
            <div class="comment-time">${cm.createdAt}</div>
        </div>
    </c:forEach>

    <%-- Add Comment --%>
    <div class="card" style="margin-top:1.5rem;">
        <h3 style="margin-bottom:1rem;">Add a Comment</h3>
        <form action="${pageContext.request.contextPath}/Controller?action=addComment"
              method="post">
            <input type="hidden" name="reportId" value="${report.reportId}">
            <div class="form-group">
                <textarea name="body" rows="3"
                          placeholder="Write your comment here..."
                          style="resize:vertical;" required></textarea>
            </div>
            <button type="submit" class="btn btn-success">Post Comment</button>
        </form>
    </div>

</div>
</body>
</html>