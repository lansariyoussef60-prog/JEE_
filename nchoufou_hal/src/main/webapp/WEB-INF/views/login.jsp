<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nchoufou Hal — Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>

<div class="auth-wrapper">
    <div class="auth-card">

        <h1>🇹🇳 Nchoufou Hal</h1>
        <p class="subtitle">Citizen Reporting Platform — Tunisia</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">${error}</div>
        <% } %>

        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success">${success}</div>
        <% } %>

        <form action="${pageContext.request.contextPath}/Controller?action=login" method="post">

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username"
                       placeholder="Enter your username" required autofocus>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password"
                       placeholder="Enter your password" required>
            </div>

            <button type="submit" class="btn btn-primary btn-full">Sign In</button>

        </form>

        <p style="text-align:center; margin-top:1.2rem; font-size:0.88rem; color:#718096;">
            No account?
            <a href="${pageContext.request.contextPath}/Controller?action=register"
               style="color:#3182ce; font-weight:600;">Register here</a>
        </p>

    </div>
</div>

</body>
</html>