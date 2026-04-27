<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register — Nchoufou Hal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>

<div class="auth-wrapper">
    <div class="auth-card">

        <h1>Create Account</h1>
        <p class="subtitle">Join as a citizen and report issues</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">${error}</div>
        <% } %>

        <form action="${pageContext.request.contextPath}/Controller?action=register" method="post">

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username"
                       placeholder="At least 3 characters" required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password"
                       placeholder="Choose a password" required>
            </div>

            <div class="form-group">
                <label for="confirm">Confirm Password</label>
                <input type="password" id="confirm" name="confirm"
                       placeholder="Repeat your password" required>
            </div>

            <button type="submit" class="btn btn-success btn-full">Create Account</button>

        </form>

        <p style="text-align:center; margin-top:1.2rem; font-size:0.88rem; color:#718096;">
            Already registered?
            <a href="${pageContext.request.contextPath}/Controller?action=login"
               style="color:#3182ce; font-weight:600;">Log in</a>
        </p>

    </div>
</div>

</body>
</html>