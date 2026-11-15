package pt.ipleiria.estg.dei.ei.dae.project.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Tag;

import java.util.List;

@Stateless
public class TagBean {

    @PersistenceContext
    private EntityManager em;

    public List<Tag> findAll() {
        return em.createNamedQuery("getAllTags", Tag.class).getResultList();
    }

    public Tag find(long id) throws EntityNotFoundException {
        Tag tag = em.find(Tag.class, id);
        if (tag == null)
            throw new EntityNotFoundException("Tag with id " + id + " not found");
        return tag;
    }

    public Tag find(String name) {
        return em.createNamedQuery("getTagByName", Tag.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void create(String name) throws EntityExistsException {
        Tag duplicatedTag = find(name);

        if (duplicatedTag != null)
            throw new EntityExistsException("Tag with name " + name + " already exists");

        try {
            Tag tag = new Tag(name);
            em.persist(tag);
            em.flush();
        } catch (ConstraintViolationException e) {
            throw new ConstraintViolationException(e.getConstraintViolations());
        }
    }

    public void update(long id, String name) throws EntityNotFoundException {
        Tag tag = em.find(Tag.class, id);
        if (tag == null) {
            System.err.println("ERROR_TAG_NOT_FOUND: " + id);
            throw new EntityNotFoundException("Tag with id " + id + " not found");
        }
        em.lock(tag, jakarta.persistence.LockModeType.OPTIMISTIC);
        tag.setName(name);
        em.merge(tag);
    }

    public void delete(long id) throws EntityNotFoundException {
        Tag tag = em.find(Tag.class, id);
        if (tag == null) {
            throw new EntityNotFoundException("Tag with id " + id + " not found");
        }
        em.remove(tag);
    }
}
