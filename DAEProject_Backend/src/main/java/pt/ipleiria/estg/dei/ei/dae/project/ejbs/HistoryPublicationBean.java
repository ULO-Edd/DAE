package pt.ipleiria.estg.dei.ei.dae.project.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.ejb.EJB;

import pt.ipleiria.estg.dei.ei.dae.project.dtos.HistoryUserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.ToggleHidePayloadDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.UserDTO;

import pt.ipleiria.estg.dei.ei.dae.project.entities.Tag;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Comment;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;
import pt.ipleiria.estg.dei.ei.dae.project.entities.HistoryPublication;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;

import pt.ipleiria.estg.dei.ei.dae.project.enums.ActionType;
import pt.ipleiria.estg.dei.ei.dae.project.enums.TargetType;

import java.util.Date;
import java.util.List;

@Stateless
public class HistoryPublicationBean {

    @PersistenceContext
    private EntityManager em;

    @EJB 
    private CommentBean commentBean;

    @EJB 
    private PublicationBean publicationBean;

    public HistoryPublication create(long userId, long publicationId, String actionType, String description) {
        User user = em.find(User.class, userId);
        if (user == null)
            throw new EntityNotFoundException("User with id " + userId + " not found");

        Publication publication = em.find(Publication.class, publicationId);
        if (publication == null)
            throw new EntityNotFoundException("Publication with id " + publicationId + " not found");

        HistoryPublication historyPublication = new HistoryPublication(
                publication,
                new Date(),
                actionType,
                userId,
                description
        );

        em.persist(historyPublication);
        em.flush();

        return historyPublication;
    }

    public void update(long id, HistoryUserDTO historyUserDTO) {
        HistoryPublication historyPublication = em.find(HistoryPublication.class, id);
        if (historyPublication == null) {
       throw new EntityNotFoundException("HistoryPublication with id " + id + " not found");
        }
        historyPublication.setActionType(historyUserDTO.getActionType().name());
        em.merge(historyPublication);
    }

    public void logAction(long userId, long publicationId, ActionType actionType) {
        User user = em.find(User.class, userId);   
        if (user == null) {
            throw new EntityNotFoundException("User with id " + userId + " not found");                 
        }

        Tag tag = publicationBean.find(publicationId).getTags().get(0); 
        String description = "";

        switch (actionType) {
            case CREATE_PUBLICATION: 
                description = "User \'" + user.getName() +  "\' created publication \'" + publicationBean.find(publicationId).getTitle() + "\'";
                break;
            case EDIT_PUBLICATION: 
                description = "User \'" + user.getName() +  "\' edited publication \'" + publicationBean.find(publicationId).getTitle() + "\'";
                break;
            case HIDE_PUBLICATION:
                description = "User \'" + user.getName() +  "\' hid publication \'" + publicationBean.find(publicationId).getTitle() + "\'";
                break;
            default:
                description = "User \'" + user.getName() +  "\' performed an action on publication \'" + publicationBean.find(publicationId).getTitle() + "\' This action should never happen, please contact the administrator.";
        };
       
        HistoryPublication historyPublication = new HistoryPublication(
                publicationBean.find(publicationId),
                new Date(),
                actionType.name(),
                userId,
                description
        ); 

        em.persist(historyPublication);
    }
   
    public void logTagAction(long userId, long publicationId, Tag tag) {
        User user = em.find(User.class, userId);   
        if (user == null) {
            throw new EntityNotFoundException("User with id " + userId + " not found");                 
        }

        String description = "User \'" + user.getName() +  "\' added the tag \'" + tag.getName() + "\' to publication \'" + publicationBean.find(publicationId).getTitle() + "\'"; 

        HistoryPublication historyPublication = new HistoryPublication(
                publicationBean.find(publicationId),
                new Date(),
                ActionType.TAG_PUBLICATION.name(),
                userId,
                description
        ); 

        em.persist(historyPublication);
    }

    public List<HistoryPublication> findByPublication(long publicationId) {
        return em.createNamedQuery("getHistoryPublicationsByPublicationId", HistoryPublication.class)
                .setParameter("publicationId", publicationId)
                .getResultList();
    }
}
