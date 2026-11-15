package pt.ipleiria.estg.dei.ei.dae.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.enums.Role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
@NamedQueries({
        @NamedQuery(
                name = "getAllUsers",
                query = "SELECT u FROM User u ORDER BY u.name" // JPQL
        ),
        @NamedQuery(
                name = "getUserByEmail",
                query = "SELECT u FROM User u WHERE u.email = :email" // JPQL
        )
})
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User extends Versionable implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
    @NonNull private String name;
    @NonNull @Email private String email;
    @NonNull private Role role;
    @NonNull private String password;
    private boolean isActive = true;
    private boolean mustChangePassword = false;
    @OneToMany(mappedBy = "submitter") private List<Publication> publications = new ArrayList<>();
    @Version
    private int version;

    @ManyToMany
    @JoinTable(
        name = "users_subscriptions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> subscriptions = new ArrayList<>();

    public void addSubscription(Tag tag) {
        if (!subscriptions.contains(tag)) {
            subscriptions.add(tag);
        }
    }

    public void removeSubscription(Tag tag) {
        subscriptions.remove(tag);
    }
}
