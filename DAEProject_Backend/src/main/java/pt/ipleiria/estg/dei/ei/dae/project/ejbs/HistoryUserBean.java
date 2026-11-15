package pt.ipleiria.estg.dei.ei.dae.project.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.ejb.EJB;

import pt.ipleiria.estg.dei.ei.dae.project.dtos.HistoryUserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.ToggleHidePayloadDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.UserDTO;

import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;
import pt.ipleiria.estg.dei.ei.dae.project.entities.HistoryUser;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;

import pt.ipleiria.estg.dei.ei.dae.project.enums.ActionType;
import pt.ipleiria.estg.dei.ei.dae.project.enums.TargetType;

import java.util.Date;
import java.util.List;

@Stateless
public class HistoryUserBean {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private PublicationBean publicationBean;

    @EJB 
    private CommentBean commentBean;

    public HistoryUser create(long userId, String actionType, String targetType, long targetId, String description) {
        User user = em.find(User.class, userId);
        if (user == null)
            throw new EntityNotFoundException("User with id " + userId + " not found");

        HistoryUser historyUser = new HistoryUser(
                user,
                new Date(),
                actionType,
                targetType,
                targetId,
                description
        );

        em.persist(historyUser);
        em.flush();

        return historyUser;
    } 

    public void update(long id, HistoryUserDTO historyUserDTO) {
        HistoryUser historyUser = em.find(HistoryUser.class, id);
        if (historyUser == null) {
            throw new EntityNotFoundException("HistoryUser with id " + id + " not found");
        }
        historyUser.setActionType(historyUserDTO.getActionType().name());
        historyUser.setTargetType(historyUserDTO.getTargetType().name());
        historyUser.setTargetId(historyUserDTO.getTargetId());
        historyUser.setDescription(historyUserDTO.getDescription());

        em.merge(historyUser);
    }
   
    public void logAction(long userId, long targetId, ActionType type) {
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
        String description = "";

        // Not the best way to do this, but it works for now
        TargetType tType = null; 
        String publicationName = "NULL";

        if (targetId != -1 && (type == ActionType.UPLOAD_PUBLICATION || type == ActionType.COMMENT_PUBLICATION || type == ActionType.HIDE_PUBLICATION || type == ActionType.RATE_PUBLICATION || type == ActionType.EDIT_PUBLICATION || type == ActionType.EDIT_COMMENT)) {
            publicationName = publicationBean.find(targetId).getTitle();
        }
        
        switch (type) {
            case UPLOAD_PUBLICATION:
                description = "Uploaded publication with name \'" + publicationName + "\'";
                tType = TargetType.PUBLICATION;
                break;
            case COMMENT_PUBLICATION:
                String commentContent = commentBean.find(userId, targetId).getContent();
                description = "Commented \'" + commentContent + "\' on publication \'" + publicationName + "\'";
                tType = TargetType.PUBLICATION;
                break;
            case EDIT_COMMENT:
                String editedCommentContent = commentBean.find(userId, targetId).getContent();
                description = "Edited comment to \'" + editedCommentContent + "\' on publication \'" + publicationName + "\'";
                tType = TargetType.PUBLICATION;
                break;
            case HIDE_PUBLICATION:
                description = "Hid publication \'" + publicationName + "\'";
                tType = TargetType.PUBLICATION;
                break;
            case HIDE_COMMENT:
                description = "Hid comment on a publication.";
                tType = TargetType.PUBLICATION;
                break;
            case RATE_PUBLICATION:
                description = "Rated publication \'" + publicationName + "\'";
                tType = TargetType.PUBLICATION;
                break;
            case EDIT_PUBLICATION:
                description = "Edited publication \'" + publicationName + "\'";
                tType = TargetType.PUBLICATION;
                break;
            case CHANGE_NAME:
                description = "Changed user name";
                tType = TargetType.USER;
                break;
            case CHANGE_EMAIL:
                description = "Changed user email";
                tType = TargetType.USER;
                break;
            case CHANGE_ROLE:
                description = "Changed user role to \'" + user.getRole().name() + "\'";
                tType = TargetType.ROLE;
                break;
            case CREATE_USER:
                description = "User \'" + user.getName() + "\' was created, Hello World!";
                tType = TargetType.USER;
                break;
            case DELETE_USER:
                description = "User \'" + user.getName() + "\' was deleted or deactivated, Goodbye!";
                tType = TargetType.USER;
                break;
            default:
                throw new IllegalArgumentException("Invalid action type: " + type);
        }

        HistoryUser historyUser = new HistoryUser(
                user,
                new Date(),
                type.name(),
                tType.name(),
                targetId,
                description
        );

        em.persist(historyUser);
        
    }

    public List<HistoryUser> findByUser(long userId) {
        return em.createNamedQuery("getHistoryUsersByUserId", HistoryUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public HistoryUser find(long id) {
        return em.find(HistoryUser.class, id);
    }
} 
