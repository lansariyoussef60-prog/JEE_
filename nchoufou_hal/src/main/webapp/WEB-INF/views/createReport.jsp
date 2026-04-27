<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>New Report — Nchoufou Hal</title>
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

<div class="container" style="max-width:600px;">
    <div class="card" style="border-left-color:#38a169;">

        <h2 style="color:#1a365d; margin-bottom:1.5rem;">📝 Submit a New Report</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">${error}</div>
        <% } %>

        <form action="${pageContext.request.contextPath}/Controller?action=createReport"
              method="post">

            <div class="form-group">
                <label for="title">Report Title</label>
                <input type="text" id="title" name="title"
                       placeholder="e.g. Broken street light on Habib Bourguiba Ave."
                       required>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="5"
                          placeholder="Describe the issue in detail..."
                          style="resize:vertical;" required></textarea>
            </div>

            <div style="display:flex; gap:0.8rem;">
                <button type="submit" class="btn btn-success">Submit Report</button>
                <a href="${pageContext.request.contextPath}/Controller?action=home"
                   class="btn btn-primary">Cancel</a>
            </div>

        </form>
    </div>
</div>

</body>
</html>