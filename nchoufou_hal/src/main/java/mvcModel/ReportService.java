package mvcModel;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import entities.Report;
import entities.User;

import java.util.List;

@Stateless
public class ReportService {

    @PersistenceContext(unitName = "nchoufou_hal")
    private EntityManager em;

    // ── CREATE ───────────────────────────────────────────────────────
    public boolean createReport(String title, String description, int userId) {
        try {
            Report report = new Report();
            report.setTitle(title);
            report.setDescription(description);
            report.setStatus("pending"); // default value (optional)

            // Attach existing user
            User user = em.find(User.class, userId);
            report.setUser(user);

            em.persist(report);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── GET ALL REPORTS ──────────────────────────────────────────────
    public List<Report> getAllReports() {
        try {
            return em.createQuery(
                    "SELECT r FROM Report r JOIN FETCH r.user ORDER BY r.createdAt DESC",
                    Report.class
            ).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // ── GET REPORTS BY USER ──────────────────────────────────────────
    public List<Report> getReportsByUser(int userId) {
        try {
            return em.createQuery(
                    "SELECT r FROM Report r JOIN FETCH r.user WHERE r.user.userId = :userId ORDER BY r.createdAt DESC",
                    Report.class
            )
            .setParameter("userId", userId)
            .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // ── GET ONE REPORT BY ID ─────────────────────────────────────────
    public Report getReportById(int reportId) {
        try {
            return em.createQuery(
                    "SELECT r FROM Report r JOIN FETCH r.user WHERE r.reportId = :id",
                    Report.class
            )
            .setParameter("id", reportId)
            .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────
    public boolean deleteReport(int reportId) {
        try {
            Report report = em.find(Report.class, reportId);
            if (report != null) {
                em.remove(report);
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── UPDATE STATUS ────────────────────────────────────────────────
    public boolean updateStatus(int reportId, String newStatus) {
        try {
            Report report = em.find(Report.class, reportId);
            if (report != null) {
                report.setStatus(newStatus);
                em.merge(report);
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}