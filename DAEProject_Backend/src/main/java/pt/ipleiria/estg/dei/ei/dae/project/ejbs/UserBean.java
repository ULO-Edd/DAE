package pt.ipleiria.estg.dei.ei.dae.project.ejbs;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.AdminUpdateUserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.UserEditDTO;
import pt.ipleiria.estg.dei.ei.dae.project.dtos.AdminUpdateUserDTO;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;
import pt.ipleiria.estg.dei.ei.dae.project.security.Hasher;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Tag;
import java.util.List;

@Stateless
public class UserBean {
    @PersistenceContext
    private EntityManager em;

    public List<User> findAllUsers() {
        return em.createNamedQuery("getAllUsers", User.class).getResultList();
    }

    public User find(String email) {
        return em.createNamedQuery("getUserByEmail", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public User find(long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createNamedQuery("getAllUsers", User.class).getResultList();
    }
    
    public boolean canLogin(String email, String password) {
        User user = find(email);
        return user != null && Hasher.verify(password, user.getPassword()) && user.isActive();
    }

    public void create(String name, String email, String password, Role role) throws EntityExistsException {
        User existingUser = find(email);
        if (existingUser != null) {
            throw new EntityExistsException("There is already a user with this email.");
        }

        String hashedPassword = Hasher.hash(password);

        try {
            User user = new User(name, email, role, hashedPassword);
            em.persist(user);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Role: " + role.getRole());
        }
    }

    public void update(long id, UserEditDTO dto) throws EntityNotFoundException {
        User user = em.find(User.class, id);
        if (user == null) {
            System.err.println("ERROR_STUDENT_NOT_FOUND: " + id);
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        em.lock(user, LockModeType.OPTIMISTIC);

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }

        em.merge(user);
    }

    public void adminUpdate(long id, AdminUpdateUserDTO dto) throws EntityNotFoundException {
        User user = em.find(User.class, id);
        if (user == null) {
            System.err.println("ERROR_STUDENT_NOT_FOUND: " + id);
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        em.lock(user, LockModeType.OPTIMISTIC);

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getIsActive() != null) {
            user.setActive(dto.getIsActive());
        }

        if (dto.getRole() != null) {
            try {
                user.setRole(dto.getRole());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid Role: " + dto.getRole().getRole());
            }
        }

        em.merge(user);
    }

    public void delete(long id) throws EntityNotFoundException {
        User user = em.find(User.class, id);
        if (user == null) {
            System.err.println("ERROR_USER_NOT_FOUND: " + id);
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        em.lock(user, LockModeType.OPTIMISTIC);
        user.setActive(false);

        em.merge(user);
    }

    public void recoverPassword(User usr, String newPassword) {
        if (usr != null) {
            usr.setMustChangePassword(true);
            usr.setPassword(Hasher.hash(newPassword));
            em.merge(usr);
        }
    }

    public void setPassword(User usr, String newPassword) {
        if (usr != null) {
            if(usr.isMustChangePassword()) {
                usr.setMustChangePassword(false);
            }
            usr.setPassword(Hasher.hash(newPassword));
            em.merge(usr);
        }
    }

    public void subscribe(long userId, long tagId) throws EntityNotFoundException {
        User user = find(userId);
        if (user == null) throw new EntityNotFoundException("User not found");

        Tag tag = em.find(Tag.class, tagId);
        if (tag == null) throw new EntityNotFoundException("Tag not found");

        if (!user.getSubscriptions().contains(tag)) {
            user.addSubscription(tag);
            em.merge(user);
        }
    }

    public void unsubscribe(long userId, long tagId) throws EntityNotFoundException {
        User user = find(userId);
        if (user == null) throw new EntityNotFoundException("User not found");

        Tag tag = em.find(Tag.class, tagId);
        if (tag == null) throw new EntityNotFoundException("Tag not found");

        if (user.getSubscriptions().contains(tag)) {
            user.removeSubscription(tag);
            em.merge(user);
        }
    }
    
    public List<Tag> getSubscriptions(long userId) throws EntityNotFoundException {
        User user = find(userId);
        if (user == null) throw new EntityNotFoundException("User not found");
        
        user.getSubscriptions().size(); 
        
        return user.getSubscriptions();
    }
}
