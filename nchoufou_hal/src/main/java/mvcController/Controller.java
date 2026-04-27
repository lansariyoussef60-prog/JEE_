package mvcController;

import entities.User;
import entities.Report;
import entities.Comment;
import mvcModel.UserService;
import mvcModel.ReportService;
import mvcModel.CommentService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/Controller")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private UserService userService;
    @EJB
    private ReportService  reportService;
    @EJB
    private CommentService commentService;

    // ── GET ──────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "login";

        switch (action) {
            case "login":        showLogin(request, response);        break;
            case "register":     showRegister(request, response);     break;
            case "home":         showHome(request, response);         break;
            case "createReport": showCreateReport(request, response); break;
            case "viewReport":   showViewReport(request, response);   break;
            case "logout":       doLogout(request, response);         break;
            default:             showLogin(request, response);
        }
    }

    // ── POST ─────────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "login";

        switch (action) {
            case "login":         processLogin(request, response);         break;
            case "register":      processRegister(request, response);      break;
            case "createReport":  processCreateReport(request, response);  break;
            case "addComment":    processAddComment(request, response);     break;
            case "deleteReport":  processDeleteReport(request, response);   break;
            case "updateStatus":  processUpdateStatus(request, response);   break;
            default:              showLogin(request, response);
        }
    }

    // ══════════════════════════════════════════════════════════════════
    // GET HANDLERS
    // ══════════════════════════════════════════════════════════════════

    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("sessionUser") != null) {
            response.sendRedirect(request.getContextPath() + "/Controller?action=home");
            return;
        }
        forward(request, response, "/WEB-INF/views/login.jsp");
    }

    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        forward(request, response, "/WEB-INF/views/register.jsp");
    }

    private void showHome(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;

        User sessionUser = getSessionUser(request);
        List<Report> reports;

        if (sessionUser.isCitizen()) {
            reports = reportService.getReportsByUser(sessionUser.getUserId());
        } else {
            reports = reportService.getAllReports();
        }

        request.setAttribute("reports", reports);
        request.setAttribute("sessionUser", sessionUser);
        forward(request, response, "/WEB-INF/views/home.jsp");
    }

    private void showCreateReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;
        User sessionUser = getSessionUser(request);
        if (!sessionUser.isCitizen()) {
            response.sendRedirect(request.getContextPath() + "/Controller?action=home");
            return;
        }
        forward(request, response, "/WEB-INF/views/createReport.jsp");
    }

    private void showViewReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/Controller?action=home");
            return;
        }

        int reportId              = Integer.parseInt(idParam);
        Report report             = reportService.getReportById(reportId);
        List<Comment> comments    = commentService.getCommentsByReport(reportId);
        User sessionUser          = getSessionUser(request);

        if (report == null) {
            response.sendRedirect(request.getContextPath() + "/Controller?action=home");
            return;
        }

        request.setAttribute("report",      report);
        request.setAttribute("comments",    comments);
        request.setAttribute("sessionUser", sessionUser);
        forward(request, response, "/WEB-INF/views/report.jsp");
    }

    // ══════════════════════════════════════════════════════════════════
    // POST HANDLERS
    // ══════════════════════════════════════════════════════════════════

    private void processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Please fill in all fields.");
            forward(request, response, "/WEB-INF/views/login.jsp");
            return;
        }

        User user = userService.login(username, password);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("sessionUser", user);
            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect(request.getContextPath() + "/Controller?action=home");
        } else {
            request.setAttribute("error", "Invalid username or password.");
            forward(request, response, "/WEB-INF/views/login.jsp");
        }
    }

    private void processRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String confirm  = request.getParameter("confirm").trim();

        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            forward(request, response, "/WEB-INF/views/register.jsp");
            return;
        }
        if (!password.equals(confirm)) {
            request.setAttribute("error", "Passwords do not match.");
            forward(request, response, "/WEB-INF/views/register.jsp");
            return;
        }
        if (username.length() < 3) {
            request.setAttribute("error", "Username must be at least 3 characters.");
            forward(request, response, "/WEB-INF/views/register.jsp");
            return;
        }
        if (userService.usernameExists(username)) {
            request.setAttribute("error", "Username already taken.");
            forward(request, response, "/WEB-INF/views/register.jsp");
            return;
        }

        boolean success = userService.register(username, password, "citizen");

        if (success) {
            request.setAttribute("success", "Account created! You can now log in.");
            forward(request, response, "/WEB-INF/views/login.jsp");
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            forward(request, response, "/WEB-INF/views/register.jsp");
        }
    }

    private void processCreateReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;

        User sessionUser = getSessionUser(request);
        if (!sessionUser.isCitizen()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String title       = request.getParameter("title").trim();
        String description = request.getParameter("description").trim();

        if (title.isEmpty() || description.isEmpty()) {
            request.setAttribute("error", "Title and description are required.");
            forward(request, response, "/WEB-INF/views/createReport.jsp");
            return;
        }

        reportService.createReport(title, description, sessionUser.getUserId());
        response.sendRedirect(request.getContextPath() + "/Controller?action=home");
    }

    private void processAddComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;

        User sessionUser = getSessionUser(request);
        String body      = request.getParameter("body").trim();
        int reportId     = Integer.parseInt(request.getParameter("reportId"));

        if (!body.isEmpty()) {
            commentService.addComment(body, sessionUser.getUserId(), reportId);
        }

        response.sendRedirect(request.getContextPath() +
                "/Controller?action=viewReport&id=" + reportId);
    }

    private void processDeleteReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;

        User sessionUser = getSessionUser(request);
        if (!sessionUser.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins only.");
            return;
        }

        int reportId = Integer.parseInt(request.getParameter("reportId"));
        reportService.deleteReport(reportId);
        response.sendRedirect(request.getContextPath() + "/Controller?action=home");
    }

    private void processUpdateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request, response)) return;

        User sessionUser = getSessionUser(request);
        if (!sessionUser.isAgent()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Agents only.");
            return;
        }

        int    reportId  = Integer.parseInt(request.getParameter("reportId"));
        String newStatus = request.getParameter("status");

        if (newStatus.equals("in_progress") || newStatus.equals("resolved")) {
            reportService.updateStatus(reportId, newStatus);
        }

        response.sendRedirect(request.getContextPath() +
                "/Controller?action=viewReport&id=" + reportId);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        response.sendRedirect(request.getContextPath() + "/Controller?action=login");
    }

    // ══════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════

    private void forward(HttpServletRequest req, HttpServletResponse res, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, res);
    }

    private boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("sessionUser") == null) {
            response.sendRedirect(request.getContextPath() + "/Controller?action=login");
            return false;
        }
        return true;
    }

    private User getSessionUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("sessionUser");
    }
}