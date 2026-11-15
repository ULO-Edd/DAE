package pt.ipleiria.estg.dei.ei.dae.project.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.CommentPayloadDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.ToggleHidePayloadDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.UserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Comment;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;

import java.util.Date;
import java.util.List;

@Stateless
public class CommentBean {

    @PersistenceContext
    private EntityManager em;

    public Comment create(long userId, long postId, String content) {
        User author = em.find(User.class, userId);
        if (author == null)
            throw new EntityNotFoundException("User with id " + userId + " not found");
        Publication publication = em.find(Publication.class, postId);
        if (publication == null)
            throw new EntityNotFoundException("Publication with id " + postId + " not found");

        Comment comment = new Comment(
                publication,
                author,
                content,
                new Date(),
                false
        );

        publication.addComment(comment);
        em.persist(comment);
        em.flush();

        return comment;
    }

    public void update(long id, CommentPayloadDTO commentDTO) {
        Comment comment = em.find(Comment.class, id);
        if (comment == null) {
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
        comment.setContent(commentDTO.getContent());
        em.merge(comment);
    }

    public void toggleHidden(long id, ToggleHidePayloadDTO dto) {
        Comment comment = em.find(Comment.class, id);
        if (comment == null) {
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
        comment.setHidden(dto.isHidden());
        em.merge(comment);
    }

    public Comment find(long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment == null) {
            throw new RuntimeException("comment " + id + " not found");
        }
        return comment;
    }

    public Comment find(long userId, long postId) {
        return em.createNamedQuery("getCommentByUserAndPost", Comment.class)
                .setParameter("userId", userId)
                .setParameter("postId", postId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<Comment> findAllFromPost(long id) {
        return em.createNamedQuery("getAllCommentsFromPost", Comment.class)
                .setParameter("postId", id)
                .getResultList();
    }

    public List<Comment> findAllFromUser(long id) {
        return em.createNamedQuery("getAllCommentsFromUser", Comment.class)
                .setParameter("userId", id)
                .getResultList();
    }

    public void delete(long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        } else {
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
    }
}
