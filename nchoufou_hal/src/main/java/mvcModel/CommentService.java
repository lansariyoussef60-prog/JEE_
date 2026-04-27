package mvcModel;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import entities.Comment;
import entities.Report;
import entities.User;

import java.util.List;

@Stateless
public class CommentService {

    @PersistenceContext(unitName = "nchoufou_hal")
    private EntityManager em;

    // ── ADD COMMENT ──────────────────────────────────────────────────
    public boolean addComment(String body, int userId, int reportId) {
        try {
            Comment comment = new Comment();
            comment.setBody(body);

            // Attach existing user
            User user = em.find(User.class, userId);
            comment.setUser(user);

            // Attach existing report
            Report report = em.find(Report.class, reportId);
            comment.setReport(report);

            em.persist(comment);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── GET COMMENTS BY REPORT ───────────────────────────────────────
    public List<Comment> getCommentsByReport(int reportId) {
        try {
            return em.createQuery(
                    "SELECT c FROM Comment c " +
                    "JOIN FETCH c.user " +
                    "WHERE c.report.reportId = :reportId " +
                    "ORDER BY c.createdAt ASC",
                    Comment.class
            )
            .setParameter("reportId", reportId)
            .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}