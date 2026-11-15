package pt.ipleiria.estg.dei.ei.dae.project.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.ToggleHidePayloadDTO;
import pt.ipleiria.estg.dei.ei.dae.project.entities.*;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.PublicationDetailsDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.VoteResponseDTO;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;
import pt.ipleiria.estg.dei.ei.dae.project.enums.VoteType;
import pt.ipleiria.estg.dei.ei.dae.project.enums.FileType;

import java.util.LinkedList;
import java.util.List;

@Stateless
public class PublicationBean {

    @PersistenceContext
    private EntityManager em;

    public void create(User submitter, String title, LinkedList<String> authors, String attachedFile, FileType type, String abstractText, LinkedList<Tag> tags) {
        Publication publication = new Publication(submitter, title, authors, attachedFile, type, abstractText, tags);
        em.persist(publication);
    }

    public void update(Publication publication) {
        em.merge(publication);
    }

    public void update(long id, PublicationDetailsDTO updatedPublication) {
        Publication publication = em.find(Publication.class, id);
        if (publication != null) {
            publication.setTitle(updatedPublication.getTitle());
            publication.setAuthors(updatedPublication.getAuthors());
            publication.setAbstractText(updatedPublication.getAbstractText());
            publication.setType(updatedPublication.getType());
            em.merge(publication);
        }
    }

    // Soft delete
    public void delete(long id) {
        Publication publication = em.find(Publication.class, id);
        if (publication != null) {
            publication.setHidden(true);
        }
    }

    public List<Publication> findAll(int page, int limit, String sort, String order, String search, User requester) {
        if (page < 1) page = 1;
        if (limit < 1) limit = 10;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Publication> cq = cb.createQuery(Publication.class);
        Root<Publication> p = cq.from(Publication.class);

        p.fetch("submitter", JoinType.LEFT);

        Predicate visibilityPredicate;
        Predicate isNotHidden = cb.isFalse(p.get("isHidden"));

        boolean isAdminOrMod = requester.getRole() == Role.ADMINISTRATOR || 
                            requester.getRole() == Role.MODERATOR;

        if (isAdminOrMod) {
            visibilityPredicate = cb.conjunction();
        } else {
            Predicate isMyPost = cb.equal(p.get("submitter").get("id"), requester.getId());
            visibilityPredicate = cb.or(isNotHidden, isMyPost);
        }

        Predicate whereClause = visibilityPredicate;

        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate titleMatch = cb.like(cb.lower(p.get("title")), searchPattern);

            Join<Publication, String> authorsJoin = p.join("authors", JoinType.LEFT);
            Predicate authorMatch = cb.like(cb.lower(authorsJoin), searchPattern);

            Predicate searchCondition = cb.or(titleMatch, authorMatch);
            whereClause = cb.and(visibilityPredicate, searchCondition);
        }

        cq.where(whereClause);
        cq.distinct(true); 

        List<Publication> results = em.createQuery(cq)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();

        results.forEach(this::hydrate);
        return results;
    }

    public List<Publication> findBySubmitter(long userId, int page, int limit, String sort, String order) {
        if (page < 1) page = 1;
        if (limit < 1) limit = 10;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Publication> cq = cb.createQuery(Publication.class);
        Root<Publication> p = cq.from(Publication.class);

        p.fetch("submitter", JoinType.LEFT);

        cq.select(p).where(
                cb.equal(p.get("submitter").get("id"), userId)
        );

        String sortField = (sort != null && !sort.isEmpty()) ? sort : "publicationDate";
        if(sortField.equalsIgnoreCase("date")) sortField = "publicationDate";

        try {
            if ("asc".equalsIgnoreCase(order)) {
                cq.orderBy(cb.asc(p.get(sortField)));
            } else {
                cq.orderBy(cb.desc(p.get(sortField)));
            }
        } catch (IllegalArgumentException e) {
            cq.orderBy(cb.desc(p.get("publicationDate")));
        }

        List<Publication> results = em.createQuery(cq)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();

        results.forEach(this::hydrate);
        
        return results;
    }

    public Publication find(long id) {
        Publication publication = em.find(Publication.class, id);
        if (publication != null) {
            hydrate(publication);
            return publication;
        }
        return null;
    }

    public Publication findWithComments(long id) throws EntityNotFoundException {
        Publication publication = em.find(Publication.class, id);
        if (publication == null)
            throw new EntityNotFoundException("Publication with id " + id + " not found.");

        Hibernate.initialize(publication.getComments());
        return publication;
    }

    private void hydrate(Publication publication) {
        publication.getTags().size();
        publication.getComments().size();
        publication.getAuthors().size();
        publication.getVotes().size();         
    }

    public VoteResponseDTO vote(long publicationId, long userId, String voteTypeStr) {
        Publication publication = find(publicationId); 
        if (publication == null) publication = em.find(Publication.class, publicationId);
        
        User user = em.find(User.class, userId);
        VoteType newVoteType = VoteType.valueOf(voteTypeStr);

        PublicationVote existingVote = null;
        try {
            existingVote = em.createNamedQuery("getVote", PublicationVote.class)
                    .setParameter("pubId", publicationId)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {}

        if (newVoteType == VoteType.NONE) {
            if (existingVote != null) {
                publication.getVotes().remove(existingVote);
                em.remove(existingVote);
            }
        } else {
            if (existingVote != null) {
                existingVote.setVoteType(newVoteType);
            } else {
                PublicationVote vote = new PublicationVote(publication, user, newVoteType);
                publication.getVotes().add(vote);
                em.persist(vote);
            }
        }
        
        em.flush();
        em.refresh(publication);
        
        int currentScore = publication.getScore();
        
        publication.setRating(currentScore);
        em.merge(publication);
        
        return new VoteResponseDTO(currentScore, newVoteType.toString());
    }

    public void toggleHidden(long id, ToggleHidePayloadDTO dto) {
        Publication publication = em.find(Publication.class, id);
        if (publication == null) {
            throw new EntityNotFoundException("Publication with id " + id + " not found");
        }
        publication.setHidden(dto.isHidden());
        em.merge(publication);
    }

    public long count() {
        return em.createNamedQuery("Publication.countAll", Long.class).getSingleResult();
    }

    public long countByUser(User user) {
        return em.createNamedQuery("Publication.countBySubmitter", Long.class)
                .setParameter("submitterId", user.getId())
                .getSingleResult();
    }
}
